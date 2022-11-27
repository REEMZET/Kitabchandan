package com.reemzet.chandan.Models;

import java.util.ArrayList;
import java.util.List;

public class ItemDetails {

    String itemtitle,itemdesc,itemmrp,itemprice,itemimg,itemstatus,itemcat,itemexam,itemavail,itemid;
  List<String> itemtag;

    public ItemDetails() {
    }

    public ItemDetails(String itemtitle, String itemdesc, String itemmrp, String itemprice, String itemimg, String itemstatus, String itemcat, String itemexam, String itemavail, String itemid) {
        this.itemtitle = itemtitle;
        this.itemdesc = itemdesc;
        this.itemmrp = itemmrp;
        this.itemprice = itemprice;
        this.itemimg = itemimg;
        this.itemstatus = itemstatus;
        this.itemcat = itemcat;
        this.itemexam = itemexam;
        this.itemavail = itemavail;
        this.itemid = itemid;
    }

    public ItemDetails(String itemtitle, String itemdesc, String itemmrp, String itemprice, String itemimg, String itemstatus, String itemcat, String itemexam, String itemavail, String itemid, List<String> itemtag) {
        this.itemtitle = itemtitle;
        this.itemdesc = itemdesc;
        this.itemmrp = itemmrp;
        this.itemprice = itemprice;
        this.itemimg = itemimg;
        this.itemstatus = itemstatus;
        this.itemcat = itemcat;
        this.itemexam = itemexam;
        this.itemavail = itemavail;
        this.itemid = itemid;
        this.itemtag = itemtag;
    }

    public String getItemtitle() {
        return itemtitle;
    }

    public void setItemtitle(String itemtitle) {
        this.itemtitle = itemtitle;
    }

    public String getItemdesc() {
        return itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
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

    public String getItemstatus() {
        return itemstatus;
    }

    public void setItemstatus(String itemstatus) {
        this.itemstatus = itemstatus;
    }

    public String getItemcat() {
        return itemcat;
    }

    public void setItemcat(String itemcat) {
        this.itemcat = itemcat;
    }

    public String getItemexam() {
        return itemexam;
    }

    public void setItemexam(String itemexam) {
        this.itemexam = itemexam;
    }

    public String getItemavail() {
        return itemavail;
    }

    public void setItemavail(String itemavail) {
        this.itemavail = itemavail;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public List<String> getItemtag() {
        return itemtag;
    }

    public void setItemtag(List<String> itemtag) {
        this.itemtag = itemtag;
    }
}
