package com.relexintern.entrancetest.requests;

import lombok.Data;

@Data
public class MessageRequest {
    String receiver;
    String content;
}
