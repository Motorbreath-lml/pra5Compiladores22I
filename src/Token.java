public class Token {
    private String simbolo;
    private int valorInt;
    private float valorfloat;
    private String tipo;

    public Token() {
        this.simbolo="";
        this.valorfloat=-1;
        this.valorInt=-1;
        this.tipo="";
    }

    @Override
    public String toString() {
        return "Token{" +
                "simbolo='" + simbolo + '\'' +
                ", valorInt=" + valorInt +
                ", valorfloat=" + valorfloat +
                ", tipo='" + tipo + '\'' +
                '}';
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public int getValorInt() {
        return valorInt;
    }

    public void setValorInt(int valorInt) {
        this.valorInt = valorInt;
    }

    public float getValorfloat() {
        return valorfloat;
    }

    public void setValorfloat(float valorfloat) {
        this.valorfloat = valorfloat;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
