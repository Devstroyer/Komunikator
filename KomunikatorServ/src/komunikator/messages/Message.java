package komunikator.messages;

import java.io.Serializable;

public class Message implements Serializable {
    private final String senderName;
    private final MsgType type;
    private final String content;
    private long senderId;
    private long reciverId;
    
    public Message(String senderName, MsgType type, String content ){
        this.senderName = senderName;
        this.type = type;
        this.content = content;
        this.senderId=0;
        this.reciverId=0;
    }
    
    public Message(String senderName,long senderId,long reciverId, MsgType type, String content ){
        this.senderName = senderName;
        this.type = type;
        this.content = content;
        this.senderId = senderId;
        this.reciverId = reciverId;
    }
    
    
    public String getSenderName() {
        return senderName;
    }
    
    public long getSenderId() {
        return senderId;
    }
    
    public long getReciverId() {
        return reciverId;
    }

    public MsgType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }    
}