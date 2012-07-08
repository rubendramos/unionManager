package models;
 
import controllers.CRUD.Hidden;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import javax.persistence.*;
import play.Logger;
import org.hibernate.annotations.Filter;
import utils.AddForeignKey;

 
import play.db.Model;
import play.data.validation.*;
import utils.AddFiltro;
import utils.Tools;
 
@Entity
public class Auditoria extends UnionSecureModel {
 
    
    
    @ManyToOne
    public User usuario ;
       
   
    
    public String accion;
    
    @AddFiltro
    public String identificador;
    
    @AddFiltro
    public Date dataAccion;    
 
    @Lob
    @MaxSize(500)
    public String descricion;
    

    
  
    public Auditoria(User usuario,Date dataAccion,String accion, String descricion){
    	this.usuario=usuario;
    	this.dataAccion=dataAccion;
    	this.accion=accion;
    	this.descricion=descricion;
        this.identificador=identificador;
    }
    
    public String toString() {
        return usuario.afiliado.persoa.nomeCompleto+" - "+dataAccion+" - "+accion+" - "+ descricion;
    }

  
    public  static void facerAuditoria(Model obj,User user,String accion,Organismo org)throws Exception{
        
        TipoEstado ti=TipoEstado.findById(Long.parseLong("2"));
        String nomeAccion=obj.getClass().getSimpleName();        
        Logger.debug("String identificador:  "+ nomeAccion);
        String sAccion=accion+ " "+ nomeAccion;
        String descricion= getDescricion(obj);        
        Date dataAccion=Tools.getCurrentDate();
        Auditoria au=new Auditoria(user,dataAccion ,sAccion,descricion);        
        au.setEstado(ti);
        au.setOrganismo(org);
        
        au.identificador=getIdentificador(nomeAccion, obj._key().toString());
        au._save();
        
    }
    
    private static String getIdentificador(String s, String id){
        Long res=Long.parseLong("0");
        int j=1;        
        if(s.length()<8){
            for(int p=s.length();p==8;p++){
                s=s+"a";
            }
        }else{
            s=s.substring(0,8);
        }
        Logger.debug("String identificador:  "+ s);
        for (int i=0; i<s.length();i++){
        Long value=Integer.valueOf((int)s.charAt(i)).longValue();
        res=res+(value*j);
        j=j*10;
        }
        return Integer.toString(res.intValue())+id;
    }
    
    private static String getDescricion(Model objModel) throws Exception{
        String res="";
        String sValue="";
        
        Field[] fillos = objModel.getClass().getFields();

        ArrayList<Field> aFields = new ArrayList<Field>(Arrays.asList(fillos));

        for (Field field : aFields) {

            if (!field.getName().equals("id") && !field.getName().equals("willBeSaved")) {

                if ("Set".equals(field.getType().getSimpleName())) {


                    Object value = new PropertyDescriptor(field.getName(), objModel.getClass()).getReadMethod().invoke(objModel);
                    Class<? extends Set> theClass = Class.forName("java.util.Set").asSubclass(Set.class);
                    Set objSet = theClass.cast(value);

                    for (Object obj : objSet) {
                       Model model=(Model)obj; 
                     if(obj!=null){
                         sValue=obj.toString();
                     } else{
                         sValue="";
                     }
                       res= res+ model.getClass().getSimpleName()+" : "+sValue+" || ";
                    }

                } else {

                    Object obj = new PropertyDescriptor(field.getName(), objModel.getClass()).getReadMethod().invoke(objModel);                        
                     if(obj!=null){
                         sValue=obj.toString();
                     } else{
                         sValue="";
                     }
                    
                    res= res+ field.getName()+" : "+ sValue +" || ";

                }
            }
        }
        
        
        return res;
    }
}
