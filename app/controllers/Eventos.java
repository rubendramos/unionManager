package controllers;

import play.mvc.*;

@Check("admin")
@With(Secure.class)
public class Eventos extends CRUD {    
}
