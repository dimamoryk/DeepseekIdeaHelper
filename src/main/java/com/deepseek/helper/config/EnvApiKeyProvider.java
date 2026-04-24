package com.deepseek.helper.service.config;

import com.deepseek.helper.service.exceptions.ApiKeyMissingException;

public class EnvApiKeyProvider implements
        ApiKeyProvider {
    @Override
    public String getApiKey() {

        String key = System.getenv("DEEPSEEK_API_KEY");
        if (key == null || key.isBlank()) {
            throw new
                    ApiKeyMissingException("DEEPSEEK_API_KEY environment variable not set!");

        }
        return key;
    }
}
