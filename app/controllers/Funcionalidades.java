package controllers;

import org.apache.commons.lang.StringUtils;
import play.*;
import play.mvc.*;

@Check("admin")
@With(Secure.class)
public class Funcionalidades extends CRUD { 
    
   @Before
static void setDefaultOrder() {
	if(StringUtils.isBlank(request.params.get("orderBy"))) {
		request.params.put("order", "ASC");
		request.params.put("orderBy", "descricion");
	}
}
    
}