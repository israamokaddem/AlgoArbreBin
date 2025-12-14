import java.util.*;

/**
 * Implantation de l'interface Collection basée sur les arbres binaires de recherche.
 */
public class ABR<E> extends AbstractCollection<E> {
    private Noeud racine;
    private int taille;
    private Comparator<? super E> cmp;

    public class Noeud {
        E cle;
        Noeud gauche;
        Noeud droit;
        Noeud pere;

        Noeud(E cle) {
            this.cle = cle;
        }

        Noeud minimum() {
            Noeud current = this;
            while (current.gauche != null) {
                current = current.gauche;
            }
            return current;
        }

        Noeud suivant() {
            if (this.droit != null) {
                return this.droit.minimum();
            }
            Noeud current = this;
            while (current.pere != null && current == current.pere.droit) {
                current = current.pere;
            }
            return current.pere;
        }
    }

    public ABR() {
        this.cmp = null;
    }

    public ABR(Comparator<? super E> cmp) {
        this.cmp = cmp;
    }

    public ABR(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    @Override
    public Iterator<E> iterator() {
        return new ABRIterator();
    }

    @Override
    public int size() {
        return taille;
    }

    public Noeud rechercher(Object o) {
        Noeud current = racine;
        while (current != null) {
            int cmpResult = comparer((E) o, current.cle);
            if (cmpResult == 0) return current;
            current = cmpResult < 0 ? current.gauche : current.droit;
        }
        return null;
    }

    public Noeud supprimer(Noeud z) {
        if (z.gauche == null || z.droit == null) {
            Noeud enfant = (z.gauche != null) ? z.gauche : z.droit;
            remplacer(z, enfant);
            return enfant;
        } else {
            Noeud successeur = z.droit.minimum();
            z.cle = successeur.cle;
            return supprimer(successeur);
        }
    }

    public void remplacer(Noeud ancien, Noeud nouveau) {
        if (ancien.pere == null) {
            racine = nouveau;
        } else if (ancien == ancien.pere.gauche) {
            ancien.pere.gauche = nouveau;
        } else {
            ancien.pere.droit = nouveau;
        }
        if (nouveau != null) {
            nouveau.pere = ancien.pere;
        }
    }

    @Override
    public boolean add(E e) {
        Noeud nouveau = new Noeud(e);
        if (racine == null) {
            racine = nouveau;
        } else {
            Noeud current = racine;
            Noeud parent = null;
            while (current != null) {
                parent = current;
                int cmpResult = comparer(e, current.cle);
                if (cmpResult == 0) return false;
                current = cmpResult < 0 ? current.gauche : current.droit;
            }
            if (comparer(e, parent.cle) < 0) {
                parent.gauche = nouveau;
            } else {
                parent.droit = nouveau;
            }
            nouveau.pere = parent;
        }
        taille++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return rechercher(o) != null;
    }

    @Override
    public boolean remove(Object o) {
        Noeud node = rechercher(o);
        if (node == null) return false;
        supprimer(node);
        taille--;
        return true;
    }

    public int comparer(E e1, E e2) {
        if (cmp != null) {
            return cmp.compare(e1, e2);
        } else {
            return ((Comparable<? super E>) e1).compareTo(e2);
        }
    }

    public class ABRIterator implements Iterator<E> {
        private Noeud prochain = (racine == null) ? null : racine.minimum();
        private Noeud dernier;

        @Override
        public boolean hasNext() {
            return prochain != null;
        }

        @Override
        public E next() {
            if (prochain == null) throw new NoSuchElementException();
            dernier = prochain;
            prochain = prochain.suivant();
            return dernier.cle;
        }

        @Override
        public void remove() {
            if (dernier == null) throw new IllegalStateException();
            supprimer(dernier);
            taille--;
            dernier = null;
        }
    }
}
