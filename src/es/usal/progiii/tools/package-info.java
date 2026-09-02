/**
 * Paquete de herramientas y utilidades de soporte para la asignatura <b>Programación III</b> (PROGIII),
 * impartida en el Grado en Ingeniería Informática, Grado en Matemáticas, Dobles Grados y Grado en Estadística
 * de la <b>Universidad de Salamanca (USAL)</b>.
 *
 * <h2>Descarga de la Biblioteca</h2>
 * <p>
 * Puedes descargar la última versión compilada lista para usar en tus proyectos:
 * </p>
 * <ul>
 *   <li>
 *     <b><a href="https://github.com/salesmendesandre/progiii/releases/latest/download/biblioteca_progiii.jar">
 *     Descargar biblioteca_progiii.jar (Ultima Version)</a></b>
 *   </li>
 *   <li>
 *     <b><a href="https://github.com/salesmendesandre/progiii/releases">
 *     Ver todas las versiones y releases en GitHub</a></b>
 *   </li>
 * </ul>
 *
 * <h2>Clases Incluidas</h2>
 * <ul>
 *   <li>
 *     {@link es.usal.progiii.tools.Esdia}: Metodos seguros de lectura interactiva por teclado
 *     (enteros, reales, textos, confirmaciones {@code siOno} y validaciones de rutas), gestion de version y soporte de colores ANSI.
 *   </li>
 *   <li>
 *     {@link es.usal.progiii.tools.Rutas}: Metodos multiplataforma para resolver rutas de sistema
 *     ({@code Desktop}/Escritorio, {@code Documents}/Documentos, {@code Home} y OneDrive).
 *   </li>
 *   <li>
 *     {@link es.usal.progiii.tools.OpMat}: Manipulacion y algebra de matrices bidimensionales,
 *     formateo tabular en pantalla, serializacion binaria en disco e importacion/exportacion de archivos CSV (UTF-8).
 *   </li>
 * </ul>
 *
 * <h2>Ejemplo Rapido de Uso</h2>
 * <pre>{@code
 * import es.usal.progiii.tools.Esdia;
 * import es.usal.progiii.tools.Rutas;
 * import es.usal.progiii.tools.OpMat;
 * import java.io.File;
 *
 * public class Ejemplo {
 *     public static void main(String[] args) {
 *         // 0. Consultar version de la biblioteca
 *         Esdia.printVersion();
 *
 *         // 1. Lectura por consola sin excepciones
 *         int edad = Esdia.readInt("Introduce tu edad: ", 18, 99);
 *         boolean guardar = Esdia.siOno("¿Deseas guardar los datos?");
 *
 *         if (guardar) {
 *             // 2. Generar y mostrar matriz
 *             int[][] matriz = OpMat.randomArrayOfInt(3, 3, 1, 100);
 *             OpMat.printToScreen4(matriz);
 *
 *             // 3. Guardar en el Escritorio
 *             File destino = Rutas.fileToFileOnDesktop("matriz.csv");
 *             try {
 *                 OpMat.exportToDisk(matriz, destino, ";");
 *             } catch (Exception e) {
 *                 System.err.println("Error: " + e.getMessage());
 *             }
 *         }
 *     }
 * }
 * }</pre>
 *
 * <h2>Reconocimiento y Equipo Docente</h2>
 * <p>
 * Este proyecto toma como base e inspiracion el material formativo desarrollado por el
 * <b>equipo docente de Programacion III</b> de la Universidad de Salamanca (USAL),
 * con especial mencion al profesor <i>J. R. Garcia-Bermejo Giner ("Coti")</i>.
 * </p>
 *
 * @author PROGIII - Universidad de Salamanca (USAL)
 * @version 1.0.0
 */
package es.usal.progiii.tools;
