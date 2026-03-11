import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Random; // L'importation de Random est déjà faite dans votre code

public class EtudeComplexite {

    // Définition des différentes tailles n à tester
    private static final int[] TAILLES = {1000, 5000, 10000, 20000, 50000, 100000};
    
    public static void main(String[] args) {
        
        // En-tête CSV pour Excel/Sheets
        System.out.println("Type;N;Cas;Temps_ms"); 
        
        // Exécution des tests et affichage des résultats en CSV
        testerARN();
        testerABR();
        
        // Message d'information (sorti sur la sortie d'erreur pour ne pas polluer le CSV)
        System.err.println("\n--- Données de performance générées. Voir fichier resultats.csv ---");
    }
    
    private static long nanosToMillis(long nanos) {
        return TimeUnit.NANOSECONDS.toMillis(nanos);
    }

    private static void testerARN() {
        for (int n : TAILLES) {
            
            // --- Cas 1: Insertion Aléatoire (Cas Moyen) ---
            ARN<Integer> arnAlea = new ARN<>();
            List<Integer> cleAlea = genererClefsAleatoires(n);

            long startTime = System.nanoTime();
            for (int key : cleAlea) {
                arnAlea.add(key);
            }
            long timeConstAlea = nanosToMillis(System.nanoTime() - startTime);
            // Format CSV: Type;N;Cas;Temps_ms
            System.out.println("ARN;" + n + ";Construction_Alea;" + timeConstAlea);


            // Recherche (2n opérations : n présentes, n absentes)
            startTime = System.nanoTime();
            for (int i = 0; i < 2 * n; i++) {
                // Note : J'utilise .rechercher() car elle est définie dans votre fichier ARN.java
                arnAlea.rechercher(i); 
            }
            long timeSearchAlea = nanosToMillis(System.nanoTime() - startTime);
            System.out.println("ARN;" + n + ";Recherche_Alea;" + timeSearchAlea);


            // --- Cas 2: Insertion Triée (Pire Cas) ---
            ARN<Integer> arnSorted = new ARN<>();

            startTime = System.nanoTime();
            for (int i = 0; i < n; i++) {
                arnSorted.add(i); // Insertion triée
            }
            long timeConstSorted = nanosToMillis(System.nanoTime() - startTime);
            System.out.println("ARN;" + n + ";Construction_Triee_Pire_Cas;" + timeConstSorted);

            // Recherche (2n opérations : n présentes, n absentes)
            startTime = System.nanoTime();
            for (int i = 0; i < 2 * n; i++) {
                arnSorted.rechercher(i);
            }
            long timeSearchSorted = nanosToMillis(System.nanoTime() - startTime);
            System.out.println("ARN;" + n + ";Recherche_Triee_Pire_Cas;" + timeSearchSorted);
        }
    }

    private static void testerABR() {
        for (int n : TAILLES) {
            
            // --- Cas 1: Insertion Aléatoire (Cas Moyen) ---
            ABR<Integer> abrAlea = new ABR<>();
            List<Integer> randomKeys = genererClefsAleatoires(n);

            long startTime = System.nanoTime();
            for (int key : randomKeys) {
                abrAlea.add(key);
            }
            long timeConstAlea = nanosToMillis(System.nanoTime() - startTime);
            System.out.println("ABR;" + n + ";Construction_Alea;" + timeConstAlea);


            // Recherche (2n opérations : n présentes, n absentes)
            startTime = System.nanoTime();
            for (int i = 0; i < 2 * n; i++) {
                // Note : J'utilise .contains() car elle est définie dans votre fichier ABR.java
                abrAlea.contains(i);
            }
            long timeSearchAlea = nanosToMillis(System.nanoTime() - startTime);
            System.out.println("ABR;" + n + ";Recherche_Alea;" + timeSearchAlea);


            // --- Cas 2: Insertion Triée (Pire Cas) ---
            ABR<Integer> abrSorted = new ABR<>();

            startTime = System.nanoTime();
            for (int i = 0; i < n; i++) {
                abrSorted.add(i); // Insertion triée
            }
            long timeConstSorted = nanosToMillis(System.nanoTime() - startTime);
            System.out.println("ABR;" + n + ";Construction_Triee_Pire_Cas;" + timeConstSorted);

            // Recherche (2n opérations : n présentes, n absentes)
            startTime = System.nanoTime();
            for (int i = 0; i < 2 * n; i++) {
                abrSorted.contains(i);
            }
            long timeSearchSorted = nanosToMillis(System.nanoTime() - startTime);
            System.out.println("ABR;" + n + ";Recherche_Triee_Pire_Cas;" + timeSearchSorted);
        }
    }
    
    /**
     * Génère une liste de clés aléatoires entre 0 et n-1 et la mélange.
     */
    private static List<Integer> genererClefsAleatoires(int n) {
        List<Integer> keys = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            keys.add(i);
        }
        Collections.shuffle(keys);
        return keys;
    }
}