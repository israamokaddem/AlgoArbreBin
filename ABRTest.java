import java.util.*;

public class ABRTest {
    public static void main(String[] args) {
        ABR<Integer> abr = new ABR<>();

        // Test d'ajout d'éléments
        abr.add(10);
        abr.add(20);
        abr.add(5);
        assert abr.size() == 3 : "Test Add échoué, taille attendue 3, taille obtenue " + abr.size();
        assert abr.contains(10) : "Test Add échoué, 10 devrait être contenu dans l'arbre.";
        assert abr.contains(20) : "Test Add échoué, 20 devrait être contenu dans l'arbre.";
        assert abr.contains(5) : "Test Add échoué, 5 devrait être contenu dans l'arbre.";

        System.out.println("Test Add réussi.");

        // Test d'ajout d'un doublon (il ne devrait pas être ajouté)
        assert !abr.add(10) : "Test Add Duplicate échoué, 10 ne devrait pas être ajouté à nouveau.";
        assert abr.size() == 3 : "La taille ne doit pas changer après l'ajout d'un doublon.";

        System.out.println("Test Add Duplicate réussi.");

        // Test de contains
        assert abr.contains(10) : "Test Contains échoué, l'élément 10 devrait être présent.";
        assert abr.contains(20) : "Test Contains échoué, l'élément 20 devrait être présent.";
        assert !abr.contains(15) : "Test Contains échoué, l'élément 15 ne devrait pas être présent.";

        System.out.println("Test Contains réussi.");

        // Test de suppression
        abr.remove(10);
        assert abr.size() == 2 : "Test Remove échoué, taille attendue 2, taille obtenue " + abr.size();
        assert !abr.contains(10) : "Test Remove échoué, l'élément 10 devrait être supprimé.";

        System.out.println("Test Remove réussi.");

        // Test de suppression d'un noeud avec un enfant (le noeud 20)
        abr.add(15);
        abr.remove(20); // 20 a un enfant (15)
        assert abr.size() == 2 : "Test Remove Node With One Child échoué, taille attendue 2, taille obtenue " + abr.size();
        assert !abr.contains(20) : "Test Remove Node With One Child échoué, 20 devrait être supprimé.";

        System.out.println("Test Remove Node With One Child réussi.");

        // Test de suppression d'un noeud avec deux enfants (le noeud 5)
        abr.add(30);
        abr.remove(5); // 5 a deux enfants (15 et 30)
        assert abr.size() == 2 : "Test Remove Node With Two Children échoué, taille attendue 2, taille obtenue " + abr.size();
        assert !abr.contains(5) : "Test Remove Node With Two Children échoué, 5 devrait être supprimé.";

        System.out.println("Test Remove Node With Two Children réussi.");

        // Test de l'itérateur
        Iterator<Integer> iterator = abr.iterator();
        List<Integer> values = new ArrayList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }
        assert values.equals(Arrays.asList(15, 30)) : "Test Iterator échoué, les valeurs attendues sont [15, 30], mais obtenues " + values;

        System.out.println("Test Iterator réussi.");

        // Test de l'itérateur avec suppression
        iterator = abr.iterator();
        iterator.next(); // Next 15
        iterator.remove();
        assert abr.size() == 1 : "Test Iterator Remove échoué, taille attendue 1, taille obtenue " + abr.size();
        assert !abr.contains(15) : "Test Iterator Remove échoué, l'élément 15 devrait être supprimé.";

        System.out.println("Test Iterator Remove réussi.");

        // Test arbre vide
        ABR<Integer> emptyABR = new ABR<>();
        assert emptyABR.size() == 0 : "Test Empty Tree échoué, taille attendue 0, taille obtenue " + emptyABR.size();
        assert !emptyABR.contains(10) : "Test Empty Tree échoué, l'élément 10 ne devrait pas être présent.";

        System.out.println("Test Empty Tree réussi.");

        // Test AddAll
        Collection<Integer> collection = Arrays.asList(10, 20, 5);
        emptyABR.addAll(collection);
        assert emptyABR.size() == 3 : "Test AddAll échoué, taille attendue 3, taille obtenue " + emptyABR.size();
        assert emptyABR.contains(10) : "Test AddAll échoué, 10 devrait être contenu dans l'arbre.";
        assert emptyABR.contains(20) : "Test AddAll échoué, 20 devrait être contenu dans l'arbre.";
        assert emptyABR.contains(5) : "Test AddAll échoué, 5 devrait être contenu dans l'arbre.";

        System.out.println("Test AddAll réussi.");

        // Test avec comparateur
        Comparator<Integer> comparator = Comparator.reverseOrder();
        ABR<Integer> abrWithComparator = new ABR<>(comparator);
        abrWithComparator.add(10);
        abrWithComparator.add(20);
        abrWithComparator.add(5);
        List<Integer> sortedValues = new ArrayList<>();
        iterator = abrWithComparator.iterator();
        while (iterator.hasNext()) {
            sortedValues.add(iterator.next());
        }
        assert sortedValues.equals(Arrays.asList(20, 10, 5)) : "Test Comparator échoué, les valeurs attendues sont [20, 10, 5], mais obtenues " + sortedValues;

        System.out.println("Test Comparator réussi.");
    }
}