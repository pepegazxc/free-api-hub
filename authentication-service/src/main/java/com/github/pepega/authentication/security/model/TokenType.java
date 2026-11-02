package com.github.pepega.authentication.security.model;

public enum TokenType {
    ACCESS_TOKEN, REFRESH_TOKEN;

    public static final String CLAIM="token_type";
}
