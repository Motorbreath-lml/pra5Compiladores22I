public class Token {
    private String simbolo;
    private float valor;
    private String tipo;

    public Token() {
        this.simbolo="";
        this.valor=-1;
        this.tipo="";
    }

    @Override
    public String toString() {
        return "Token{" +
                "simbolo='" + simbolo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", valor=" + valor +
                '}';
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
