package com.deepseek.helper.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {

    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;
}
