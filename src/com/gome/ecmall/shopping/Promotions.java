package com.gome.ecmall.shopping;

public class Promotions {

    private String promType;
    private String promDesc;

    public Promotions() {
        super();
    }

    public Promotions(String promType, String promDesc) {
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
