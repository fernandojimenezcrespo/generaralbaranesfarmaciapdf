package generaralbaranesfarmaciapdf;

import generaralbaranesfarmaciapdf.utils.Desencriptar;
import static generaralbaranesfarmaciapdf.Generaralbaranesfarmaciapdf.arrProveedorPedido;
import static generaralbaranesfarmaciapdf.Generaralbaranesfarmaciapdf.emailProveedor;
import generaralbaranesfarmaciapdf.beans.proveedoresBE;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

/**
 *
 * @author 06553669A
 */
public class EnviarMailProveedor {

    private String HOST_SMTP, PORT_SMTP, USER_SMTP, MAIL_TO, MAIL_TO2, MAIL_CC, MAIL_CCO;
    private String MAIL_CONECT_HOST, MAIL_CONECT_USER, MAIL_CONECT_PASS, MAIL_ASUNTO;
    private ArrayList<proveedoresBE> proveedor;
    private String pathFicheroDat;
    private static Logger log = Logger.getLogger("GenerarAlbaranesFarmaciaPDF");
    private static int vecesReprocesado = 0;

    public void setPathFicheroDat(String pathFicheroDat) {
        this.pathFicheroDat = pathFicheroDat;
    }

    public void setProveedor(ArrayList<proveedoresBE> proveedor) {
        this.proveedor = proveedor;
    }

    public void vecesReprocesadoPedido(int vecesReprocesado) throws MessagingException {
        this.vecesReprocesado = vecesReprocesado;

    }

    private String EnviarMailProveedor() throws MessagingException {
        leerVariablesConexionMail();
        String correoProveedor = "correo Proveedor Desconocido";
        String nombreProveedor = "Nombre Proveedor Desconocido";
        if (this.proveedor != null) {
            correoProveedor = this.proveedor.get(0).getE_mail();
            nombreProveedor = this.proveedor.get(0).getNombre() + "(" + this.proveedor.get(0).getTelefono() + ")";
            nombreProveedor = this.proveedor.get(0).getNombre(); //20200116 propuesto JEFE SERV. FARMACIA
        }
        String pathFicheroDat = this.pathFicheroDat;
        Properties props = new Properties();
        // Nombre del host de correo, es smtp.gmail.com
        props.setProperty("mail.smtp.host", HOST_SMTP);
        // TLS si está disponible
        props.setProperty("mail.smtp.starttls.enable", "true");

        // Puerto de gmail para envio de correos
        props.setProperty("mail.smtp.port", PORT_SMTP);
        // Nombre del usuario
        props.setProperty("mail.smtp.user", USER_SMTP);
         

        // Si requiere o no usuario y password para conectarse.
        props.setProperty("mail.smtp.auth", "true");
        //Con esto estamos en disposición de obtener nuestra instancia de Session.
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        //Hemos puesto setDebug(true) para obtener más información por pantalla de lo que está sucediendo.Una vez que el programa nos funcione
        //, podemos quitar esa línea sin problemas
        MimeMessage message = new MimeMessage(session);
        try {
            // Quien envia el correo
            message.setFrom(new InternetAddress(USER_SMTP,"Serv. Farmacia HOSPITAL NTRA.SRA. DE SONSOLES"));
        } catch (UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(EnviarMailProveedor.class.getName()).log(Level.SEVERE, null, ex);
        }
        message.setHeader("De:", "CORREO FARMACIA HNSS");
        // A quien va dirigido
        if (MAIL_TO.equals("proveedor")) {
            MAIL_TO = correoProveedor;
        } else {
            MAIL_TO = "CorreoProveedor@saludcastillayleon.ex";
        }
        /*if (vecesReprocesado > 0) {
            MAIL_TO = correoProveedor;
        }*/
        
        InternetAddress[] internetAddresses = {
            new InternetAddress(MAIL_TO),
            new InternetAddress(MAIL_TO2),
            new InternetAddress(MAIL_CC),
            new InternetAddress(MAIL_CCO)};
        message.setRecipients(Message.RecipientType.TO, internetAddresses);
        //message.addRecipient(Message.RecipientType.TO, new InternetAddress(MAIL_TO));
        //message.addRecipient(Message.RecipientType.CC, new InternetAddress(MAIL_CC));
        //message.addRecipient(Message.RecipientType.BCC, new InternetAddress(MAIL_CCO));

        BodyPart texto = new MimeBodyPart();

        String pathFicheroPdf = pathFicheroDat.replace("dat", "pdf");
        String numeroPedido = pathFicheroPdf.replace("/Albaranes_farmacia/ficheros/PDF/", "");
        numeroPedido = numeroPedido.replace("/NewAlbaranes_farmacia/ficheros/PDF/", "");
        numeroPedido = numeroPedido.replace("/ProgramasHNSS/Albaranes_Farmacia/ficheros/PDF/", "");

        numeroPedido = numeroPedido.replace(".pdf", "");
        if (vecesReprocesado == 0) {
            message.setSubject(MAIL_ASUNTO + numeroPedido + " a " + nombreProveedor + " de Serv.Farmacia(Hospital Ntra.Sra.Sonsoles)-Avila-");
        } else {
            message.setSubject( " AVISO Pedido " + numeroPedido + " MODIFICADO   para " + nombreProveedor);
        }
        if ( proveedor==null || proveedor.get(0).getCodigo().equals("NO EXISTE")) {
            message.setSubject("ERROR EN EL PEDIDO DE FARMACIA: Numero " + numeroPedido + " a " + nombreProveedor + " de Serv.Farmacia(Hospital Ntra.Sra.Sonsoles)-Avila-");
        }

        BodyPart adjunto = new MimeBodyPart();
        adjunto.setDataHandler(new DataHandler(new FileDataSource(pathFicheroPdf)));
        pathFicheroPdf = pathFicheroPdf.replace("/Albaranes_farmacia/ficheros/PDF/", "");
        pathFicheroPdf = pathFicheroPdf.replace("/NewAlbaranes_farmacia/ficheros/PDF/", "");
        pathFicheroPdf = pathFicheroPdf.replace("/ProgramasHNSS/Albaranes_Farmacia/ficheros/PDF/", "");

        String textoCorreo = textoContenido(nombreProveedor, correoProveedor, numeroPedido, vecesReprocesado);
        texto.setText(textoCorreo);
        texto.setContent(textoCorreo, "text/html");

        adjunto.setFileName(pathFicheroPdf);
        MimeMultipart multiParte = new MimeMultipart();

        multiParte.addBodyPart(texto);
        multiParte.addBodyPart(adjunto);

        message.setContent(multiParte);

        Transport transporteMail = session.getTransport("smtp");
        transporteMail.connect(MAIL_CONECT_HOST + MAIL_CONECT_USER, MAIL_CONECT_PASS);
        transporteMail.sendMessage(message, message.getAllRecipients());
        transporteMail.close();
        log.info("Pedido " + numeroPedido + " enviado al correo  " + correoProveedor + " junto a los correos:" + MAIL_TO + " " + MAIL_CC + " " + MAIL_CCO);
        return "Mail enviado";

    }

    private void leerVariablesConexionMail() {

        LeerXML leerxml = new LeerXML();
        Desencriptar desencripta = new Desencriptar();
        HOST_SMTP = leerxml.devuelveValor("HOST_SMTP");
        PORT_SMTP = leerxml.devuelveValor("PORT_SMTP");
        USER_SMTP = leerxml.devuelveValor("USER_SMTP");
        MAIL_TO = leerxml.devuelveValor("MAIL_TO");
        MAIL_TO2 = leerxml.devuelveValor("MAIL_TO2");
        MAIL_CC = leerxml.devuelveValor("MAIL_CC");
        MAIL_CCO = leerxml.devuelveValor("MAIL_CCO");
        MAIL_CONECT_HOST = leerxml.devuelveValor("MAIL_CONECT_HOST");
        MAIL_CONECT_USER = desencripta.descifraClave(leerxml.devuelveValor("MAIL_CONECT_USER"));
        MAIL_CONECT_PASS = desencripta.descifraClave(leerxml.devuelveValor("MAIL_CONECT_PASS"));
        MAIL_ASUNTO = leerxml.devuelveValor("MAIL_ASUNTO");

    }

    private String textoContenido(String nombreProveedor, String correoProveedor, String numeroPedido, int reprocesado) {

        String textoReprocesado = "";

        if (reprocesado != 0) {
            textoReprocesado = "<br><h3>AVISO: ESTE PEDIDO CON NUMERO (" + numeroPedido + ") ANULA AL ANTERIOR CON EL MISMO NUMERO DE PEDIDO</h3><BR>";
        }
        return " <html><body> PROVEEDOR:" + nombreProveedor
                + "<p><h4> Adjunto remito documento de pedido Num.:" + numeroPedido + "</h4>"
                + "<p> Rogamos responder este correo sin modificar el Asunto. Gracias.<p> "
                + "<p>Apreciados se&ntilde;ores: Nos complace hacerles llegar un PEDIDO de productos del Complejo Asistencial de &Aacute;vila (Servicio de Farmacia)"
                + " a trav&eacute;s de los nuevos servicios de env&iacute;os.</p> "
                + "<p>Para cualquier aclaraci&oacute;n o consulta sobre el contenido del pedido deber&aacute;n dirigirse "
                + "al departamento de compras del Servicio de Farmacia de este Complejo Asistencial, "
                + "al tel&eacute;fono 920.358000 (Ext. 31254) "
                + "o a trav&eacute;s de la cuenta de correo electr&oacute;nico: farmacia.hnss@saludcastillayleon.es.</p> "
                + "<p>Se ruega que el Albar&aacute;n de Entrega de la mercanc&iacute;a figure en lugar visible en el exterior de los "
                + "bultos para facilitar la comprobaci&oacute;n de la mercanc&iacute;a recibida.</p> "
                + "<p>En el documento deber&aacute;n hacer referencia al"
                + " -Num. Pedido:" + numeroPedido + "</p>"
                + textoReprocesado
                + " <br><p>Atentamente Servicio de Farmacia Consejo Asistencial de &Aacute;vila</p>"
                + " <br>Telefono 920358000 Ext. 31254 "
                + " <br>Correo electronico farmaciahnss@saludcastillayleon.es"
                + "<br><br><br><br></body></html>";
    }

    public void enviaCorreo(String AlbaranPDF, ArrayList<proveedoresBE> arrProveedorPedido) {
        if (!emailProveedor.equals("") || 1 == 1) {
            try {
                //System.out.println("VOY A GENERAR EL CORREO :" + AlbaranPDF);
                EnviarMailProveedor enviaMail = new EnviarMailProveedor();
                enviaMail.setProveedor(arrProveedorPedido);
                enviaMail.setPathFicheroDat(AlbaranPDF);
                String textoAlerta = enviaMail.EnviarMailProveedor();
            } catch (MessagingException ex) {
                java.util.logging.Logger.getLogger(Generaralbaranesfarmaciapdf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
