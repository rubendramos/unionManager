package controllers;

import models.ChatRoom;
import play.Logger;
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
    
    public static void room(String user) {
        render(user);
    }

    public static class ChatRoomSocket extends WebSocketController {
        
        public static void join(String user) {
            
            ChatRoom room = ChatRoom.get();
            
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
                }
                
                // Case: TextEvent received on the socket
                for(String userMessage: TextFrame.match(e._1)) {
                    
                    room.say(user, userMessage);
                }
                
                // Case: Someone joined the room
                for(ChatRoom.Join joined: ClassOf(ChatRoom.Join.class).match(e._2)) {
                    outbound.send("join:%s:%s:%s", joined.user,joined.membros,Tools.getFormatTimeStamp(joined.timestamp));
                }
                
                // Case: New message on the chat room
                for(ChatRoom.Message message: ClassOf(ChatRoom.Message.class).match(e._2)) {
                    outbound.send("message:%s:%s:%s#%s", message.user, message.membros,Tools.getFormatHour(message.timestamp),message.text);
                }
                
                // Case: Someone left the room
                for(ChatRoom.Leave left: ClassOf(ChatRoom.Leave.class).match(e._2)) {
                    outbound.send("leave:%s:%s:%s", left.user,left.membros,Tools.getFormatTimeStamp(left.timestamp));
                }
                
                // Case: The socket has been closed
                for(WebSocketClose closed: SocketClosed.match(e._1)) {
                    room.leave(user);
                    disconnect();
                }
                
            }
            
        }
        
    }
    
}

