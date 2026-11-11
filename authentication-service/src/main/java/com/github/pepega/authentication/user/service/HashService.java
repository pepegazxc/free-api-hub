package com.github.pepega.authentication.user.service;

import com.github.pepega.authentication.exception.exceptions.HashException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HexFormat;
import java.util.Locale;


@Component
@Slf4j
public class HashService {

    private final SecretKeySpec key;

    public HashService(@Value("${app.hash.hmac-key}") String hmacKey) {
        this.key = new SecretKeySpec(hmacKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public byte[] hash(String value){
        try{
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
            return HexFormat.of().formatHex(mac.doFinal(value.getBytes(StandardCharsets.UTF_8))).getBytes();
        }catch (GeneralSecurityException e){
            throw new HashException(e, e.getMessage());
        }
    }
}
