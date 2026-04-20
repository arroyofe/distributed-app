package com.example.web.utils;

public class ErrorClassifier {

    public static String classify(Exception ex) {
        if (ex instanceof java.io.IOException) return "Archivo";
        if (ex instanceof java.sql.SQLException) return "Base de datos";
        if (ex instanceof org.springframework.web.client.RestClientException) return "API externa";
        if (ex instanceof IllegalArgumentException) return "Validación";
        return "General";
    }
}
