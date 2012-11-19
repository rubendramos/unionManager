package notificacions;

import play.mvc.*;

import javax.mail.internet.*;

import models.*;
import org.apache.commons.mail.EmailAttachment;
import play.Play;
import utils.Tools;

public class Notificador extends Mailer {

    private static String NOTIFICADOR = Play.configuration.getProperty("mail.notificador");

    public static boolean notificacionAviso(Aviso aviso) throws Exception {

        int i = 0;

        //asunto do mail
        setSubject(aviso.getAsunto());


        //Engadimos os destinatarios               

        for (Contacto con : aviso.getContactosListasDistribucion()) {

            String nome = con.apelido1 + " " + con.apelido2 + "," + con.nome;

            if (i == 0) {
                addRecipient(new InternetAddress(con.email, nome));
            } else {
                addBcc(new InternetAddress(con.email, nome));
            }
            i++;
        }
        //Contacto que o envía
        setFrom(new InternetAddress(NOTIFICADOR));

        //Adxuntamos os docs
        if (aviso.docsAdxuntos != null && !aviso.docsAdxuntos.isEmpty()) {
            for (Documento doc : aviso.docsAdxuntos) {
                EmailAttachment attachment = new EmailAttachment();
                attachment.setDescription(doc.descricion);
                attachment.setName(doc.nome);
                attachment.setPath(doc.ficheiro.getFile().getAbsolutePath());
                addAttachment(attachment);
            }
        }
        //Enviase e esperase resposta
        return sendAndWait(aviso);

    }

    public static boolean notificacionSistema(Aviso aviso) throws Exception {


        //asunto do mail
        setSubject(aviso.getAsunto());
       
        //Engadimos os destinatarios
        addRecipient("rubendramos@gmail.com");

        //Contacto que o envía
        setFrom(new InternetAddress(NOTIFICADOR, NOTIFICADOR));

        //Enviase e esperase resposta
        return sendAndWait(aviso);
    }

    public static boolean notificacionAltaUsuario(User u, String asunto, String contido,String firma) throws Exception {

        //asunto do mail
        setSubject(asunto);

        //Engadimos os destinatarios               


        String nome = u.afiliado.persoa.apelido1 + " " + u.afiliado.persoa.apelido2 + "," + u.afiliado.persoa.nome;

        addRecipient(new InternetAddress(u.afiliado.persoa.email, nome));

        //Contacto que o envía
        setFrom(new InternetAddress(NOTIFICADOR));

        //Enviase e esperase resposta
        return sendAndWait(contido,firma);

    }
    
    public static boolean notificacionBaixaUsuario(User u, String asunto, String contido,String firma) throws Exception {

        //asunto do mail
        setSubject(asunto);

        //Engadimos os destinatarios               


        String nome = u.afiliado.persoa.apelido1 + " " + u.afiliado.persoa.apelido2 + "," + u.afiliado.persoa.nome;

        addRecipient(new InternetAddress(u.afiliado.persoa.email, nome));

        //Contacto que o envía
        setFrom(new InternetAddress(NOTIFICADOR));

        //Enviase e esperase resposta
        return sendAndWait(contido,firma);

    }
    
   public static boolean notificacionRecuperaPassword(User u, String asunto, String contido) throws Exception {

        //asunto do mail
        setSubject(asunto);
        String firma=play.i18n.Messages.get("mensaxe.firmaSistema");

        //Engadimos os destinatarios               


        String nome = u.afiliado.persoa.apelido1 + " " + u.afiliado.persoa.apelido2 + "," + u.afiliado.persoa.nome;

        addRecipient(new InternetAddress(u.afiliado.persoa.email, nome));

        //Contacto que o envía
        setFrom(new InternetAddress(NOTIFICADOR));

        //Enviase e esperase resposta
        return sendAndWait(contido,firma);

    }    
    
  public static boolean notificacionBaixaAfiliado(Afiliado u, String asunto, String contido,String firma) throws Exception {

        //asunto do mail
        setSubject(asunto);

        //Engadimos os destinatarios               


        String nome = u.persoa.apelido1 + " " + u.persoa.apelido2 + "," + u.persoa.nome;

        addRecipient(new InternetAddress(u.persoa.email, nome));

        //Contacto que o envía
        setFrom(new InternetAddress(NOTIFICADOR));

        //Enviase e esperase resposta
        return sendAndWait(contido,firma);

    }    
  
  public static boolean notificacionAltaAfiliado(Afiliado u, String asunto, String contido,String firma) throws Exception {

        //asunto do mail
        setSubject(asunto);

        //Engadimos os destinatarios               

        String nome = u.persoa.apelido1 + " " + u.persoa.apelido2 + "," + u.persoa.nome;

        addRecipient(new InternetAddress(u.persoa.email, nome));

        //Contacto que o envía
        setFrom(new InternetAddress(NOTIFICADOR));

        //Enviase e esperase resposta
        return sendAndWait(contido,firma);

    }   
  
    public static boolean notificacionPrestamoVencido(PrestamoFondo prestamoFondo) throws Exception {

        //asunto do mail
        String asunto=play.i18n.Messages.get("mensaxe.asunto.vencementoPrestamo", prestamoFondo.entradaFondo.titulo);    
        String contido=play.i18n.Messages.get("mensaxe.contido.vencementoPrestamo", prestamoFondo.entradaFondo.titulo,Tools.getLocaleDateFormat(prestamoFondo.dataPrestamo),Tools.getLocaleDateFormat(prestamoFondo.getDataVencemento()));
        String firma="";
        Afiliado af=prestamoFondo.afiliado;
        
        //Engadimos asusnto
        setSubject(asunto);

        //Engadimos os destinatarios               
        String nome = af.persoa.apelido1 + " " + af.persoa.apelido2 + "," + af.persoa.nome;

        addRecipient(new InternetAddress(af.persoa.email, nome));

        //Contacto que o envía
        setFrom(new InternetAddress(NOTIFICADOR));

        //Enviase e esperase resposta
        return sendAndWait(contido,firma);

    }  
    
}
