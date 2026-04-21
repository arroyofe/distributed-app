# Módulo Python 1 (PY1) - API de Gestión Pokémon

## 📋 Descripción
Este módulo es un microservicio desarrollado con **Flask** encargado de la lógica de negocio y exposición de datos relativos a los Pokémon. Es una pieza clave de la arquitectura híbrida del proyecto, interactuando con el Frontend Java y compartiendo una base de datos **MySQL**.

## 🏗️ Funcionalidades Principales
*   **API REST**: Expone endpoints para el CRUD de Pokémon consumidos por el `PokemonService` de Java.
*   **Persistencia**: Gestión de datos en la base de datos común.
*   **Integración**: Proporciona datos dinámicos (habilidades, Pokémon aleatorios) para enriquecer el frontend.

## 🚀 Instalación y Ejecución Local (sin Docker)

### 1. Configuración del entorno
Copia el archivo de ejemplo y ajusta las credenciales de tu base de datos local:
```bash
cp .env.example .env
