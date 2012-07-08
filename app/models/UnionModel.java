package models;
 
import controllers.CRUD;
import javax.persistence.*;
import play.data.validation.Required;

 
import play.db.jpa.*;
 
@MappedSuperclass
public class UnionModel extends Model  {
 
    
    
    
    @ManyToOne   
    @CRUD.Hidden
    public Organismo organismo;
  
    
    
    /**
     * @return the organismo
     */
    public Organismo getOrganismo() {
        return organismo;
    }

    /**
     * @param organismo the organismo to set
     */
    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }
}
