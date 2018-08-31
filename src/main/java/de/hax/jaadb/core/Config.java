package de.hax.jaadb.core;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {

    JSONObject jsonObject;

    public Config read(String file) {
        try {
            jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(new File(file).toURI()))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
