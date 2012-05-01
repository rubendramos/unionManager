package controllers;

import play.mvc.*;

@Check("accionSindical")
@With(Secure.class)
public class Conflitos extends CRUD {    
}
