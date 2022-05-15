import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //La gramatica esta formada por lista de producciones
        //Conjunto de terminales: terminales
        //Conjunto de no terminales: noTerminales
        //La produccion de inicio es la primera en el Conjunto de Estados [0]

        //Gramatica
        String pro0="<expr>→<expr>+<term>";
        String pro1="<expr>→<term>";
        String pro2="<term>→<term>*<unit>";
        String pro3="<term>→<unit>";
        String pro4="<unit>→num";
        String pro5="<unit>→(<expr>)";
        Gramatica gramatica=new Gramatica();
        gramatica.agregarProduccion(pro0);
        gramatica.agregarProduccion(pro1);
        gramatica.agregarProduccion(pro2);
        gramatica.agregarProduccion(pro3);
        gramatica.agregarProduccion(pro4);
        gramatica.agregarProduccion(pro5);

        System.out.println("Gramatica almacenada:\n"+gramatica.toString());
        System.out.println("La gramatica almacenada es recursiva por la izquierda: "+gramatica.esRecursivaIzquierda());

        if(gramatica.esRecursivaIzquierda()){
            System.out.println("Se realiza una refactorizacion de la gramatica");
            gramatica.refactor();
        }
        System.out.println("La gramatica almacenada es recursiva por la izquierda: "+gramatica.esRecursivaIzquierda());
        System.out.println("\nGramatica refactorizada:\n"+gramatica.toString());

        System.out.println("Los simbolos NO terminales:\n"+gramatica.getNoTerminales());
        System.out.println("Los simbolos terminales:\n"+gramatica.getTerminales());

        gramatica.calcularFirstFollow();

        System.out.println("\nFirst de la gramatica");
        gramatica.mostrarFirst();
        System.out.println("\nFollow de la gramatica");
        gramatica.mostrarFollow();

        Parser parser= new Parser(gramatica.getTerminales(),
                gramatica.getNoTerminales(),gramatica.getProduccciones(),
                gramatica.getFirst(),gramatica.getFollow());

        System.out.println("\nLa tabla parser antes de ser llenada");
        parser.mostrarTablaParser();

        parser.llenarTabalaParser();
        System.out.println("\nLa tabla parser despues de ser LLenada");
        parser.mostrarTablaParser();

        System.out.println("\nProducciones enumeradas:\nNo.|LHS -> RHS");
        System.out.println(gramatica.toString());
        /**/
        String cadenaAnalizar="num * ( num + num )";
        System.out.println("\nLa cadena que se analizara es: "+cadenaAnalizar);

        String []cadenaElementos=cadenaAnalizar.split(" ");
        System.out.println("Numero elementos:"+cadenaElementos.length);

        for (String i:cadenaElementos)
            System.out.println(i);
        /**/

        /**/
        Scanner teclado=new Scanner(System.in);
        cadenaAnalizar=teclado.nextLine();//5*(6.67+22)
        System.out.println("La cadena para lex: "+cadenaAnalizar);
        Lexico lex=new Lexico();
        ArrayList<Token> tokens=lex.tablaSimbolos(cadenaAnalizar);
        for(int i=0;i< tokens.size();i++){
            System.out.println("Token "+i+" "+tokens.get(i));
        }


        /**/

        ArrayList<Integer>secuenciaNumeros=parser.analizarCadena(cadenaElementos);
        parser.mostrarSecuanciaProducciones();

        System.out.println("\nIndices de las producciones aplicadas: "+secuenciaNumeros);

        ArbolTernario arbol=new ArbolTernario(gramatica.getTerminales(),gramatica.getNoTerminales(),gramatica.getProduccciones());

        ArrayList<String>arbolSecuencial=arbol.crearArbol(secuenciaNumeros);

        System.out.println("\nEl arbol ternario en un array completo: "+arbolSecuencial);

        System.out.println("\nLos elementos significativos del arbol\n[R]: Raiz\n[I]: Hijo Izquierdo\n[C]: Hijo Central\n[D]: Hijo Derecho\n\nArbol ternario sintactico\n");
        arbol.mostrarArbol();

    }
}