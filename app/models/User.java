package models;
 
import java.util.*;
import javax.persistence.*;

import com.jamonapi.utils.Logger;
 
import play.db.jpa.*;
import play.data.validation.*;

 
@Entity
public class User extends Model {
 
    
    @Required
    public String password;
    
    @Required    
    public String usuario;
    
        
    @Required
    @OneToOne
    public Afiliado afiliado;
    
    public boolean isAdmin;
    
    public boolean isComite;    
    
    public boolean isSecretario;
    
    public boolean isTesoureiro;
    
    public boolean isPropaganda;
    
    public boolean isOrganizacion;
    
    public boolean isPermanecia;
    
    public boolean isAaccionSindical;
    
    public boolean isAccionSocial;
    
    public boolean isFormacion;
    

    public User(String password, Afiliado afiliado) {
        this.password = password;
        this.afiliado= afiliado;       
    }
    
    public static User connect(String usuario, String password) {
    	play.Logger.debug("usuariooooooooooooooooooo: "+usuario);
        return find("byUsuarioAndPassword", usuario, password).first();
        
    }
    
    public String toString() {
        return this.afiliado.toString();
    }
       
    public  String getSindicato() {
        return this.afiliado.sindicato.toString();
    }
 
}