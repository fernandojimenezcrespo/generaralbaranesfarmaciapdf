/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generaralbaranesfarmaciapdf;

 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * 
 */

/**
 * @author fernando
 * 
 */
public class LeerLineasFichero {
	ArrayList<String> arrLineas = new ArrayList<String>();
	private static Logger log= Logger.getLogger("GenerarAlbaranesFarmaciaPDF");

	public ArrayList<String> LeerLineasFichero(String nombreFichero) {
		File fichero = new File(nombreFichero);
		leerFichero(fichero);
		return arrLineas;

	}

	private void leerFichero(File fichero) {
		//System.out.println("El fichero que estoy cogiendo es:" + fichero.getAbsoluteFile());
		
		if (!fichero.exists()) {
			//System.out.println("FICHERO NO EXISTE. Módulo abrirFichero");
			log.error("FICHERO NO EXISTE. Módulo abrirFichero"+fichero);
		} else {

			try {
				FileReader ficheroLeer = new FileReader(fichero);
				BufferedReader bufferLeer = new BufferedReader(ficheroLeer);

				String linea = null;
				while ((linea = bufferLeer.readLine()) != null) {
					//System.out.println(linea);
					arrLineas.add(linea);
				}
				 
				bufferLeer.close();
				ficheroLeer.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}

