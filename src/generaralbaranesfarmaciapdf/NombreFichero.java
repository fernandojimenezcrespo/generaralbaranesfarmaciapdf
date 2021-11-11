package generaralbaranesfarmaciapdf;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class NombreFichero {

    private static Logger log = Logger.getLogger("GenerarAlbaranesFarmaciaPDF");
    /* Existe un comando desde MSDOS para ejecutar el JAR con debug de log4j
	 * java -jar -Dlog4j.debug -Dlog4j.configuration="./scr/log4j.properties" GenerarAlbaranes.jar
	 * Tienes que tener en cuenta que debes refrescar el proyecto para que actualice los ficheros correctamente.
     */

    LeerXML leerxml = new LeerXML();
    String directorio_pendiente = leerxml.devuelveValor("DIRECTORIO_PENDIENTE");
    String directorio_procesado = leerxml.devuelveValor("DIRECTORIO_PROCESADO");
    String directorio_reprocesado = leerxml.devuelveValor("DIRECTORIO_REPROCESADO");
    String directorio_pdf = leerxml.devuelveValor("DIRECTORIO_PDF");

    public String nombreFicheroOrigen(String fichero) {

        if (fichero != null) {
            if (existeFichero(directorio_pendiente)) {
                return directorio_pendiente + "/" + fichero;
            }
            else
                salirDelPrograma(directorio_pendiente);

        }
        return null;
    }
    private void salirDelPrograma(String fichero)
    {
            log.error("Error NO EXISTE EL DIRECTORIO O FICHERO :" + fichero);
            log.error("SALGO DEL PROGRAMA.............");
            System.exit(0);
    }
    public String devuelveNombreFichero() {

        File directorio = new File(directorio_pendiente);
        if (!existeFichero(directorio_pendiente)) {
            salirDelPrograma(directorio_pendiente);
            return null;
        } else {
            String[] ficheros = directorio.list();
            if (ficheros.length > 0) {
                return ficheros[0];
            } else {

                //System.out.println("NO EXISTEN FICHEROS A PROCESAR");
                //log.setLevel(Level.OFF);
                //log.info("NO EXISTEN FICHEROS A PROCESAR");
                return null;
            }
        }
    }

    public String nombreFicheroDestino(String fichero) {
        fichero = fichero.substring(1, fichero.length());
        if (!existeFichero(directorio_procesado))
        {
            salirDelPrograma(directorio_procesado);
            
        }
        if (!existeFichero(fichero)) {
            return directorio_procesado + "/" + fichero;
        } else {
            return null;
        }
    }

    public String nombreFicheroDestinoPDF(String fichero) {
        fichero = fichero.substring(1, fichero.length());
        if (existeFichero(directorio_pdf)) {
            return directorio_pdf + "/" + fichero;
        } else {
            salirDelPrograma(directorio_pdf);
        }
        return null;
    }

    public int mover_fichero(String fichero) throws java.io.IOException {
        File fich1 = new File(directorio_pendiente + "/" + fichero);
        File fich2 = new File(directorio_procesado + "/" + fichero);
        if (!existeFichero(directorio_reprocesado))
             salirDelPrograma(directorio_reprocesado);
        if (!existeFichero(directorio_pendiente))
             salirDelPrograma(directorio_pendiente);
        if (!existeFichero(directorio_procesado))
             salirDelPrograma(directorio_procesado);
            
        int intentos = 0;
        boolean success = fich1.renameTo(fich2);
        if (!success) {
            intentos = 1;
            log.warn("Error intentando cambiar el nombre de fichero:" + fich2 + " Nº intento:" + intentos);
            fich2 = new File(directorio_reprocesado + "/" + fichero + "_" + intentos);
            success = fich1.renameTo(fich2);
            while (!success) {
                intentos++;
                log.warn("ERROR. ESTE FICHERO YA EXISTE EN REPROCESADOS-->" + fich2);
                
                
                fich2 = new File(directorio_reprocesado + "/" + fichero + "_" + intentos);
                success = fich1.renameTo(fich2);

            }
            return intentos;
        } else {
            return intentos;
        }
    }

    public String quitar_extension(String fichero) {
        String xfichero = fichero.replace("dat", "pdf");
        return xfichero;
    }

    private boolean existeFichero(String fichero) {
        File directorioFichero = new File(fichero);
        if (!directorioFichero.exists()) {
          return false;
        }
        return true;
    }
}
