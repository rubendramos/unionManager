package models;
 
import controllers.Contactos;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
import utils.NewForeignKey;
 
@Entity
@FilterDef(name="listadistribucion", defaultCondition="((tipoListaDistribucion_id != 1))" )
@Filter(name="listadistribucion")
public class ListaDistribucion extends UnionModel {
 
    
    
    @Required
    @ManyToOne
    @AddFiltro
    public TipoListaDistribucion tipoListaDistribucion;

    @Required
    @MaxSize(50)
    public String descricion;   
    
    
    @ManyToMany
    @AddForeignKey    
    @NewForeignKey
    public Set<Contacto> contactos;
        
    @Lob
    @MaxSize(500)
    public String sentenciaSQL;
   
    public ListaDistribucion(String descricion,TipoListaDistribucion tipoListaDistribucion,
            Set<Contacto> contactos,String sentenciaSQL){
        this.descricion=descricion;
        this.tipoListaDistribucion=tipoListaDistribucion;
    	this.contactos=contactos;
        this.sentenciaSQL=sentenciaSQL;
        
    }
      
    public String toString() {
        return this.tipoListaDistribucion + "-"+this.descricion;
    }
    
    private static List<Contacto> getContactosAutomaticos(String query) {
    List<Contacto> contactos= new ArrayList<Contacto>();    
    List<Afiliado> afiliados=find(query).fetch();        
    for(Afiliado af: afiliados){
        Contacto c=new Contacto(new TipoContacto("1"),af.persoa.nome,af.persoa.apelido1, af.persoa.apelido2, af.persoa.email, af.persoa.enderezo,  "", "");
        contactos.add(c);
    }
    return contactos;
    }
    
    
    public List<Contacto> getContactos(Long organismo){

        if(this.tipoListaDistribucion.id==1){
            return getAfiliacion(organismo);
        }else if(this.tipoListaDistribucion.id==2 && !"".equals(this.sentenciaSQL)){
            return getContactosAutomaticos(this.sentenciaSQL);
        }else{
            return new ArrayList<Contacto>(this.contactos);
        }        
    }
    
public static List<Contacto> getAfiliacion(Long organismo) {
    List<Contacto> contactos= new ArrayList<Contacto>();
    String query="from Afiliado af where af.dataBaixa is null and af.estadoAfiliado=1 and af.organismo="+organismo.toString();
    List<Afiliado> afiliados=find(query).fetch();        
    for(Afiliado af: afiliados){
        Contacto c=new Contacto(new TipoContacto("1"),af.persoa.nome,af.persoa.apelido1, af.persoa.apelido2, af.persoa.email, af.persoa.enderezo,  "", "");
        contactos.add(c);
    }
    return contactos;
    }    
 
public static ListaDistribucion getListaAutomaticaAfiliados(Organismo org) {
    String query="from ListaDistribucion ld where ld.tipoListaDistribucion='1' and ld.organismo='"+org.id+"'";    
    return find(query).first();
} 


}