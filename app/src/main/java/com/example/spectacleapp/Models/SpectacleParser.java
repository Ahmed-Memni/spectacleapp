package com.example.spectacleapp.Models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.util.List;

public class SpectacleParser {

    public List<Spectacles> parseSpectaclesFromJson(InputStreamReader reader) {
        Gson gson = new Gson();
        // Parse the spectaclesList from the JSON
        SpectaclesListWrapper wrapper = gson.fromJson(reader, SpectaclesListWrapper.class);
        return wrapper.spectaclesList;
    }

    public class SpectaclesListWrapper {
        List<Spectacles> spectaclesList;
    }
}
