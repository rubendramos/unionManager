package models.informes;

import models.*;
import com.ning.http.client.websocket.WebSocket;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing;
import java.util.*;
import org.eclipse.jdt.core.dom.ThisExpression;
import play.Logger;

import play.libs.*;
import play.libs.F.*;
import utils.Tools;


public  class Rooms {
    
    private static HashMap<String,ChatRoom>hashRooms=new HashMap<String, ChatRoom>();
    
    public static void setRoom(String roomId,ChatRoom chat){
        hashRooms.put(roomId,chat);
    }
    
    public static ChatRoom getRoom(String roomId){
      ChatRoom cr= hashRooms.get(roomId);    
      if (cr==null){
          setRoom(roomId, new ChatRoom());
          cr=hashRooms.get(roomId); 
      }
      return cr;
    }
    
    public static void deleteRoom(String roomId){
        hashRooms.remove(roomId);
    }
}

