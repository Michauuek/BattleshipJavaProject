package com.example.model;

import java.util.Map;

public class Message {
    private String from;
    private String to;
    private String content;
    private Map<String,String> additionalContent;

    @Override
    public String toString() {
        return super.toString();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getAdditionalContent() {
        return additionalContent;
    }

    public void setAdditionalContent(Map<String, String> additionalContent) {
        this.additionalContent = additionalContent;
    }
}
