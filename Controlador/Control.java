package Controlador;

import Modelo.Automata;
import Modelo.Token;
import Vista.MainFrame;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Control
{
    private final MainFrame mFrame;
    private final Automata auto;
    private boolean ban_archivo;
    
    public Control(MainFrame mFrame)
    {
        this.mFrame = mFrame;
        auto = new Automata(14);
        ban_archivo = false;
    }
    
    public void start(String path, String expresion)
    {
        try
        {
            ban_archivo = auto.leerAutomata(path);
            
            if (ban_archivo)
            {
                ArrayList<Token> tokens = auto.comprobarArchivo(expresion);
                String res = "";
                
                for (int i = 0; i < tokens.size(); i++) 
                    res += tokens.get(i).toString()+"\n";
                
                printOutput(res);
            }
            else
            {
                setError("No se han cargado las reglas del Analizador");
            }
        }
        catch (IOException e) {
        }
    }
    
    public void setError(String err) { mFrame.getTxtError().setText(err); }
    
    public void printOutput(String out) { mFrame.getTxtOutput().append(out+"\n"); }
    
    public void printOutError(String out)
    {
        mFrame.getTxtOutput().setForeground(Color.red);
        mFrame.getTxtOutput().append(out+"\n");
        
        mFrame.getTxtError().setText(out);
    }
}
