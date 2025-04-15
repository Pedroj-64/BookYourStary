/**
 * ================================================================
 * Capa: Controlador (Controller) – Patrón de diseño MVC
 * Propósito: Coordinar la interacción entre la Vista y el Modelo
 * ================================================================
 *
 * La carpeta "controller" contiene las clases encargadas de gestionar
 * la lógica de control de la aplicación. Estas clases actúan como 
 * intermediarias entre la Vista (interfaz de usuario) y el Modelo 
 * (datos y lógica del dominio).
 *
 * Responsabilidades principales:
 * - Recibir y procesar las acciones del usuario (eventos, comandos, etc.).
 * - Invocar métodos en las clases del Modelo para consultar o modificar datos.
 * - Actualizar la Vista según los resultados de las operaciones.
 * - Coordinar la ejecución de reglas de negocio utilizando los servicios.
 *
 * Esta capa permite mantener una clara separación de responsabilidades,
 * facilitando la organización del código, su mantenibilidad y su escalabilidad.
 * 
 * Nota: Los controladores no deben contener lógica de acceso a datos 
 * directamente ni lógica compleja de negocio. Para ello, deben apoyarse 
 * en la capa de servicios.
 */
