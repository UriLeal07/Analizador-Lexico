package automatatest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Automata {
    String[] alfabeto;
    int[] estados;
    int[] retrocesos;
    String[] tipos_tokens;
    int estado_inicial;
    int[] estados_finales;
    String[] palabras_reservadas;
    int[][] matriz_T;
    int limite;
    
    public Automata(int limite) {
        this.limite = limite;
    }
    
    public boolean leerAutomata(String ruta) throws IOException, FileNotFoundException {
        String cadena = "";
        BufferedReader b = new BufferedReader(new FileReader(ruta));
        int cont = 0;
        
        while ((cadena = b.readLine()) != null && cont < limite) {
            String[] aux;
            cont++;
            switch (cont) {
                case 2:
                    aux = cadena.split(",");
                    alfabeto = new String[aux.length];
                    alfabeto = aux;
                    break;
                case 4:
                    aux = cadena.split(",");
                    estados = new int[aux.length];
                    for (int i = 0; i < estados.length; i++) {
                        estados[i] = Integer.parseInt(aux[i]);
                    }
                    break;
                case 6:
                    aux = cadena.split(",");
                    retrocesos = new int[aux.length];
                    for (int i = 0; i < retrocesos.length; i++) {
                        retrocesos[i] = Integer.parseInt(aux[i]);
                    }
                    break;
                case 8:
                    aux = cadena.split(",");
                    tipos_tokens = new String[aux.length];
                    tipos_tokens = aux;
                    break;
                case 10:
                    estado_inicial = Integer.parseInt(cadena);
                    break;
                case 12:
                    aux = cadena.split(",");
                    estados_finales = new int[aux.length];
                    for (int i = 0; i < estados_finales.length; i++) {
                        estados_finales[i] = Integer.parseInt(aux[i]);
                    }
                    break;
                case 14:
                    aux = cadena.split(",");
                    palabras_reservadas = aux;
                    break;
            }
        }
        
        if (cadena == null && cont < limite) {
            System.out.println("ERROR AL LEER EL ARCHIVO");
            return false;
        } else {
            matriz_T = new int[estados.length][alfabeto.length];
            for (int i = 0; i < estados.length; i++) {
                for (int j = 0; j < alfabeto.length; j++) {
                    matriz_T[i][j] = -1;
                }
            }
            while ((cadena = b.readLine()) != null) {
                String[] aux = cadena.split(",");
                String[] tmp = aux[0].split("/");
                int col = -1;
                boolean ban = false;
                int i = 0;
                while (i < alfabeto.length && !ban) {
                    if (alfabeto[i].equals(tmp[1])) {
                        col = i;
                        ban = true;
                    }
                    i++;
                }
                matriz_T[Integer.parseInt(tmp[0])][col] = Integer.parseInt(aux[1]);
            }
            return true;
        }
    }
    
    public boolean comprobarCadena(String cadena) {
        int estado_actual = estado_inicial;
        for (int i = 0; i < cadena.length(); i++) {
            char aux = cadena.charAt(i);
            int j = 0;
            boolean ban = false;
            while (j < alfabeto.length && !ban) {
                if (alfabeto[j].equals(String.valueOf(aux))) {
                    if (matriz_T[estado_actual][j] != -1) {
                        estado_actual = matriz_T[estado_actual][j];
                        ban = true;
                    }
                }
                j++;
            }
            if (!ban) {
                return false;
            }
        }
        boolean ban = false;
        int i = 0;
        while (i < estados_finales.length && !ban) {
            if (estados_finales[i] == estado_actual) {
                ban = true;
            }
            i++;
        }
        return ban;
    }
    
    public ArrayList<Token> comprobarArchivo(String cadena) {
        ArrayList<Token> tokens = new ArrayList<>();
        // limpiar cadena
        cadena = cadena.replaceAll("\t", " ");
        String aux = "";
        int i = 0;
        while (i < cadena.length()) {
            if (cadena.charAt(i) != ' ') {
                aux += cadena.charAt(i);
                i++;
            } else {
                aux += cadena.charAt(i);
                do {
                    i++;
                } while (cadena.charAt(i) == ' ');
            }
        }
        cadena = aux + " ";
        char[] buffer = cadena.toCharArray();
        
        int estado_actual = estado_inicial;
        int numLinea = 0;
        String lexema = "";
        int apuntador = 0;
        while (apuntador < buffer.length) {
            char temp = buffer[apuntador];
            
            // si es salto de línea
            if (temp == '\n') {
                numLinea++;
                temp = ' ';
                
            } 
            if (Character.isLetter(temp)) {
                temp = 'l';
            } else if (Character.isDigit(temp)) {
                temp = 'd';
            }

            int j = 0;
            boolean ban = false;
            while (j < alfabeto.length && !ban) {
                if (alfabeto[j].equals(String.valueOf(temp))) {
                    if (matriz_T[estado_actual][j] != -1) {
                        estado_actual = matriz_T[estado_actual][j];
                        ban = true;
                    }
                }
                j++;
            }

            // si no se encontró transición
            if (!ban) {
                if (temp != ' ') {
                    lexema += temp;
                    Token t = new Token();
                    t.setLexema(lexema);
                    t.setNumLinea(numLinea + 1);
                    t.setTipoToken("?");
                    tokens.add(t);

                    // se limpia lexema
                    lexema = "";
                    estado_actual = estado_inicial;
                }
                apuntador++;
            } else {
                // se comprueba si llegó a un estado final
                j = 0;
                ban = false;
                while (j < estados_finales.length && !ban) {
                    if (estados_finales[j] == estado_actual) {
                        ban = true;
                    }
                    j++;
                }

                // si es estado final
                if (ban) {
                    // si es un estado final sin retroceso, se puede agregar el caracter en el lexema
                    if (retrocesos[estado_actual] == 0) {
                        lexema += buffer[apuntador];
                    }

                    Token t = new Token();
                    j = 0;
                    ban = false;
                    while (j < palabras_reservadas.length && !ban) {
                        if (lexema.equals(palabras_reservadas[j])) {
                            ban = true;
                        }
                        j++;
                    }
                    t.setLexema(lexema);
                    t.setNumLinea(numLinea + 1);
                    // si es palabra reservada
                    if (ban) {
                        t.setTipoToken("palabraReservada");
                    } else {
                        t.setTipoToken(tipos_tokens[estado_actual]);
                    }
                    tokens.add(t);

                    apuntador++;
                    // actualizar el apuntador
                    if (apuntador > 0 && buffer[apuntador - 1] != '\n') {
                        apuntador = apuntador - retrocesos[estado_actual];
                    }
                    lexema = "";
                    estado_actual = estado_inicial;
                } else {
                    lexema += buffer[apuntador];
                    apuntador++;
                }
            }
        }
        
        /* cadena = cadena.trim();
        String[] cadenas = cadena.split("\n");        
        int i = 0;
        while (i < cadenas.length) {
            int j = 0;
            while (j < cadenas[i].length()) {
                char caracter = cadenas[i].charAt(j);
                char temp;
                // se comprueba qué tipo de caracter es y se almacena en una variable temporal.
                if (Character.isLetter(caracter)) {
                    temp = 'l';
                } else if (Character.isDigit(caracter)) {
                    temp = 'd';
                } else {
                    temp = caracter;
                }
                int k = 0;
                boolean ban = false;
                while (k < alfabeto.length && !ban) {
                    if (alfabeto[k].equals(String.valueOf(temp))) {
                        if (matriz_T[estado_actual][k] != -1) {
                            estado_actual = matriz_T[estado_actual][k];
                            ban = true;
                        }
                    }
                    k++;
                }
                
                // si no encontró transición
                if (!ban) {
                    lexema += caracter;
                    Token t = new Token();
                    t.setTipoToken("?");
                    t.setLexema(lexema);
                    t.setNumLinea(i + 1);
                    tokens.add(t);
                    lexema = "";
                    estado_actual = 0;
                    j++;
                } else {
                    // se comprueba si llegó a un estado final
                    k = 0;
                    ban = false;
                    while (k < estados_finales.length && !ban) {
                        if (estados_finales[k] == estado_actual) {
                            ban = true;
                        }
                        k++;
                    }
                    
                    if (ban) {
                        // si es un estado final sin retroceso
                        if (retrocesos[estado_actual] == 0) {
                            lexema += caracter;
                        }
                        j++;
                        // actualizar el apuntador a la cadena
                        if (j > 0) {
                            j = j - retrocesos[estado_actual];
                        }
                        Token t = new Token();
                        t.setTipoToken(tipos_tokens[estado_actual]);
                        t.setLexema(lexema);
                        if (j == 0) {
                            t.setNumLinea(i);
                        } else {
                            t.setNumLinea(i + 1);
                        }
                        tokens.add(t);
                        lexema = "";
                        estado_actual = 0;
                    } else {
                        // si no es un estado final
                        lexema += caracter;
                        j++;
                    }
                }  
            }
            i++;
        } */
        
        return tokens;
    }
}
