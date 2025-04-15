# 📚 BookYourStary - Plataforma de Reservas Turísticas

**BookYourStary** es una plataforma de reservas de alojamientos turísticos en diferentes ciudades de Colombia. Permite a los clientes buscar, reservar y calificar alojamientos, mientras que un administrador gestiona la plataforma, publica ofertas y analiza estadísticas.

---

## 🧱 Arquitectura del Proyecto

El proyecto sigue una **arquitectura por capas** y MVC que favorece la separación de responsabilidades:

### 🧩 Modelo (`model`)
Contiene las clases que representan las entidades del dominio, como `Cliente`, `Administrador`, `Alojamiento`, `Reserva`, `Factura`, `Ciudad`, `Habitacion`, etc.

### 💾 Repositorio (`repository`)
Encapsula el acceso a los datos. Define interfaces y métodos CRUD para interactuar con entidades. Solo debe ser utilizada por la capa de servicios.

```java
/**
 * Esta clase pertenece a la capa de Repositorio en la arquitectura por capas.
 * Se encarga de interactuar con la base de datos o almacenamiento persistente.
 * Define métodos para Crear, Leer, Actualizar y Eliminar entidades del dominio.
 */
