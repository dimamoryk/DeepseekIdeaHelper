package com.deepseek.helper;

import com.deepseek.helper.config.ApiKeyProvider;
import com.deepseek.helper.dto.ChatRequest;
import com.deepseek.helper.dto.Message;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeepSeekAIServiceTest {

    @Test
    void testChatRequestSerialization() {
        ChatRequest request = new ChatRequest();
        request.setMessages(List.of(
                new Message("user", "Hello")
        ));
        Gson gson = new Gson();
        String json = gson.toJson(request);
        assertTrue(json.contains("Hello"));
    }
}
