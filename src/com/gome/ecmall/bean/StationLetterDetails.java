package com.gome.ecmall.bean;

/**
 * 站内信详情
 * 
 * @author Administrator
 * 
 */
public class StationLetterDetails {
    private String messageId;
    private String messageTime;
    private String messageTitle;
    private String messageContent;

    public StationLetterDetails() {
        super();
    }

    public StationLetterDetails(String messageTime, String messageTitle, String messageContent) {
        super();
        this.messageTime = messageTime;
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
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

    @Override
    public String toString() {
        return "StationLetterDetails [messageId=" + messageId + ", messageTime=" + messageTime + ", messageTitle="
                + messageTitle + ", messageContent=" + messageContent + "]";
    }

}
