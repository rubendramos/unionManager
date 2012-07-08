package controllers;

import play.*;
import play.mvc.*;

@Check("comite")
@With(Secure.class)
public class PuntoAsembleas extends CRUD {    
}