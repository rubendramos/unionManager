package controllers;

import java.lang.reflect.Constructor;
import java.util.List;
import models.*;
import play.*;
import play.db.Model;
import play.db.jpa.GenericModel;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;
import utils.Tools;

@Check("propaganda")
@With(Secure.class)
public class PrestamoFondos extends CRUD {    
    
     public static void facerPrestamo(String afiliados,String efId,String page,String where,String search){
     EntradaFondo ef=EntradaFondo.findById(Long.parseLong(efId));
     Afiliado afiliado=Afiliado.findById(Long.parseLong(afiliados));
     PrestamoFondo pf=new PrestamoFondo(afiliado,ef ,Tools.getCurrentDate() ,null);
     
     ef.setnExemplaresPrestados(Integer.toString(Integer.parseInt(ef.getnExemplaresPrestados())+1));
     ef._save();
     pf.setOrganismo(Seguridade.organismo());
     pf._save();
     flash.success(play.i18n.Messages.get("crud.avisoGardado", pf.toString()));
     EntradaFondos.listaPrestables(Integer.parseInt(page), where,search, "false",null, null, null);
     
     }
        
    
    public static void seleccionaAfiliado(String entradaFondoID,String page,String where,String search) throws Exception{
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        EntradaFondo ef=EntradaFondo.findById(Long.parseLong(entradaFondoID));
        PrestamoFondo object=new PrestamoFondo(null,ef ,null ,null);
        //Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        //Model object = (Model) constructor.newInstance();
        //notFoundIfNull(object);
        List<Afiliado> afiliados =Afiliado.getListaAfiliadosAlta();
        render(afiliados,ef, page,where,search);
        
        
        
    }
}