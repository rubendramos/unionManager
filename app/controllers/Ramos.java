package controllers;

import play.*;
import play.mvc.*;

@Check("admin")
@With(Secure.class)
public class Ramos extends CRUD {    
}