package controllers;

import play.*;
import play.mvc.*;

@Check("autenticado")
@With(Secure.class)
public class Enderezos extends CRUD {    
}