package com.gome.ecmall.bean;

/**
 * 站内信
 * 
 * @author Administrator
 * 
 */
public class StationLetter {
    private String messageArray;
    private String messageId;
    private String messageTime;
    private String messageTitle;
    private String messageContent;
    private String readStatus;

    public StationLetter() {
        super();
    }

    public StationLetter(String messageArray, String messageId, String messageTime, String messageTitle,
            String messageContent, String readStatus) {
        super();
        this.messageArray = messageArray;
        this.messageId = messageId;
        this.messageTime = messageTime;
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
        this.readStatus = readStatus;
    }

    public String getMessageArray() {
        return messageArray;
    }

    public void setMessageArray(String messageArray) {
        this.messageArray = messageArray;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    @Override
    public String toString() {
        return "StationLetter [messageArray=" + messageArray + ", messageId=" + messageId + ", messageTime="
                + messageTime + ", messageTitle=" + messageTitle + ", messageContent=" + messageContent
                + ", readStatus=" + readStatus + "]";
    }

}
