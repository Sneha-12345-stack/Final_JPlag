
package com.plagiarism.plagiarismchecker.utils;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    public static void unzip(Path zipFile, Path destDir) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.toFile()))) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                Path newPath = destDir.resolve(entry.getName()).normalize();

                // 🔐 Prevent Zip Slip
                if (!newPath.startsWith(destDir)) {
                    throw new IOException("Invalid ZIP entry");
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {

                    if (!isValidFile(entry.getName())) {
                        continue;
                    }

                    Files.createDirectories(newPath.getParent());

                    try (OutputStream os = Files.newOutputStream(newPath)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            os.write(buffer, 0, len);
                        }
                    }
                }
            }
        }
    }

    private static boolean isValidFile(String name) {
        return name.endsWith(".java") ||
               name.endsWith(".py") ||
               name.endsWith(".cpp") ||
               name.endsWith(".c");
    }
}