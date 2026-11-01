package tecnomovil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase principal. Crea unos datos de prueba que simulan un dia del sistema
 * de transporte de TecnoValle y muestra el resultado de cada requerimiento.
 * Aqui si se imprime en consola; las funciones de ServicioTransporte no imprimen nada.
 */
public class Main {

    public static void main(String[] args) {

        // List.of crea una lista inmutable: nadie le puede agregar ni quitar elementos.
        List<RegistroTransporte> registros = List.of(
                reg("U01", "R1", "Central",     "entrada", 6, 50),
                reg("U01", "R1", "Universidad", "salida",  7, 20),
                reg("U02", "R2", "Poblado",     "entrada", 7, 0),
                reg("U02", "R2", "Industriales","salida",  7, 25),
                reg("U03", "R1", "Central",     "entrada", 7, 10),
                reg("U03", "R1", "Caribe",      "salida",  7, 40),
                reg("U04", "R3", "San Antonio", "entrada", 7, 30),
                reg("U04", "R3", "Cisneros",    "salida",  7, 45),
                reg("U05", "R1", "Central",     "entrada", 7, 45),
                reg("U05", "R1", "Universidad", "salida",  8, 15),
                reg("U01", "R1", "Universidad", "entrada", 12, 10),
                reg("U01", "R1", "Central",     "salida",  12, 40),
                reg("U06", "R2", "Envigado",    "entrada", 17, 15),
                reg("U06", "R2", "Poblado",     "salida",  17, 35),
                reg("U02", "R2", "Industriales","entrada", 17, 45),
                reg("U02", "R2", "Poblado",     "salida",  18, 10),
                reg("U07", "R1", "Caribe",      "entrada", 17, 50),
                reg("U07", "R1", "Central",     "salida",  18, 20),
                reg("U03", "R1", "Caribe",      "entrada", 18, 0),
                reg("U03", "R1", "Central",     "salida",  18, 30)
        );

        System.out.println("=== TecnoMovil Data - Procesamiento funcional ===");
        System.out.println("Registros cargados: " + registros.size());

        // a)
        System.out.println("\na) Afluencia por estacion (entradas):");
        ServicioTransporte.afluenciaPorEstacion(registros)
                .forEach((estacion, total) -> System.out.println("   " + estacion + " -> " + total));

        // b)
        System.out.println("\nb) Registros por hora:");
        ServicioTransporte.registrosPorHora(registros)
                .forEach((hora, total) -> System.out.println("   " + hora + ":00 -> " + total));
        System.out.println("   Hora(s) pico: " + ServicioTransporte.horasPico(registros));

        // c)
        System.out.println("\nc) Rutas mas utilizadas:");
        ServicioTransporte.rutasMasUtilizadas(registros)
                .forEach(e -> System.out.println("   " + e.getKey() + " -> " + e.getValue() + " registros"));

        // d)
        System.out.println("\nd) Patrones de viaje por usuario:");
        ServicioTransporte.patronesDeViaje(registros)
                .forEach((usuario, estaciones) -> System.out.println("   " + usuario + ": " + estaciones));

        // e)
        System.out.printf("%ne) Tiempo promedio entre estaciones: %.1f minutos%n",
                ServicioTransporte.tiempoPromedioEntreEstaciones(registros));

        // f)
        long umbral = 8;
        System.out.println("\nf) Estado de las rutas (umbral: mas de " + umbral + " registros):");
        ServicioTransporte.estadoDeRutas(registros, umbral)
                .forEach((ruta, estado) -> System.out.println("   " + ruta + " -> " + estado));

        // Orden superior con una lambda definida aqui mismo
        List<RegistroTransporte> manana = ServicioTransporte.filtrar(registros,
                r -> r.timestamp().getHour() < 9);
        System.out.println("\nExtra) Registros antes de las 9:00 (filtro con lambda): " + manana.size());

        // parallelStream: como las funciones son puras, el resultado es el mismo en paralelo
        long entradasSecuencial = registros.stream().filter(r -> r.accion().equals("entrada")).count();
        long entradasParalelo = registros.parallelStream().filter(r -> r.accion().equals("entrada")).count();
        System.out.println("Extra) Entradas con stream: " + entradasSecuencial
                + " | con parallelStream: " + entradasParalelo);

        // Inmutabilidad: la lista original no cambio despues de todo el procesamiento
        System.out.println("Extra) Registros en la lista original al final: " + registros.size());
    }

    /** Metodo de ayuda para crear registros mas corto (todos del mismo dia). */
    private static RegistroTransporte reg(String usuario, String ruta, String estacion,
                                          String accion, int hora, int minuto) {
        return new RegistroTransporte(usuario, ruta, estacion, accion,
                LocalDateTime.of(2026, 10, 30, hora, minuto));
    }
}
