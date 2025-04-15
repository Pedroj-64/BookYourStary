/*
 * ======================================================
 * Carpeta: service
 * Propósito: Lógica de negocio del sistema BookYourStary
 * ======================================================
 *
 * En esta carpeta se encuentran las clases e interfaces 
 * encargadas de implementar la lógica de negocio de la
 * aplicación. 
 * 
 * La capa de servicios actúa como intermediaria entre 
 * los controladores (interfaz gráfica) y los repositorios 
 * (acceso a datos), asegurando que se cumplan todas las 
 * reglas y validaciones del dominio.
 * 
 * Por ejemplo:
 * - Validar si un cliente tiene saldo suficiente en la billetera
 *   antes de confirmar una reserva.
 * - Verificar la disponibilidad de un alojamiento en las fechas 
 *   seleccionadas.
 * - Aplicar descuentos y promociones según las ofertas activas.
 * - Calcular estadísticas y métricas de rendimiento para los 
 *   alojamientos.
 * - Gestionar la creación de facturas, códigos de activación, 
 *   envío de correos, etc.
 *
 * Cada servicio trabaja generalmente con uno o varios repositorios
 * y expone métodos que pueden ser llamados desde los controladores.
 * 
 * Siguiendo el principio de responsabilidad única (SRP - SOLID), 
 * cada clase de servicio debe encargarse de un único caso de uso 
 * del negocio, como:
 *   - ClienteService
 *   - AlojamientoService
 *   - ReservaService
 *   - FacturaService
 *   - EstadisticaService
 *   - AutenticacionService
 * 
 * Esta organización mejora la mantenibilidad del código, facilita 
 * la realización de pruebas unitarias (JUnit) y permite una 
 * evolución ordenada del sistema.
 *
 */
