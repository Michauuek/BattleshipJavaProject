package com.example.model;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private static final Gson gson = new Gson();
    public String content;
    public Map<String,String> adds;

    public Message(String content, Map<String,String> additionalContent) {
        this.content = content;
        this.adds = additionalContent;
    }
    public Message(String content) {
        this.content = content;
        this.adds = new HashMap<>();
    }
    public static Message newMessage(String message) {
        return new Message("message", Map.of("message", message));
    }

    public static Message fromJson(String json) {
        return gson.fromJson(json, Message.class);
    }

    @Override
    public String toString() {
        return "{" + content + "}" + adds;
    }

    public String toJson() {
        return gson.toJson(this);
    }
}

