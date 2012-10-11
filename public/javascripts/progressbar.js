$(function() {

   // Initialise the progress bar.
   var $progressbar = $('#progressbar').progressbar();
        $('#overlay').show('slide',2000); 
        $('#overlayMsg').show('slide',2000); 
        $progressbar.progressbar('complete', $('#progressbar').show('slide'),2000); 
        
        

   // Open the web socket connection.
   var serverUrl = $progressbar.data('url');
   var socket = new WebSocket(serverUrl);

   // Use web socket messages to update the progress bar.
   socket.onmessage = function(event) {
      $progressbar.progressbar('value', parseInt(event.data));
   };
  
  
 
   $('#progressbar').progressbar({
    
    //complete: function(){$progressbar.progressbar('complete', $('<p>The progressbar is complete</p>').insertAfter('#progressbar')); }});
        
    complete: function(){
        if(event.data==102){ 
        $progressbar.progressbar('complete', $('#progressbar').hide('fast')); 
        $('#overlayMsg').hide('fast');
        $('#overlayMsg2').hide('fast');
        $('#overlayMsg3').hide('fast');
        $('#overlayMsg4').hide('fast');
        $('#overlayError').show('slow'); 
        }else{
        $progressbar.progressbar('complete', $('#progressbar').hide(2000)); 
        $('#overlayMsg4').hide(2000); 
        $('#overlay').hide('slide',2000); 
        
        }
        },
    
        change: function(){
            if(event.data==25){                
                $('#overlayMsg').hide('fast').delay(); 
                $('#overlayMsg2').show('slide',2000); 
            }else if(event.data==50){
                $('#overlayMsg2').hide('fast').delay(); 
                $('#overlayMsg3').show('slide',2000); 
            }else if(event.data==75){
                $('#overlayMsg3').hide('fast').delay(); 
                $('#overlayMsg4').show('slide',2000); 
            }

        }});    
  
   
});
