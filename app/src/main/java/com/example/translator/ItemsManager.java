package com.example.translator;

public class ItemsManager {
    private String originalText;
    private String translatedText;
    private String lang;

    public ItemsManager(String originalText, String translatedText, String lang) {
        this.originalText = originalText;
        this.translatedText = translatedText;
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getTranslatedText() {
        return translatedText;
    }


}

