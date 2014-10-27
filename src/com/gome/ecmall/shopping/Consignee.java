package com.gome.ecmall.shopping;

/**
 * 收货人信息
 * 
 * @author Administrator
 * 
 */
public class Consignee {
    private String name;
    private String tel;
    private String district;// 地区
    private String address;
    private String email;
    private int postcode;// 邮编

    public Consignee() {
        super();
    }

    public Consignee(String name, String tel, String district, String address, String email, int postcode) {
        super();
        this.name = name;
        this.tel = tel;
        this.district = district;
        this.address = address;
        this.email = email;
        this.postcode = postcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAdd(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

}
