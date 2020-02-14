package com.sep.paypalservice.dto;

public class TextLinkDTO {

    private String text;
    private String websiteLink;

    public TextLinkDTO() {

    }

    public TextLinkDTO(String text, String link) {
        this.text = text;
        this.websiteLink = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }
}
