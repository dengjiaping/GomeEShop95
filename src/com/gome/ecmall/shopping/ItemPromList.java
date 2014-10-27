package com.gome.ecmall.shopping;

/**
 * 套购商品促销信息
 * 
 * @author Administrator
 * 
 */
public class ItemPromList {

    private String promType;
    private String promDesc;

    public ItemPromList() {
        super();
    }

    public ItemPromList(String promType, String promDesc) {
        super();
        this.promType = promType;
        this.promDesc = promDesc;
    }

    public String getPromType() {
        return promType;
    }

    public void setPromType(String promType) {
        this.promType = promType;
    }

    public String getPromDesc() {
        return promDesc;
    }

    public void setPromDesc(String promDesc) {
        this.promDesc = promDesc;
    }

}
