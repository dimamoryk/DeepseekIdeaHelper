package com.deepseek.helper.service.config;
/**
 * Провайдер API-ключа для DeepSeek.
 * Разные реализации могут читать ключ из env, файла, системы аутентификации.
 */
public interface ApiKeyProvider {
    /**
     * Возвращает API-ключ.
     * @throws IllegalStateException если ключ не найден
     */
    String getApiKey();
}

