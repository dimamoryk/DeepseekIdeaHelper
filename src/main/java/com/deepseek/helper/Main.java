package com.deepseek.helper.service;

import com.deepseek.helper.service.config.ApiKeyProvider;
import com.deepseek.helper.service.config.EnvApiKeyProvider;
import com.deepseek.helper.service.exceptions.DeepSeekException;
import com.deepseek.helper.service.service.AIService;
import com.deepseek.helper.service.service.DeepSeekAIService;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java -jar deepseek-idea-helper.jar \"your prompt\"");
            System.exit(1);
        }

        String prompt = String.join(" ", args);

        ApiKeyProvider apiKeyProvider = new EnvApiKeyProvider();
        AIService aiService = new DeepSeekAIService(apiKeyProvider);

        try {
            String response = aiService.sendPrompt(prompt);
            System.out.println(response);
        } catch (DeepSeekException e) {
            System.err.println("DeepSeek Helper error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
