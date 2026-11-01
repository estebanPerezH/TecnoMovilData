package tecnomovil;

import java.time.LocalDateTime;

/**
 * Representa un registro del sistema de transporte de TecnoValle.
 * Cada vez que un usuario pasa la tarjeta por un validador se genera uno.
 *
 * Use un record porque sus atributos son final: una vez se crea el objeto
 * no se puede modificar (inmutabilidad). Asi ninguna funcion puede
 * cambiar los datos originales por error.
 */
public record RegistroTransporte(
        String idUsuario,
        String ruta,
        String estacion,
        String accion,          // "entrada" o "salida"
        LocalDateTime timestamp
) {
}
