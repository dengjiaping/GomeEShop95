package com.gome.ecmall.bean;

import java.io.Serializable;

/**
 * 发票信息
 */
public class Invoice implements Serializable {
    /** 发票类型 */
    private String invoiceType;

    /** 发票抬头类型 */
    private String invoiceTitleType;

    /** 发票抬头 */
    private String invoiceTitle;

    /** 发票名称 */
    private String invoiceName;

    /** 发票内容 */
    private String invoiceContent;

    /** 单位名称 */
    private String companyName;

    /** 纳税人识别号 */
    private String taxPayerNo;

    /** 注册地址 */
    private String regAdress;

    /** 注册电话 */
    private String regTel;

    /** 开户银行 */
    private String bankName;

    /** 银行帐户 */
    private String bankAccount;

    public Invoice() {
        super();
    }

    public Invoice(String invoiceType, String invoiceTitleType, String invoiceTitle, String invoiceName,
            String invoiceContent, String companyName, String taxPayerNo, String regAdress, String regTel,
            String bankName, String bankAccount) {
        super();
        this.invoiceType = invoiceType;
        this.invoiceTitleType = invoiceTitleType;
        this.invoiceTitle = invoiceTitle;
        this.invoiceName = invoiceName;
        this.invoiceContent = invoiceContent;
        this.companyName = companyName;
        this.taxPayerNo = taxPayerNo;
        this.regAdress = regAdress;
        this.regTel = regTel;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceTitleType() {
        return invoiceTitleType;
    }

    public void setInvoiceTitleType(String invoiceTitleType) {
        this.invoiceTitleType = invoiceTitleType;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxPayerNo() {
        return taxPayerNo;
    }

    public void setTaxPayerNo(String taxPayerNo) {
        this.taxPayerNo = taxPayerNo;
    }

    public String getRegAdress() {
        return regAdress;
    }

    public void setRegAdress(String regAdress) {
        this.regAdress = regAdress;
    }

    public String getRegTel() {
        return regTel;
    }

    public void setRegTel(String regTel) {
        this.regTel = regTel;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

}
