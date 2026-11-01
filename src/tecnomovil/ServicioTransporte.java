package tecnomovil;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Servicio que contiene las operaciones funcionales del caso de estudio.
 * 
 * Todas las operaciones estan disenadas como funciones puras:
 * - No modifican la lista recibida por parametro (inmutabilidad).
 * - No tienen efectos secundarios (no escriben en consola ni cambian variables externas).
 * - Solo dependen de sus parametros de entrada y retornan nuevas estructuras.
 */
public class ServicioTransporte {

    // ---------------- Funciones de orden superior ----------------

    /**
     * Funcion de orden superior que recibe una lista y una condicion (Predicate).
     * Quien llama al metodo define el criterio usando una expresion lambda.
     */
    public static List<RegistroTransporte> filtrar(List<RegistroTransporte> registros,
                                                   Predicate<RegistroTransporte> criterio) {
        return registros.stream()
                .filter(criterio)
                .toList(); // toList() genera una lista inmutable
    }

    /**
     * Funcion de orden superior que recibe una funcion clasificadora (Function).
     * Agrupa y cuenta las ocurrencias del campo especificado para evitar repetir codigo.
     */
    public static Map<String, Long> contarPor(List<RegistroTransporte> registros,
                                              Function<RegistroTransporte, String> campo) {
        return registros.stream()
                .collect(Collectors.groupingBy(campo, TreeMap::new, Collectors.counting()));
    }

    // ---------------- a) Afluencia por estacion ----------------

    /**
     * Cuenta cuantos pasajeros ingresan a cada estacion.
     * Filtra los registros con accion "entrada" y luego cuenta por nombre de estacion.
     */
    public static Map<String, Long> afluenciaPorEstacion(List<RegistroTransporte> registros) {
        List<RegistroTransporte> entradas = filtrar(registros, r -> r.accion().equals("entrada"));
        return contarPor(entradas, RegistroTransporte::estacion);
    }

    // ---------------- b) Horas pico ----------------

    /**
     * Agrupa los registros por la hora del dia (0 a 23) y calcula el volumen de uso.
     */
    public static Map<Integer, Long> registrosPorHora(List<RegistroTransporte> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(r -> r.timestamp().getHour(),
                        TreeMap::new, Collectors.counting()));
    }

    /**
     * Identifica la hora o las horas con mayor flujo del dia buscando el valor maximo.
     */
    public static List<Integer> horasPico(List<RegistroTransporte> registros) {
        Map<Integer, Long> porHora = registrosPorHora(registros);
        long maximo = porHora.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);
        return porHora.entrySet().stream()
                .filter(e -> e.getValue() == maximo)
                .map(Map.Entry::getKey)
                .toList();
    }

    // ---------------- c) Rutas mas utilizadas ----------------

    /**
     * Determina el volumen de viajes por ruta y las ordena de mayor a menor uso.
     */
    public static List<Map.Entry<String, Long>> rutasMasUtilizadas(List<RegistroTransporte> registros) {
        return contarPor(registros, RegistroTransporte::ruta).entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .toList();
    }

    // ---------------- d) Patrones de viaje por usuario ----------------

    /**
     * Para cada usuario, obtiene la lista de estaciones visitadas en estricto orden cronologico.
     */
    public static Map<String, List<String>> patronesDeViaje(List<RegistroTransporte> registros) {
        return registros.stream()
                .sorted(Comparator.comparing(RegistroTransporte::timestamp))
                .collect(Collectors.groupingBy(RegistroTransporte::idUsuario, TreeMap::new,
                        Collectors.mapping(RegistroTransporte::estacion, Collectors.toList())));
    }

    // ---------------- e) Tiempo promedio entre estaciones ----------------

    /**
     * Calcula la duracion promedio (en minutos) entre una entrada y la salida consecutiva del mismo usuario.
     * Utiliza IntStream para recorrer pares contiguos sin usar bucles for tradicionales.
     */
    public static double tiempoPromedioEntreEstaciones(List<RegistroTransporte> registros) {
        Map<String, List<RegistroTransporte>> porUsuario = registros.stream()
                .sorted(Comparator.comparing(RegistroTransporte::timestamp))
                .collect(Collectors.groupingBy(RegistroTransporte::idUsuario));

        return porUsuario.values().stream()
                .flatMap(viajes -> IntStream.range(0, viajes.size() - 1)
                        .filter(i -> viajes.get(i).accion().equals("entrada")
                                && viajes.get(i + 1).accion().equals("salida"))
                        .mapToObj(i -> Duration.between(viajes.get(i).timestamp(),
                                viajes.get(i + 1).timestamp()).toMinutes()))
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);
    }

    // ---------------- f) Sobrecarga en rutas ----------------

    /**
     * Clasifica cada ruta como "CRITICA" si supera el umbral de ocupacion indicado, o "NORMAL" en caso contrario.
     */
    public static Map<String, String> estadoDeRutas(List<RegistroTransporte> registros, long umbral) {
        return contarPor(registros, RegistroTransporte::ruta).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() > umbral ? "CRITICA" : "NORMAL",
                        (a, b) -> a,
                        TreeMap::new));
    }
}
