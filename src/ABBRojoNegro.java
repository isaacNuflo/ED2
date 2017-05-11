import java.util.NoSuchElementException;

public class ABBRojoNegro <Clave extends Comparable<Clave>, Valor>
{
    private static final boolean ROJO = true;
    private static final boolean NEGRO = false;
    
    private Nodo raiz;
    private int N;
    
    private class Nodo{
        private Clave key;
        private Valor val;
        private Nodo izq,der;
        private boolean color;//enlace al padre
        
        public Nodo(Clave key, Valor val, boolean color){
            this.key = key;
            this.val = val;
            this.color = color;
        }
    }
    private boolean esRojo(Nodo x){
        if (x==null) 
            return false;
        return (x.color == ROJO);
    }
    public boolean estaVacio(){
        return (raiz==null);
    }
    public Valor obtener(Clave key){
        return obtener(raiz,key);
    }
    public Valor obtener(Nodo x, Clave key){
        if (x==null)    return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0)        return obtener(x.izq,key);
        else if (cmp > 0)   return obtener(x.der,key);
        else                return x.val;
    }
    public boolean contiene(Clave key){
        return obtener(key) != null;
    }
    public void insertar(Clave key, Valor val){
        raiz = insertar(raiz,key,val);
        raiz.color = NEGRO;
    }
    private Nodo insertar(Nodo h, Clave key, Valor val){
        if (h==null){
            N = N+1;
            return new Nodo(key,val,ROJO);
        }
        int cmp = key.compareTo(h.key);
        if (cmp<0) h.izq = insertar(h.izq,key,val);
        else if (cmp>0) h.der = insertar(h.der,key,val);
        else h.val = val;
        
        if(esRojo(h.der) && !esRojo(h.izq)) h = rotarIzquierda(h);
        if(esRojo(h.izq) && esRojo(h.izq.izq)) h = rotarDerecha(h);
        if(esRojo(h.izq) && esRojo(h.der)) cambiarColor(h);
        
        return h;
    }
    public void borrarMax() {
        if (estaVacio()) throw new NoSuchElementException("BST underflow");

        if (!esRojo(raiz.izq) && !esRojo(raiz.der))
            raiz.color = ROJO;

        raiz = borrarMax(raiz);
        if (!estaVacio()) raiz.color = NEGRO;
    }
    private Nodo borrarMax(Nodo h) { 
        if (esRojo(h.izq))
            h = rotarDerecha(h);

        if (h.der == null)
            return null;

        if (!esRojo(h.der) && !esRojo(h.der.izq))
            h = moverRojoDerecha(h);

        h.der = borrarMax(h.der);

        return balancear(h);
    }
    public void borrarMin() {
        if (estaVacio()) throw new NoSuchElementException("BST underflow");

        if (!esRojo(raiz.izq) && !esRojo(raiz.der))
            raiz.color = ROJO;

        raiz = borrarMin(raiz);
        if (!estaVacio()) raiz.color = NEGRO;
    }
    private Nodo borrarMin(Nodo h) { 
        if (h.izq == null)
            return null;

        if (!esRojo(h.izq) && !esRojo(h.izq.izq))
            h = moverRojoIzquierda(h);

        h.izq = borrarMin(h.izq);
        return balancear(h);
    }
    public void borrar(Clave key) { 
        if (!contiene(key)) {
            System.err.println("symbol table does not contain " + key);
            return;
        }

        if (!esRojo(raiz.izq) && !esRojo(raiz.der))
            raiz.color = ROJO;

        raiz = borrar(raiz, key);
        if (!estaVacio()) raiz.color = NEGRO;
    }
    private Nodo borrar(Nodo h, Clave key) { 
        // assert get(h, key) != null;

        if (key.compareTo(h.key) < 0)  {
            if (!esRojo(h.izq) && !esRojo(h.izq.izq))
                h = moverRojoIzquierda(h);
            h.izq = borrar(h.izq, key);
        }
        else {
            if (esRojo(h.izq))
                h = rotarDerecha(h);
            if (key.compareTo(h.key) == 0 && (h.der == null))
                return null;
            if (!esRojo(h.der) && !esRojo(h.der.izq))
                h = moverRojoDerecha(h);
            if (key.compareTo(h.key) == 0) {
                Nodo x = minNodo(h.der);
                h.key = x.key;
                h.val = x.val;
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.der = borrarMin(h.der);
            }
            else h.der = borrar(h.der, key);
        }
        return balancear(h);
    }
    
    private Nodo balancear(Nodo h) {

        if (esRojo(h.der))                      h = rotarIzquierda(h);
        if (esRojo(h.izq) && esRojo(h.izq.izq)) h = rotarDerecha(h);
        if (esRojo(h.izq) && esRojo(h.der))     intercambiarColores(h);

        //h.N = size(h.left) + size(h.right) + 1;
        return h;
    }
    private Nodo moverRojoIzquierda(Nodo h) {

        intercambiarColores(h);
        if (esRojo(h.der.izq)) { 
            h.der = rotarDerecha(h.der);
            h = rotarIzquierda(h);
            intercambiarColores(h);
        }
        return h;
    }
    private Nodo moverRojoDerecha(Nodo h) {
        intercambiarColores(h);
        if (esRojo(h.izq.izq)) { 
            h = rotarDerecha(h);
            intercambiarColores(h);
        }
        return h;
    }   
    private void intercambiarColores(Nodo h) {
        h.color = !h.color;
        h.izq.color = !h.izq.color;
        h.der.color = !h.der.color;
    }
    private Nodo rotarDerecha(Nodo h){
        Nodo x = h.izq;
        h.izq = x.der;
        x.der = h;
        x.color = h.color;
        h.color = ROJO;
        return x;
    }
    private Nodo rotarIzquierda(Nodo h){
        Nodo x = h.der;
        h.der = x.izq;
        x.izq = h;
        x.color = h.color;
        h.color = ROJO;
        return x;
    }
    private void cambiarColor(Nodo h){
        h.color = ROJO;
        h.izq.color = NEGRO;
        h.der.color = NEGRO;
    }
    public int tamanno(){
        return N;
    }
    public int altura(){
        return altura(raiz);
    }
    public int altura(Nodo x){
        if (x==null)
            return -1;
        return 1 + Math.max(altura(x.izq),altura(x.der));
    }
    public Clave min(){
        return min(raiz);
    }
    private Clave min(Nodo x){
        Clave key = null;
        while (x != null){
            key = x.key;
            x = x.izq;
        }
        return key;
    }
    private Nodo minNodo(Nodo x) { 
        // assert x != null;
        if (x.izq == null) return x; 
        else  return minNodo(x.izq); 
    } 
    public Clave max(){
        return max(raiz);
    }
    private Clave max(Nodo x){
        Clave key = null;
        while (x != null){
            key = x.key;
            x = x.der;
        }
        return key;
    }
    private Nodo maxNodo(Nodo x) { 
        // assert x != null;
        if (x.der == null) return x; 
        else                 return maxNodo(x.der); 
    } 
    public Clave floor(Clave key) {
        if (estaVacio()) throw new NoSuchElementException("called floor() with empty symbol table");
        Nodo x = floor(raiz, key);
        if (x == null) return null;
        else           return x.key;
    }    
    private Nodo floor(Nodo x, Clave key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0)  return floor(x.izq, key);
        Nodo t = floor(x.der, key);
        if (t != null) return t; 
        else           return x;
    }
    public Clave ceiling(Clave key) {  
        if (estaVacio()) throw new NoSuchElementException("called ceiling() with empty symbol table");
        Nodo x = ceiling(raiz, key);
        if (x == null) return null;
        else           return x.key;  
    }

    private Nodo ceiling(Nodo x, Clave key) {  
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp > 0)  return ceiling(x.der, key);
        Nodo t = ceiling(x.izq, key);
        if (t != null) return t; 
        else           return x;
    }
    public int rank(Clave key) {
        return rank(key, raiz);
    } 

    // number of keys less than key in the subtree rooted at x
    private int rank(Clave key, Nodo x) {
        if (x == null) return 0; 
        int cmp = key.compareTo(x.key); 
        if      (cmp < 0) return rank(key, x.izq); 
        else if (cmp > 0) return 1 + altura(x.izq) + rank(key, x.izq); 
        else              return altura(x.izq); 
    } 
    public Iterable<Clave> claves(){
        Cola<Clave> cola = new Cola<Clave>();
        claves(raiz,cola);
        return cola;
    }
    public void claves(Nodo x, Cola<Clave> cola){
        if (x==null) return;
        claves(x.izq,cola);
        cola.entrar(x.key);
        claves(x.der,cola);
    }
}
