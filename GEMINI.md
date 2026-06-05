# Proyecto: Gestión de Películas (En Memoria)

Este proyecto es una aplicación web basada en **Spring Boot 4.0.5** y **Java 17** para gestionar un catálogo de películas en memoria.

---

## 📂 Estructura del Proyecto

A continuación se detallan los componentes principales del proyecto con enlaces a sus respectivos archivos fuentes:

*   **Punto de Entrada**: [GestionPeliculasMemoriaApplication.java](file:///C:/Users/Alumnos_IBT/gestion_peliculas_memoria/src/main/java/cl/usm/gestionPeliculasMemoria/GestionPeliculasMemoriaApplication.java) - Clase principal para iniciar la aplicación.
*   **Controladores**: 
    *   [PeliculasController.java](file:///C:/Users/Alumnos_IBT/gestion_peliculas_memoria/src/main/java/cl/usm/gestionPeliculasMemoria/controllers/PeliculasController.java) - Controlador REST que expone los endpoints de la API.
*   **Servicios**:
    *   [PeliculasService.java](file:///C:/Users/Alumnos_IBT/gestion_peliculas_memoria/src/main/java/cl/usm/gestionPeliculasMemoria/services/PeliculasService.java) - Interfaz de lógica de negocio.
    *   [PeliculasServiceImpl.java](file:///C:/Users/Alumnos_IBT/gestion_peliculas_memoria/src/main/java/cl/usm/gestionPeliculasMemoria/services/PeliculasServiceImpl.java) - Implementación del servicio (incluye la generación segura de tokens de descarga usando Apache Commons Lang).
*   **Repositorio**:
    *   [PeliculasRepository.java](file:///C:/Users/Alumnos_IBT/gestion_peliculas_memoria/src/main/java/cl/usm/gestionPeliculasMemoria/repositories/PeliculasRepository.java) - Interfaz de acceso a datos.
    *   [PeliculasRepositoryImpl.java](file:///C:/Users/Alumnos_IBT/gestion_peliculas_memoria/src/main/java/cl/usm/gestionPeliculasMemoria/repositories/PeliculasRepositoryImpl.java) - Implementación en memoria del repositorio usando una colección (`ArrayList`).
*   **Entidades**:
    *   [Pelicula.java](file:///C:/Users/Alumnos_IBT/gestion_peliculas_memoria/src/main/java/cl/usm/gestionPeliculasMemoria/entities/Pelicula.java) - Clase que representa una película.
    *   [Comentario.java](file:///C:/Users/Alumnos_IBT/gestion_peliculas_memoria/src/main/java/cl/usm/gestionPeliculasMemoria/entities/Comentario.java) - Clase que representa los comentarios asociados a una película.
*   **Configuración y Dependencias**:
    *   [pom.xml](file:///C:/Users/Alumnos_IBT/gestion_peliculas_memoria/pom.xml) - Archivo de configuración de dependencias de Maven.
*   **Documentación de API**:
    *   [openapi.yaml](file:///C:/Users/Alumnos_IBT/gestion_peliculas_memoria/docs/openapi.yaml) - Definición de la API REST en formato OpenAPI / Swagger.

---

## 🚀 Endpoints de la API

Los endpoints se exponen por defecto en el puerto `8094` (según se detalla en la definición OpenAPI).

| Método | Endpoint | Descripción | Parámetros |
| :--- | :--- | :--- | :--- |
| **GET** | `/peliculas` | Obtiene la lista completa de películas o filtra por término de búsqueda. | `q` (query param opcional para filtrar por ID o título) |
| **POST** | `/peliculas` | Registra una nueva película en memoria. Genera automáticamente un `tokenDescarga` seguro de 10 caracteres. | Body con la estructura de la película (JSON) |
| **GET** | `/peliculas/{id}` | Obtiene los detalles de una película específica por su ID. | `id` (path variable) |
| **GET** | `/peliculas/{id}/comentarios` | Obtiene el listado de comentarios de una película específica. | `id` (path variable) |

---

## 🛠️ Ejecución y Compilación

Para compilar y empaquetar el proyecto usando el Maven Wrapper provisto:

```bash
# Compilar el proyecto
./mvnw clean package

# Iniciar la aplicación en desarrollo
./mvnw spring-boot:run
```
