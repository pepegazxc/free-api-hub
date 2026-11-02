package com.github.pepega.authentication.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtKeysConfig {

    @Bean
    public PrivateKey privateKey(JwtProperties props) throws GeneralSecurityException{
        byte[] der = Base64.getDecoder().decode(props.publicKey().trim());
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(der));
    }

    @Bean
    public PublicKey publicKey(JwtProperties props) throws GeneralSecurityException{
        byte[] der = Base64.getDecoder().decode(props.publicKey().trim());
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(der));
    }

}
