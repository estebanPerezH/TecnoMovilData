# TecnoMovil Data – Procesamiento funcional en Java

- **Actividad:** EA2. Creación de repositorio
- **Asignatura:** Programación Orientada a Objetos II – IU Digital de Antioquia
- **Docente:** Leonardo Montes Gutiérrez
- **Estudiante:** Jhoan Esteban Pérez Herrera (trabajo individual) – 2026

## ¿De qué se trata?
Solución al caso de estudio *TecnoMovil Data*: procesar los registros de entrada y salida
del sistema de transporte de la ciudad de TecnoValle usando programación funcional en Java
(funciones puras, inmutabilidad, lambdas, funciones de orden superior y Streams).
No se usa ningún framework externo, únicamente las capacidades nativas del lenguaje Java.

## Estructura del proyecto
```
src/tecnomovil/
 ├── RegistroTransporte.java   -> record inmutable (idUsuario, ruta, estacion, accion, timestamp)
 ├── ServicioTransporte.java   -> funciones puras con Streams (requerimientos a-f)
 └── Main.java                 -> datos de prueba simulados y ejecucion
```

## Requerimientos resueltos
| | Requerimiento | Método en ServicioTransporte |
|---|---|---|
| a | Afluencia por estación | `afluenciaPorEstacion` |
| b | Horas pico | `registrosPorHora`, `horasPico` |
| c | Rutas más utilizadas | `rutasMasUtilizadas` |
| d | Patrones de viaje por usuario | `patronesDeViaje` |
| e | Tiempo promedio entre estaciones | `tiempoPromedioEntreEstaciones` |
| f | Sobrecarga en rutas | `estadoDeRutas` |

Funciones de orden superior implementadas: `filtrar(lista, Predicate)` y `contarPor(lista, Function)`.

## Cómo ejecutarlo
Requiere Java 17 o superior (probado en Java 21 LTS).
```bash
# 1. Compilar clases
javac -d bin src/tecnomovil/*.java

# 2. Ejecutar clase principal
java -cp bin tecnomovil.Main
```

## Archivos de entrega
- `EA2_Creación_de_repositorio_PerezJhoan.pdf` – Informe académico completo bajo normas APA 7.ª edición.
- `TecnoMovilData.zip` – Código fuente comprimido del proyecto.
