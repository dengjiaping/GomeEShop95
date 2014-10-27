package com.gome.ecmall.bean;

/**
 * 属性【键-值】
 */
public class Attributes {
    private String name;
    private String value;

    public Attributes() {
    }

    public Attributes(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
