package com.gome.ecmall.home.mygome;

/**
 * 用户可用的优惠券
 * 
 * @author Administrator
 * 
 */
public class UserCoupon {
    /**
     * 优惠券编码
     */
    protected String ticketID;

    /**
     * 优惠券名称
     */
    protected String ticketName;
    /**
     * 优惠券金额
     */
    protected double ticketAmount;

    public UserCoupon() {
    }

    public UserCoupon(String ticketID, String ticketName, double ticketAmount, String ticketExpirationDate,
            boolean isExpiring, String ticketDesc) {
        super();
        this.ticketID = ticketID;
        this.ticketName = ticketName;
        this.ticketAmount = ticketAmount;
        this.ticketExpirationDate = ticketExpirationDate;
        this.isExpiring = isExpiring;
        this.ticketDesc = ticketDesc;
    }

    /**
     * 红券使用最后期限
     */
    protected String ticketExpirationDate;

    /**
     * 是否即将过期
     */
    protected boolean isExpiring;
    /**
     * 优惠券使用说明
     */
    protected String ticketDesc;

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public double getTicketAmount() {
        return ticketAmount;
    }

    public void setTicketAmount(double ticketAmount) {
        this.ticketAmount = ticketAmount;
    }

    public String getTicketExpirationDate() {
        return ticketExpirationDate;
    }

    public void setTicketExpirationDate(String ticketExpirationDate) {
        this.ticketExpirationDate = ticketExpirationDate;
    }

    public boolean isExpiring() {
        return isExpiring;
    }

    public void setExpiring(boolean isExpiring) {
        this.isExpiring = isExpiring;
    }

    public String getTicketDesc() {
        return ticketDesc;
    }

    public void setTicketDesc(String ticketDesc) {
        this.ticketDesc = ticketDesc;
    }

}
