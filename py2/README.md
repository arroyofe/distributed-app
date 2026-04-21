# Módulo Python 2 (PY2) - Servicio de Gestión de Ítems

## 📋 Descripción
Este microservicio basado en **Flask** actúa como el backend especializado para la gestión del catálogo de ítems del sistema. Su función principal es procesar las peticiones CRUD enviadas desde el frontend Java y asegurar la integridad de los datos en la base de datos compartida.

## 🏗️ Rol en la Arquitectura
*   **Gestor de Inventario**: Procesa las operaciones de creación, edición y borrado de ítems.
*   **Servidor de Producción**: Utiliza **Gunicorn** como servidor de aplicaciones WSGI para garantizar la estabilidad y el manejo de múltiples peticiones.
*   **Interoperabilidad**: Expone una interfaz REST que cumple estrictamente con el formato de datos definido en el `ItemDto` del frontend.

## 🚀 Instalación y Ejecución Local

### 1. Preparación del Entorno
Se ha creado un entorno virtual para gestionar las dependencias:
```bash
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
``