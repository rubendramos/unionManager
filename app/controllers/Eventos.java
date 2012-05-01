package controllers;

import play.mvc.*;

@Check("comite")
@With(Secure.class)
public class Eventos extends CRUD {    
}
