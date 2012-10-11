package controllers;

import models.User;
import org.hibernate.Filter;
import org.hibernate.Session;
import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

@Check("comite")
@With(Secure.class)
public class ListaDistribucions extends CRUD {
    
 @Before
public static void setFilters() { 
    ((Session)JPA.em().getDelegate()).enableFilter("tipolistadistribucion");
    }    
   
    
}