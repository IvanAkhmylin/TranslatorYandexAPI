package com.example.translator;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Adapter {
    @SerializedName("code")
    private String code;
    @SerializedName("lang")
    private String lang;
    @SerializedName("text")
    private List<String> text = null;
    @SerializedName("options")
    private int options;
    @SerializedName("ui")
    private List<String> languageCode = null;


    public int getOptions() {
        return options;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    public Adapter(int options) {
        this.options = options;
    }

    public List<String> getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(List<String> languageCode) {
        this.languageCode = languageCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }



}