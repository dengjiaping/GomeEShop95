package com.gome.ecmall.home.mygome;

public class UserRedTicket extends UserCoupon {
    /**
     * 红券状态
     */
    protected int ticketStatus;
    /**
     * 红券激活日期
     */
    protected String ticketClaimedDate;
    /**
     * 红券有效期
     */
    protected int ticketValidDays;

    public UserRedTicket() {
    }

    public UserRedTicket(int ticketStatus, String ticketClaimedDate, int ticketValidDays) {
        super();
        this.ticketStatus = ticketStatus;
        this.ticketClaimedDate = ticketClaimedDate;
        this.ticketValidDays = ticketValidDays;
    }

    public int getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(int ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketClaimedDate() {
        return ticketClaimedDate;
    }

    public void setTicketClaimedDate(String ticketClaimedDate) {
        this.ticketClaimedDate = ticketClaimedDate;
    }

    public int getTicketValidDays() {
        return ticketValidDays;
    }

    public void setTicketValidDays(int ticketValidDays) {
        this.ticketValidDays = ticketValidDays;
    }

}
