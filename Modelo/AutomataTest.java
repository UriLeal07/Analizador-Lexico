package automatatest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AutomataTest {
    public static void main(String[] args) {
        Automata automata = new Automata(12);
        boolean ban_archivo;
        try {
            ban_archivo = automata.leerAutomata("FORMATO_DT.txt");
            if (ban_archivo) {
                String cadena = "", aux;
                BufferedReader b = new BufferedReader(new FileReader("ejemplo1.txt"));
        
                while ((aux = b.readLine()) != null) {
                    cadena += aux + "\n";
                }
                cadena = cadena.replace(" ", "");
                cadena = cadena.replace("\t", "");
                ArrayList<Token> tokens = automata.comprobarArchivo(cadena);
                
                for (int i = 0; i < tokens.size(); i++) {
                    System.out.println(tokens.get(i).toString());
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
