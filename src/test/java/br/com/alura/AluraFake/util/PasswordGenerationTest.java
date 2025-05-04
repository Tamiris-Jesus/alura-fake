package br.com.alura.AluraFake.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGenerationTest {

    /* Os métodos foram refatorados para fazerem sentido num cenário em que as senhas são
    criptografadas
    * */
    @Test
    void generatePassword__hashed_passwords_should_be_different() {
        String rawPassword = "123456";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String encodedPassword1 = encoder.encode(rawPassword);
        String encodedPassword2 = encoder.encode(rawPassword);

        assertNotEquals(encodedPassword1, encodedPassword2);
    }

    @Test
    void encodeAndMatchPassword() {
        String rawPassword = "123456";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String encodedPassword = encoder.encode(rawPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void encodeAndFailToMatchPassword() {
        String rawPassword = "123456";
        String wrongPassword = "654321";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        assertFalse(encoder.matches(wrongPassword, encodedPassword));
    }

}