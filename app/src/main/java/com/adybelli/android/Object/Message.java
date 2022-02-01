package com.adybelli.android.Object;

public class Message {
    private String text;
    private String text_ru;

    public Message(String text, String text_ru) {
        this.text = text;
        this.text_ru = text_ru;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText_ru() {
        return text_ru;
    }

    public void setText_ru(String text_ru) {
        this.text_ru = text_ru;
    }
}
