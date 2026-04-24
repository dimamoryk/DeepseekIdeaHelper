package com.deepseek.helper.service.dto;

import lombok.Data;

@Data
public class Choice {

    private int index;
    private Message message;
    private String finish_reason;
}
