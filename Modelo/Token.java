package automatatest;

public class Token {
    private String tipoToken;
    private String lexema;
    private int numLinea;
    
    public Token() {
        tipoToken = "";
        lexema = "";
        numLinea = 0;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(int numLinea) {
        this.numLinea = numLinea;
    }
    
    @Override
    public String toString() {
        if (this.tipoToken.equals("?")) {
            return "<" + this.tipoToken + ", " + this.lexema + ">. Error: Token no identificado. Línea: " + this.numLinea;
        } else {
            return "<" + this.tipoToken + ", " + this.lexema + ">";
        }
    }
}
