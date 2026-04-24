package com.deepseek.helper.config;

import com.deepseek.helper.exceptions.ApiKeyMissingException;

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
