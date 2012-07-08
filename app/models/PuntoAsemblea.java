package models;
 
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.ManyToAny;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
 
@Entity
public class PuntoAsemblea extends UnionSecureModel {
 
    
    @Required   
    @ManyToOne
    public TipoPuntoAsemblea tipoPuntoAsemblea ;      
    
    @Required   
    @Lob
    @MaxSize(500)
    public String descricion ;    
    
    @Lob
    @MaxSize(500)
    public String acordo ;
    
    @ManyToOne
    public Afiliado aPeticionDe;
    
    @ManyToMany
    @AddForeignKey
    public Set<Documento> documentacion;
      
    public PuntoAsemblea(TipoPuntoAsemblea tipoPuntoAsemblea,String descricion,String acordo,Afiliado aPeticionDe,Set<Documento> documentacion){
        this.tipoPuntoAsemblea= tipoPuntoAsemblea;
        this.descricion=descricion;
    	this.acordo=acordo;
    	this.aPeticionDe=aPeticionDe;
        this.documentacion=documentacion;
    }

    
    
    public String toString() {
        return this.descricion;
    }
 
}