package models;
 
import java.util.*;
import javax.persistence.*;

import com.jamonapi.utils.Logger;
import controllers.CRUD;
 
import play.db.jpa.*;
import play.data.validation.*;

 
@Entity
public class User extends UnionModel implements Avisable {
 
    
    @Required
    @CRUD.Hidden
    public String password;
    
    @Required
    @CRUD.Hidden
    public String usuario;
    
        
    @Required
    @OneToOne
    public Afiliado afiliado;
    
    public boolean isAdmin;
    
    public boolean isComite;    
    
    public boolean isSecretariaXeral;
    
    public boolean isTesoureiro;
    
    public boolean isPropaganda;
    
    public boolean isOrganizacion;
    
    public boolean isPermanencia;
    
    public boolean isAccionSindical;
    
    public boolean isAccionSocial;
    
    public boolean isFormacion;
    

  
    
    public static User connect(String usuario, String password) {
        return find("byUsuarioAndPassword", usuario, password).first();
        
    }
    
    public String toString() {
        return this.afiliado.toString();
    }
       
    public  String getSindicato() {
        return this.getOrganismo().toString();
    }
    
     public void sendAviso(){
            Aviso aviso=Aviso.findById(Long.decode("1"));
            aviso.setAsunto("Alta usuario");
            aviso.setContido("Alta usuario");            
        }

   
 
}