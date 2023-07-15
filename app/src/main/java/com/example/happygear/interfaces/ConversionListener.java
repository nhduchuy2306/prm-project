package com.example.happygear.interfaces;

import com.example.happygear.models.ChatMessage;
import com.example.happygear.models.User;

public interface ConversionListener {
    void onConversionClick(User user, ChatMessage chatMessage);
}

