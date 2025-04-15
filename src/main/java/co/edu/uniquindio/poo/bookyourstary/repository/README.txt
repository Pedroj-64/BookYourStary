/**
 * ================================================================
 * Capa: Repositorio (Repository)
 * Propósito: Encapsular el acceso a los datos del sistema
 * ================================================================
 *
 * Esta clase forma parte de la capa de Repositorio dentro de la 
 * arquitectura por capas. Su principal responsabilidad es manejar 
 * la persistencia de datos, actuando como intermediaria entre la 
 * aplicación y la base de datos (o cualquier otra fuente de datos).
 *
 * Aquí se definen los métodos necesarios para realizar operaciones
 * CRUD (Crear, Leer, Actualizar, Eliminar) sobre las entidades del 
 * dominio, así como consultas personalizadas cuando se requiera.
 *
 * Funciones típicas de un repositorio:
 * - Consultar registros (por ID, filtros, etc.).
 * - Guardar o actualizar entidades en el almacenamiento.
 * - Eliminar datos existentes.
 * - Ejecutar consultas específicas según la lógica del dominio.
 *
 * Esta capa garantiza la separación de responsabilidades, 
 * evitando que otras capas accedan directamente a la base de datos. 
 * De esta forma, se favorece la mantenibilidad, reutilización y 
 * testeo del sistema.
 *
 * Importante: Esta clase debe ser utilizada exclusivamente por la 
 * capa de Servicio, que se encarga de aplicar las reglas de negocio.
 *
 */
