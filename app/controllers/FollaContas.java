package controllers;

import play.*;
import play.mvc.*;

@Check("tesoureria")
@With(Secure.class)
public class FollaContas extends CRUD {    
}