package com.jsuchinski.galeria.utils;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

public class FileUtils {
    public FileUtils() {
    }

    public boolean makeDirectory(String path) {
        return new File(path).mkdir();
    }

    public boolean saveFoto(Part filePart, String path) {
        try {
//            String a = filePart.getContentType();
//            String b = filePart.getSubmittedFileName();
            filePart.write(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean deleteFoto(String path) {
        File foto = new File(path);
        return foto.delete();
    }
}
