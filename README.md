# TecnoMovil Data – Procesamiento funcional en Java

- **Actividad:** EA2. Creación de repositorio
- **Asignatura:** Programación Orientada a Objetos II – IU Digital de Antioquia
- **Docente:** Leonardo Montes Gutiérrez
- **Estudiante:** Jhoan Esteban Pérez Henao (trabajo individual) – 2026

## ¿De qué se trata?
Solución al caso de estudio *TecnoMovil Data*: procesar los registros de entrada y salida
del sistema de transporte de la ciudad de TecnoValle usando programación funcional en Java
(funciones puras, inmutabilidad, lambdas, funciones de orden superior y Streams).
No se usa ningún framework, solo Java.

## Estructura
```
src/tecnomovil/
 ├── RegistroTransporte.java   -> record inmutable (idUsuario, ruta, estacion, accion, timestamp)
 ├── ServicioTransporte.java   -> funciones puras con Streams (requerimientos a-f)
 └── Main.java                 -> datos de prueba y ejecución
```

## Requerimientos resueltos
| | Requerimiento | Método |
|---|---|---|
| a | Afluencia por estación | `afluenciaPorEstacion` |
| b | Horas pico | `registrosPorHora`, `horasPico` |
| c | Rutas más utilizadas | `rutasMasUtilizadas` |
| d | Patrones de viaje por usuario | `patronesDeViaje` |
| e | Tiempo promedio entre estaciones | `tiempoPromedioEntreEstaciones` |
| f | Sobrecarga en rutas | `estadoDeRutas` |

Funciones de orden superior: `filtrar(lista, Predicate)` y `contarPor(lista, Function)`.

## Cómo ejecutarlo
Requiere Java 17 o superior (probado con Java 21).
```bash
javac -d bin src/tecnomovil/*.java
java -cp bin tecnomovil.Main
```

## Archivos de la entrega
- `EA2_Creación_de_repositorio_PerezJhoan.pdf` – informe en normas APA 7
- `TecnoMovilData.zip` – proyecto comprimido
