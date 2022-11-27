package com.reemzet.chandan.Models;

public class CartModel {
    String itemid,itemqty,itemname,itemmrp,itemprice,itemimg;

    public CartModel() {
    }

    public CartModel(String itemid, String itemqty, String itemname, String itemmrp, String itemprice, String itemimg) {
        this.itemid = itemid;
        this.itemqty = itemqty;
        this.itemname = itemname;
        this.itemmrp = itemmrp;
        this.itemprice = itemprice;
        this.itemimg = itemimg;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemqty() {
        return itemqty;
    }

    public void setItemqty(String itemqty) {
        this.itemqty = itemqty;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemmrp() {
        return itemmrp;
    }

    public void setItemmrp(String itemmrp) {
        this.itemmrp = itemmrp;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getItemimg() {
        return itemimg;
    }

    public void setItemimg(String itemimg) {
        this.itemimg = itemimg;
    }
}
