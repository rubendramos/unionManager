package controllers;

import play.*;
import play.mvc.*;

@Check("tesoureria")
@With(Secure.class)
public class LibroContas extends CRUD {    
}