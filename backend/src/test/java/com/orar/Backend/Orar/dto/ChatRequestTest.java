package com.orar.Backend.Orar.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.orar.Backend.Orar.dto.ChatRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatRequestTest {

    @Test
    void testGetterAndSetter() {
        ChatRequest request = new ChatRequest();
        request.setMessage("Care este orarul meu de luni?");

        assertEquals("Care este orarul meu de luni?", request.getMessage());
    }
}
