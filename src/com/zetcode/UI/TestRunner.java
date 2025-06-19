package com.zetcode.UI;

public class TestRunner {
    public static void runTests() {
        try {
            ProcessBuilder builder = new ProcessBuilder("mvn", "-Dtest=AlienTest,BoardIntegrationTest,miTest,PlayerTest,PowerUpTest", "test");
            builder.inheritIO(); // para mostrar la salida en la consola
            Process process = builder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("✅ Tests ejecutados correctamente.");
            } else {
                System.out.println("❌ Algunos tests fallaron. Código de salida: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

