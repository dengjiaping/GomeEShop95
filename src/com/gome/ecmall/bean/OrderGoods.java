package com.gome.ecmall.bean;

import java.util.ArrayList;

/**
 * 订单商品 
 */
public class OrderGoods extends Goods {
    private ArrayList<Promotions> promotions;
    private ArrayList<Gift> gifts;

    public OrderGoods(ArrayList<Promotions> promotions, ArrayList<Gift> gifts) {
        super();
        this.promotions = promotions;
        this.gifts = gifts;
    }

    public OrderGoods() {
        super();
    }

    public ArrayList<Promotions> getPromotions() {
        return promotions;
    }

    public void setPromotions(ArrayList<Promotions> promotions) {
        this.promotions = promotions;
    }

    public ArrayList<Gift> getGiftList() {
        return gifts;
    }

    public void setGiftList(ArrayList<Gift> gifts) {
        this.gifts = gifts;
    }

}
