package com.example.web.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DbHealthService {

    private final JdbcTemplate jdbc;

    public DbHealthService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean isDatabaseUp() {
        try {
            jdbc.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getDbVersion() {
        try {
            return jdbc.queryForObject("SELECT VERSION()", String.class);
        } catch (Exception e) {
            return "No disponible";
        }
    }
}