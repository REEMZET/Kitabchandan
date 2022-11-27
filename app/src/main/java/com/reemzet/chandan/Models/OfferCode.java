package com.reemzet.chandan.Models;

public class OfferCode {
    public OfferCode() {
    }

    String Offername,discountpercent,promocode;

    public OfferCode(String offername, String discountpercent, String promocode) {
        Offername = offername;
        this.discountpercent = discountpercent;
        this.promocode = promocode;
    }

    public String getOffername() {
        return Offername;
    }

    public void setOffername(String offername) {
        Offername = offername;
    }

    public String getDiscountpercent() {
        return discountpercent;
    }

    public void setDiscountpercent(String discountpercent) {
        this.discountpercent = discountpercent;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }
}
