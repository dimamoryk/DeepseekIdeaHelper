package com.deepseek.helper.service;

import com.deepseek.helper.exceptions.ApiCommunicationException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OllamaAIService implements AIService {

    private static final String OLLAMA_API_URL = "http://localhost:11434/api/generate";
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String modelName;

//    public OllamaAIService() {
//        this("deepseek-coder");
//    }
    public OllamaAIService() {
        this("tinyllama");
    }

    public OllamaAIService(String modelName) {
        this.modelName = modelName;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();
        this.gson = new Gson();
    }

    @Override
    public String sendPrompt(String prompt) {
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("model", modelName);
        jsonBody.addProperty("prompt", prompt);
        jsonBody.addProperty("stream", false);

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(OLLAMA_API_URL)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiCommunicationException(
                        "Unexpected code " + response.code() + ": " + response.body().string(),
                        null
                );
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

            return jsonResponse.get("response").getAsString();
        } catch (IOException e) {
            throw new ApiCommunicationException(
                    "Network error while communicating with Ollama API. Is Ollama running?",
                    e
            );
        }
    }
}
