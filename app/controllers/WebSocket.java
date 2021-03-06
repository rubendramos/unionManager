package controllers;

import java.util.ArrayList;
import models.ChatRoom;
import models.informes.Rooms;
import play.Logger;
import play.Play;
import play.libs.F.Either;
import play.libs.F.EventStream;
import static play.libs.F.Matcher.ClassOf;
import static play.libs.F.Matcher.Equals;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.WebSocketClose;
import play.mvc.Http.WebSocketEvent;
import static play.mvc.Http.WebSocketEvent.SocketClosed;
import static play.mvc.Http.WebSocketEvent.TextFrame;
import play.mvc.WebSocketController;
import utils.Tools;

public class WebSocket extends Controller {
    
    
    
    public static void room(String user,String idOrganismo) {
        render(user,idOrganismo);
    }
    

    public static class ChatRoomSocket extends WebSocketController {
        
        public static void join(String user,String idOrganismo) {
            
            String chatLogPath=Play.configuration.getProperty("chatLog.path");
            String chatLogFile="chatLog"+idOrganismo+".txt";
            
            
            ChatRoom room = Rooms.getRoom(idOrganismo);
            
            // Socket connected, join the chat room
            EventStream<ChatRoom.Event> roomMessagesStream = room.join(user);
  
            
            // Loop while the socket is open
            while(inbound.isOpen()) {
                
                // Wait for an event (either something coming on the inbound socket channel, or ChatRoom messages)
                Either<WebSocketEvent,ChatRoom.Event> e = await(Promise.waitEither(
                    inbound.nextEvent(), 
                    roomMessagesStream.nextEvent()
                ));
                
                // Case: User typed 'quit'
                for(String userMessage: TextFrame.and(Equals("quit")).match(e._1)) {
                    room.leave(user);
                    outbound.send("quit:ok");
                    disconnect();
                    Rooms.deleteRoom(idOrganismo);
                }
                
                // Case: TextEvent received on the socket
                for(String userMessage: TextFrame.match(e._1)) {
                    
                    room.say(user, userMessage);
                }
                
                // Case: Someone joined the room
                for(ChatRoom.Join joined: ClassOf(ChatRoom.Join.class).match(e._2)) {
                    outbound.send("join:%s:%s:%s", joined.user,joined.membros,Tools.getFormatTimeStamp(joined.timestamp));
                    Tools.writeMessageInFile(chatLogPath,chatLogFile,joined.user+ " " + play.i18n.Messages.get("chat.uniuseAoChat")+ " " +Tools.getFormatTimeStamp(joined.timestamp));
                }
                
                // Case: New message on the chat room
                for(ChatRoom.Message message: ClassOf(ChatRoom.Message.class).match(e._2)) {
                    outbound.send("message:%s:%s:%s#%s", message.user, message.membros,Tools.getFormatHour(message.timestamp),message.text);
                    Tools.writeMessageInFile(chatLogPath,chatLogFile,message.user+ "(" + Tools.getFormatHour(message.timestamp)+ ") :" + message.text);
                    
                }
                
                // Case: Someone left the room
                for(ChatRoom.Leave left: ClassOf(ChatRoom.Leave.class).match(e._2)) {
                    outbound.send("leave:%s:%s:%s", left.user,left.membros,Tools.getFormatTimeStamp(left.timestamp));
                    Tools.writeMessageInFile(chatLogPath,chatLogFile,left.user+ " " + play.i18n.Messages.get("chat.deixouAoChat")+ " " +Tools.getFormatTimeStamp(left.timestamp));

                }
                
                // Case: The socket has been closed
                for(WebSocketClose closed: SocketClosed.match(e._1)) {
                    room.leave(user);
                    disconnect();
                    Rooms.deleteRoom(idOrganismo);
                }
                
            }
            
        }
        
    }
    
}

