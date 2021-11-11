 
package generaralbaranesfarmaciapdf.utils;
 
public class Desencriptar {

    public String generaClave_old(String clave) {
        String newclave = "";
        for (int i = 0; i < clave.length(); i++) {
            char letra;
            letra = clave.charAt(i);
            int codigoAsciiLetra = clave.codePointAt(i);
            codigoAsciiLetra++;
            char newletra = (char) codigoAsciiLetra;
            // System.out.println("Caracter " + i + ": " + letra + "--->"+ newletra);
            if (!newclave.isEmpty()) {
                newclave = newclave + newletra;
            } else {
                newclave = "" + newletra;
            }

        }

        return newclave;
    }

    public String descifraClave_old(String clave) {
        String newclave = "";
        for (int i = 0; i < clave.length(); i++) {
            char letra;
            letra = clave.charAt(i);
            int codigoAsciiLetra = clave.codePointAt(i);
            codigoAsciiLetra--;
            char newletra = (char) codigoAsciiLetra;
            //  System.out.println("Caracter " + i + ": " + letra + "--->"+ newletra);
            if (!newclave.isEmpty()) {
                newclave = newclave + newletra;
            } else {
                newclave = "" + newletra;
            }

        }

        return newclave;
    }

    public String generaClave(String clave) {
        char letra_original, letra_transformada;

        String original = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        String transfor = "~}|{zyxwvutsrqponmlkjihgfedcba`_^]\\[ZYXWVUTSRQPONMLKJIHGFEDCBA@?>=<;:9876543210/.-,+*)('&%$#\"!";
        String newClave = "";
        for (int i = 0; i < clave.length(); i++) {
            letra_original = clave.charAt(i);
            int posicion = original.indexOf(letra_original);
            letra_transformada = transfor.charAt(posicion);
            newClave = newClave + letra_transformada;
        }

        return newClave;
    }

    public String descifraClave(String clave) {
        char letra_original, letra_transformada;

        String transfor = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        // CAMBIO 20191126 String original = "~}|{zyxwvutsrqponmlkjihgfedcba`_^]\\[ZYXWVUTSRQPONMLKJIHGFEDCBA@?>=<;:9876543210/.-,+*)('&%$#\"!";
        String original = "~}|{zyxwvutsrqponmlkjihgfedcba`_^]\\[ZYXWVUTSRQPONMLKJIHGFEDCBA@?>=¿;:9876543210/.-,+*)('&%$#\"!";

        String newClave = "";
        for (int i = 0; i < clave.length(); i++) {
            letra_original = clave.charAt(i);
            int posicion = original.indexOf(letra_original);
            letra_transformada = transfor.charAt(posicion);
            newClave = newClave + letra_transformada;
        }

        return newClave;
    }
}
