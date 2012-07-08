package notificacions;

import play.mvc.*;

import javax.mail.internet.*;

import models.*;
import org.apache.commons.mail.EmailAttachment;
import play.Play;

public class Notificador extends Mailer {
    
    private static String NOTIFICADOR="rubendramos@gmail.com";

    public static boolean notificacionEvento(Aviso aviso) throws Exception {
        
        int i=0;
        //asunto do mail
        setSubject(aviso.getAsunto());
                
        
        //Engadimos os destinatarios               
        
       for(Contacto con : aviso.getContactosListasDistribucion()){
           
           String nome=con.apelido1+" "+con.apelido2+","+con.nome;
           
           if(i==0){
               addRecipient(new InternetAddress(con.email, nome));
           }else{
                addBcc(new InternetAddress(con.email, nome));
           }
           i++;
       }
       //Contacto que o envía
       setFrom(new InternetAddress(NOTIFICADOR));
        
       if(aviso.evento.getAdxuntos()!=null && !aviso.evento.getAdxuntos().isEmpty()){
            for(Documento doc : aviso.evento.getAdxuntos()){
                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setDescription(doc.descricion);
                    attachment.setPath(doc.ficheiro.getFile().getPath());
                    addAttachment(attachment);                                
            }
        }
                         
       

        
        //Enviase e esperase resposta
        return sendAndWait(aviso);
        
    }
    
    
   public static boolean notificacionAsemblea(Aviso aviso) throws Exception {
        
        int i=0;
        //asunto do mail
        setSubject(aviso.getAsunto());
                
        
        //Engadimos os destinatarios               
        
       for(Contacto con : aviso.getContactosListasDistribucion()){
           
           String nome=con.apelido1+" "+con.apelido2+","+con.nome;
           
           if(i==0){
               addRecipient(new InternetAddress(con.email, nome));
           }else{
                addBcc(new InternetAddress(con.email, nome));
           }
           i++;
       }
       //Contacto que o envía
       setFrom(new InternetAddress(NOTIFICADOR));
        
       if(aviso.asemblea.documentacionAsemblea!=null && !aviso.asemblea.documentacionAsemblea.isEmpty()){
            for(Documento doc : aviso.asemblea.documentacionAsemblea){
                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setDescription(doc.descricion);
                    attachment.setPath(doc.ficheiro.getFile().getPath());                    
                    addAttachment(attachment);                                
            }
        }
                         
       

        
        //Enviase e esperase resposta
        return sendAndWait(aviso);
        
    }    
    
    public static boolean notificacionAviso(Aviso aviso) throws Exception {
        
        int i=0;
        //asunto do mail
        setSubject(aviso.getAsunto());
                
        
        //Engadimos os destinatarios               
        
       for(Contacto con : aviso.getContactosListasDistribucion()){
           
           String nome=con.apelido1+" "+con.apelido2+","+con.nome;
           
           if(i==0){
               addRecipient(new InternetAddress(con.email, nome));
           }else{
                addBcc(new InternetAddress(con.email, nome));
           }
           i++;
       }
       //Contacto que o envía
       setFrom(new InternetAddress(NOTIFICADOR));
        
        //Adxuntamos os docs
        if(aviso.docsAdxuntos!=null && !aviso.docsAdxuntos.isEmpty()){
            for(Documento doc : aviso.docsAdxuntos){
                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setDescription(doc.descricion);
                    attachment.setPath(Play.getFile(doc.nome).getPath());
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
    
}

