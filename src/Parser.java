import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private ArrayList<String> terminales;
    private ArrayList<String> noTerminales;
    private ArrayList<ArrayList <String>> produccciones;
    private HashMap<String,ArrayList <String>> first;
    private HashMap<String,ArrayList <String>> follow;
    private String[] columnas;
    private  HashMap<String,Integer[]> tablaParser;

    private ArrayList<String>secuenciaProducciones;

    public Parser(ArrayList<String> terminales, ArrayList<String> noTerminales, ArrayList<ArrayList<String>> produccciones, HashMap<String, ArrayList<String>> first, HashMap<String, ArrayList<String>> follow) {
        this.terminales = terminales;
        this.noTerminales = noTerminales;
        this.produccciones = produccciones;
        this.first = first;
        this.follow = follow;
        this.tablaParser=new HashMap<>();
        llenarColumans();
        inicializarTablaParser();
    }

    public void llenarTabalaParser(){
        for(String noTerminalI:noTerminales){
            for(int i=0;i<produccciones.size();i++){
                ArrayList<String>produccionI=produccciones.get(i);
                //Ubicar las produccion que produce el noTerminalI
                if(noTerminalI.equals(produccionI.get(0))){
                    //Revisar que si lo primero que vee es un terminal
                    String primerElemento=produccionI.get(1);
                    //Si es la palabra vacia
                    if(primerElemento.equals("Є")){
                        //Usar los elementos del Follow
                        ArrayList<String>folloNoTerminalI=follow.get(noTerminalI);
                        //Obtener el array de tabla parser correspondiente al no terminal
                        Integer[]parserNoterminalI=tablaParser.get(noTerminalI);
                        //Recorrer las columnas, en busca de que un follow coincida
                        //Agregar al array Integer el numero de la produccion correspondiente
                        for (int x=0;x<columnas.length;x++) {
                            if(folloNoTerminalI.contains(columnas[x])){
                                parserNoterminalI[x]=i;
                            }
                        }
                    }else if(terminales.contains(primerElemento)){
                        //Si lo primero que se ve es un terminal
                        //Obtener el array de tabla parser correspondiente al no terminal
                        Integer[]parserNoterminalI=tablaParser.get(noTerminalI);
                        for (int x=0;x<columnas.length;x++) {
                            if(primerElemento.equals(columnas[x])){
                                parserNoterminalI[x]=i;
                            }
                        }
                    }else{
                        //El primer elemento es un NoTerminal
                        //Hay que usar First
                        //El first del NoTerminalI y del primerElemento contienen lo mismo
                        ArrayList<String>firstNoterminalI=first.get(noTerminalI);
                        //Obtener el array de tabla parser correspondiente al no terminal
                        Integer[]parserNoterminalI=tablaParser.get(noTerminalI);
                        for (int x=0;x<columnas.length;x++) {
                            if(firstNoterminalI.contains(columnas[x])){
                                parserNoterminalI[x]=i;
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Integer> analizarCadena(String[]cadena){
        ArrayList<Integer> secuencia=new ArrayList<>();
        ArrayList<String> entrada=new ArrayList<>();
        ArrayList<String> pila=new ArrayList<>();
        secuenciaProducciones=new ArrayList<>();

        //Llenar la entrada con el arreglo y poner al final "$" que simboliza el final de cadena
        for (String elemento:cadena) {
            entrada.add(elemento);
        }
        entrada.add("$");
        System.out.println(entrada);

        //Los primeros elementos de la pila
        //poner el primer No terminal de la primera produccion
        pila.add(produccciones.get(0).get(0));
        //El simbolo fin de la pila
        pila.add("$");
        System.out.println(pila);

        boolean cadenaTerminada=false;
        boolean pilaTerminada=false;

        while(!cadenaTerminada || !pilaTerminada){
            String topeCadena=entrada.get(0);
            String topePila=pila.get(0);
            //Revisamos la condicion de terminado
            if(topeCadena.equals("$") && topePila.equals("$")){
                cadenaTerminada=pilaTerminada=true;
                System.out.println("\nCadena aceptada!\n");
            }
            //si ambos son iguales es por que ambos son terminales
            else if(topeCadena.equals(topePila)) {
                entrada.remove(0);
                pila.remove(0);
                //La pila se le aplico la palabra vacia
            }else if(topePila.equals("Є")){
                //Se retira la palabra vacia de la pila
                pila.remove(0);
                //La pila tiene un NoTerminal
            }else if(noTerminales.contains(topePila)){
                //Obtener el numero de columna de la entrada
                int i;
                for (i=0;i<columnas.length;i++ ) {
                    if(topeCadena.equals(columnas[i])){
                        break;
                    }
                }
                //Obtener la produccion a aplicar
                int numProd=tablaParser.get(topePila)[i];
                secuencia.add(numProd);
                String proSecuencia="";
                ArrayList<String> produccionI=produccciones.get(numProd);
                proSecuencia+=produccionI.get(0)+"->";
                //Remover el No terminal padre
                pila.remove(0);
                //Agregar LHS de la produccion a la pila
                for(int x=1;x<produccionI.size();x++){
                    pila.add(x-1,produccionI.get(x));
                    proSecuencia+=produccionI.get(x);
                }
                secuenciaProducciones.add(proSecuencia);
            }
            //Hay un error ya que se espera un no terminal
            //en la pila
            else{
                System.out.println("Tope pila:"+topePila+" Tope cadena:"+topeCadena);
                System.out.println("Error hay un terminal en lugar de un No terminal en la pila");
                break;
            }
        }
        return secuencia;
    }

    public void mostrarSecuanciaProducciones(){
        System.out.println("\nSecuencia de producciones aplicadas");
        for(int i=0;i<secuenciaProducciones.size();i++){
            System.out.println(secuenciaProducciones.get(i));
        }
    }

    private void mostrarColumas(){
        System.out.print("\t");
        for (int i=0;i<columnas.length;i++) {
            System.out.print("\t"+columnas[i]);
        }
        System.out.println();
    }
    public void mostrarTablaParser(){
        mostrarColumas();
        for(String llave:tablaParser.keySet()){
            System.out.print(llave+"\t");
            Integer[] arreglo=tablaParser.get(llave);
            for(int i=0;i<arreglo.length;i++){
                if(arreglo[i]<0)
                    System.out.print("-\t");
                else
                    System.out.print(arreglo[i]+"\t");
            }
            System.out.println();
        }
    }

    private void inicializarTablaParser(){
        for (String noTerminal:noTerminales) {

            Integer[]simbolosTerminales=new Integer[columnas.length];
            for (int i=0;i<columnas.length;i++) {
                simbolosTerminales[i]=-1;
            }
            tablaParser.put(noTerminal, simbolosTerminales);

        }

    }

    private void llenarColumans() {
        columnas=new String[terminales.size()];
        int i=0;
        for (String terminal:terminales) {
            if(terminal.equals("Є")){
                columnas[i]="$";
            }else {
                columnas[i]=terminal;
            }
            i++;
        }
    }
}
