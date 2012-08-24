package models;

import com.ning.http.client.websocket.WebSocket;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing;
import java.util.*;
import play.Logger;

import play.libs.*;
import play.libs.F.*;
import utils.Tools;

public class ChatRoom {
    
    // ~~~~~~~~~ Let's chat! 
    
    final ArchivedEventStream<ChatRoom.Event> chatEvents = new ArchivedEventStream<ChatRoom.Event>(100);
    // Members of this room.
    final static ArrayList<String> members = new ArrayList<String>();
    /**
     * For WebSocket, when a user join the room we return a continuous event stream
     * of ChatEvent
     */
    public EventStream<ChatRoom.Event> join(String user) {
        chatEvents.publish(new Join(user));
        return chatEvents.eventStream();
    }
    
    /**
     * A user leave the room
     */
    public void leave(String user) {
        chatEvents.publish(new Leave(user));
    }
    
    /**
     * A user say something on the room
     */
    public void say(String user, String text) {
        if(text == null || text.trim().equals("")) {
            return;
        }                
        chatEvents.publish(new Message(user, text));
    }
    
    /**
     * For long polling, as we are sometimes disconnected, we need to pass 
     * the last event seen id, to be sure to not miss any message
     */
    public Promise<List<IndexedEvent<ChatRoom.Event>>> nextMessages(long lastReceived) {
        return chatEvents.nextEvents(lastReceived);
    }
    
    /**
     * For active refresh, we need to retrieve the whole message archive at
     * each refresh
     */
    public List<ChatRoom.Event> archive() {
        return chatEvents.archive();
    }
    
    // ~~~~~~~~~ Chat room events

    public static abstract class Event {
        
        final public String type;
        final public Long timestamp;
        
        public Event(String type) {
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
        
    }
    
    public static class Join extends Event {
        
        final public String user;
        final public ArrayList<String> membros;
        
        public Join(String user) {
            super("join");
            this.user = user;
            members.add(user);
            this.membros=members;
        }
        
    }
    
    public static class Leave extends Event {
        
        final public String user;
        final public ArrayList<String> membros;
        
        public Leave(String user) {
            super("leave");
            this.user = user;
            members.remove(user);
            this.membros=members;
        }
        
    }
    
    public static class Message extends Event {
        
        final public String user;
        final public String text;
        final public ArrayList<String> membros;
        
        public Message(String user, String text) {
            super("message");
            this.user = user;
            this.text = text;    
            this.membros=members;                        
        }
        
    }
    
    // ~~~~~~~~~ Chat room factory

    static ChatRoom instance = null;
    public static ChatRoom get() {
        if(instance == null) {
            instance = new ChatRoom();
        }
        return instance;
    }
    
}

