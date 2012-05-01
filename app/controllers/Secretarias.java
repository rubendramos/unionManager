package controllers;

import play.*;
import play.mvc.*;

@Check("secretariaXeral")
@With(Secure.class)
public class Secretarias extends CRUD {    
}