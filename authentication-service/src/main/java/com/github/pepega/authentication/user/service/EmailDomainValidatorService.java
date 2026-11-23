package com.github.pepega.authentication.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.IDN;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailDomainValidatorService {

    private final DnsLookUpService dnsLookUpService;

    public boolean isEmailDomainValid(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }
        String rawDomain = email.substring(email.indexOf('@') + 1).trim().toLowerCase();
        if (rawDomain.isEmpty()) {
            return false;
        }

        try {
            String asciiDomain = IDN.toASCII(rawDomain);
            return dnsLookUpService.checkMxRecordCached(asciiDomain);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
