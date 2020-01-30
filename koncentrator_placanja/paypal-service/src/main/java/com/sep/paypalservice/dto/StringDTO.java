package com.sep.paypalservice.dto;

public class StringDTO {


    private String text;

    public StringDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
