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
 * Aqui estan todas las funciones que procesan los registros.
 *
 * Todas son funciones puras:
 *  - Solo usan lo que reciben por parametro.
 *  - No modifican la lista original (los streams crean resultados nuevos).
 *  - No imprimen ni guardan nada, solo retornan un resultado.
 * Por eso, si les paso los mismos datos, siempre devuelven lo mismo.
 */
public class ServicioTransporte {

    // ---------------- Funciones de orden superior ----------------

    /**
     * Funcion de orden superior: recibe otra funcion (un Predicate) como parametro.
     * El criterio de filtro lo decide quien la llama, usando una lambda.
     */
    public static List<RegistroTransporte> filtrar(List<RegistroTransporte> registros,
                                                   Predicate<RegistroTransporte> criterio) {
        return registros.stream()
                .filter(criterio)
                .toList(); // toList() devuelve una lista inmutable
    }

    /**
     * Funcion de orden superior: recibe una Function que dice por que campo agrupar
     * (estacion, ruta, usuario...) y cuenta cuantos registros hay en cada grupo.
     * La reutilizo en varios requerimientos para no repetir codigo.
     */
    public static Map<String, Long> contarPor(List<RegistroTransporte> registros,
                                              Function<RegistroTransporte, String> campo) {
        return registros.stream()
                .collect(Collectors.groupingBy(campo, TreeMap::new, Collectors.counting()));
    }

    // ---------------- a) Afluencia por estacion ----------------

    /** Cuenta cuantas personas ENTRAN a cada estacion. */
    public static Map<String, Long> afluenciaPorEstacion(List<RegistroTransporte> registros) {
        List<RegistroTransporte> entradas = filtrar(registros, r -> r.accion().equals("entrada"));
        return contarPor(entradas, RegistroTransporte::estacion);
    }

    // ---------------- b) Horas pico ----------------

    /** Agrupa los registros por la hora del dia (0 a 23) y cuenta cuantos hay en cada hora. */
    public static Map<Integer, Long> registrosPorHora(List<RegistroTransporte> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(r -> r.timestamp().getHour(),
                        TreeMap::new, Collectors.counting()));
    }

    /** Devuelve las horas pico: las horas que tienen la mayor cantidad de registros. */
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

    /** Devuelve las rutas ordenadas de la mas usada a la menos usada. */
    public static List<Map.Entry<String, Long>> rutasMasUtilizadas(List<RegistroTransporte> registros) {
        return contarPor(registros, RegistroTransporte::ruta).entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .toList();
    }

    // ---------------- d) Patrones de viaje por usuario ----------------

    /** Para cada usuario, lista las estaciones por donde paso, en orden de hora. */
    public static Map<String, List<String>> patronesDeViaje(List<RegistroTransporte> registros) {
        return registros.stream()
                .sorted(Comparator.comparing(RegistroTransporte::timestamp))
                .collect(Collectors.groupingBy(RegistroTransporte::idUsuario, TreeMap::new,
                        Collectors.mapping(RegistroTransporte::estacion, Collectors.toList())));
    }

    // ---------------- e) Tiempo promedio entre estaciones ----------------

    /**
     * Calcula el tiempo promedio (en minutos) entre una entrada y la salida
     * siguiente del mismo usuario, es decir, lo que dura el viaje entre dos estaciones.
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

    /** Marca cada ruta como "CRITICA" si sus registros superan el umbral, o "NORMAL" si no. */
    public static Map<String, String> estadoDeRutas(List<RegistroTransporte> registros, long umbral) {
        return contarPor(registros, RegistroTransporte::ruta).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() > umbral ? "CRITICA" : "NORMAL",
                        (a, b) -> a,
                        TreeMap::new));
    }
}
