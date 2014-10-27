package com.gome.ecmall.shopping;

/**
 * 商品促销
 * 
 * @author Administrator
 * 
 */
public class ProductProm {

    /** 促销类型 */
    private String promType;

    /** 促销描述 */
    private String promDesc;

    public ProductProm() {
        super();
    }

    public ProductProm(String promType, String promDesc) {
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
