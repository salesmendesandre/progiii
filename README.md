# PROGIII Tools

Variante actualizada y documentada de la biblioteca de utilidades utilizada en la asignatura **Programación III** impartida en el **Grado en Ingeniería Informática**, **Grado en Matemáticas**, **Dobles Grados** y **Grado en Estadística** de la **Universidad de Salamanca (USAL)** (con contribución e inspiración histórica del equipo docente de la asignatura y la libreria del profesor *J. R. García-Bermejo Giner ("Coti")*).

- 📦 [Descargar `biblioteca_progiii.jar`](https://github.com/salesmendesandre/progiii/releases/latest/download/biblioteca_progiii.jar)
- 📖 [Documentación Javadoc](https://salesmendesandre.github.io/progiii/es/usal/progiii/tools/package-summary.html)

---

## Contenido

El paquete `es.usal.progiii.tools` incluye 3 utilidades principales:

- **`Esdia`**: Lectura interactiva y segura por consola con validación de tipos y rangos (`readInt`, `readDouble`, `readString`, `siOno`...), además de soporte para texto subrayado y secuencias ANSI.
- **`Rutas`**: Gestión de rutas multiplataforma (Escritorio, Documentos, Descargas, soporte para OneDrive, etc.) usando `java.nio.file.Path`.
- **`OpMat`**: Operaciones con matrices bidimensionales (generación aleatoria, operaciones aritméticas, importación/exportación a CSV y persistencia binaria).

---

## Cómo usar el JAR en tu proyecto

### En VS Code
1. Descarga [`biblioteca_progiii.jar`](https://github.com/salesmendesandre/progiii/releases/latest/download/biblioteca_progiii.jar).
2. En la vista **Java Projects** (panel lateral de VS Code), ve a **Referenced Libraries** y pulsa el botón **`+`**.
3. Selecciona el archivo `.jar` descargado.

### Por línea de comandos
```bash
# Compilar
javac -cp biblioteca_progiii.jar MiPrograma.java

# Ejecutar (Linux/Mac usa ':' y Windows ';')
java -cp .:biblioteca_progiii.jar MiPrograma
```

---

## Ejemplo de uso

```java
import es.usal.progiii.tools.Esdia;
import es.usal.progiii.tools.OpMat;
import es.usal.progiii.tools.Rutas;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Esdia.underline2("MI PROGRAMA");

        // Lectura segura por teclado
        String nombre = Esdia.readString_ne("Tu nombre: ");
        int filas = Esdia.readInt("Número de filas (1-10): ", 1, 10);
        int cols = Esdia.readInt("Número de columnas (1-10): ", 1, 10);

        // Generar y mostrar matriz
        int[][] matriz = OpMat.randomArrayOfInt(filas, cols, 1, 100);
        System.out.println("\nMatriz generada:");
        OpMat.printToScreen(matriz);

        // Guardar en CSV en el Escritorio
        if (Esdia.siOno("¿Guardar en el Escritorio?")) {
            File destino = Rutas.fileToFileOnDesktop("matriz.csv");
            try {
                OpMat.exportToDisk(matriz, destino, ";");
                System.out.println("Guardado en: " + destino.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("Error al guardar: " + e.getMessage());
            }
        }
    }
}
```

---

## Compilación y Tests

Para compilar la biblioteca desde el código fuente o pasar los tests:

```bash
# Compilar clases
javac -d out src/es/usal/progiii/tools/*.java src/es/usal/progiii/test/*.java

# Ejecutar tests
java -ea -cp out es.usal.progiii.test.TestProgiiiTools

# Generar el JAR
jar cvf biblioteca_progiii.jar -C out/ es/
```

---

## Créditos y Licencia

Basado en el material desarrollado por el equipo docente de la asignatura (Universidad de Salamanca).  
Distribuido bajo licencia MIT.
