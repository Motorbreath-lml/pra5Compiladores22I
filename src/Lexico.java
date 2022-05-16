import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexico {

    public ArrayList<Token> tablaSimbolos(String cadena) {
        ArrayList<Token> tablaSimbolos=new ArrayList<>();

        /*Obtener en donde estan los numeros*/
        ArrayList<Integer> indices=identificarNumeros(cadena);

        //Recorrer la cadena obteniendo los tokens
        int inicioDeNumero=-1;
        for(int i=0;i<cadena.length();i++){
            if(!indices.isEmpty()) {
                inicioDeNumero = indices.get(0);
            }else {
                inicioDeNumero = -1;
            }
            if (i == inicioDeNumero) {
                //Obtener el numero de la cadena
                String subcadena = cadena.substring(indices.get(0), indices.get(1));
                //Quitar los primeros indices, para preparar si es que hay otro nomuero
                indices.remove(0);
                indices.remove(0);

                Token numero = new Token();
                numero.setSimbolo("num");
                numero.setValor(Float.parseFloat(subcadena));
                //Identificar si el numero es flotante
                if (subcadena.contains(".")) {
                    numero.setTipo("float");
                } else {
                    numero.setTipo("int");
                }
                tablaSimbolos.add(numero);
            }else{
                char elemento=cadena.charAt(i);
                switch (elemento){
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '(':
                    case ')':
                        Token terminal=new Token();
                        terminal.setTipo("terminal");
                        terminal.setSimbolo(elemento+"");
                        tablaSimbolos.add(terminal);
                        break;
                    case ' ':
                    case '\n':
                    case '\t':
                    case '.':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    case '0':
                        /*ignorarlos*/
                        break;
                    default:
                        /*En caso de que exista otro elemento, se mandaria error*/
                        System.out.println("Caracter no reconocido: '"+elemento+"' en la posicion: "+i);
                }
            }
        }
        return tablaSimbolos;
    }

    /*Encontrar en donde empieza y termina un numero en la cadena*/
    private ArrayList<Integer> identificarNumeros(String cadena){
        /*Son parejas de numero el primero es el inicio del numero
        * y el segundo es el final del numero*/
        ArrayList<Integer> indices=new ArrayList<>();

        Pattern pattern = Pattern.compile("([0-9]+\\.?[0-9]*)");
        Matcher matcher = pattern.matcher(cadena);//"112.6+152*12.0+55.98"
        while(matcher.find()){
            indices.add(matcher.start());
            indices.add(matcher.end());
        }
        return indices;
    }
}
