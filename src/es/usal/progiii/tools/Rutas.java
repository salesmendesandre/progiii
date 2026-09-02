package es.usal.progiii.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Clase de utilidades para la resolución de rutas de ficheros y directorios multiplataforma.
 * <p>
 * Proporciona métodos estáticos para obtener objetos {@link Path} y {@link File}
 * que apuntan a las carpetas estándar de usuario (Escritorio, Documentos, Directorio Personal),
 * con detección inteligente de idioma del sistema (inglés/español), soporte robusto para
 * sincronización con OneDrive y navegación limpia sin concatenaciones frágiles.
 * </p>
 *
 * @author PROGIII - Universidad de Salamanca (USAL)
 * @version 2.0
 */
public class Rutas {

    /** Ruta base al directorio personal del usuario actual ({@code user.home}). */
    private static final String USER_HOME = System.getProperty("user.home");
    private static final Path PATH_HOME = Paths.get(USER_HOME);

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

    /** Constructor privado para evitar instanciación de clase estática. */
    private Rutas() {
    }

    // =========================================================================
    // UTILIDADES DE DETECCIÓN Y RESOLUCIÓN INTERNAS
    // =========================================================================

    /**
     * Comprueba si el directorio de OneDrive existe en el sistema del usuario.
     *
     * @return {@code true} si se detecta OneDrive, {@code false} en caso contrario.
     */
    public static boolean isOneDriveAvailable() {
        Path odPath = pathToHomeOD();
        return Files.exists(odPath) && Files.isDirectory(odPath);
    }

    /**
     * Resuelve la primera carpeta existente entre una lista de candidatas respecto a una ruta base.
     */
    private static Path resolveFirstExisting(Path base, String... candidateNames) {
        for (String candidate : candidateNames) {
            Path target = base.resolve(candidate);
            if (Files.exists(target)) {
                return target;
            }
        }
        // Si ninguna existe físicamente, devolver la primera por defecto
        return base.resolve(candidateNames[0]);
    }

    // =========================================================================
    // DIRECTORIO PERSONAL (HOME)
    // =========================================================================

    /**
     * Devuelve el {@link Path} al directorio personal del usuario.
     *
     * @return {@link Path} del directorio personal ({@code user.home}).
     */
    public static Path pathToHome() {
        return PATH_HOME;
    }

    /**
     * Devuelve el {@link File} al directorio personal del usuario.
     *
     * @return {@link File} del directorio personal.
     */
    public static File fileToHome() {
        return pathToHome().toFile();
    }

    /**
     * Devuelve el {@link Path} al directorio de OneDrive en el usuario.
     *
     * @return {@link Path} al directorio de OneDrive.
     */
    public static Path pathToHomeOD() {
        String envOd = System.getenv("OneDrive");
        if (envOd != null && !envOd.trim().isEmpty() && Files.exists(Paths.get(envOd))) {
            return Paths.get(envOd);
        }
        return PATH_HOME.resolve("OneDrive");
    }

    /**
     * Devuelve el {@link File} al directorio de OneDrive en el usuario.
     *
     * @return {@link File} al directorio de OneDrive.
     */
    public static File fileToHomeOD() {
        return pathToHomeOD().toFile();
    }

    // =========================================================================
    // ESCRITORIO (DESKTOP)
    // =========================================================================

    /**
     * Devuelve el {@link Path} al Escritorio del usuario, detectando automáticamente
     * si la carpeta se llama {@code Desktop} o {@code Escritorio}.
     *
     * @return {@link Path} al Escritorio.
     */
    public static Path pathToDesktop() {
        return resolveFirstExisting(PATH_HOME, "Desktop", "Escritorio");
    }

    /**
     * Devuelve el {@link File} al Escritorio del usuario.
     *
     * @return {@link File} al Escritorio.
     */
    public static File fileToDesktop() {
        return pathToDesktop().toFile();
    }

    /**
     * Devuelve el {@link Path} al Escritorio ubicado dentro de OneDrive.
     *
     * @return {@link Path} al Escritorio en OneDrive.
     */
    public static Path pathToDesktopOD() {
        return resolveFirstExisting(pathToHomeOD(), "Desktop", "Escritorio");
    }

    /**
     * Devuelve el {@link File} al Escritorio ubicado dentro de OneDrive.
     *
     * @return {@link File} al Escritorio en OneDrive.
     */
    public static File fileToDesktopOD() {
        return pathToDesktopOD().toFile();
    }

    /**
     * Devuelve el {@link Path} a una carpeta específica en el Escritorio.
     *
     * @param nameOfFolder Nombre de la carpeta en el Escritorio.
     * @return {@link Path} correspondiente a la carpeta.
     */
    public static Path pathToFolderOnDesktop(String nameOfFolder) {
        Objects.requireNonNull(nameOfFolder, "El nombre de la carpeta no puede ser nulo");
        return pathToDesktop().resolve(nameOfFolder);
    }

    /**
     * Devuelve el {@link Path} a una carpeta específica en el Escritorio de OneDrive.
     *
     * @param nameOfFolder Nombre de la carpeta en el Escritorio de OneDrive.
     * @return {@link Path} correspondiente a la carpeta en OneDrive.
     */
    public static Path pathToFolderOnDesktopOD(String nameOfFolder) {
        Objects.requireNonNull(nameOfFolder, "El nombre de la carpeta no puede ser nulo");
        return pathToDesktopOD().resolve(nameOfFolder);
    }

    /**
     * Devuelve el {@link File} a una carpeta en el Escritorio.
     *
     * @param nameOfFolder Nombre de la carpeta en el Escritorio.
     * @return {@link File} correspondiente.
     */
    public static File fileToFolderOnDesktop(String nameOfFolder) {
        return pathToFolderOnDesktop(nameOfFolder).toFile();
    }

    /**
     * Devuelve el {@link File} a una carpeta en el Escritorio de OneDrive.
     *
     * @param nameOfFolder Nombre de la carpeta en el Escritorio de OneDrive.
     * @return {@link File} correspondiente.
     */
    public static File fileToFolderOnDesktopOD(String nameOfFolder) {
        return pathToFolderOnDesktopOD(nameOfFolder).toFile();
    }

    /**
     * Devuelve el {@link Path} a un archivo ubicado directamente en el Escritorio.
     *
     * @param nameOfFile Nombre del archivo con su extensión.
     * @return {@link Path} al archivo.
     */
    public static Path pathToFileOnDesktop(String nameOfFile) {
        Objects.requireNonNull(nameOfFile, "El nombre del archivo no puede ser nulo");
        return pathToDesktop().resolve(nameOfFile);
    }

    /**
     * Devuelve el {@link Path} a un archivo ubicado en el Escritorio de OneDrive.
     *
     * @param nameOfFile Nombre del archivo con su extensión.
     * @return {@link Path} al archivo en OneDrive.
     */
    public static Path pathToFileOnDesktopOD(String nameOfFile) {
        Objects.requireNonNull(nameOfFile, "El nombre del archivo no puede ser nulo");
        return pathToDesktopOD().resolve(nameOfFile);
    }

    /**
     * Devuelve el {@link File} a un archivo ubicado en el Escritorio.
     *
     * @param nameOfFile Nombre del archivo con su extensión.
     * @return {@link File} correspondiente.
     */
    public static File fileToFileOnDesktop(String nameOfFile) {
        return pathToFileOnDesktop(nameOfFile).toFile();
    }

    /**
     * Devuelve el {@link File} a un archivo ubicado en el Escritorio de OneDrive.
     *
     * @param nameOfFile Nombre del archivo con su extensión.
     * @return {@link File} correspondiente en OneDrive.
     */
    public static File fileToFileOnDesktopOD(String nameOfFile) {
        return pathToFileOnDesktopOD(nameOfFile).toFile();
    }

    /**
     * Devuelve el {@link Path} a un archivo dentro de una subcarpeta en el Escritorio.
     *
     * @param nameOfFolder Nombre de la subcarpeta.
     * @param nameOfFile   Nombre del archivo con su extensión.
     * @return {@link Path} al archivo.
     */
    public static Path pathToFileInFolderOnDesktop(String nameOfFolder, String nameOfFile) {
        Objects.requireNonNull(nameOfFolder, "El nombre de la carpeta no puede ser nulo");
        Objects.requireNonNull(nameOfFile, "El nombre del archivo no puede ser nulo");
        return pathToFolderOnDesktop(nameOfFolder).resolve(nameOfFile);
    }

    /**
     * Devuelve el {@link Path} a un archivo dentro de una subcarpeta en el Escritorio de OneDrive.
     *
     * @param nameOfFolder Nombre de la subcarpeta.
     * @param nameOfFile   Nombre del archivo con su extensión.
     * @return {@link Path} al archivo en OneDrive.
     */
    public static Path pathToFileInFolderOnDesktopOD(String nameOfFolder, String nameOfFile) {
        Objects.requireNonNull(nameOfFolder, "El nombre de la carpeta no puede ser nulo");
        Objects.requireNonNull(nameOfFile, "El nombre del archivo no puede ser nulo");
        return pathToFolderOnDesktopOD(nameOfFolder).resolve(nameOfFile);
    }

    /**
     * Devuelve el {@link File} a un archivo dentro de una subcarpeta en el Escritorio.
     *
     * @param nameOfFolder Nombre de la subcarpeta.
     * @param nameOfFile   Nombre del archivo con su extensión.
     * @return {@link File} correspondiente.
     */
    public static File fileToFileInFolderOnDesktop(String nameOfFolder, String nameOfFile) {
        return pathToFileInFolderOnDesktop(nameOfFolder, nameOfFile).toFile();
    }

    /**
     * Devuelve el {@link File} a un archivo dentro de una subcarpeta en el Escritorio de OneDrive.
     *
     * @param nameOfFolder Nombre de la subcarpeta.
     * @param nameOfFile   Nombre del archivo con su extensión.
     * @return {@link File} correspondiente en OneDrive.
     */
    public static File fileToFileInFolderOnDesktopOD(String nameOfFolder, String nameOfFile) {
        return pathToFileInFolderOnDesktopOD(nameOfFolder, nameOfFile).toFile();
    }

    // =========================================================================
    // DOCUMENTOS (DOCUMENTS)
    // =========================================================================

    /**
     * Devuelve el {@link Path} a la carpeta de Documentos del usuario, detectando
     * automáticamente {@code Documents} o {@code Documentos}.
     *
     * @return {@link Path} a Documentos.
     */
    public static Path pathToDocuments() {
        return resolveFirstExisting(PATH_HOME, "Documents", "Documentos");
    }

    /**
     * Devuelve el {@link File} a la carpeta de Documentos.
     *
     * @return {@link File} a Documentos.
     */
    public static File fileToDocuments() {
        return pathToDocuments().toFile();
    }

    /**
     * Devuelve el {@link Path} a la carpeta de Documentos en OneDrive.
     *
     * @return {@link Path} a Documentos en OneDrive.
     */
    public static Path pathToDocumentsOD() {
        return resolveFirstExisting(pathToHomeOD(), "Documents", "Documentos");
    }

    /**
     * Devuelve el {@link File} a la carpeta de Documentos en OneDrive.
     *
     * @return {@link File} a Documentos en OneDrive.
     */
    public static File fileToDocumentsOD() {
        return pathToDocumentsOD().toFile();
    }

    /**
     * Devuelve el {@link Path} a una subcarpeta en Documentos.
     *
     * @param nameOfFolder Nombre de la subcarpeta.
     * @return {@link Path} correspondiente.
     */
    public static Path pathToFolderInDocuments(String nameOfFolder) {
        Objects.requireNonNull(nameOfFolder, "El nombre de la carpeta no puede ser nulo");
        return pathToDocuments().resolve(nameOfFolder);
    }

    /**
     * Devuelve el {@link Path} a una subcarpeta en Documentos de OneDrive.
     *
     * @param nameOfFolder Nombre de la subcarpeta en OneDrive.
     * @return {@link Path} correspondiente.
     */
    public static Path pathToFolderInDocumentsOD(String nameOfFolder) {
        Objects.requireNonNull(nameOfFolder, "El nombre de la carpeta no puede ser nulo");
        return pathToDocumentsOD().resolve(nameOfFolder);
    }

    /**
     * Devuelve el {@link File} a una subcarpeta en Documentos.
     *
     * @param nameOfFolder Nombre de la subcarpeta.
     * @return {@link File} correspondiente.
     */
    public static File fileToFolderInDocuments(String nameOfFolder) {
        return pathToFolderInDocuments(nameOfFolder).toFile();
    }

    /**
     * Devuelve el {@link File} a una subcarpeta en Documentos de OneDrive.
     *
     * @param nameOfFolder Nombre de la subcarpeta en OneDrive.
     * @return {@link File} correspondiente.
     */
    public static File fileToFolderInDocumentsOD(String nameOfFolder) {
        return pathToFolderInDocumentsOD(nameOfFolder).toFile();
    }

    /**
     * Devuelve el {@link Path} a un archivo ubicado en la raíz de Documentos.
     *
     * @param nameOfFile Nombre del archivo con su extensión.
     * @return {@link Path} al archivo.
     */
    public static Path pathToFileInDocuments(String nameOfFile) {
        Objects.requireNonNull(nameOfFile, "El nombre del archivo no puede ser nulo");
        return pathToDocuments().resolve(nameOfFile);
    }

    /**
     * Devuelve el {@link Path} a un archivo ubicado en la raíz de Documentos de OneDrive.
     *
     * @param nameOfFile Nombre del archivo con su extensión.
     * @return {@link Path} al archivo en OneDrive.
     */
    public static Path pathToFileInDocumentsOD(String nameOfFile) {
        Objects.requireNonNull(nameOfFile, "El nombre del archivo no puede ser nulo");
        return pathToDocumentsOD().resolve(nameOfFile);
    }

    /**
     * Devuelve el {@link File} a un archivo ubicado en Documentos.
     *
     * @param nameOfFile Nombre del archivo con su extensión.
     * @return {@link File} correspondiente.
     */
    public static File fileToFileInDocuments(String nameOfFile) {
        return pathToFileInDocuments(nameOfFile).toFile();
    }

    /**
     * Devuelve el {@link File} a un archivo ubicado en Documentos de OneDrive.
     *
     * @param nameOfFile Nombre del archivo con su extensión.
     * @return {@link File} correspondiente en OneDrive.
     */
    public static File fileToFileInDocumentsOD(String nameOfFile) {
        return pathToFileInDocumentsOD(nameOfFile).toFile();
    }

    /**
     * Devuelve el {@link Path} a un archivo dentro de una subcarpeta en Documentos.
     *
     * @param nameOfFolder Nombre de la subcarpeta.
     * @param nameOfFile   Nombre del archivo.
     * @return {@link Path} al archivo.
     */
    public static Path pathToFileInFolderInDocuments(String nameOfFolder, String nameOfFile) {
        Objects.requireNonNull(nameOfFolder, "El nombre de la carpeta no puede ser nulo");
        Objects.requireNonNull(nameOfFile, "El nombre del archivo no puede ser nulo");
        return pathToFolderInDocuments(nameOfFolder).resolve(nameOfFile);
    }

    /**
     * Devuelve el {@link Path} a un archivo dentro de una subcarpeta en Documentos de OneDrive.
     *
     * @param nameOfFolder Nombre de la subcarpeta en OneDrive.
     * @param nameOfFile   Nombre del archivo.
     * @return {@link Path} al archivo en OneDrive.
     */
    public static Path pathToFileInFolderInDocumentsOD(String nameOfFolder, String nameOfFile) {
        Objects.requireNonNull(nameOfFolder, "El nombre de la carpeta no puede ser nulo");
        Objects.requireNonNull(nameOfFile, "El nombre del archivo no puede ser nulo");
        return pathToFolderInDocumentsOD(nameOfFolder).resolve(nameOfFile);
    }

    /**
     * Devuelve el {@link File} a un archivo dentro de una subcarpeta en Documentos.
     *
     * @param nameOfFolder Nombre de la subcarpeta.
     * @param nameOfFile   Nombre del archivo.
     * @return {@link File} correspondiente.
     */
    public static File fileToFileInFolderInDocuments(String nameOfFolder, String nameOfFile) {
        return pathToFileInFolderInDocuments(nameOfFolder, nameOfFile).toFile();
    }

    /**
     * Devuelve el {@link File} a un archivo dentro de una subcarpeta en Documentos de OneDrive.
     *
     * @param nameOfFolder Nombre de la subcarpeta en OneDrive.
     * @param nameOfFile   Nombre del archivo.
     * @return {@link File} correspondiente en OneDrive.
     */
    public static File fileToFileInFolderInDocumentsOD(String nameOfFolder, String nameOfFile) {
        return pathToFileInFolderInDocumentsOD(nameOfFolder, nameOfFile).toFile();
    }

    // =========================================================================
    // UTILIDADES ADICIONALES DE SISTEMA DE ARCHIVOS
    // =========================================================================

    /**
     * Crea un directorio (y sus carpetas intermedias necesarias) si aún no existe.
     *
     * @param directory Ruta del directorio a crear.
     * @return El mismo objeto {@link Path} recibido tras asegurar su existencia.
     * @throws IOException Si ocurre un error al crear el directorio.
     */
    public static Path ensureDirectoryExists(Path directory) throws IOException {
        Objects.requireNonNull(directory, "El directorio no puede ser nulo");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        return directory;
    }
}
