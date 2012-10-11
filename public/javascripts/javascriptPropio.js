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
 
function limparFiltroData(id1,id2) {
ini = document.getElementById(id1);
fin = document.getElementById(id2);
ini.value=""; 
fin.value="";
} 

function confirmaEliminar(text,href) {
if(confirm(text))  document.location.href = href;
}

function confirmaAccion(text,href) {
if(confirm(text))  document.location.href = href;
}


function confirmaAccionSubmitSearching(text,accion,page,id) {
if(confirm(text)) {
    document.searching.page.value=page;
    document.searching.id.value=id;
    document.searching.action=accion;
    document.searching.submit();  
} 
return null;
}

function accionSubmitSearching(accion,page,id) {

    document.searching.page.value=page;
    document.searching.id.value=id;
    document.searching.action=accion;    
    document.searching.submit();  
}



function submitAccionSave(save,saveAndContinue) {
    
    document.searching._save.value=save;
    document.searching._saveAndContinue.value=saveAndContinue;  
    document.searching.submit();  
}

function submitAccion(accion) {

    document.searching.action=accion;
    document.searching.submit();  
}

function showPopupChat(url) {
newwindow=window.open(url,'name','height=670,width=520,top=100,left=550,location=0');
if (window.focus) {newwindow.focus()}
}

 
