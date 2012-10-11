package controllers;

import org.apache.commons.lang.StringUtils;
import play.*;
import play.mvc.*;

@Check("comite")
@With(Secure.class)
public class Persoas extends CRUD {    
    
     @Before
    public static void setDefaultOrder() {
	if(StringUtils.isBlank(request.params.get("orderBy"))) {
		request.params.put("order", "ASC");
		request.params.put("orderBy", "nomeCompleto");
	}        
    }
    
}