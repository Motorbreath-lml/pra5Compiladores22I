import java.util.ArrayList;
import java.util.HashMap;

public class Gramatica {
    private ArrayList<String> terminales;
    private ArrayList<String> noTerminales;
    private ArrayList<ArrayList <String>> produccciones;
    private HashMap<String,ArrayList <String>> first;
    private HashMap<String,ArrayList <String>> follow;
    /*
    [i] noTerm[0] noTerm[1] term[1] ... α[i]
    * */

    public Gramatica() {
        this.terminales=new ArrayList<>();
        this.noTerminales=new ArrayList<>();
        this.produccciones=new ArrayList<>();
    }
    //La gramatica de esta tarea define a los no terminales entre <>
    //Asumire que to do lo demas es no terminal
    //"<expr>→<expr>+<term>"
    //"<unit>→(<expr>)"
    public void agregarProduccion(String produccion){
        String lhs=produccion.substring(0,produccion.indexOf('→'));
        String rhs=produccion.substring(produccion.indexOf('→')+1);
        int numRegla=produccciones.size();
        produccciones.add(new ArrayList<>());
        produccciones.get(numRegla).add(lhs);
        //Parte izquierda de la produccion es un no terminal
        if(!noTerminales.contains(lhs)){
            noTerminales.add(lhs);
        }
        //Analizar la parte derecha para obtener los elementos que la componen
        char analizar[]=rhs.toCharArray();
        int i=0;
        String elemento="";
        while(i<analizar.length){
            if(analizar[i]=='<'){
                //Analisis de no terminales
                elemento+=analizar[i];
                i++;
                while (analizar[i]!='>'){
                    elemento+=analizar[i];
                    i++;
                }
                elemento+=analizar[i];
                produccciones.get(numRegla).add(elemento);
                elemento="";
            }else{
                //Analisis de terminales
                if (analizar[i]=='n') {
                    terminales.add("num");
                    produccciones.get(numRegla).add("num");
                    i+=3;
                }else {
                    terminales.add(analizar[i] + "");
                    produccciones.get(numRegla).add(analizar[i] + "");
                }
            }
            i++;
        }
    }
    /*Se revisa si la gramatica almacenada es recursiva por la izquierda
    Esta recursividad se debe elimnar para poder usar el parseo LL1
    * */
    public boolean esRecursivaIzquierda(){
        //Se recorre la gramatica, cehcando que el primer elemento de loa produccion
        //No se el mismo
        for (ArrayList<String>produccion:produccciones) {
            if(produccion.get(0).equals(produccion.get(1))){
                return true;
            }
        }
        return false;
    }
    public int numeroReglas(){
        return produccciones.size();
    }

    public boolean esTerminal(String elemento){
        return terminales.contains(elemento);
    }

    public ArrayList<ArrayList <String>> getProduccciones(){
        return produccciones;
    }

    public ArrayList <String> getNoTerminales() {
        return noTerminales;
    }

    public ArrayList <String> getTerminales() {
        return terminales;
    }

    public HashMap<String,ArrayList<String>> getFirst(){return first;}

    public HashMap<String,ArrayList<String>> getFollow(){return follow;}

    public String toString(){
        String gramarica="";
        for (int i = 0; i < produccciones.size(); i++) {
            ArrayList<String> produccion=produccciones.get(i);
            gramarica+=i+": ";
            for (int j = 0; j < produccion.size(); j++) {
                if (j==1)
                    gramarica+="→";
                gramarica+=produccion.get(j);
            }
            gramarica+="\n";
        }
        return gramarica;
    }

    public ArrayList<ArrayList<String>> getProduccionesDelNoTerminal(String noTerminal) {
        ArrayList<ArrayList<String>>produccionesDelNoTerminal=new ArrayList<>();
        for (ArrayList<String>produccion:produccciones) {
            if(produccion.get(0).equals(noTerminal))
                produccionesDelNoTerminal.add(produccion);
        }
        return produccionesDelNoTerminal;
    }

    //El siguiente metodo tiene como objetivo quitar la recursion por la izquierda de la gramatica
    public void refactor() {
        for (int i=0;i<produccciones.size();i++) {
            ArrayList<String>produccion=produccciones.get(i);
            if(produccion.get(0).equals(produccion.get(1))){
                //Definir el no temrinal nuevo
                String noTerminalOriginal=produccion.get(0);
                String nuevoNoTerminal=noTerminalOriginal.substring(0,noTerminalOriginal.indexOf('>'))+"'>";
                ArrayList<String>nuevaRegla=new ArrayList<>();
                nuevaRegla.add(nuevoNoTerminal);

                //Tomar la siguiente regla ya que en este caso el no terminal recursivo produce dos
                //reglas
                ArrayList<String> siguiente= (ArrayList<String>) produccciones.get(i+1).clone();

                //Agregar a la nueva regla los elementos que estan despues del elemento recursivo
                for (int x=2;x<produccion.size();x++){
                    nuevaRegla.add(produccion.get(x));
                }
                //Agregar recursion por la derecha
                nuevaRegla.add(nuevoNoTerminal);
                //Eliminar la regla original y la siguiente
                produccciones.remove(i);
                produccciones.remove(i);

                //Agregar la nueva regla al principio
                produccciones.add(i,nuevaRegla);
                //Se crea otra nueva regla la cual tiene la palabra vacia
                nuevaRegla=new ArrayList<>();
                nuevaRegla.add(nuevoNoTerminal);
                nuevaRegla.add("Є");
                produccciones.add(i+1,nuevaRegla);
                if(!terminales.contains("Є"))
                    terminales.add("Є");
                if(!noTerminales.contains(nuevoNoTerminal))
                    noTerminales.add(nuevoNoTerminal);

                //La segunda regla original hay que reacomodarla
                siguiente.add(nuevoNoTerminal);
                produccciones.add(i,siguiente);
                /*
                for (int x=2;x<produccion.size();x++){
                    if(terminales.contains(produccion.get(x))){
                        String primerNoTerminal=produccion.get(x);
                        produccion.clear();
                        produccion.add(noTerminalOriginal);
                        produccion.add(primerNoTerminal);
                        produccion.add(nuevoNoTerminal);
                        produccion.add("Esto es lo ultimo");
                        break;
                    }
                }*/
            }
        }
    }

    //Calcular first y follow
    public void calcularFirstFollow(){
        first=new HashMap<>();
        follow=new HashMap<>();
        ArrayList<String>tmpFirst;
        //First
        for (ArrayList<String> produccion:produccciones) {
            if(!first.containsKey(produccion.get(0))){
                tmpFirst=new ArrayList<>();
                tmpFirst=obtenerFirst(produccion.get(0));
                first.put(produccion.get(0),tmpFirst);
            }
        }
        //Follow
        ArrayList<String>tmpFollow=new ArrayList<>();
        //El simbolo de termino para la primera regla
        tmpFollow.add("$");
        agregarFollow(tmpFollow,produccciones.get(0).get(0),-1);
        follow.put(produccciones.get(0).get(0),tmpFollow);
        for(int i=0;i<produccciones.size();i++){
            String LHS=produccciones.get(i).get(0);
            if(!follow.containsKey(LHS)){
                tmpFollow=new ArrayList<>();
                agregarFollow(tmpFollow,LHS,i);
                follow.put(LHS,tmpFollow);
            }
        }

    }

    private void agregarFollow(ArrayList<String> tmpFollow, String s,int numPro) {
        for(int i=0;i<produccciones.size();i++) {
            if (i != numPro) {
                ArrayList<String> tmpPro = produccciones.get(i);
                int numElementos = tmpPro.size();
                for (int j = 1; j < numElementos; j++) {
                    String elemento = tmpPro.get(j);
                    //encontrar el elemento en LHS
                    if (s.equals(elemento)) {
                        //Si el elemento esta al final se la prodduccion
                        //A->aX Follow(X) contiene Follow(A)
                        if (j == numElementos - 1) {
                            tmpFollow.addAll(follow.get(tmpPro.get(0)));
                        }else {
                            //Recorrer los elementos siguientes al elemento hasta el final de la produccion
                            for (int z=j+1;z<numElementos;z++) {
                                //El siguiente elemento despues del No terminal en la produccion analizada
                                String siguiente=tmpPro.get(z);
                                //Si el siguienteElemento es un terminal diferes a la palabra vacia
                                if (terminales.contains(siguiente) && !siguiente.equals("Є")) {
                                    tmpFollow.add(siguiente);
                                    break;
                                }else if(noTerminales.contains(siguiente)){
                                    //Agregar el first del noTerminal al follow
                                    agregarElemtosFollow(tmpFollow,first.get(siguiente));
                                    //A->aX Follow(X) contiene Follow(A)
                                    agregarElemtosFollow(tmpFollow,follow.get(tmpPro.get(0)));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void agregarElemtosFollow(ArrayList<String> tmpFollow, ArrayList<String> elementos) {
        for(String elemento:elementos){
            if (!elemento.equals("Є") && !tmpFollow.contains(elemento))
                tmpFollow.add(elemento);
        }
    }

    private ArrayList<String> obtenerFirst(String s) {
        ArrayList<String>first=new ArrayList<>();
        String noTerminal=s;
        for (int i=0;i<produccciones.size();i++){
            ArrayList<String>produccionTmp=produccciones.get(i);
            if(noTerminal.equals(produccionTmp.get(0))){
                if(terminales.contains(produccionTmp.get(1))){
                    first.add(produccionTmp.get(1));
                }else
                    noTerminal=produccionTmp.get(1);
            }
        }
        return first;
    }

    public void mostrarFirst(){
        for (String noTerminal:first.keySet() ) {
            System.out.println("FIRST("+noTerminal+")="+first.get(noTerminal));
        }
    }

    public void mostrarFollow(){
        for (String noTerminal:follow.keySet() ) {
            System.out.println("FOLLOW("+noTerminal+")="+follow.get(noTerminal));
        }
    }
}