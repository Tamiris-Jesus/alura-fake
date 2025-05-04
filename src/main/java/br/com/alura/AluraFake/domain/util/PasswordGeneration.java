package br.com.alura.AluraFake.domain.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

public class PasswordGeneration{

    public static String generatePassword() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        String rawPassword = String.valueOf(password);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        return encodedPassword;
    }
}
