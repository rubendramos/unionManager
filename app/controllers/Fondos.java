package controllers;

import play.*;
import play.mvc.*;

@Check("propaganda")
@With(Secure.class)
public class Fondos extends CRUD {    
}