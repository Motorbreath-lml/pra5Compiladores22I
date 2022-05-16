import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;

public class GramaticaAtributos {

    private ArrayList<String>arbolSecuencial;
    private ArrayList<String>terminales;

    public GramaticaAtributos(ArrayList<String> arbolSecuencial,ArrayList<String>terminales) {
        this.arbolSecuencial = arbolSecuencial;
        this.terminales=terminales;
    }

    public Token resultado(int padre){

        Token padreToken =new Token();
        String simboloPadre=arbolSecuencial.get(padre);

        Token hijoI = null;
        Token hijoC = null;
        Token hijoD = null;

        if(!terminales.contains(simboloPadre)) {
             hijoI= resultado(padre * 3 + 1);
             hijoC = resultado(padre * 3 + 2);
             hijoD = resultado(padre * 3 + 3);
        }

        float valor=-2;
        String tipo="no iniciado";

        switch (simboloPadre){
            case "<expr>":
                /*<expr> → <term> <expr'> null*/
                //Si <expr'> es la palabra vacia
                if(hijoC.getTipo().contains("Є")){
                    valor= hijoI.getValor();
                    tipo= hijoI.getTipo();
                }else{
                    //<expr'> → + <term> <expr'>
                    //esprexion es la suma
                    valor= hijoI.getValor()+ hijoC.getValor();
                    if(hijoI.getTipo().equals("float")||hijoC.getTipo().equals("float")){
                        tipo="float";
                    }else{
                        tipo="int";
                    }
                }
                break;
            case "<expr'>":

                break;
        }
        padreToken.setValor(valor);
        padreToken.setTipo(tipo);
        padreToken.setSimbolo(simboloPadre);
        System.out.println("Token: "+padreToken.getSimbolo()+" tipo: "+padreToken.getTipo()+" valor: "+padreToken.getValor());
        return padreToken;
    }
}
