package com.aurumplus.utils;

import java.io.BufferedReader;
import java.io.IOException;

import com.aurumplus.compiler.components.IOComponent;

public class AurumPlusFile extends IOComponent<AurumPlusFile> {
    private final String source;

    public AurumPlusFile(String path) {
        ValidationComponent<String> validator = new FileExtensionValidator("amp");
        validator.validate(path);
        
        source = getFileContent(path);
    }

    private String getFileContent(String path) {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

    public String getSource() {
        return source;
    }
}
