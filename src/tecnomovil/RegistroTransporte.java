package tecnomovil;

import java.time.LocalDateTime;

/**
 * Representa un registro de paso por estación en el sistema TecnoValle.
 * 
 * Uso un record en vez de una clase normal porque en Java los records son
 * inmutables por defecto: todos sus campos son final, no tienen setters y
 * evitan que alguien modifique los datos originales durante el procesamiento.
 */
public record RegistroTransporte(
        String idUsuario,
        String ruta,
        String estacion,
        String accion,          // "entrada" o "salida"
        LocalDateTime timestamp
) {
}
