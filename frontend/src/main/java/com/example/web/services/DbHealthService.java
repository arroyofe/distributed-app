package com.example.web.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio de diagnóstico para la salud de la base de datos.
 * <p>
 * Proporciona métodos para verificar la disponibilidad y obtener información
 * técnica de la instancia de MySQL compartida entre los módulos Java y Python.
 */
@Service
public class DbHealthService {

    private final JdbcTemplate jdbc;

    /**
     * Inyección del template JDBC de Spring.
     * @param jdbc Herramienta para la ejecución de consultas SQL nativas.
     */
    public DbHealthService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Verifica si la conexión con la base de datos está activa.
     * <p>
     * Ejecuta una consulta mínima de validación ("SELECT 1") para confirmar
     * que el motor de la base de datos responde correctamente.
     *
     * @return {@code true} si la conexión es exitosa, {@code false} en caso de excepción.
     */
    public boolean isDatabaseUp() {
        try {
            jdbc.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene la versión actual del motor de base de datos MySQL.
     *
     * @return Una cadena con la versión del servidor (ej. "8.0.32") o
     *         "No disponible" en caso de fallo en la consulta.
     */
    public String getDbVersion() {
        try {
            return jdbc.queryForObject("SELECT VERSION()", String.class);
        } catch (Exception e) {
            return "No disponible";
        }
    }
}
