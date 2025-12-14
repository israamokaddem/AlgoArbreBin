import java.util.*;
public class ARNTest {
    public static void main(String[] args) {
        // Test 1: Création d'un arbre ARN et ajout d'éléments
        ARN<Integer> arbre = new ARN<>();
        System.out.println("Test 1: Insertion d'éléments");
        arbre.add(10);
        arbre.add(20);
        arbre.add(15);
        arbre.add(5);
        arbre.add(30);
        arbre.add(25);

        System.out.println("Arbre après insertion : ");
        for (Integer val : arbre) {
            System.out.print(val + " ");
        }
        System.out.println();

        // Test 2: Recherche et suppression d'un élément
        System.out.println("Test 2: Suppression de l'élément 15");
        arbre.remove(15);

        System.out.println("Arbre après suppression de 15 : ");
        for (Integer val : arbre) {
            System.out.print(val + " ");
        }
        System.out.println();

        // Test 3: Suppression d'un élément qui n'existe pas
        System.out.println("Test 3: Tentative de suppression de l'élément 100 (n'existe pas)");
        boolean result = arbre.remove(100);
        System.out.println("Élément 100 supprimé ? " + result);

        
        // Test 4: Vérification de la taille de l'arbre après suppression
        System.out.println("Test 4: Taille de l'arbre après suppression : " + arbre.size());

        // Test 5: Test de la recherche (chercher un élément qui existe)
      //  System.out.println("Test 5: Recherche de l'élément 20 (devrait être trouvé)");
     //   Noeud noeud = arbre.rechercher(20);
      //  System.out.println("Élément 20 trouvé ? " + (noeud != null ? "Oui" : "Non"));

        // Test 6: Test de l'itérateur (doit afficher les éléments dans l'ordre)
        System.out.println("Test 6: Itération sur l'arbre (devrait afficher les éléments dans l'ordre)");
        for (Integer val : arbre) {
            System.out.print(val + " ");
        }
        System.out.println();

        // Test 7: Test de la suppression d'un nœud avec deux enfants
        System.out.println("Test 7: Suppression d'un nœud avec deux enfants (exemple de 20)");
        arbre.remove(20);
        System.out.println("Arbre après suppression de 20 : ");
        for (Integer val : arbre) {
            System.out.print(val + " ");
        }
        System.out.println();

        // Test 8: Test de l'ajout d'un élément existant
        System.out.println("Test 8: Tentative d'ajout d'un élément existant (10)");
        boolean addResult = arbre.add(10);
        System.out.println("L'élément 10 a-t-il été ajouté ? " + addResult);

        // Test 9: Test d'une suppression d'un élément et vérification de la taille
        System.out.println("Test 9: Suppression de l'élément 5");
        arbre.remove(5);
        System.out.println("Taille de l'arbre après suppression de 5 : " + arbre.size());
    }
}
