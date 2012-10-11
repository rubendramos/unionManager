package models;
 
import java.util.*;
import javax.persistence.*;

import com.jamonapi.utils.Logger;
import controllers.CRUD;
import notificacions.Notificador;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import play.Play;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
import utils.Tools;

 
@Entity
public class User extends UnionModel {
 
    
    @Required
    @CRUD.Hidden
    public String password;
    
    @Required
    @CRUD.Hidden
    public String usuario;
    

    @Required
    @OneToOne
    @AddForeignKey
    public Afiliado afiliado;
    

    @ManyToOne
    @Required
    @AddFiltro
    public TipoEstadoUsuario estadoUsuario;
            
    
    @Lob
    @MaxSize(200)
    public String firma;

    @Required
    @ManyToMany
    @AddForeignKey
    public Set<Permiso> permisos;
    
    
   

    @CRUD.Hidden
    @CRUD.Exclude
    public Date dataAlta;
    
    @CRUD.Hidden
    @CRUD.Exclude
    public Date dataBaixa;
    
    
   public User(String password,String usuario, Afiliado afiliado,String firma,Set<Permiso> permisos,Sexo sexo, 
           TipoEstadoUsuario tipoEstadoUsuario,Date dataAlta,Date dataBaixa){
    	this.password=password;
    	this.usuario=usuario;
    	this.afiliado=afiliado;
    	this.firma=firma;
    	this.permisos=permisos;
        this.estadoUsuario=estadoUsuario;
        this.dataAlta=dataAlta;
        this.dataBaixa=dataBaixa;
        
    }
    
    public static User connect(String usuario, String password) {        
       
       User u=find("byUsuarioAndPassword", usuario, password).first();
       if(u!=null && u.estadoUsuario.id==1){
           return u;
       }else{
        return null;
       }
    }
    
   public static boolean existsAdminUser() {       
        Boolean res=false;
        User u= find("byUsuario", "admin").first();
        
        if (u==null || "".equals(u.usuario)){
            res=false;
        }else if ("admin".equals(u.usuario)){
            res=true;
        }
        
        return res;
        
    }    
    
    public String toString() {
        if(afiliado!=null){
        return this.afiliado.persoa.toString();
        }else{
        return "Administrador";
        }
    }
       
    public  String getSindicato() {
        if(afiliado!=null){
            return this.organismo.toString();
        } else{
            return "Administrador";
        }
    }
    
     public void sendAviso(){
            Aviso aviso=Aviso.findById(Long.decode("1"));
            aviso.setAsunto("Alta usuario");
            aviso.setContido("Alta usuario");            
        }

       public static List<User> usersNotificacions(Long organismoId) {
           String query = "from User u   "
                + "join u.permisos p "
                + "join p.funcionalidade f "
                + "where  f.nome='Notificacion' "
                + "and u.organismo='"+organismoId+"'";         
        return find(query).fetch();
        
    } 
       
       public  boolean tenPermiso(String permiso) {           
         String query = "select u from User u   "
                + "join u.permisos p "                 
                + "where  p.nome='"+permiso+"' "
                + "and u.id='"+this.id+"'";             
                  
         return !find(query).fetch().isEmpty();   
         
         
    } 
       
       
       public  boolean tenPermisoFuncionalidade(String funcioanlidade) {
         String query = "select u from User u   "
                + "join u.permisos p "
                + "join p.funcionalidade f "
                + "where  f.nome='"+funcioanlidade+"' "
                + "and u.id='"+this.id+"'";;                  
         return !find(query).fetch().isEmpty();    
         
    }
       
       
     public  boolean mandarEmailAltaUsuario(User u) throws Exception { 
     String NOTIFICADOR_URL= Play.configuration.getProperty("url.notificador");
     return Notificador.notificacionAltaUsuario(this, play.i18n.Messages.get("mensaxe.asunto.altaUsuario"), play.i18n.Messages.get("mensaxe.corpo.altaUsuario",this.afiliado.organismo,NOTIFICADOR_URL,this.usuario,this.password),u.firma); 
       
    }   
     
     
     public  boolean mandarEmailBaixaUsuario(User u) throws Exception {      
     return Notificador.notificacionBaixaUsuario(this, play.i18n.Messages.get("mensaxe.asunto.baixaUsuario"), play.i18n.Messages.get("mensaxe.corpo.baixaUsuario",this.afiliado.organismo),u.firma); 
       
    }

     public  boolean mandarEmailRecuperaPassword() throws Exception {      
     return Notificador.notificacionRecuperaPassword(this, play.i18n.Messages.get("mensaxe.asunto.recuperaPassword"), play.i18n.Messages.get("mensaxe.corpo.recuperaPassword",this.usuario,this.password)); 
       
    }     
     
     
    public  void enviarNotificacionInternaAlta() throws Exception { 
        User avisoDe=User.findById(Long.parseLong("1"));
        TipoPrioridade tp=TipoPrioridade.findById(Long.parseLong("3"));
        String asunto=play.i18n.Messages.get("notificacion.asunto.altaUsuario");
        String contido=play.i18n.Messages.get("notificacion.corpo.altaUsuario");
        HashSet<User> des=new HashSet<User>();
        des.add(this);
        Date dataAltaAviso=Tools.getCurrentDate();
        NotificacionInterna ni=new NotificacionInterna(asunto,contido,des,dataAltaAviso,avisoDe,tp);    
        ni._save();
       
    }     
     
    /**
     * @return the permisos
     */
    public Set<Permiso> getPermisos() {
        return permisos;
    }

    /**
     * @param permisos the permisos to set
     */
    public void setPermisos(Set<Permiso> permisos) {
        this.permisos = permisos;
    }
     
     
            
     
 
}