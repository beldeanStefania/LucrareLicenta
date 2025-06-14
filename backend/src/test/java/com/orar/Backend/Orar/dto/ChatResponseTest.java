package com.orar.Backend.Orar.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.orar.Backend.Orar.dto.ChatResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatResponseTest {

    @Test
    void testConstructorAndGetter() {
        ChatResponse response = new ChatResponse("Răspunsul generat de asistent.");

        assertEquals("Răspunsul generat de asistent.", response.getReply());
    }
}
