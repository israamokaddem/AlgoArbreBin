import java.util.*;

public class ComplexiteABR {
    public static void main(String[] args) {
        int[] tailles = {1000, 5000, 10000, 20000}; // Différentes tailles de l'arbre
        Random rand = new Random();

        for (int n : tailles) {
            // Cas 1: ABR avec insertion aléatoire
            ABR<Integer> abr = new ABR<>();
            long startTime = System.nanoTime();
            // Insertion des clés dans un ordre aléatoire
            for (int i = 0; i < n; i++) {
                abr.add(rand.nextInt());
            }
            long endTime = System.nanoTime();
            System.out.println("Temps de construction de l'ABR avec insertion aléatoire pour n=" + n + ": " + (endTime - startTime) / 1000000 + " ms");

            // Cas 2: Recherche de clés présentes et absentes
            startTime = System.nanoTime();
            for (int i = 0; i < 2 * n; i++) {
                abr.contains(i); // Recherche des éléments présents et absents
            }
            endTime = System.nanoTime();
            System.out.println("Temps de recherche dans l'ABR pour n=" + n + ": " + (endTime - startTime) / 1000000 + " ms");

            // Cas 3: ABR avec insertion triée (cas défavorable)
            ABR<Integer> abrSorted = new ABR<>();
            startTime = System.nanoTime();
            for (int i = 0; i < n; i++) {
                abrSorted.add(i); // Insertion triée
            }
            endTime = System.nanoTime();
            System.out.println("Temps de construction de l'ABR avec insertion triée pour n=" + n + ": " + (endTime - startTime) / 1000000 + " ms");
        }
    }
}
