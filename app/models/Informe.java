package models;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.LibroConta;
import models.Organismo;
import models.UnionModel;

import play.db.jpa.*;
import play.mvc.Scope.Params;
import utils.Tools;

 
 
@Entity
public abstract class Informe extends UnionModel implements InformeI{
    


   public abstract InputStream xeneraInforme(Organismo org,Params params);
   public abstract String getNomeInforme();     
   public abstract String getInforme();
   public abstract String getFormato();
    
    
    
 
}
