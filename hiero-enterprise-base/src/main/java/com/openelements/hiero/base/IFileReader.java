package com.openelements.hiero.base;

import java.io.IOException;
import java.nio.file.Path;

public interface IFileReader {
    byte[] readAllBytes(Path path) throws IOException;
}
