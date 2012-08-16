package com.agical.golddigger.model;

import com.agical.golddigger.model.fieldcreator.Reader;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {
    public static String read(String fileName) throws IOException {
        URL resource = ConfigReader.class.getResource(fileName);
        String fileContent = Reader.read(resource);
        return fileContent;
    }

    
}
