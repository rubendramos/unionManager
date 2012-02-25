function ChangeSelectByValue(ddlID, value, change) {
     var ddl = document.getElementById(ddlID);
     for (var i = 0; i < ddl.options.length; i++) {
         if (ddl.options[i].value == value) {
                 ddl.selectedIndex = i;
                 ddl.onchange;
             break;
         }
     }
 }
 
 
 function summitInputTypeSubmit(id){
     document.getElementById(id).click();     
 }
 
 function deleteMultiSelecRegister(ddlID, value, change,id){
     ChangeSelectByValue(ddlID, value, change);
     summitInputTypeSubmit(id);
     
 }
 
