package no.oslomet.cs.algdat.Oblig3;


import java.util.*;

public class SBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public SBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    //Oppgave1
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "ikke lov med nullverdier");

        Node<T> p = rot, q = null;
        int cmp = 0;
        while (p != null) {
            q = p;
            cmp = comp.compare(verdi, p.verdi);
            p = cmp < 0 ? p.venstre : p.høyre;
        }
        p = new Node<>(verdi, q);

        if (q == null) rot = p;
        else if (cmp < 0) q.venstre = p;
        else q.høyre = p;

        antall++;
        endringer++;
        return true;
    }


    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    public int fjernAlle(T verdi) {
        int antallslettet = 0;
        if (!tom()) {
            while (fjern(verdi)) {
                antallslettet++;
            }
        }
        return antallslettet;

    }

    //Opgave2
    public int antall(T verdi) {
        Node<T> p = rot;
        int teller = 0;
        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) {
                p = p.venstre;
            } else {
                if (cmp == 0) {
                    teller++;
                    p = p.høyre;
                }
            }
        }
        return teller;
    }


    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


   //Oppgave3 del1
    private static <T> Node<T> førstePostorden(Node<T> p) {
        if (p == null) {
            throw new NoSuchElementException("Dette treet er tomt!!");
        }
        while (true) {
            if (p.venstre != null) p = p.venstre;
            else if (p.høyre != null) p = p.høyre;
            else return p;
        }
    }
    //Oppgave3 del2
    private static <T> Node<T> nestePostorden(Node<T> p) {
        if (p == null) {
            throw new NoSuchElementException("Dette treet er tomt!!");
        }

        else if (p.forelder == null) {
            p = null;
        }

        else if (p == p.forelder.høyre) {
            p = p.forelder;
        }

        else if (p == p.forelder.venstre){

            if (p.forelder.høyre == null){
                p = p.forelder;
    }
            else
                p= førstePostorden (p.forelder.høyre);

        }
        return p;
    }

    //Oppgave4
    public void postorden(Oppgave<? super T> oppgave) {
        if(rot == null)
            return;
        Node<T> q = førstePostorden (rot);
        oppgave.utførOppgave(q.verdi);
        Node<T> r = nestePostorden(q);
        while (r != null){
            oppgave.utførOppgave(r.verdi);
            r= nestePostorden(r);
        }
    }

//opggave4
    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

//oppgave4
    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        if(p == null){
            return;
        }
        if(p.venstre != null){
            postordenRecursive(p.venstre, oppgave);
        }
        if(p.høyre != null){
            postordenRecursive(p.høyre,oppgave);
        }
        oppgave.utførOppgave(p.verdi);
    }
//oppgave5
    public ArrayList<T> serialize() {
        ArrayList<T> list = new ArrayList<>();
        ArrayDeque<Node> kø = new ArrayDeque<Node>();

        kø.addLast(rot);
        while (!kø.isEmpty()) {

            Node<T> nåværende = kø.removeFirst();

            if (nåværende.venstre != null) {
                kø.addLast(nåværende.venstre);
            }
            if (nåværende.høyre != null) {
                kø.addLast(nåværende.høyre);
            }
            list.add(nåværende.verdi);

        }
        return list;
    }

    static <K> SBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        SBinTre<K> tre = new SBinTre<K>(c);
        for (int i = 0; i < data.size(); i++){
            tre.leggInn(data.get(i));
        }
        return tre;
    }


} // ObligSBinTre
