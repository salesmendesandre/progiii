package es.usal.progiii.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.out;
import static es.usal.progiii.tools.Esdia.readDouble;
import static es.usal.progiii.tools.Esdia.readFloat;
import static es.usal.progiii.tools.Esdia.readInt;
import static es.usal.progiii.tools.Esdia.readString_ne;

/**
 * Clase de utilidades para operaciones con matrices y arrays bidimensionales.
 * <p>
 * Incluye generación de matrices aleatorias, lectura interactiva por teclado,
 * visualización tabular formateada en consola, importación y exportación de archivos
 * estructurados (CSV y delimitados por separador), serialización binaria en disco
 * y operaciones de álgebra matricial (suma, producto, suma de diagonales y uniones).
 * </p>
 *
 * @author PROGIII - Universidad de Salamanca (USAL)
 * @version 2.0
 */
public class OpMat {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "áéíóúÁÉÍÓÚñÑ";

    /** Versión actual de la biblioteca PROGIII Tools. */
    public static final String VERSION = "1.0.0";

    /**
     * Devuelve la versión actual de la biblioteca.
     *
     * @return Cadena con el número de versión (ej. {@code "1.0.0"}).
     */
    public static String getVersion() {
        return VERSION;
    }

    /** Constructor privado para clase de utilidades estáticas. */
    private OpMat() {
    }

    // =========================================================================
    // GENERACIÓN DE CADENAS Y MATRICES ALEATORIAS
    // =========================================================================

    /**
     * Genera una cadena alfanumérica aleatoria con una longitud máxima dada.
     *
     * @param maxLength Longitud máxima de la cadena a generar (mínimo 1).
     * @return Cadena aleatoria generada.
     */
    private static String randomString(int maxLength) {
        int length = ThreadLocalRandom.current().nextInt(Math.max(1, maxLength)) + 1;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = ThreadLocalRandom.current().nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(idx));
        }
        return sb.toString();
    }

    /**
     * Genera una matriz de cadenas ({@code String[][]}) rellena con textos aleatorios.
     *
     * @param numRows    Número de filas.
     * @param numColumns Número de columnas.
     * @return Matriz de cadenas generada.
     */
    public static String[][] randomArrayOfString(int numRows, int numColumns) {
        validateDimensions(numRows, numColumns);
        String[][] matrix = new String[numRows][numColumns];
        final int maxLen = 8;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                matrix[r][c] = randomString(maxLen);
            }
        }
        return matrix;
    }

    /**
     * Genera una matriz de enteros ({@code int[][]}) con valores aleatorios entre 0 y 99.
     *
     * @param numRows    Número de filas.
     * @param numColumns Número de columnas.
     * @return Matriz de enteros generada.
     */
    public static int[][] randomArrayOfInt(int numRows, int numColumns) {
        return randomArrayOfInt(numRows, numColumns, 0, 100);
    }

    /**
     * Genera una matriz de enteros ({@code int[][]}) con valores aleatorios dentro de un rango [min, max).
     *
     * @param numRows    Número de filas.
     * @param numColumns Número de columnas.
     * @param min        Límite inferior (inclusive).
     * @param max        Límite superior (exclusive).
     * @return Matriz de enteros generada.
     */
    public static int[][] randomArrayOfInt(int numRows, int numColumns, int min, int max) {
        validateDimensions(numRows, numColumns);
        if (min >= max) {
            max = min + 1;
        }
        int[][] matrix = new int[numRows][numColumns];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                matrix[r][c] = ThreadLocalRandom.current().nextInt(min, max);
            }
        }
        return matrix;
    }

    /**
     * Genera una matriz de reales en precisión simple ({@code float[][]}) con valores aleatorios entre 0.0 y 1.0.
     *
     * @param numRows    Número de filas.
     * @param numColumns Número de columnas.
     * @return Matriz de floats generada.
     */
    public static float[][] randomArrayOfFloat(int numRows, int numColumns) {
        validateDimensions(numRows, numColumns);
        float[][] matrix = new float[numRows][numColumns];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                matrix[r][c] = ThreadLocalRandom.current().nextFloat();
            }
        }
        return matrix;
    }

    /**
     * Genera una matriz de reales en doble precisión ({@code double[][]}) con valores aleatorios entre 0.0 y 1.0.
     *
     * @param numRows    Número de filas.
     * @param numColumns Número de columnas.
     * @return Matriz de doubles generada.
     */
    public static double[][] randomArrayOfDouble(int numRows, int numColumns) {
        validateDimensions(numRows, numColumns);
        double[][] matrix = new double[numRows][numColumns];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                matrix[r][c] = ThreadLocalRandom.current().nextDouble();
            }
        }
        return matrix;
    }

    // =========================================================================
    // LECTURA DE MATRICES POR TECLADO
    // =========================================================================

    /**
     * Solicita por consola elemento a elemento los valores de una matriz de {@code double}.
     *
     * @param matrix Matriz a rellenar (no nula).
     * @throws IllegalArgumentException Si la matriz es nula o vacía.
     */
    public static void inputMat(double[][] matrix) {
        checkMatrixNotNullOrEmpty(matrix, "double[][]");
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[i][j] = readDouble(String.format("m[%d][%d] = ", i, j));
            }
        }
    }

    /**
     * Solicita por consola elemento a elemento los valores de una matriz de {@code float}.
     *
     * @param matrix Matriz a rellenar (no nula).
     * @throws IllegalArgumentException Si la matriz es nula o vacía.
     */
    public static void inputMat(float[][] matrix) {
        checkMatrixNotNullOrEmpty(matrix, "float[][]");
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[i][j] = readFloat(String.format("m[%d][%d] = ", i, j));
            }
        }
    }

    /**
     * Solicita por consola elemento a elemento los valores de una matriz de {@code int}.
     *
     * @param matrix Matriz a rellenar (no nula).
     * @throws IllegalArgumentException Si la matriz es nula o vacía.
     */
    public static void inputMat(int[][] matrix) {
        checkMatrixNotNullOrEmpty(matrix, "int[][]");
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[i][j] = readInt(String.format("m[%d][%d] = ", i, j));
            }
        }
    }

    /**
     * Solicita por consola elemento a elemento los valores de una matriz de {@code String}.
     *
     * @param matrix Matriz a rellenar (no nula).
     * @throws IllegalArgumentException Si la matriz es nula o vacía.
     */
    public static void inputMat(String[][] matrix) {
        checkMatrixNotNullOrEmpty(matrix, "String[][]");
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[i][j] = readString_ne(String.format("m[%d][%d] = ", i, j));
            }
        }
    }

    // =========================================================================
    // IMPRESIÓN Y FORMATEO DE MATRICES
    // =========================================================================

    /**
     * Imprime una matriz de cadenas con columnas de ancho fijo (20 caracteres).
     *
     * @param matrix Matriz a imprimir.
     */
    public static void printToScreen(String[][] matrix) {
        if (matrix == null) {
            out.println("[Matriz nula]");
            return;
        }
        for (String[] row : matrix) {
            out.print("|");
            if (row != null) {
                for (String value : row) {
                    out.printf(" %20s |", value != null ? value : "null");
                }
            }
            out.println();
        }
    }

    /**
     * Imprime una matriz de cadenas calculando un ancho uniforme para todas las columnas
     * basado en la celda más larga.
     *
     * @param matrix Matriz a imprimir.
     */
    public static void printToScreen2(String[][] matrix) {
        if (matrix == null) {
            out.println("[Matriz nula]");
            return;
        }
        int maxWidth = 1;
        for (String[] row : matrix) {
            if (row != null) {
                for (String col : row) {
                    if (col != null && col.length() > maxWidth) {
                        maxWidth = col.length();
                    }
                }
            }
        }
        String format = "| %" + maxWidth + "s ";
        for (String[] row : matrix) {
            if (row != null) {
                for (String value : row) {
                    out.printf(format, value != null ? value : "null");
                }
            }
            out.println("|");
        }
    }

    /**
     * Imprime una matriz de cadenas ajustando el ancho de cada columna individualmente
     * según su contenido máximo, sin bordes laterales.
     *
     * @param matrix Matriz a imprimir.
     */
    public static void printToScreen3(String[][] matrix) {
        if (matrix == null) {
            out.println("[Matriz nula]");
            return;
        }
        int[] colWidths = calculateColumnWidths(matrix);
        for (String[] row : matrix) {
            if (row != null) {
                for (int col = 0; col < row.length; col++) {
                    int w = col < colWidths.length ? colWidths[col] : 1;
                    String val = row[col] != null ? row[col] : "null";
                    out.printf("%" + w + "s  ", val);
                }
            }
            out.println();
        }
    }

    /**
     * Imprime una matriz de cadenas ajustando el ancho de cada columna individualmente
     * con separadores verticales {@code |}.
     *
     * @param matrix Matriz a imprimir.
     */
    public static void printToScreen4(String[][] matrix) {
        if (matrix == null) {
            out.println("[Matriz nula]");
            return;
        }
        int[] colWidths = calculateColumnWidths(matrix);
        for (String[] row : matrix) {
            out.print("|");
            if (row != null) {
                for (int col = 0; col < row.length; col++) {
                    int w = col < colWidths.length ? colWidths[col] : 1;
                    String val = row[col] != null ? row[col] : "null";
                    out.printf(" %" + w + "s |", val);
                }
            }
            out.println();
        }
    }

    /**
     * Convierte una matriz de cadenas en una representación formateada como {@link String}
     * con anchos de columna dinámicos.
     *
     * @param matrix Matriz a formatear.
     * @return Cadena con la tabla formateada.
     */
    public static String printToString3(String[][] matrix) {
        if (matrix == null) {
            return "[Matriz nula]\n";
        }
        int[] colWidths = calculateColumnWidths(matrix);
        StringBuilder sb = new StringBuilder();
        for (String[] row : matrix) {
            sb.append("|");
            if (row != null) {
                for (int col = 0; col < row.length; col++) {
                    int w = col < colWidths.length ? colWidths[col] : 1;
                    String val = row[col] != null ? row[col] : "null";
                    sb.append(String.format(" %" + w + "s |", val));
                }
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Imprime una matriz de {@code double} con formato numérico tabulado.
     *
     * @param matrix Matriz a imprimir.
     */
    public static void printToScreen(double[][] matrix) {
        if (matrix == null) {
            out.println("[Matriz nula]");
            return;
        }
        for (double[] row : matrix) {
            out.print("|");
            if (row != null) {
                for (double value : row) {
                    out.printf(" %10.3f ", value);
                }
            }
            out.println("|");
        }
    }

    /**
     * Imprime una matriz de {@code float} con formato numérico tabulado.
     *
     * @param matrix Matriz a imprimir.
     */
    public static void printToScreen(float[][] matrix) {
        if (matrix == null) {
            out.println("[Matriz nula]");
            return;
        }
        for (float[] row : matrix) {
            out.print("|");
            if (row != null) {
                for (float value : row) {
                    out.printf(" %10.3f ", value);
                }
            }
            out.println("|");
        }
    }

    /**
     * Imprime una matriz de {@code int} con formato numérico tabulado.
     *
     * @param matrix Matriz a imprimir.
     */
    public static void printToScreen(int[][] matrix) {
        if (matrix == null) {
            out.println("[Matriz nula]");
            return;
        }
        for (int[] row : matrix) {
            out.print("|");
            if (row != null) {
                for (int value : row) {
                    out.printf(" %8d ", value);
                }
            }
            out.println("|");
        }
    }

    private static int[] calculateColumnWidths(String[][] matrix) {
        int maxCols = 0;
        for (String[] row : matrix) {
            if (row != null && row.length > maxCols) {
                maxCols = row.length;
            }
        }
        int[] widths = new int[maxCols];
        Arrays.fill(widths, 1);
        for (String[] row : matrix) {
            if (row != null) {
                for (int c = 0; c < row.length; c++) {
                    if (row[c] != null && row[c].length() > widths[c]) {
                        widths[c] = row[c].length();
                    }
                }
            }
        }
        return widths;
    }

    // =========================================================================
    // PERSISTENCIA BINARIA (SERIALIZACIÓN)
    // =========================================================================

    /**
     * Guarda una matriz de {@code double} en disco en formato binario serializado.
     *
     * @param f      Archivo de destino.
     * @param matrix Matriz a guardar.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    public static void saveToDisk(File f, double[][] matrix) throws IOException {
        Objects.requireNonNull(f, "El archivo no puede ser nulo");
        Objects.requireNonNull(matrix, "La matriz no puede ser nula");
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)))) {
            oos.writeObject(matrix);
        }
    }

    /**
     * Guarda una matriz de {@code float} en disco en formato binario serializado.
     *
     * @param f      Archivo de destino.
     * @param matrix Matriz a guardar.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    public static void saveToDisk(File f, float[][] matrix) throws IOException {
        Objects.requireNonNull(f, "El archivo no puede ser nulo");
        Objects.requireNonNull(matrix, "La matriz no puede ser nula");
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)))) {
            oos.writeObject(matrix);
        }
    }

    /**
     * Guarda una matriz de {@code int} en disco en formato binario serializado.
     *
     * @param f      Archivo de destino.
     * @param matrix Matriz a guardar.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    public static void saveToDisk(File f, int[][] matrix) throws IOException {
        Objects.requireNonNull(f, "El archivo no puede ser nulo");
        Objects.requireNonNull(matrix, "La matriz no puede ser nula");
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)))) {
            oos.writeObject(matrix);
        }
    }

    /**
     * Carga una matriz de {@code double} serializada previamente desde disco.
     *
     * @param f     Archivo a leer.
     * @param dummy Parámetro de sobrecarga de tipo.
     * @return Matriz de {@code double} recuperada.
     * @throws Exception Si el archivo no existe, está corrupto o no corresponde al tipo.
     */
    public static double[][] loadFromDisk(File f, double dummy) throws Exception {
        Objects.requireNonNull(f, "El archivo no puede ser nulo");
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)))) {
            return (double[][]) ois.readObject();
        }
    }

    /**
     * Carga una matriz de {@code float} serializada previamente desde disco.
     *
     * @param f     Archivo a leer.
     * @param dummy Parámetro de sobrecarga de tipo.
     * @return Matriz de {@code float} recuperada.
     * @throws Exception Si el archivo no existe, está corrupto o no corresponde al tipo.
     */
    public static float[][] loadFromDisk(File f, float dummy) throws Exception {
        Objects.requireNonNull(f, "El archivo no puede ser nulo");
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)))) {
            return (float[][]) ois.readObject();
        }
    }

    /**
     * Carga una matriz de {@code int} serializada previamente desde disco.
     *
     * @param f     Archivo a leer.
     * @param dummy Parámetro de sobrecarga de tipo.
     * @return Matriz de {@code int} recuperada.
     * @throws Exception Si el archivo no existe, está corrupto o no corresponde al tipo.
     */
    public static int[][] loadFromDisk(File f, int dummy) throws Exception {
        Objects.requireNonNull(f, "El archivo no puede ser nulo");
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)))) {
            return (int[][]) ois.readObject();
        }
    }

    // =========================================================================
    // IMPORTACIÓN Y EXPORTACIÓN DE ARCHIVOS DELIMITADOS (CSV / TSV)
    // =========================================================================

    /**
     * Importa una matriz de cadenas {@code String[][]} desde un archivo delimitado por texto.
     *
     * @param f         Archivo a leer.
     * @param delimiter Cadena o expresión delimitadora entre columnas (por ejemplo {@code ","} o {@code "\t"}).
     * @return Matriz {@code String[][]} con los contenidos del archivo.
     * @throws IOException Si el archivo no existe o no se puede leer.
     */
    public static String[][] importFromDisk(File f, String delimiter) throws IOException {
        Objects.requireNonNull(f, "El archivo no puede ser nulo");
        Objects.requireNonNull(delimiter, "El delimitador no puede ser nulo");

        if (!f.exists()) {
            throw new IOException("El archivo especificado no existe: " + f.getAbsolutePath());
        }

        List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
        List<String[]> rows = new ArrayList<>();

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                rows.add(line.split(delimiter, -1));
            }
        }

        return rows.toArray(new String[0][]);
    }

    /**
     * Importa una matriz de {@code int} desde un archivo delimitado.
     *
     * @param f         Archivo a leer.
     * @param delimiter Delimitador de columnas.
     * @param dummy     Parámetro de sobrecarga.
     * @return Matriz de enteros importada.
     * @throws Exception Si ocurre un fallo de lectura o de conversión numérica.
     */
    public static int[][] importFromDisk(File f, String delimiter, int dummy) throws Exception {
        String[][] raw = importFromDisk(f, delimiter);
        int numRows = raw.length;
        if (numRows == 0) return new int[0][0];

        int numCols = raw[0].length;
        int[][] result = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                result[i][j] = Integer.parseInt(raw[i][j].trim());
            }
        }
        return result;
    }

    /**
     * Importa una matriz de {@code float} desde un archivo delimitado.
     *
     * @param f         Archivo a leer.
     * @param delimiter Delimitador de columnas.
     * @param dummy     Parámetro de sobrecarga.
     * @return Matriz de floats importada.
     * @throws Exception Si ocurre un fallo de lectura o de conversión numérica.
     */
    public static float[][] importFromDisk(File f, String delimiter, float dummy) throws Exception {
        String[][] raw = importFromDisk(f, delimiter);
        int numRows = raw.length;
        if (numRows == 0) return new float[0][0];

        int numCols = raw[0].length;
        float[][] result = new float[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                result[i][j] = Float.parseFloat(raw[i][j].trim().replace(',', '.'));
            }
        }
        return result;
    }

    /**
     * Importa una matriz de {@code double} desde un archivo delimitado.
     *
     * @param f         Archivo a leer.
     * @param delimiter Delimitador de columnas.
     * @param dummy     Parámetro de sobrecarga.
     * @return Matriz de doubles importada.
     * @throws Exception Si ocurre un fallo de lectura o de conversión numérica.
     */
    public static double[][] importFromDisk(File f, String delimiter, double dummy) throws Exception {
        String[][] raw = importFromDisk(f, delimiter);
        int numRows = raw.length;
        if (numRows == 0) return new double[0][0];

        int numCols = raw[0].length;
        double[][] result = new double[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                result[i][j] = Double.parseDouble(raw[i][j].trim().replace(',', '.'));
            }
        }
        return result;
    }

    /**
     * Exporta una matriz de cadenas a un archivo de texto con columnas separadas por un delimitador.
     *
     * @param matrix    Matriz a exportar.
     * @param f         Archivo de destino.
     * @param delimiter Separador entre columnas (ej. {@code ","} o {@code "\t"}).
     * @throws IOException Si ocurre un error de escritura.
     */
    public static void exportToDisk(String[][] matrix, File f, String delimiter) throws IOException {
        Objects.requireNonNull(matrix, "La matriz no puede ser nula");
        Objects.requireNonNull(f, "El archivo de destino no puede ser nulo");
        Objects.requireNonNull(delimiter, "El delimitador no puede ser nulo");

        StringBuilder sb = new StringBuilder();
        for (String[] row : matrix) {
            if (row != null) {
                sb.append(String.join(delimiter, row));
            }
            sb.append(System.lineSeparator());
        }
        Files.writeString(f.toPath(), sb.toString(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    /**
     * Exporta una matriz de enteros a un archivo delimitado.
     *
     * @param matrix    Matriz a exportar.
     * @param f         Archivo de destino.
     * @param delimiter Separador de columnas.
     * @throws IOException Si ocurre un error de escritura.
     */
    public static void exportToDisk(int[][] matrix, File f, String delimiter) throws IOException {
        Objects.requireNonNull(matrix, "La matriz no puede ser nula");
        Objects.requireNonNull(f, "El archivo de destino no puede ser nulo");
        Objects.requireNonNull(delimiter, "El delimitador no puede ser nulo");

        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            if (row != null && row.length > 0) {
                sb.append(row[0]);
                for (int c = 1; c < row.length; c++) {
                    sb.append(delimiter).append(row[c]);
                }
            }
            sb.append(System.lineSeparator());
        }
        Files.writeString(f.toPath(), sb.toString(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    /**
     * Exporta una matriz de floats a un archivo delimitado.
     *
     * @param matrix    Matriz a exportar.
     * @param f         Archivo de destino.
     * @param delimiter Separador de columnas.
     * @throws IOException Si ocurre un error de escritura.
     */
    public static void exportToDisk(float[][] matrix, File f, String delimiter) throws IOException {
        Objects.requireNonNull(matrix, "La matriz no puede ser nula");
        Objects.requireNonNull(f, "El archivo de destino no puede ser nulo");
        Objects.requireNonNull(delimiter, "El delimitador no puede ser nulo");

        StringBuilder sb = new StringBuilder();
        for (float[] row : matrix) {
            if (row != null && row.length > 0) {
                sb.append(row[0]);
                for (int c = 1; c < row.length; c++) {
                    sb.append(delimiter).append(row[c]);
                }
            }
            sb.append(System.lineSeparator());
        }
        Files.writeString(f.toPath(), sb.toString(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    /**
     * Exporta una matriz de doubles a un archivo delimitado.
     *
     * @param matrix    Matriz a exportar.
     * @param f         Archivo de destino.
     * @param delimiter Separador de columnas.
     * @throws IOException Si ocurre un error de escritura.
     */
    public static void exportToDisk(double[][] matrix, File f, String delimiter) throws IOException {
        Objects.requireNonNull(matrix, "La matriz no puede ser nula");
        Objects.requireNonNull(f, "El archivo de destino no puede ser nulo");
        Objects.requireNonNull(delimiter, "El delimitador no puede ser nulo");

        StringBuilder sb = new StringBuilder();
        for (double[] row : matrix) {
            if (row != null && row.length > 0) {
                sb.append(row[0]);
                for (int c = 1; c < row.length; c++) {
                    sb.append(delimiter).append(row[c]);
                }
            }
            sb.append(System.lineSeparator());
        }
        Files.writeString(f.toPath(), sb.toString(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    /**
     * Variante optimizada de exportación de matriz double a disco.
     *
     * @param matrix    Matriz a exportar.
     * @param f         Archivo de destino.
     * @param delimiter Separador de columnas.
     * @throws IOException Si ocurre un error de escritura.
     */
    public static void exportToDisk2(double[][] matrix, File f, String delimiter) throws IOException {
        exportToDisk(matrix, f, delimiter);
    }

    // =========================================================================
    // ÁLGEBRA MATRICIAL (SUMA, PRODUCTO, DIAGONALES)
    // =========================================================================

    /**
     * Calcula la suma de dos matrices de enteros de iguales dimensiones.
     *
     * @param a Primera matriz.
     * @param b Segunda matriz.
     * @return Matriz resultante de la suma elemento a elemento.
     * @throws IllegalArgumentException Si las dimensiones son incompatibles o alguna matriz es nula.
     */
    public static int[][] sum(int[][] a, int[][] b) {
        if (a == null || b == null || a.length != b.length || a[0].length != b[0].length) {
            throw new IllegalArgumentException("Las matrices no tienen dimensiones compatibles para la suma.");
        }
        int rows = a.length;
        int cols = a[0].length;
        int[][] res = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res[i][j] = a[i][j] + b[i][j];
            }
        }
        return res;
    }

    /**
     * Calcula la suma de dos matrices de floats de iguales dimensiones.
     *
     * @param a Primera matriz.
     * @param b Segunda matriz.
     * @return Matriz resultante de la suma.
     * @throws IllegalArgumentException Si las dimensiones son incompatibles o alguna matriz es nula.
     */
    public static float[][] sum(float[][] a, float[][] b) {
        if (a == null || b == null || a.length != b.length || a[0].length != b[0].length) {
            throw new IllegalArgumentException("Las matrices no tienen dimensiones compatibles para la suma.");
        }
        int rows = a.length;
        int cols = a[0].length;
        float[][] res = new float[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res[i][j] = a[i][j] + b[i][j];
            }
        }
        return res;
    }

    /**
     * Calcula la suma de dos matrices de doubles de iguales dimensiones.
     *
     * @param a Primera matriz.
     * @param b Segunda matriz.
     * @return Matriz resultante de la suma.
     * @throws IllegalArgumentException Si las dimensiones son incompatibles o alguna matriz es nula.
     */
    public static double[][] sum(double[][] a, double[][] b) {
        if (a == null || b == null || a.length != b.length || a[0].length != b[0].length) {
            throw new IllegalArgumentException("Las matrices no tienen dimensiones compatibles para la suma.");
        }
        int rows = a.length;
        int cols = a[0].length;
        double[][] res = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res[i][j] = a[i][j] + b[i][j];
            }
        }
        return res;
    }

    /**
     * Calcula el producto matricial {@code A × B} de dos matrices de enteros.
     *
     * @param a Matriz izquierda (m × k).
     * @param b Matriz derecha (k × n).
     * @return Matriz producto (m × n).
     * @throws IllegalArgumentException Si el número de columnas de A no coincide con las filas de B.
     */
    public static int[][] product(int[][] a, int[][] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0 || a[0].length != b.length) {
            throw new IllegalArgumentException("Las dimensiones de las matrices son incompatibles para la multiplicación.");
        }
        int rows = a.length;
        int cols = b[0].length;
        int kDim = b.length;
        int[][] res = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sum = 0;
                for (int k = 0; k < kDim; k++) {
                    sum += a[i][k] * b[k][j];
                }
                res[i][j] = sum;
            }
        }
        return res;
    }

    /**
     * Calcula el producto matricial {@code A × B} de dos matrices de floats.
     *
     * @param a Matriz izquierda (m × k).
     * @param b Matriz derecha (k × n).
     * @return Matriz producto (m × n).
     * @throws IllegalArgumentException Si el número de columnas de A no coincide con las filas de B.
     */
    public static float[][] product(float[][] a, float[][] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0 || a[0].length != b.length) {
            throw new IllegalArgumentException("Las dimensiones de las matrices son incompatibles para la multiplicación.");
        }
        int rows = a.length;
        int cols = b[0].length;
        int kDim = b.length;
        float[][] res = new float[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float sum = 0.0f;
                for (int k = 0; k < kDim; k++) {
                    sum += a[i][k] * b[k][j];
                }
                res[i][j] = sum;
            }
        }
        return res;
    }

    /**
     * Calcula el producto matricial {@code A × B} de dos matrices de doubles.
     *
     * @param a Matriz izquierda (m × k).
     * @param b Matriz derecha (k × n).
     * @return Matriz producto (m × n).
     * @throws IllegalArgumentException Si el número de columnas de A no coincide con las filas de B.
     */
    public static double[][] product(double[][] a, double[][] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0 || a[0].length != b.length) {
            throw new IllegalArgumentException("Las dimensiones de las matrices son incompatibles para la multiplicación.");
        }
        int rows = a.length;
        int cols = b[0].length;
        int kDim = b.length;
        double[][] res = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double sum = 0.0;
                for (int k = 0; k < kDim; k++) {
                    sum += a[i][k] * b[k][j];
                }
                res[i][j] = sum;
            }
        }
        return res;
    }

    /**
     * Suma los elementos de la diagonal principal de una matriz cuadrada de {@code double}.
     *
     * @param matrix Matriz cuadrada.
     * @return Suma de la diagonal principal.
     * @throws IllegalArgumentException Si la matriz es nula o no es cuadrada.
     */
    public static double sumMainDiagonal(double[][] matrix) {
        checkMatrixNotNullOrEmpty(matrix, "double[][]");
        if (matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("La matriz debe ser cuadrada para sumar su diagonal principal.");
        }
        double sum = 0.0;
        for (int i = 0; i < matrix.length; i++) {
            sum += matrix[i][i];
        }
        return sum;
    }

    /**
     * Suma los elementos de una columna específica en una matriz de {@code double}.
     *
     * @param matrix       Matriz bidimensional.
     * @param columnNumber Índice de la columna a sumar (0-indexado).
     * @return Suma de los elementos de dicha columna.
     * @throws IllegalArgumentException Si el índice de columna está fuera de rango.
     */
    public static double sumColumn(double[][] matrix, int columnNumber) {
        checkMatrixNotNullOrEmpty(matrix, "double[][]");
        if (columnNumber < 0 || columnNumber >= matrix[0].length) {
            throw new IllegalArgumentException("Índice de columna fuera de rango: " + columnNumber);
        }
        double sum = 0.0;
        for (double[] row : matrix) {
            if (row != null && columnNumber < row.length) {
                sum += row[columnNumber];
            }
        }
        return sum;
    }

    // =========================================================================
    // UNIÓN Y CONCATENACIÓN DE MATRICES
    // =========================================================================

    /**
     * Une horizontalmente (por columnas) dos matrices de cadenas {@code String[][]}.
     *
     * @param a Matriz izquierda.
     * @param b Matriz derecha.
     * @return Matriz combinada horizontalmente.
     * @throws IllegalArgumentException Si tienen distinto número de filas o son nulas.
     */
    public static String[][] join(String[][] a, String[][] b) {
        if (a == null || b == null || a.length != b.length) {
            throw new IllegalArgumentException("Las matrices a unir deben tener el mismo número de filas.");
        }
        int numRows = a.length;
        int colsA = a.length > 0 && a[0] != null ? a[0].length : 0;
        int colsB = b.length > 0 && b[0] != null ? b[0].length : 0;
        String[][] res = new String[numRows][colsA + colsB];

        for (int r = 0; r < numRows; r++) {
            if (a[r] != null) {
                System.arraycopy(a[r], 0, res[r], 0, a[r].length);
            }
            if (b[r] != null) {
                System.arraycopy(b[r], 0, res[r], colsA, b[r].length);
            }
        }
        return res;
    }

    /**
     * Une horizontalmente una matriz {@code String[][]} y una columna {@code String[]}.
     *
     * @param a Matriz base (izquierda).
     * @param b Columna a añadir (derecha).
     * @return Matriz combinada.
     */
    public static String[][] join(String[][] a, String[] b) {
        if (a == null || b == null || a.length != b.length) {
            throw new IllegalArgumentException("La matriz y el vector deben tener la misma cantidad de filas.");
        }
        int numRows = a.length;
        int colsA = a.length > 0 && a[0] != null ? a[0].length : 0;
        String[][] res = new String[numRows][colsA + 1];

        for (int r = 0; r < numRows; r++) {
            if (a[r] != null) {
                System.arraycopy(a[r], 0, res[r], 0, a[r].length);
            }
            res[r][colsA] = b[r];
        }
        return res;
    }

    /**
     * Une horizontalmente una columna {@code String[]} y una matriz {@code String[][]}.
     *
     * @param a Columna a añadir (izquierda).
     * @param b Matriz base (derecha).
     * @return Matriz combinada.
     */
    public static String[][] join(String[] a, String[][] b) {
        if (a == null || b == null || a.length != b.length) {
            throw new IllegalArgumentException("El vector y la matriz deben tener la misma cantidad de filas.");
        }
        int numRows = a.length;
        int colsB = b.length > 0 && b[0] != null ? b[0].length : 0;
        String[][] res = new String[numRows][1 + colsB];

        for (int r = 0; r < numRows; r++) {
            res[r][0] = a[r];
            if (b[r] != null) {
                System.arraycopy(b[r], 0, res[r], 1, b[r].length);
            }
        }
        return res;
    }

    /**
     * Concatena dos arrays unidimensionales de cadenas {@code String[]}.
     *
     * @param a Primer array.
     * @param b Segundo array.
     * @return Array concatenado resultante.
     */
    public static String[] join(String[] a, String[] b) {
        if (a == null) return b != null ? b.clone() : new String[0];
        if (b == null) return a.clone();
        String[] res = new String[a.length + b.length];
        System.arraycopy(a, 0, res, 0, a.length);
        System.arraycopy(b, 0, res, a.length, b.length);
        return res;
    }

    /**
     * Concatena verticalmente (por filas) dos matrices {@code String[][]}.
     *
     * @param a Matriz superior.
     * @param b Matriz inferior.
     * @return Matriz combinada resultante.
     */
    public static String[][] concat(String[][] a, String[][] b) {
        if (a == null) return b != null ? b.clone() : new String[0][];
        if (b == null) return a.clone();
        String[][] res = new String[a.length + b.length][];
        System.arraycopy(a, 0, res, 0, a.length);
        System.arraycopy(b, 0, res, a.length, b.length);
        return res;
    }

    /**
     * Concatena verticalmente una fila {@code String[]} y una matriz {@code String[][]}.
     *
     * @param a Fila superior.
     * @param b Matriz inferior.
     * @return Matriz combinada resultante.
     */
    public static String[][] concat(String[] a, String[][] b) {
        if (a == null) return b != null ? b.clone() : new String[0][];
        if (b == null) return new String[][]{a};
        String[][] res = new String[1 + b.length][];
        res[0] = a;
        System.arraycopy(b, 0, res, 1, b.length);
        return res;
    }

    // =========================================================================
    // VALIDACIONES INTERNAS
    // =========================================================================

    private static void validateDimensions(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Las dimensiones deben ser positivas (filas > 0 y columnas > 0).");
        }
    }

    private static void checkMatrixNotNullOrEmpty(Object matrix, String typeName) {
        if (matrix == null) {
            throw new IllegalArgumentException("La matriz " + typeName + " no puede ser nula.");
        }
    }
}
