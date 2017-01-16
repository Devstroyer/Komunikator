package komunikator.messages;

import java.io.Serializable;

public class Message implements Serializable {
    private final String senderName;
    private final MsgType type;
    private final String content;
    
    public Message(String senderName, MsgType type, String content ){
        this.senderName = senderName;
        this.type = type;
        this.content = content;
    }
    
    public String getSenderName() {
        return senderName;
    }

    public MsgType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }    
}
