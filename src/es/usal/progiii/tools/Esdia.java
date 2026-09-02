package es.usal.progiii.tools;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

/**
 * Clase de utilidades para la entrada y salida interactiva por consola.
 * <p>
 * Proporciona métodos estáticos y seguros para solicitar datos numéricos, alfanuméricos,
 * rutas y confirmaciones booleanas al usuario a través del teclado, con validación
 * en bucle, control de excepciones y soporte para caracteres especiales y secuencias ANSI.
 * </p>
 *
 * @author PROGIII - Universidad de Salamanca (USAL)
 * @version 2.0
 */
public class Esdia {

    // =========================================================================
    // CONSTANTES DE COLOR ANSI (Texto / Foreground)
    // =========================================================================

    /** Restablece el formato y color de la terminal al estado por defecto. */
    public static final String ANSI_RESET = "\u001B[0m";

    /** Color de texto negro. */
    public static final String ANSI_BLACK = "\u001B[30m";

    /** Color de texto rojo. */
    public static final String ANSI_RED = "\u001B[31m";

    /** Color de texto verde. */
    public static final String ANSI_GREEN = "\u001B[32m";

    /** Color de texto amarillo. */
    public static final String ANSI_YELLOW = "\u001B[33m";

    /** Color de texto azul. */
    public static final String ANSI_BLUE = "\u001B[34m";

    /** Color de texto púrpura / magenta. */
    public static final String ANSI_PURPLE = "\u001B[35m";

    /** Color de texto cian. */
    public static final String ANSI_CYAN = "\u001B[36m";

    /** Color de texto blanco. */
    public static final String ANSI_WHITE = "\u001B[37m";

    // =========================================================================
    // CONSTANTES DE COLOR ANSI (Fondo / Background)
    // =========================================================================

    /** Color de fondo negro. */
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";

    /** Color de fondo rojo. */
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";

    /** Color de fondo verde. */
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    /** Color de fondo amarillo. */
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    /** Color de fondo azul. */
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

    /** Color de fondo púrpura / magenta. */
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    /** Color de fondo cian. */
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";

    /** Color de fondo blanco. */
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // =========================================================================
    // CONFIGURACIÓN DEL SCANNER
    // =========================================================================

    /**
     * Instancia única de {@link Scanner} configurada con el conjunto de caracteres
     * por defecto del sistema operativo para evitar problemas de codificación.
     */
    public static final Scanner SC = initScanner();

    private static Scanner initScanner() {
        try {
            Charset charset = Charset.defaultCharset();
            return new Scanner(in, charset);
        } catch (Exception e) {
            return new Scanner(in, StandardCharsets.UTF_8);
        }
    }

    // =========================================================================
    // INFORMACIÓN DE VERSIÓN
    // =========================================================================

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

    /**
     * Muestra en la salida estándar la información de versión y atribución de la biblioteca.
     */
    public static void printVersion() {
        out.printf("PROGIII Tools v%s — Universidad de Salamanca (USAL)%n", VERSION);
    }

    /** Constructor privado para prevenir instanciación de clase de utilidades. */
    private Esdia() {
    }

    // =========================================================================
    // LECTURA DE TEXTO (STRINGS)
    // =========================================================================

    /**
     * Muestra un mensaje en consola y lee una línea de texto completa introducida por el usuario.
     *
     * @param prompt Mensaje de solicitud para el usuario.
     * @return La cadena de texto introducida (puede estar vacía).
     */
    public static String readString(String prompt) {
        if (prompt != null) {
            out.print(prompt);
        }
        return SC.nextLine();
    }

    /**
     * Muestra un mensaje y lee una cadena de texto, garantizando que no esté vacía ni contenga
     * únicamente espacios en blanco.
     *
     * @param prompt Mensaje de solicitud para el usuario.
     * @return Una cadena de texto no vacía.
     */
    public static String readString_ne(String prompt) {
        String input;
        do {
            input = readString(prompt);
            if (input == null || input.trim().isEmpty()) {
                out.println("Error: No se permiten entradas vacías. Por favor, inténtelo de nuevo.");
            }
        } while (input == null || input.trim().isEmpty());
        return input;
    }

    /**
     * Solicita al usuario elegir entre dos opciones de texto válidas.
     *
     * @param prompt Mensaje base de solicitud.
     * @param op1    Primera opción válida.
     * @param op2    Segunda opción válida.
     * @return La opción elegida (coincidente de forma exacta con {@code op1} o {@code op2}).
     */
    public static String readString(String prompt, String op1, String op2) {
        Objects.requireNonNull(op1, "La opción 1 no puede ser nula");
        Objects.requireNonNull(op2, "La opción 2 no puede ser nula");

        String fullPrompt = String.format("%s (%s / %s): ", prompt != null ? prompt.trim() : "", op1, op2);
        String input;
        boolean valid = false;

        do {
            input = readString_ne(fullPrompt).trim();
            if (input.equals(op1) || input.equals(op2)) {
                valid = true;
            } else {
                out.printf("Opción no válida. Debe escribir '%s' o '%s'.%n%n", op1, op2);
            }
        } while (!valid);

        return input;
    }

    /**
     * Solicita al usuario seleccionar una opción de entre un conjunto de opciones permitidas.
     *
     * @param prompt  Mensaje base de solicitud.
     * @param options Array de opciones válidas.
     * @return La opción introducida por el usuario coincidente con una del array.
     * @throws IllegalArgumentException Si el array de opciones es nulo o está vacío.
     */
    public static String readString(String prompt, String[] options) {
        if (options == null || options.length == 0) {
            throw new IllegalArgumentException("El listado de opciones no puede ser nulo ni estar vacío.");
        }

        String optionsFormatted = String.join(", ", options);
        String fullPrompt = String.format("%s [%s]: ", prompt != null ? prompt.trim() : "", optionsFormatted);
        List<String> validOptions = Arrays.asList(options);
        String input;
        boolean valid = false;

        do {
            input = readString_ne(fullPrompt).trim();
            if (validOptions.contains(input)) {
                valid = true;
            } else {
                out.printf("'%s' no es una opción válida. Por favor, seleccione una de: [%s]%n%n", input, optionsFormatted);
            }
        } while (!valid);

        return input;
    }

    // =========================================================================
    // LECTURA DE NÚMEROS ENTEROS (INT / LONG)
    // =========================================================================

    /**
     * Muestra un mensaje y solicita un número entero ({@code int}), repitiendo la petición
     * hasta que la entrada sea válida.
     *
     * @param prompt Mensaje de solicitud.
     * @return El número entero introducido.
     */
    public static int readInt(String prompt) {
        int value = 0;
        boolean valid = false;
        do {
            String input = readString_ne(prompt);
            try {
                value = Integer.parseInt(input.trim());
                valid = true;
            } catch (NumberFormatException e) {
                out.println("Error: El valor introducido no es un número entero válido. Inténtelo de nuevo.");
            }
        } while (!valid);
        return value;
    }

    /**
     * Muestra un mensaje y solicita un número entero dentro de un rango cerrado [min, max].
     *
     * @param prompt Mensaje de solicitud.
     * @param min    Valor mínimo permitido (inclusive).
     * @param max    Valor máximo permitido (inclusive).
     * @return El número entero validado dentro del rango.
     */
    public static int readInt(String prompt, int min, int max) {
        if (min > max) {
            int tmp = min;
            min = max;
            max = tmp;
        }

        String fullPrompt = String.format("%s (%d <= n <= %d): ", prompt != null ? prompt.trim() : "", min, max);
        int value = 0;
        boolean valid = false;

        do {
            value = readInt(fullPrompt);
            if (value < min || value > max) {
                out.printf("Error: Debe introducir un número comprendido entre %d y %d.%n%n", min, max);
            } else {
                valid = true;
            }
        } while (!valid);

        return value;
    }

    /**
     * Muestra un mensaje y solicita un número entero largo ({@code long}).
     *
     * @param prompt Mensaje de solicitud.
     * @return El número {@code long} introducido.
     */
    public static long readLong(String prompt) {
        long value = 0L;
        boolean valid = false;
        do {
            String input = readString_ne(prompt);
            try {
                value = Long.parseLong(input.trim());
                valid = true;
            } catch (NumberFormatException e) {
                out.println("Error: El valor introducido no es un número entero largo válido. Inténtelo de nuevo.");
            }
        } while (!valid);
        return value;
    }

    // =========================================================================
    // LECTURA DE NÚMEROS REALES (FLOAT / DOUBLE)
    // =========================================================================

    /**
     * Muestra un mensaje y solicita un número real en coma flotante de precisión simple ({@code float}).
     *
     * @param prompt Mensaje de solicitud.
     * @return El valor {@code float} introducido.
     */
    public static float readFloat(String prompt) {
        float value = 0.0f;
        boolean valid = false;
        do {
            String input = readString_ne(prompt).trim().replace(',', '.');
            try {
                value = Float.parseFloat(input);
                valid = true;
            } catch (NumberFormatException e) {
                out.println("Error: El valor introducido no es un número real válido. Inténtelo de nuevo.");
            }
        } while (!valid);
        return value;
    }

    /**
     * Muestra un mensaje y solicita un número {@code float} dentro de un rango cerrado [min, max].
     *
     * @param prompt Mensaje de solicitud.
     * @param min    Valor mínimo permitido.
     * @param max    Valor máximo permitido.
     * @return El número {@code float} validado dentro del rango.
     */
    public static float readFloat(String prompt, float min, float max) {
        if (min > max) {
            float tmp = min;
            min = max;
            max = tmp;
        }

        String fullPrompt = String.format("%s (%.2f <= n <= %.2f): ", prompt != null ? prompt.trim() : "", min, max);
        float value = 0.0f;
        boolean valid = false;

        do {
            value = readFloat(fullPrompt);
            if (value < min || value > max) {
                out.printf("Error: Debe introducir un número entre %.2f y %.2f.%n%n", min, max);
            } else {
                valid = true;
            }
        } while (!valid);

        return value;
    }

    /**
     * Muestra un mensaje y solicita un número real en coma flotante de doble precisión ({@code double}).
     *
     * @param prompt Mensaje de solicitud.
     * @return El valor {@code double} introducido.
     */
    public static double readDouble(String prompt) {
        double value = 0.0;
        boolean valid = false;
        do {
            String input = readString_ne(prompt).trim().replace(',', '.');
            try {
                value = Double.parseDouble(input);
                valid = true;
            } catch (NumberFormatException e) {
                out.println("Error: El valor introducido no es un número double válido. Inténtelo de nuevo.");
            }
        } while (!valid);
        return value;
    }

    /**
     * Muestra un mensaje y solicita un número {@code double} dentro de un rango cerrado [min, max].
     *
     * @param prompt Mensaje de solicitud.
     * @param min    Valor mínimo permitido.
     * @param max    Valor máximo permitido.
     * @return El número {@code double} validado dentro del rango.
     */
    public static double readDouble(String prompt, double min, double max) {
        if (min > max) {
            double tmp = min;
            min = max;
            max = tmp;
        }

        String fullPrompt = String.format("%s (%.2f <= n <= %.2f): ", prompt != null ? prompt.trim() : "", min, max);
        double value = 0.0;
        boolean valid = false;

        do {
            value = readDouble(fullPrompt);
            if (value < min || value > max) {
                out.printf("Error: Debe introducir un número entre %.2f y %.2f.%n%n", min, max);
            } else {
                valid = true;
            }
        } while (!valid);

        return value;
    }

    // =========================================================================
    // CONFIRMACIONES BOOLEANAS
    // =========================================================================

    /**
     * Solicita una confirmación booleana en español, admitiendo exclusivamente [s/S/n/N].
     *
     * @param prompt Mensaje base de la pregunta.
     * @return {@code true} si el usuario responde afirmativamente ('s'/'S'),
     *         {@code false} si responde negativamente ('n'/'N').
     */
    public static boolean siOno(String prompt) {
        String fullPrompt = String.format("%s [s/n]: ", prompt != null ? prompt.trim() : "");
        while (true) {
            String input = readString_ne(fullPrompt).trim();
            if (input.equalsIgnoreCase("s") || input.equalsIgnoreCase("si") || input.equalsIgnoreCase("sí")) {
                return true;
            } else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
                return false;
            }
            out.println("Respuesta no válida. Por favor, responda 's' para Sí o 'n' para No.");
        }
    }

    /**
     * Solicita una confirmación booleana en inglés, admitiendo exclusivamente [y/Y/n/N].
     *
     * @param prompt Mensaje base de la pregunta.
     * @return {@code true} si el usuario responde afirmativamente ('y'/'Y'),
     *         {@code false} si responde negativamente ('n'/'N').
     */
    public static boolean yesOrNo(String prompt) {
        String fullPrompt = String.format("%s [y/n]: ", prompt != null ? prompt.trim() : "");
        while (true) {
            String input = readString_ne(fullPrompt).trim();
            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                return true;
            } else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
                return false;
            }
            out.println("Invalid input. Please answer 'y' for Yes or 'n' for No.");
        }
    }

    // =========================================================================
    // RUTAS DEL SISTEMA DE ARCHIVOS
    // =========================================================================

    /**
     * Comprueba si una cadena de texto representa una sintaxis de ruta válida para el sistema operativo.
     *
     * @param possiblePath Cadena de texto a evaluar.
     * @return {@code true} si la ruta tiene sintaxis válida, {@code false} en caso contrario.
     */
    public static boolean isValidPath(String possiblePath) {
        if (possiblePath == null || possiblePath.trim().isEmpty()) {
            return false;
        }
        try {
            Paths.get(possiblePath.trim());
            return true;
        } catch (InvalidPathException e) {
            return false;
        }
    }

    /**
     * Solicita una ruta por teclado y garantiza que sea sintácticamente válida antes de devolverla.
     *
     * @param prompt Mensaje de solicitud.
     * @return Un objeto {@link Path} válido correspondiente a la entrada del usuario.
     */
    public static Path readPathFromKeyboard(String prompt) {
        while (true) {
            String input = readString_ne(prompt).trim();
            if (isValidPath(input)) {
                return Paths.get(input);
            }
            out.println("Error: La ruta introducida no tiene un formato válido para este sistema. Inténtelo de nuevo.");
        }
    }

    // =========================================================================
    // FORMATO Y PRESENTACIÓN VISUAL
    // =========================================================================

    /**
     * Genera una cadena que contiene el texto recibido subrayado con caracteres '='.
     *
     * @param text Texto a subrayar.
     * @return Cadena con el texto y la línea de subrayado formateada con saltos de línea.
     */
    public static String underline(String text) {
        if (text == null) {
            return "";
        }
        int length = Math.max(1, text.length());
        char[] bar = new char[length];
        Arrays.fill(bar, '=');
        return String.format("%s%n%s%n", text, new String(bar));
    }

    /**
     * Imprime en la salida estándar el texto recibido subrayado con caracteres '='.
     *
     * @param text Texto a imprimir y subrayar.
     */
    public static void underline2(String text) {
        out.print(underline(text));
    }
}
