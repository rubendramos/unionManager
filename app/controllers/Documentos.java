package controllers;

import play.*;
import play.mvc.*;

@Check("admin")
@With(Secure.class)
public class Documentos extends CRUD {    
}