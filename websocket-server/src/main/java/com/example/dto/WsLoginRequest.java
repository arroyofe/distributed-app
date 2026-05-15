package com.example.dto;

/**
 * DTO utilizado para recibir las credenciales de inicio de sesión
 * enviadas por el cliente.
 * <p>
 * Contiene el nombre de usuario y la contraseña en texto plano,
 * que serán validados por el controlador de autenticación.
 */
public class WsLoginRequest {

    /**
     * Nombre de usuario enviado por el cliente.
     */
    private String username;

    /**
     * Contraseña enviada por el cliente.
     */
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
