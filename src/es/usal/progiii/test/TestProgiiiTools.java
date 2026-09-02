package es.usal.progiii.test;

import es.usal.progiii.tools.Esdia;
import es.usal.progiii.tools.OpMat;
import es.usal.progiii.tools.Rutas;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Suite de pruebas unitarias y de integración para validar es.usal.progiii.tools.
 */
public class TestProgiiiTools {

    public static void main(String[] args) throws Exception {
        System.out.println(Esdia.ANSI_GREEN + "===============================================" + Esdia.ANSI_RESET);
        System.out.println(Esdia.ANSI_GREEN + "  INICIANDO TEST SUITE ES.USAL.PROGIII.TOOLS   " + Esdia.ANSI_RESET);
        System.out.println(Esdia.ANSI_GREEN + "===============================================" + Esdia.ANSI_RESET);

        testEsdiaFormatting();
        testRutasResolution();
        testOpMatOperations();
        testOpMatPersistence();

        System.out.println(Esdia.ANSI_GREEN + "\n>>> TODAS LAS PRUEBAS COMPLETADAS CON ÉXITO <<<" + Esdia.ANSI_RESET);
    }

    private static void testEsdiaFormatting() {
        System.out.println("\n--- Probando Esdia (formato, versión y validaciones) ---");
        assert "1.0.0".equals(Esdia.getVersion()) : "Versión incorrecta en Esdia";
        assert "1.0.0".equals(Rutas.getVersion()) : "Versión incorrecta en Rutas";
        assert "1.0.0".equals(OpMat.getVersion()) : "Versión incorrecta en OpMat";
        Esdia.printVersion();
        System.out.println("Control de versión: OK");

        String promptConPorcentaje = "Total % de descuento y %s extra:";
        String formatted = Esdia.underline(promptConPorcentaje);
        assert formatted.contains(promptConPorcentaje) : "Fallo en underline con %";
        System.out.println("Underline con %: OK");

        assert Esdia.isValidPath("/usr/local/bin") : "Fallo validando ruta Unix";
        assert !Esdia.isValidPath("") : "Fallo validando ruta vacía";
        System.out.println("Validación de rutas: OK");
    }

    private static void testRutasResolution() {
        System.out.println("\n--- Probando Rutas ---");
        Path home = Rutas.pathToHome();
        Path desktop = Rutas.pathToDesktop();
        Path documents = Rutas.pathToDocuments();
        Path desktopOD = Rutas.pathToDesktopOD();

        assert home != null : "Home no debe ser nulo";
        assert desktop != null : "Desktop no debe ser nulo";
        assert documents != null : "Documents no debe ser nulo";
        assert desktopOD != null : "DesktopOD no debe ser nulo";

        Path folderOnDesktopOD = Rutas.pathToFolderOnDesktopOD("MiProyecto");
        assert folderOnDesktopOD.toString().contains("OneDrive") : "Fallo en ruta OneDrive Desktop";
        System.out.printf("Ruta Desktop: %s%n", desktop);
        System.out.printf("Ruta Documents: %s%n", documents);
        System.out.printf("Ruta OneDrive Desktop Folder: %s%n", folderOnDesktopOD);
        System.out.println("Rutas: OK");
    }

    private static void testOpMatOperations() {
        System.out.println("\n--- Probando OpMat (Álgebra y Manipulación) ---");
        int[][] a = {
                {1, 2, 3},
                {4, 5, 6}
        };
        int[][] b = {
                {7, 8, 9},
                {1, 2, 3}
        };
        int[][] suma = OpMat.sum(a, b);
        assert suma[0][0] == 8 && suma[1][2] == 9 : "Fallo en suma de matrices";

        int[][] c = {
                {1, 2},
                {3, 4},
                {5, 6}
        };
        int[][] prod = OpMat.product(a, c);
        assert prod.length == 2 && prod[0].length == 2 : "Dimensiones incorrectas en producto";
        assert prod[0][0] == 22 : "Producto cálculo erróneo";

        double[][] mCuadrada = {
                {2.0, 5.0, 1.0},
                {3.0, 4.0, 7.0},
                {9.0, 0.0, 6.0}
        };
        double diag = OpMat.sumMainDiagonal(mCuadrada);
        assert diag == 12.0 : "Fallo sumando diagonal: " + diag;

        double colSum = OpMat.sumColumn(mCuadrada, 1);
        assert colSum == 9.0 : "Fallo sumando columna 1: " + colSum;

        String[][] s1 = {{"A", "B"}, {"C", "D"}};
        String[][] s2 = {{"1", "2"}, {"3", "4"}};
        String[][] joined = OpMat.join(s1, s2);
        assert joined.length == 2 && joined[0].length == 4 : "Fallo en join";

        String[][] concat = OpMat.concat(s1, s2);
        assert concat.length == 4 && concat[0].length == 2 : "Fallo en concat";

        System.out.println("Operaciones matriciales: OK");
    }

    private static void testOpMatPersistence() throws Exception {
        System.out.println("\n--- Probando OpMat (Persistencia y CSV) ---");
        File tempBin = File.createTempFile("test_mat_", ".bin");
        tempBin.deleteOnExit();

        double[][] original = {
                {1.23, 4.56, 7.89},
                {9.87, 6.54, 3.21}
        };

        OpMat.saveToDisk(tempBin, original);
        double[][] loaded = OpMat.loadFromDisk(tempBin, 0.0);
        assert Arrays.deepEquals(original, loaded) : "Fallo en serialización binaria";
        System.out.println("Serialización binaria: OK");

        File tempCsv = File.createTempFile("test_mat_", ".csv");
        tempCsv.deleteOnExit();

        String[][] textMatrix = {
                {"Nombre", "Ciudad", "Nota"},
                {"Ana", "Salamanca", "9.5"},
                {"Carlos", "Madrid", "8.0"}
        };
        OpMat.exportToDisk(textMatrix, tempCsv, ";");
        String[][] imported = OpMat.importFromDisk(tempCsv, ";");
        assert imported.length == 3 && imported[1][0].equals("Ana") : "Fallo importando CSV";
        System.out.println("Exportación / Importación CSV UTF-8: OK");
    }
}
