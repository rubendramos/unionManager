package controllers;

import play.*;
import play.mvc.*;

@Check("tesoureria")
@With(Secure.class)
public class Apuntamentos extends CRUD {    
}