package kirey.com.icap.model;

import java.io.Serializable;

/**
 * Created by kitanoskan on 08/06/2017.
 */

public class RecievedMessage implements Serializable {

    private String messageId;
    private String messageTitle;
    private String messageText;
    private Long messageTimestamp;
    //private String location;
    private String address;
    private int id;
    private String timeSent;
    private String notificationIcon;

    private String targetTimestamp;
    private boolean read;

    public RecievedMessage(){
        this.read = false;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Long getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(Long messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTargetTimestamp() {
        return targetTimestamp;
    }

    public void setTargetTimestamp(String targetTimestamp) {
        this.targetTimestamp = targetTimestamp;
    }

    //public String getLocation() {return location; }

    //public void setLocation(String location) {this.location = location; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotificationIcon() {
        return notificationIcon;
    }

    public void setNotificationIcon(String notificationIcon) {
        this.notificationIcon = notificationIcon;
    }
}
