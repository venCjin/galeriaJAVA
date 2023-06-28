package com.jsuchinski.galeria.utils;

import java.io.File;

public class FileUtils {
    public FileUtils() {
    }

    public boolean makeDirectory(String path) {
        return new File(path).mkdir();
    }
}
