package com.openelements.hiero.base.implementation;

import com.openelements.hiero.base.IFileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SystemFileReader implements IFileReader {
    @Override
    public byte[] readAllBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }
}
