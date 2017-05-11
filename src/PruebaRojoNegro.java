public class PruebaRojoNegro {
    public static void main(String[] args){
        String prueba = "U N I V E R S I D A D A N A C I O N A L M A Y O R D E S A N M A R C O S";
        String[] claves = prueba.split(" ");
        ABBRojoNegro<String,Integer> tabla = 
                new ABBRojoNegro<String,Integer>();
        for(int i=0;i<claves.length;i++){
            tabla.insertar(claves[i], i);
        }
        System.out.println("Tamaño: "+tabla.tamanno());
        System.out.println("Mínimo: "+tabla.min());
        System.out.println("Máximo: "+tabla.max());
    }
}
