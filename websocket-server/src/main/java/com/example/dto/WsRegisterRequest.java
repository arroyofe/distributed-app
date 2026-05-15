package com.example.dto;

/**
 * DTO utilizado para recibir los datos de registro de un nuevo usuario.
 * <p>
 * Incluye el nombre de usuario y la contraseña en texto plano.
 * El controlador correspondiente se encarga de validar y almacenar
 * la información.
 */
public class WsRegisterRequest {

    /**
     * Nombre de usuario elegido por el nuevo cliente.
     */
    private String username;

    /**
     * Contraseña elegida por el nuevo cliente.
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
