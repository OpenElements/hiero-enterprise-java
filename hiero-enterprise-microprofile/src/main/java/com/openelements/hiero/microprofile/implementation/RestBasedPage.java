package com.openelements.hiero.microprofile.implementation;

import com.openelements.hiero.base.data.Page;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class RestBasedPage<T> implements Page<T> {
    private final String restTarget;
    private final Function<JsonObject, List<T>> dataExtractionFunction;
    private final List<T> data;
    private final String rootPath;
    private final String currentPath;
    private final String nextPath;
    private final int number;

    public RestBasedPage(@NonNull String restTarget, @NonNull Function<JsonObject,
            @NonNull List<T>> dataExtractionFunction, @NonNull String path) {
        this(restTarget, dataExtractionFunction, path, path, 0);
    }

    public RestBasedPage(@NonNull String restTarget,
                         @NonNull Function<JsonObject, List<T>> dataExtractionFunction, @NonNull String path,
                         @NonNull String rootPath, int number) {
        this.restTarget =  Objects.requireNonNull(restTarget, "restTarget must not be null");
        this.dataExtractionFunction =  Objects.requireNonNull(dataExtractionFunction, "dataExtractionFunction must not be null");
        this.rootPath = Objects.requireNonNull(rootPath, "rootPath must not be null");
        this.currentPath =  Objects.requireNonNull(path, "path must not be null");
        this.number = number;

        String[] pathParts = currentPath.split("\\?");
        final String requestPath = pathParts[0];

        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(restTarget).path(requestPath);
            if (pathParts.length > 1) {
                String[] params = pathParts[1].split("&");
                for (String param : params) {
                    String[] p = param.split("=");
                    target = target.queryParam(p[0], p[1]);
                }
            }
            Response response = target.request(MediaType.APPLICATION_JSON).get();

            final JsonObject jsonObject = response.readEntity(JsonObject.class);
            this.data = Collections.unmodifiableList(dataExtractionFunction.apply(jsonObject));
            this.nextPath = getNextPath(jsonObject);
        } catch (Exception e) {
            throw new IllegalStateException("Can not parse JSON: " +  e);
        }
    }

    private String getNextPath(final JsonObject jsonObject) {
        if (!jsonObject.containsKey("links")) {
            return null;
        }
        final JsonObject linksObject = jsonObject.getJsonObject("links");
        if (linksObject == null || !linksObject.containsKey("next")) {
            return null;
        }

        try {
            final String next;
            if (!linksObject.isNull("next")) {
                next = linksObject.getString("next");
            } else {
                next = null;
            }
            return next;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing next link '" + linksObject + "'", e);
        }
    }

    @Override
    public int getPageIndex() {
        return number;
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public boolean hasNext() {
        return nextPath != null;
    }

    @Override
    public Page<T> next() {
        if (nextPath == null) {
            throw new IllegalStateException("No next Page");
        }
        return new RestBasedPage<T>(restTarget, dataExtractionFunction, nextPath, rootPath, number+1);
    }

    @Override
    public Page<T> first() {
        return new RestBasedPage<T>(restTarget, dataExtractionFunction, rootPath);
    }

    @Override
    public boolean isFirst() {
        return Objects.equals(rootPath, currentPath);
    }
}
