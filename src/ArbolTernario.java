import java.util.ArrayList;

public class ArbolTernario {
    private ArrayList<String> terminales;
    private ArrayList<String> noTerminales;
    private ArrayList<ArrayList <String>> produccciones;
    private ArrayList<Integer>secuenciaProducciones;
    private ArrayList<String> arbol;


    public ArbolTernario(ArrayList<String> terminales, ArrayList<String> noTerminales, ArrayList<ArrayList<String>> produccciones) {
        this.terminales = terminales;
        this.noTerminales = noTerminales;
        this.produccciones = produccciones;
    }

    public void mostrarArbol(){
        int salto=3;
        int x=0;
        for (int i=0;i<arbol.size();i++) {
            String elemento=arbol.get(i);
            if(!elemento.equals("VACIO")){
                if(i==0)
                    System.out.print("Nivel: "+i+" ");
                System.out.print(elemento);
                if(i!=0) {
                    if (i % 3 == 1)
                        System.out.print("[I] ");
                    else if (i % 3 == 2)
                        System.out.print("[C] ");
                    else
                        System.out.print("[D] ");
                }else
                    System.out.print("[R] ");

            }
            if(i==0){
                x++;
                System.out.print("\nNivel: "+x+" ");
            }
            if(i==salto){
                x++;
                System.out.print("\nNivel: "+x+" ");
                salto*=3;
            }

        }

    }

    public ArrayList<String> crearArbol(ArrayList<Integer> secuenciaProducciones){
        this.arbol=new ArrayList<>();
        this.secuenciaProducciones=secuenciaProducciones;
        //         <expr>
        //         /  |  \
        //       num  + <term>   (<term> -> num)
        //              /  |  \
        //            num NULL NULL

        //Primera produccion - inicializar arbol
        ArrayList<String> producion=produccciones.get(secuenciaProducciones.get(0));
        int tamanioPro=producion.size();
        for (int i=0;i<=3;i++){
            if(i<tamanioPro){
                arbol.add(producion.get(i));
            }else {
                arbol.add("NULL");
            }
        }
        System.out.println("La primera iteraccion del arbol: "+arbol);

        //recorrer las producciones aplicadas
        for (int i=1;i<secuenciaProducciones.size();i++){
            System.out.println("i:"+i+" "+arbol);


            //Obtener la produccion a aplicar
            producion=produccciones.get(secuenciaProducciones.get(i));
            tamanioPro=producion.size();
            String lhs=producion.get(0);
            int arbolTamanio=arbol.size();

            //Se busca primero en profundidad despues entre los nodos tios
            int posicionLHS=buscarNoTerminal(lhs,arbolTamanio-3,arbolTamanio);
            int hijoIzquierdo=posicionLHS*3+1;
            int hijoDerecho=posicionLHS*3+3;

            //Agregar elementos al arbol para que funcionen como nodos vacios
            if(hijoDerecho>arbolTamanio){
                for(int a=arbolTamanio;a<=hijoDerecho;a++){
                    arbol.add("VACIO");
                }
            }

            //Colocar RHS en el arbol
            for(int b=1;b<=3;b++){
                if(b<tamanioPro){
                    arbol.set(hijoIzquierdo,producion.get(b));
                }else{
                    arbol.set(hijoIzquierdo,"NULL");
                }
                hijoIzquierdo++;
            }
        }
        return arbol;
    }

    //Buscar LHS en el arbol para aplicarle a produccion correspondiente
    private int buscarNoTerminal(String lhs,int limInferior, int limSuperior){
        int posicion=0;

        //Buscar entre las hojas si esta lhs
        for (int h=limInferior;h<limSuperior;h++){
            String posiblePadre=arbol.get(h);
            if(lhs.equals(posiblePadre)){
                posicion=h;
                break;
            }else{
                //Buscar en profundidad
                if(noTerminales.contains(posiblePadre)){
                    //Si hay hijos que investigar
                    int posicionPrimerHijo=h*3+1;
                    if(!arbol.get(posicionPrimerHijo).equals("VACIO")){
                        posicion=buscarNoTerminal(lhs,posicionPrimerHijo,posicionPrimerHijo+3);
                        break;
                    }
                }
            }
        }
        //Revisar que el primer hijo de la produccio diha "VACIO"
        //Eso indicaria que esta la variable pero no inicializado
        //Esto para evitar nodos que ya esten llegos
        int posibleHijo=posicion*3+1;
        if(posibleHijo<arbol.size()){
            //Existe en el arbol
            if(!arbol.get(posibleHijo).equals("VACIO")){
                posicion=0;
            }
        }

        //Si no esta en las hojas posicion vale 0 y eso no esta bien ya que seria la raiz
        //Las hojas serian temrinales o NULL
        //Entonces se busca en los tios siguiente
        if(posicion==0){
            //ubicar el padre del limite superior
            int padre=(limSuperior-1)/3;
            padre--;
            int posicionPadre=padre%3;
            if(posicionPadre==0)
                posicionPadre=3;
            //Se buscaria en los tios posteriores al padre
            if(padre!=0)
                posicion=buscarNoTerminal(lhs,padre+1,padre-posicionPadre+4);
            else
                System.out.println("Error lleque a la raiz y no encontre a: "+lhs+" limInf:"+limInferior+" limSup:"+limSuperior);
        }
        return posicion;
    }
}
