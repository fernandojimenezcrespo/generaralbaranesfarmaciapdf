package generaralbaranesfarmaciapdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class ProcesaFichero {

    final static String ENCABEZADO1 = "COMPLEJO ASISTENCIAL AVILA";
    final static String ENCABEZADO2 = "SERVICIO DE FARMACIA";
    final static String ENCABEZADO3 = "AVILA";
    public static Double ImporteTotalAlbaran = (double) 0;
    private int numeroIntentosMoverFichero=0;

    public int getNumeroIntentosMoverFichero() {
        return numeroIntentosMoverFichero;
    }

    public void setNumeroIntentosMoverFichero(int numeroIntentosMoverFichero) {
        this.numeroIntentosMoverFichero = numeroIntentosMoverFichero;
    }

    public static void CalcularImporte(Double importe) {
        ImporteTotalAlbaran = ImporteTotalAlbaran + importe;
    }
    private static Logger log = Logger.getLogger("GenerarAlbaranesFarmaciaPDF");

    public String  procesaFichero(String fichero, NombreFichero nombreFichero) throws FileNotFoundException, DocumentException {

        String origenFichero = nombreFichero.nombreFicheroOrigen(fichero);
        LeerLineasFichero leerFichero = new LeerLineasFichero();
        ArrayList<String> arrLineas = null;
        arrLineas = leerFichero.LeerLineasFichero(origenFichero);
        String destinoFichero = nombreFichero.nombreFicheroDestino(fichero);
        //String xdestinoFichero = nombreFichero.quitar_extension(destinoFichero);
        String destinoFicheroPDF = nombreFichero.nombreFicheroDestinoPDF(fichero);
        String xdestinoFicheroPDF = nombreFichero.quitar_extension(destinoFicheroPDF);

        if (xdestinoFicheroPDF != null) {
            FileOutputStream archivo = new FileOutputStream(xdestinoFicheroPDF);
            Document documento = new Document(PageSize.A4.rotate()); // Pongo el
            // documento
            // horizontal.
            PdfWriter.getInstance(documento, archivo);

            try {
                documento.open();
                documento.addAuthor("Complejo Asistencial Avila. Serv.Farmacia");
                documento.addTitle("");
                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                        BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                Font font = new Font(bf, 10);

                ObjetosDelPDF objetosPDF = new ObjetosDelPDF();
                Phrase phrase = new Phrase();

                //**************************************************************
                //Ejemplos de IMAGE
                Image foto = Image.getInstance("img/LogoComplejo.png");
                foto.scaleToFit(150, 150);
                foto.setAlignment(Chunk.ALIGN_LEFT);
                documento.add(foto);
				//FIN Ejemplos de IMAGE
                //**********************

                //documento.add(new Paragraph(ENCABEZADO1, font));
                //documento.add(new Paragraph(ENCABEZADO2, font));
                //documento.add(new Paragraph(ENCABEZADO3, font));
                phrase = objetosPDF.SaltosDeLinea(1);
                documento.add(phrase);

                int tamano=arrLineas.size();
                if ( tamano==0 || arrLineas.get(0).isEmpty() || arrLineas.get(0).equals(" "))
               
                {
                    log.error("Fichero " + fichero + " VACIO O CON ALGUN ERROR ");
                              // documento.add(table);
                               documento.add(new Paragraph("ERROR EN EL FICHERO "+fichero+" ESTA VACIO O CARACTERES RAROS ...." ));
                               phrase = objetosPDF.SaltosDeLinea(1);
                               documento.add(phrase);
                               Chunk chunk = objetosPDF.ChunkPedidosEncabezado("ERROR EL FICHERO VACIO O CERO FILAS ");
                               documento.add(chunk);
                               documento.add(Chunk.NEWLINE);
                               LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
                               documento.add(line);
                               phrase = objetosPDF.SaltosDeLinea(2);
                               documento.add(phrase);
                               Phrase pieTexto = new Phrase("El pedido "+ fichero + " tiene algún error ", font);
                                
                               pieTexto.setLeading(5);
                               documento.add(pieTexto);

                //table = objetosPDF.TablaPiePedido();
                // documento.add(table);
                documento.close();
                numeroIntentosMoverFichero=nombreFichero.mover_fichero(fichero);
                return destinoFicheroPDF;
               // return "HA HABIDO UN ERROR";

                }
                else
                {    
                PdfPTable table = objetosPDF.TablaLaboratorioPedido(arrLineas.get(0));
                System.out.println(arrLineas.get(0));
                documento.add(table);
                documento.add(new Paragraph(" "));
                phrase = objetosPDF.SaltosDeLinea(1);
                documento.add(phrase);
                //table = objetosPDF.TablaPedidosEncabezado(arrLineas.get(0));
                //documento.add(table);
                //C:\Users\fernando\Documents\NetBeansProjects\generaralbaranesfarmaciapdf\src\generaralbaranesfarmaciapdf\img
                Chunk chunk = objetosPDF.ChunkPedidosEncabezado(arrLineas.get(0));
                documento.add(chunk);
                documento.add(Chunk.NEWLINE);
                LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
                documento.add(line);
                phrase = objetosPDF.SaltosDeLinea(2);
                documento.add(phrase);
                ImporteTotalAlbaran = 0.0;
                for (int i = 1; i < arrLineas.size(); i++) {
                    //table = objetosPDF.TablaPedidosLineas(arrLineas.get(i));
                    if (arrLineas.get(i) != "" && arrLineas.get(i) != null && arrLineas.get(i).length() > 0) {
                        chunk = null;
                        chunk = objetosPDF.ChunkPedidosLineas(arrLineas.get(i));
                        documento.add(chunk);
                        documento.add(Chunk.NEWLINE);
                    }
                }
                BigDecimal ImporteRedondeado = new java.math.BigDecimal(ImporteTotalAlbaran);

                ImporteRedondeado = ImporteRedondeado.setScale(2, ImporteRedondeado.ROUND_HALF_UP);
                phrase = objetosPDF.SaltosDeLinea(1);
                documento.add(phrase);
                documento.add(new Paragraph("                             Total........" + ImporteRedondeado.toString()));

                phrase = objetosPDF.SaltosDeLinea(3);
                documento.add(phrase);
                Phrase observaciones = objetosPDF.TextoObservaciones();
                documento.add(observaciones);

                phrase = objetosPDF.SaltosDeLinea(1);
                documento.add(phrase);
                Phrase pieTexto = objetosPDF.TextoPiePedido();
                pieTexto.setLeading(5);
                documento.add(pieTexto);

                table = objetosPDF.TablaPiePedido();
                documento.add(table);
                documento.close();
                }
                numeroIntentosMoverFichero=nombreFichero.mover_fichero(fichero);

                log.info("Fichero " + fichero + " procesado con un numero de intentos de:" + numeroIntentosMoverFichero);
                 return destinoFicheroPDF;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            //System.out.println("NO HAY NINGÚN FICHERO. HE TERMINADO");
            log.trace("NINGUN FICHERO A PROCESAR");
        }
        return null;
    }
    
}
