package com.gome.ecmall.bean;

/**
 * 【暂无调用】【AdvisoryReplyService】
 */
public class AdvisoryReply {
    private String profileID;
    private String questionTime;
    private String questContent;
    private String returnArray;

    private String returnStatus;

    public AdvisoryReply() {
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(String questionTime) {
        this.questionTime = questionTime;
    }

    public String getQuestionContent() {
        return questContent;
    }

    public void setQuestionContent(String questContent) {
        this.questContent = questContent;
    }

    public String getReturnArray() {
        return returnArray;
    }

    public void setReturnArray(String returnArray) {
        this.returnArray = returnArray;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

}
