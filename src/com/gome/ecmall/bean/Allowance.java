package com.gome.ecmall.bean;

/**
 * 节能补贴
 */
public class Allowance {

    private String headType;// 购买方式
    private String head;// 单位名称或个有姓
    private String payerID;// 组织机构代码或身份身份证
    private String bank;// 开户行
    private String account;// 开户行账号
    private String isForegoAllowance; // Y 放弃节能补贴， N 不放弃

    public Allowance() {
        super();
    }

    public Allowance(String headType, String head, String payerID, String bank, String account) {
        super();
        this.headType = headType;
        this.head = head;
        this.payerID = payerID;
        this.bank = bank;
        this.account = account;
    }

    public String getHeadType() {
        return headType;
    }

    public void setHeadType(String headType) {
        this.headType = headType;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getPayerID() {
        return payerID;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIsForegoAllowance() {
        return isForegoAllowance;
    }

    public void setIsForegoAllowance(String isForegoAllowance) {
        this.isForegoAllowance = isForegoAllowance;
    }

    @Override
    public String toString() {
        return "Allowance [headType=" + headType + ", head=" + head + ", payerID=" + payerID + ", bank=" + bank
                + ", account=" + account + "]";
    }

}
