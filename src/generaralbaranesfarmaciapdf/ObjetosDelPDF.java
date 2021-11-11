/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generaralbaranesfarmaciapdf;

import org.apache.log4j.Logger;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import generaralbaranesfarmaciapdf.beans.proveedoresBE;
import generaralbaranesfarmaciapdf.dao.ProveedoresDAO;
import generaralbaranesfarmaciapdf.utils.Utils;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ObjetosDelPDF {

    String observa = "";
    private static Logger log = Logger.getLogger("GenerarAlbaranesFarmaciaPDF");

    public PdfPTable TablaLaboratorioPedido(String lineaTablaLaboratorio)
            throws DocumentException, IOException {
        // TODO Auto-generated constructor stub
        PdfPTable table = new PdfPTable(2);
        String delimiter = "\\|";
        String[] campos = lineaTablaLaboratorio.split(delimiter);

        Integer num_pedido = null;
        Integer num_presu = null;
        String nif_proveedor = null;
        String fecha_pedido = null;
        String codigoProveedor = null;
        String clave_hos = null;
        String num_config = null;
        String direccionHNSS = null;

        String urgente = null;
        Utils util = new Utils();
        for (int i = 0; i < campos.length; i++) {
            switch (i) {
                case 0:
                    num_pedido = util.isNumeric(campos[i]);
                    break;
                case 1:
                    num_presu = util.isNumeric(campos[i]);
                    break;
                case 2:
                    nif_proveedor = campos[i];
                    break;
                case 3:
                    fecha_pedido = campos[i];
                    break;
                case 4:
                    codigoProveedor = campos[i];
                    break;
                case 5:
                    clave_hos = campos[i];
                    break;
                case 6:
                    num_config = campos[i];
                    break;
                case 7:
                    direccionHNSS = campos[i];
                    break;
                case 8:
                    observa = campos[i];
                    break;
                case 9:
                    urgente = campos[i];
                    break;

                default:
                    break;
            }

        }
        ProveedoresDAO proveedor = new ProveedoresDAO();
        ArrayList<proveedoresBE> lista = new ArrayList<>();
        if (num_pedido == 0) {
            log.info("TENGO NUMERO DE PEDIDO CERO. HAY ALGUN ERROR EN EL FICHERO TEXTO DEL PEDIDO ");
            log.error("TENGO NUMERO DE PEDIDO CERO. HAY ALGUN ERROR EN EL FICHERO TEXTO DEL PEDIDO ");

            observa = "ERROR porque tengo un numero de pedido CERO";
            
            proveedoresBE xlista = new proveedoresBE();
            xlista.setCod_postal("C.P.:XXXXX");
            xlista.setCodigo("NO EXISTE");
            xlista.setDireccion("C/ XXXXXX");
            xlista.setE_mail("MAIL");
            xlista.setFax("FAX");
            xlista.setFax_rec("FAX REC.:XXXXXX");
            xlista.setNombre("NOMBRE:XXXXXXX");
            xlista.setTelefono("Telf.:XXXXXXXX");
            lista.add(0, xlista);
            Generaralbaranesfarmaciapdf.arrProveedorPedido = lista;
        } else {
            //ArrayList<proveedoresBE> lista = proveedor.getProveedor(codigoProveedor);
            lista = proveedor.getProveedor(codigoProveedor);
            Generaralbaranesfarmaciapdf.arrProveedorPedido = lista;
        }
        String codigo = null;
        String nombre = null;
        String direccion = null;
        String telefono = null;
        String fax = null;
        String e_mail = null;
        String fax_rec = null;
        String cod_postal = null;
        for (proveedoresBE element : lista) {
            codigo = element.getCodigo();
            nombre = element.getNombre();
            direccion = element.getDireccion();
            telefono = element.getTelefono();
            fax = element.getFax();
            e_mail = element.getE_mail();
            Generaralbaranesfarmaciapdf.emailProveedor = e_mail;
            fax_rec = element.getFax_rec();
            cod_postal = element.getCod_postal();
        }

        lineaTablaLaboratorio = lineaTablaLaboratorio + "Fecha:"
                + fechaActual();
        lineaTablaLaboratorio = "LABORATORIO: " + codigo + " " + nombre + "(" + telefono + ")";
        Paragraph frase = new Paragraph(lineaTablaLaboratorio, fuenteLetra(8));
        table.addCell(frase);
        lineaTablaLaboratorio = "Nº Pedido:" + num_pedido;
        frase = new Paragraph(lineaTablaLaboratorio, fuenteLetra(14));
        table.addCell(frase);
        lineaTablaLaboratorio = "Fax:" + fax + "\t email:" + e_mail;
        frase = new Paragraph(lineaTablaLaboratorio, fuenteLetra(8));
        table.addCell(frase);
        if (fecha_pedido==null ) 
                fecha_pedido="";
        lineaTablaLaboratorio = " Fecha Pedido:" + fecha_pedido;
        frase = new Paragraph(lineaTablaLaboratorio, fuenteLetra(8));
        table.addCell(frase);

        return table;
    }

    public Phrase TextoPiePedido() throws DocumentException, IOException {

        String lineaTablaPie = null;
        Font font = new Font();
        font = fuenteLetra(6);
        String lineaTextoPie = "Rogamos confirmación del pedido o información inmediata de incidencias en su suministro al teléfono "
                + "920.358000(31254). El Albarán de entrega deberá venir obligatoriamente valorado con impuestos incluidos.\n"
                + "La factura debe venir acompañada de una copia del albarán de entrada debidamente diligenciado; la no concordancia"
                + "con el pedido supondrá retrasos en la tramitación poseterior de la misma.\n"
                + "Nos reservamos el derecho de no aceptar definitivamente esta mercancía hasta su total comprobación. LA RECEPCIÓN DE "
                + "MERCANCÍA SERÁ DE 8:30 A 11:00h DE LA MAÑANA";
        Phrase phraseEncabezado = new Phrase(lineaTextoPie, font);
        return phraseEncabezado;

    }

    public Phrase TextoObservaciones() throws DocumentException, IOException {

        String lineaObservaciones = "";
        Font font = new Font();
        font = fuenteLetra(12);
        if (observa.length() > 0) {
            lineaObservaciones = "OBSERVACIONES:" + observa;
        } else {
            lineaObservaciones = "";
        }
        Phrase phraseObservaciones = new Phrase(lineaObservaciones, font);
        return phraseObservaciones;

    }

    public PdfPTable TablaPiePedido() throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(4);
        String lineaTablaPie = null;
        Font fnt = new Font();
        fnt = fuenteLetra(8);
        lineaTablaPie = "Entregar en:\n Almacen Farmacia \n HOSPITAL NTRA. SRA. SONSOLES \nAvda.Juan Carlos I s/n\n05004 Ávila";
        Paragraph frase = new Paragraph(lineaTablaPie, fnt);
        table.addCell(frase);

        lineaTablaPie = "FARMACEUTICO JEFE";
        frase = new Paragraph(lineaTablaPie, fnt);
        table.addCell(frase);

        lineaTablaPie = "CURSADO PEDIDO\n\n\nTeléfono:              ";
        frase = new Paragraph(lineaTablaPie, fnt);
        table.addCell(frase);

        lineaTablaPie = "RECIBIDO PEDIDO";
        frase = new Paragraph(lineaTablaPie, fnt);
        table.addCell(frase);

        return table;

    }

    public Chunk ChunkPedidosEncabezado(String Linea0)
            throws DocumentException, IOException {
        // TODO Auto-generated constructor stub
        String lineaEncabezado = "";
        lineaEncabezado += "Cód.Nac." + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 8 - lineaEncabezado.length());
        lineaEncabezado += "Oferta" + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 50 - lineaEncabezado.length());
        lineaEncabezado += "Env." + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 58 - lineaEncabezado.length());
        lineaEncabezado += "Epigrafe" + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 66 - lineaEncabezado.length());
        lineaEncabezado += "UPE." + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 74 - lineaEncabezado.length());
        lineaEncabezado += "U.Bonif." + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 84 - lineaEncabezado.length());
        lineaEncabezado += "P.V.L." + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 95 - lineaEncabezado.length());
        lineaEncabezado += "Dto." + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 100 - lineaEncabezado.length());
        lineaEncabezado += "D.Prov." + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 108 - lineaEncabezado.length());
        lineaEncabezado += "IVA" + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 113 - lineaEncabezado.length());
        lineaEncabezado += "Importe" + " ";
        lineaEncabezado = rellenaBlancos(lineaEncabezado, 120 - lineaEncabezado.length());
        BaseFont bf = BaseFont.createFont(BaseFont.COURIER,
                BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        Font font = new Font(bf, 8);
        Chunk chunkEncabezado = new Chunk(lineaEncabezado, font);

        return chunkEncabezado;
    }

    public Chunk ChunkPedidosLineas(String linea) throws DocumentException, IOException {
        // TODO Auto-generated constructor stub

        String delimiter = "\\|";
        String[] campos = linea.split(delimiter);
        if (campos.length==1 || campos.length==0) 
        {
            Chunk chunk = new Chunk("LINE VACIA");
            return chunk;
        }  
        String lineaTexto = null;

        //	lineaTexto=campos[3].trim();
        /*1*/ lineaTexto = campos[3].trim();
        lineaTexto = rellenaBlancos(lineaTexto, 8 - lineaTexto.length());

        String nombreFarmaco = campos[2];
        /*2*/ lineaTexto = lineaTexto + " " + nombreFarmaco;
        lineaTexto = rellenaBlancos(lineaTexto, 50 - lineaTexto.length());

        Double numeroEnvases = Double.parseDouble(campos[5].replaceAll(",", "."));

        /*3*/	//lineaTexto=lineaTexto+" "+precioEnvase.toString();
        lineaTexto = lineaTexto + " " + campos[5];
        lineaTexto = rellenaBlancos(lineaTexto, 58 - lineaTexto.length());
        /*4*/ lineaTexto = lineaTexto + " " + campos[4].trim();
        lineaTexto = rellenaBlancos(lineaTexto, 66 - lineaTexto.length());
        Double precioEnvase = Double.parseDouble(campos[7].replaceAll(",", "."));
        /*5*/	//lineaTexto=lineaTexto+" "+numeroEnvases.toString();
        lineaTexto = lineaTexto + " " + campos[7];
        lineaTexto = rellenaBlancos(lineaTexto, 74 - lineaTexto.length());
        Double bonificacion = Double.parseDouble(campos[8].replaceAll(",", "."));
        /*6*/
        lineaTexto = lineaTexto + " " + bonificacion.toString();
        lineaTexto = rellenaBlancos(lineaTexto, 84 - lineaTexto.length());
        Double precio = Double.parseDouble(campos[12].replaceAll(",", "."));
        /*7*/ lineaTexto = lineaTexto + " " + precio.toString();
        lineaTexto = rellenaBlancos(lineaTexto, 95 - lineaTexto.length());

        Double descuento = Double.parseDouble(campos[9].replaceAll(",", "."));
        /*8*/ lineaTexto = lineaTexto + " " + descuento.toString();
        lineaTexto = rellenaBlancos(lineaTexto, 100 - lineaTexto.length());

        Double dto_prov = Double.parseDouble(campos[10].replaceAll(",", "."));
        /*9*/ lineaTexto = lineaTexto + " " + dto_prov.toString();
        lineaTexto = rellenaBlancos(lineaTexto, 108 - lineaTexto.length());

        Double iva = Double.parseDouble(campos[11].replaceAll(",", "."));
        /*10*/ lineaTexto = lineaTexto + " " + iva.toString();
        lineaTexto = rellenaBlancos(lineaTexto, 113 - lineaTexto.length());

        Double importe = null;
        //importe=((((numeroEnvases-bonificacion)*precio)*(1-(descuento/100)))*(1-dto_prov/100)) * (1+(iva/100));
        // EL 28/28/2017 después de más de 10 años generando informes se dan cuenta que la bonificación
        // no tiene que influir para nada en el precio.
        importe = ((((numeroEnvases) * precio) * (1 - (descuento / 100))) * (1 - dto_prov / 100)) * (1 + (iva / 100));
        Generaralbaranesfarmaciapdf importeTotal = new Generaralbaranesfarmaciapdf();
        ProcesaFichero.CalcularImporte(importe);
        BigDecimal importeRedondeado = new java.math.BigDecimal(importe);
        importeRedondeado = importeRedondeado.setScale(2, importeRedondeado.ROUND_HALF_UP);

        lineaTexto = lineaTexto + " " + importeRedondeado.toString();
        lineaTexto = rellenaBlancos(lineaTexto, 120 - lineaTexto.length());
        BaseFont bf = BaseFont.createFont(BaseFont.COURIER,
                BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        Font font = new Font(bf, 10);
        font = fuenteLetra(8);
        Chunk chunk = new Chunk(lineaTexto, font);
        return chunk;

    }

    private String fechaActual() {

        Calendar c = new GregorianCalendar();

        String dia = Integer.toString(c.get(Calendar.DATE));
        int mes = c.get(Calendar.MONTH);
        String anyo = Integer.toString(c.get(Calendar.YEAR));

        return dia + "/" + Mes(mes) + "/" + anyo;
    }

    public Phrase SaltosDeLinea(int salto) {

        Phrase phrase = new Phrase();
        String lineas = "";
        for (int i = 0; i < salto; i++) {
            lineas += "\n";
        }

        phrase.add(lineas);
        return phrase;

    }

    private String Mes(int mes) {
        String NombreMes = "MES SIN IDENTIFICAR";
        switch (mes) {
            case 0:
                NombreMes = "ENERO";
                break;
            case 1:
                NombreMes = "FEBRERO";
                break;
            case 2:
                NombreMes = "MARZO";
                break;
            case 3:
                NombreMes = "ABRIL";
                break;
            case 4:
                NombreMes = "MAYO";
                break;
            case 5:
                NombreMes = "JUNIO";
                break;
            case 6:
                NombreMes = "JULIO";
                break;
            case 7:
                NombreMes = "AGOSTO";
                break;
            case 8:
                NombreMes = "SEPTIEMBRE";
                break;
            case 9:
                NombreMes = "OCTUBRE";
                break;
            case 10:
                NombreMes = "NOVIEMBRE";
                break;
            case 11:
                NombreMes = "DICIEMBRE";
                break;

            default:
                break;
        }
        return NombreMes;
    }

    private Font fuenteLetra(int tamano) throws DocumentException, IOException {
        //BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,BaseFont.NOT_EMBEDDED);
        BaseFont bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI,
                BaseFont.NOT_EMBEDDED);

        Font fuente = new Font(bf, tamano);
        return fuente;
    }

    private String rellenaBlancos(String texto, int blancos) {
        for (int i = 0; i < blancos; i++) {
            texto = texto + " ";
        }
        return texto;
    }

}
