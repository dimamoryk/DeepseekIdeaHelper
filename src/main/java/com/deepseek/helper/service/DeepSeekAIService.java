package com.deepseek.helper.service;

import com.deepseek.helper.config.ApiKeyProvider;
import com.deepseek.helper.dto.ChatRequest;
import com.deepseek.helper.dto.ChatResponse;
import com.deepseek.helper.dto.Message;
import com.deepseek.helper.exceptions.ApiCommunicationException;
import com.deepseek.helper.exceptions.ApiResponseParseException;
import com.deepseek.helper.exceptions.ApiServerErrorException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeepSeekAIService implements AIService {

    private static final String API_URL =
            System.getProperty("api.url", "https://api.deepseek.com/v1/chat/completions");

    private final ApiKeyProvider apiKeyProvider;
    private final Gson gson;
    private final OkHttpClient httpClient;

    // конструктор для ручного DI
    public DeepSeekAIService(ApiKeyProvider apiKeyProvider, Gson gson, OkHttpClient httpClient) {
        this.apiKeyProvider = apiKeyProvider;
        this.gson = gson;
        this.httpClient = httpClient;
    }

    // упрощённый конструктор с настройками по умолчанию
    public DeepSeekAIService(ApiKeyProvider apiKeyProvider) {
        this(apiKeyProvider,
                new Gson(),
                new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .build());
    }

    @Override
    public String sendPrompt(String prompt) {
        ChatRequest request = new ChatRequest();
        request.setMessages(List.of(
                new Message("system", "You are a helpful assistant."),
                new Message("user", prompt)
        ));

        String jsonBody = gson.toJson(request);
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request httpRequest = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKeyProvider.getApiKey())
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() >= 500) {
                    throw new ApiServerErrorException("DeepSeek server error: " + response.code());
                } else {
                    // body может быть null, но мы его читаем только в случае ошибки
                    String errorBody = response.body() != null ? response.body().string() : "";
                    throw new ApiCommunicationException("HTTP error " + response.code() + ": " + errorBody, null);
                }
            }

            String responseBody = response.body().string();
            try {
                ChatResponse chatResponse = gson.fromJson(responseBody, ChatResponse.class);
                // Проверяем, что есть выборы и первый выбор содержит сообщение
                if (chatResponse.getChoices() != null && !chatResponse.getChoices().isEmpty()) {
                    return chatResponse.getChoices().get(0).getMessage().getContent();
                } else {
                    return "No response from API";
                }
            } catch (JsonSyntaxException e) {
                throw new ApiResponseParseException("Failed to parse API response", e);
            }
        } catch (IOException e) {
            throw new ApiCommunicationException("Network error while communicating with DeepSeek API", e);
        }
    }
}
