package Controlador;

import Vista.MainFrame;
import java.awt.Color;

public class Control
{
    private final MainFrame mFrame;
    
    public Control(MainFrame mFrame)
    {
        this.mFrame = mFrame;
    }
    
    public void start(String path)
    {
        // Pasa la ruta del archivo abierto
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
