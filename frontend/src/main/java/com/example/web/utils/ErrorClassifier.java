package com.example.web.utils;

/**
 * Utilidad para la categorización semántica de excepciones.
 * <p>
 * Proporciona una lógica centralizada para transformar excepciones técnicas
 * en categorías amigables, facilitando su visualización en la interfaz de usuario.
 */
public class ErrorClassifier {

    /**
     * Clasifica una excepción en una categoría funcional basada en su tipo.
     * <p>
     * Mapea excepciones comunes a etiquetas descriptivas:
     * <ul>
     *   <li>{@link java.io.IOException} -> "Archivo"</li>
     *   <li>{@link java.sql.SQLException} -> "Base de datos"</li>
     *   <li>{@code RestClientException} -> "API externa" (servicios Python)</li>
     *   <li>{@link IllegalArgumentException} -> "Validación"</li>
     * </ul>
     *
     * @param ex La excepción capturada durante la ejecución.
     * @return Una cadena con la categoría del error, o "General" si no hay coincidencia específica.
     */
    public static String classify(Exception ex) {
        if (ex instanceof java.io.IOException) return "Archivo";
        if (ex instanceof java.sql.SQLException) return "Base de datos";
        if (ex instanceof org.springframework.web.client.RestClientException) return "API externa";
        if (ex instanceof IllegalArgumentException) return "Validación";
        return "General";
    }
}

