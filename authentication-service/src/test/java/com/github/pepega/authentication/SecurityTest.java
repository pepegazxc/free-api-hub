package com.github.pepega.authentication;

import com.github.pepega.authentication.user.service.HashService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SecurityTest {

    private final HashService hashService = new HashService("Jefe");

    @Test
    void hash_matchesRfc4231Vector() {
        assertEquals(
                "5bdcc146bf60754e6a042426089575c75a003f089d2739839dec58b964ec3843",
                hashService.hash("what do ya want for nothing?"));
    }

    @Test
    void hash_normalizesCaseAndWhitespace() {
        assertEquals(hashService.hash("a@x.com"), hashService.hash("  A@X.com "));
    }

    @Test
    void hash_differentInputsGiveDifferentHashes() {
        assertNotEquals(hashService.hash("a@x.com"), hashService.hash("b@x.com"));
    }

    @Test
    void hash_differentKeysGiveDifferentHashes() {
        assertNotEquals(
                hashService.hash("a@x.com"),
                new HashService("other").hash("a@x.com"));
    }
}

