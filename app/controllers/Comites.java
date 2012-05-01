package controllers;

import play.*;
import play.mvc.*;

@Check("secretariaXeral")
@With(Secure.class)
public class Comites extends CRUD {    
}