package com.reemzet.chandan.Models;

public class OrderDetails {
    String orderdate,paymentoption,ordertime,ordertitle,orderqty,ordertotalprice,orderid,orderstatus,deliveryaddress,deliverylocation,username,userphone,userid,userdeviceid;

    public OrderDetails() {
    }

    public OrderDetails(String orderdate, String paymentoption, String ordertime, String ordertitle, String orderqty, String ordertotalprice, String orderid, String orderstatus, String deliveryaddress, String deliverylocation, String username, String userphone, String userid, String userdeviceid) {
        this.orderdate = orderdate;
        this.paymentoption = paymentoption;
        this.ordertime = ordertime;
        this.ordertitle = ordertitle;
        this.orderqty = orderqty;
        this.ordertotalprice = ordertotalprice;
        this.orderid = orderid;
        this.orderstatus = orderstatus;
        this.deliveryaddress = deliveryaddress;
        this.deliverylocation = deliverylocation;
        this.username = username;
        this.userphone = userphone;
        this.userid = userid;
        this.userdeviceid = userdeviceid;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getPaymentoption() {
        return paymentoption;
    }

    public void setPaymentoption(String paymentoption) {
        this.paymentoption = paymentoption;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getOrdertitle() {
        return ordertitle;
    }

    public void setOrdertitle(String ordertitle) {
        this.ordertitle = ordertitle;
    }

    public String getOrderqty() {
        return orderqty;
    }

    public void setOrderqty(String orderqty) {
        this.orderqty = orderqty;
    }

    public String getOrdertotalprice() {
        return ordertotalprice;
    }

    public void setOrdertotalprice(String ordertotalprice) {
        this.ordertotalprice = ordertotalprice;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getDeliveryaddress() {
        return deliveryaddress;
    }

    public void setDeliveryaddress(String deliveryaddress) {
        this.deliveryaddress = deliveryaddress;
    }

    public String getDeliverylocation() {
        return deliverylocation;
    }

    public void setDeliverylocation(String deliverylocation) {
        this.deliverylocation = deliverylocation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserdeviceid() {
        return userdeviceid;
    }

    public void setUserdeviceid(String userdeviceid) {
        this.userdeviceid = userdeviceid;
    }
}
