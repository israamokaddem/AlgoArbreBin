import java.util.*;

public class ARN<E> extends AbstractCollection<E> {
    public Noeud racine;
    private int taille;
    private Comparator<? super E> cmp;

    private static final boolean ROUGE = false;
    private static final boolean NOIR = true;

    public class Noeud {
        E cle;
        Noeud gauche, droit, parent;
        boolean couleur; // false = rouge, true = noir

        public Noeud(E cle) {
            this.cle = cle;
            this.couleur = ROUGE; // Initialement rouge
        }

        public Noeud minimum() {
            Noeud n = this;
            while (n.gauche != null) {
                n = n.gauche;
            }
            return n;
        }

        public Noeud maximum() {
            Noeud n = this;
            while (n.droit != null) {
                n = n.droit;
            }
            return n;
        }

        public Noeud successeur() {
            if (this.droit != null) {
                return this.droit.minimum();
            }
            Noeud p = this.parent;
            Noeud n = this;
            while (p != null && n == p.droit) {
                n = p;
                p = p.parent;
            }
            return p;
        }

        public Noeud prédécesseur() {
            if (this.gauche != null) {
                return this.gauche.maximum();
            }
            Noeud p = this.parent;
            Noeud n = this;
            while (p != null && n == p.gauche) {
                n = p;
                p = p.parent;
            }
            return p;
        }
    }

    public ARN() {
        this.cmp = null;
    }

    public ARN(Comparator<? super E> cmp) {
        this.cmp = cmp;
    }

    public ARN(Collection<? extends E> c) {
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

    @Override
    public boolean add(E e) {
        Noeud nouveau = new Noeud(e);
        if (racine == null) {
            racine = nouveau;
            racine.couleur = NOIR; // La racine doit être noire
        } else {
            Noeud current = racine;
            Noeud parent = null;
            while (current != null) {
                parent = current;
                int cmpResult = comparer(e, current.cle);
                if (cmpResult == 0) return false;
                current = cmpResult < 0 ? current.gauche : current.droit;
            }
            int cmpResult = comparer(e, parent.cle);
            if (cmpResult < 0) {
                parent.gauche = nouveau;
            } else {
                parent.droit = nouveau;
            }
            nouveau.parent = parent;
        }

        // Correction de la couleur après l'insertion
        corrigerInsertion(nouveau);
        taille++;
        return true;
    }
    
    private void corrigerInsertion(Noeud x) {
        while (x != racine && x.parent.couleur == ROUGE) {
            if (x.parent == x.parent.parent.gauche) {
                Noeud oncle = x.parent.parent.droit;
                if (oncle != null && oncle.couleur == ROUGE) {
                    // Cas 1: L'oncle est rouge
                    x.parent.couleur = NOIR;
                    oncle.couleur = NOIR;
                    x.parent.parent.couleur = ROUGE;
                    x = x.parent.parent; // Remonter l'arbre
                } else {
                    if (x == x.parent.droit) {
                        // Cas 2: x est à droite, mais parent est à gauche
                        x = x.parent;
                        rotationGauche(x);
                    }
                    // Cas 3: x est à gauche, parent est à gauche
                    x.parent.couleur = NOIR;
                    x.parent.parent.couleur = ROUGE;
                    rotationDroite(x.parent.parent);
                }
            } else {
                // Cas miroir si parent est à droite de son parent
                Noeud oncle = x.parent.parent.gauche;
                if (oncle != null && oncle.couleur == ROUGE) {
                    // Cas 1: L'oncle est rouge
                    x.parent.couleur = NOIR;
                    oncle.couleur = NOIR;
                    x.parent.parent.couleur = ROUGE;
                    x = x.parent.parent; // Remonter l'arbre
                } else {
                    if (x == x.parent.gauche) {
                        // Cas 2: x est à gauche, mais parent est à droite
                        x = x.parent;
                        rotationDroite(x);
                    }
                    // Cas 3: x est à droite, parent est à droite
                    x.parent.couleur = NOIR;
                    x.parent.parent.couleur = ROUGE;
                    rotationGauche(x.parent.parent);
                }
            }
        }
        racine.couleur = NOIR; // La racine doit toujours être noire
    }

    @Override
    public boolean remove(Object o) {
        Noeud node = rechercher(o);
        if (node == null) return false;
        supprimer(node);
        taille--;
        return true;
    }

    private void supprimer(Noeud x) {
        Noeud y = x;
        Noeud xRemplacer;
        boolean couleurOriginale = y.couleur;

        if (x.gauche == null) {
            xRemplacer = x.droit;
            transplanter(x, x.droit);
        } else if (x.droit == null) {
            xRemplacer = x.gauche;
            transplanter(x, x.gauche);
        } else {
            y = x.successeur();  // Utilisation du successeur pour trouver l'élément suivant
            couleurOriginale = y.couleur;
            xRemplacer = y.droit;
            if (y.parent == x) {
                if (xRemplacer != null) xRemplacer.parent = y;
            } else {
                transplanter(y, y.droit);
                y.droit = x.droit;
                y.droit.parent = y;
            }
            transplanter(x, y);
            y.gauche = x.gauche;
            y.gauche.parent = y;
            y.couleur = x.couleur;
        }

        if (couleurOriginale == NOIR) {
            supprimerCorrection(xRemplacer);  // Appel pour rééquilibrer l'arbre
        }
    }

    private void transplanter(Noeud u, Noeud v) {
        if (u.parent == null) {
            racine = v;
        } else if (u == u.parent.gauche) {
            u.parent.gauche = v;
        } else {
            u.parent.droit = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private void supprimerCorrection(Noeud x) {
        while (x != racine && x.couleur == NOIR) {
            Noeud w;

            if (x == x.parent.gauche) {
                w = x.parent.droit; // Frère de x
                if (w.couleur == ROUGE) {
                    w.couleur = NOIR;
                    x.parent.couleur = ROUGE;
                    rotationGauche(x.parent);
                    w = x.parent.droit; // Mise à jour de w après la rotation
                }

                if ((w.gauche == null || w.gauche.couleur == NOIR) && (w.droit == null || w.droit.couleur == NOIR)) {
                    w.couleur = ROUGE;
                    x = x.parent; // On remonte l'arbre pour continuer à équilibrer
                } else {
                    if (w.droit == null || w.droit.couleur == NOIR) {
                        if (w.gauche != null) w.gauche.couleur = NOIR;
                        w.couleur = ROUGE;
                        rotationDroite(w);
                        w = x.parent.droit; // Mise à jour de w après la rotation
                    }

                    w.couleur = x.parent.couleur;
                    x.parent.couleur = NOIR;
                    if (w.droit != null) w.droit.couleur = NOIR;
                    rotationGauche(x.parent);
                    x = racine; // On termine l'équilibrage
                }
            } else {
                // Cas miroir pour le frère gauche
                w = x.parent.gauche;
                if (w.couleur == ROUGE) {
                    w.couleur = NOIR;
                    x.parent.couleur = ROUGE;
                    rotationDroite(x.parent);
                    w = x.parent.gauche; // Mise à jour de w après la rotation
                }

                if ((w.gauche == null || w.gauche.couleur == NOIR) && (w.droit == null || w.droit.couleur == NOIR)) {
                    w.couleur = ROUGE;
                    x = x.parent; // On remonte l'arbre pour continuer à équilibrer
                } else {
                    if (w.gauche == null || w.gauche.couleur == NOIR) {
                        if (w.droit != null) w.droit.couleur = NOIR;
                        w.couleur = ROUGE;
                        rotationGauche(w);
                        w = x.parent.gauche; // Mise à jour de w après la rotation
                    }

                    w.couleur = x.parent.couleur;
                    x.parent.couleur = NOIR;
                    if (w.gauche != null) w.gauche.couleur = NOIR;
                    rotationDroite(x.parent);
                    x = racine; // On termine l'équilibrage
                }
            }
        }
        x.couleur = NOIR;
    }

    private void rotationGauche(Noeud x) {
        Noeud y = x.droit;
        x.droit = y.gauche;
        if (y.gauche != null) y.gauche.parent = x;
        y.parent = x.parent;
        if (x.parent == null) {
            racine = y;
        } else if (x == x.parent.gauche) {
            x.parent.gauche = y;
        } else {
            x.parent.droit = y;
        }
        y.gauche = x;
        x.parent = y;
    }

    private void rotationDroite(Noeud x) {
        Noeud y = x.gauche;
        x.gauche = y.droit;
        if (y.droit != null) y.droit.parent = x;
        y.parent = x.parent;
        if (x.parent == null) {
            racine = y;
        } else if (x == x.parent.droit) {
            x.parent.droit = y;
        } else {
            x.parent.gauche = y;
        }
        y.droit = x;
        x.parent = y;
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

    public int comparer(E e1, E e2) {
        if (cmp == null) {
            return ((Comparable<? super E>) e1).compareTo(e2);
        }
        return cmp.compare(e1, e2);
    }


public class ABRIterator implements Iterator<E> {
    private Noeud courant;

    public ABRIterator() {
        courant = (racine == null) ? null : minimum(racine);
    }

    @Override
    public boolean hasNext() {
        return courant != null;
    }

    @Override
    public E next() {
        if (!hasNext()) throw new NoSuchElementException();
        E val = courant.cle;
        courant = successeur(courant);
        return val;
    }

    // Méthode pour trouver le successeur d'un noeud dans l'arbre
    private Noeud successeur(Noeud node) {
        // Si le noeud a un enfant droit, son successeur est le plus à gauche de cet enfant
        if (node.droit != null) {
            return minimum(node.droit);
        }
        
        // Sinon, on remonte dans l'arbre jusqu'à trouver un noeud qui est à gauche de son parent
        Noeud parent = node.parent;
        while (parent != null && node == parent.droit) {
            node = parent;
            parent = parent.parent;
        }
        return parent;  // Si parent est null, c'est qu'on a atteint la fin de l'arbre
    }

    // Méthode pour obtenir le minimum d'un sous-arbre (le noeud le plus à gauche)
    private Noeud minimum(Noeud node) {
        while (node.gauche != null) {
            node = node.gauche;
        }
        return node;
    }
}
}
