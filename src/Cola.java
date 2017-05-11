import java.util.Iterator;
import java.util.NoSuchElementException;


public class Cola<Item> implements Iterable<Item> {
    private int N;         // number of elements on queue
    private Nodo inicio;    // beginning of queue
    private Nodo ultimo;     // end of queue

    // helper linked list class
    private class Nodo {
        private Item item;
        private Nodo prox;
    }

   /**
     * Create an empty queue.
     */
    public Cola() {
        inicio = null;
        ultimo  = null;
        N = 0;
        assert revisar();
    }

   /**
     * Is the queue empty?
     */
    public boolean estaVacio() {
        return inicio == null;
    }

   /**
     * Return the number of items in the queue.
     */
    public int tamanno() {
        return N;     
    }

   /**
     * Return the item least recently added to the queue.
     * @throws java.util.NoSuchElementException if queue is empty.
     */
    public Item alFinal() {
        if (estaVacio()) throw new NoSuchElementException("Cola vacia");
        return inicio.item;
    }

   /**
     * Add the item to the queue.
     */
    public void entrar(Item item) {
        Nodo anteriorUltimo = ultimo;
        ultimo = new Nodo();
        ultimo.item = item;
        ultimo.prox = null;
        if (estaVacio()) inicio = ultimo;
        else           anteriorUltimo.prox = ultimo;
        N++;
        assert revisar();
    }

   /**
     * Remove and return the item on the queue least recently added.
     * @throws java.util.NoSuchElementException if queue is empty.
     */
    public Item salir() {
        if (estaVacio()) throw new NoSuchElementException("Cola vacia");
        Item item = inicio.item;
        inicio = inicio.prox;
        N--;
        if (estaVacio()) ultimo = null;   // to avoid loitering
        assert revisar();
        return item;
    }

   /**
     * Return string representation.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    } 

    // revisar internal invariants
    private boolean revisar() {
        if (N == 0) {
            if (inicio != null) return false;
            if (ultimo  != null) return false;
        }
        else if (N == 1) {
            if (inicio == null || ultimo == null) return false;
            if (inicio != ultimo)                 return false;
            if (inicio.prox != null)            return false;
        }
        else {
            if (inicio == ultimo)      return false;
            if (inicio.prox == null) return false;
            if (ultimo.prox  != null) return false;

            // revisar internal consistency of instance variable N
            int numeroDeNodos = 0;
            for (Nodo x = inicio; x != null; x = x.prox) {
               numeroDeNodos++;
            }
            if (numeroDeNodos != N) return false;

            // revisar internal consistency of instance variable ultimo
            Nodo ultimoNodo = inicio;
            while (ultimoNodo.prox != null) {
               ultimoNodo = ultimoNodo.prox;
            }
            if (ultimo != ultimoNodo) return false;
        }

        return true;
    } 
 

   /**
     * Return an iterator that iterates over the items on the queue in FIFO order.
     */
    public Iterator<Item> iterator()  {
        return new IteradorDeLista();  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class IteradorDeLista implements Iterator<Item> {
        private Nodo current = inicio;

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.prox; 
            return item;
        }
    }
   
}

