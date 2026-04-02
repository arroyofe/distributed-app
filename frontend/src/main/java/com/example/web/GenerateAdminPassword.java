package com.example.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class GenerateAdminPassword {
    static void main() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("");
        log.info("Hash admin = {}", hash);
    }
}
