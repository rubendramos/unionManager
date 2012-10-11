package controllers;

import models.User;
import org.hibernate.Filter;
import org.hibernate.Session;
import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

@Check("admin")
@With(Secure.class)
public class TipoListaDistribucions extends CRUD { 
    
    
}