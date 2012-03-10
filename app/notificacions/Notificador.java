package notificacions;

import play.mvc.*;

import javax.mail.internet.*;

import models.*;
import org.apache.commons.mail.EmailAttachment;
import play.Play;

public class Notificador extends Mailer {

    public static boolean notificacionXenerica(Aviso aviso) throws Exception {
        
        
        //asunto do mail
        setSubject(aviso.asunto);
        
        //Engadimos os destinatarios
       addRecipient(aviso.getEmailsListasDistribucion());
        
       //Contacto que o envía
       setFrom(new InternetAddress(aviso.avisoDe, aviso.avisoDe));
        
        //Adxuntamos os docs
        if(aviso.docsAdxuntos!=null){
            for(Documento doc : aviso.docsAdxuntos){
                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setDescription(doc.descricion);
                    attachment.setPath(Play.getFile(doc.nome).getPath());
                    addAttachment(attachment);                                
            }

        }
                         
       

        
        //Enviase e esperase resposta
        return sendAndWait();
    }
    
}

