/**
 *
 * @author fernando
 */
package generaralbaranesfarmaciapdf;

import java.io.FileNotFoundException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.itextpdf.text.DocumentException;
import generaralbaranesfarmaciapdf.beans.proveedoresBE;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.mail.MessagingException;
import jdk.nashorn.internal.codegen.CompilerConstants;

//import itextpdf.text.DocumentException;
public class Generaralbaranesfarmaciapdf {
    private static Logger log = Logger.getLogger("GenerarAlbaranesFarmaciaPDF");
    public static String emailProveedor = "";
    public static ArrayList<proveedoresBE> arrProveedorPedido;
   

    public static void main(String[] String) throws FileNotFoundException,
            DocumentException {
        PropertyConfigurator.configure("xml/log4j.properties");
        log.info("Iniciando programa......");
        NombreFichero nombreFichero = new NombreFichero();
        ProcesaFichero procesaFichero = new ProcesaFichero();
        String fichero = nombreFichero.devuelveNombreFichero();
        PropertyConfigurator.configure("log4j.properties");
        while (fichero != null) {
            arrProveedorPedido=null;
            String AlbaranPDF = procesaFichero.procesaFichero(fichero, nombreFichero);
            int vecesReprocesado=procesaFichero.getNumeroIntentosMoverFichero();
            EnviarMailProveedor envio=new EnviarMailProveedor();
            try {
                envio.vecesReprocesadoPedido(vecesReprocesado);
            } catch (MessagingException ex) {
                java.util.logging.Logger.getLogger(Generaralbaranesfarmaciapdf.class.getName()).log(Level.SEVERE, null, ex);
            }
            envio.enviaCorreo(AlbaranPDF, arrProveedorPedido);
            fichero = nombreFichero.devuelveNombreFichero();
        }

    }

    

}
