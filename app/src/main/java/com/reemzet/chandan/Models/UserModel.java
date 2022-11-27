package com.reemzet.chandan.Models;

public class UserModel {
    String username,useruid,usercity,userlocation,userregisterdate,userphone,useraddress,pincode,Accounttype,deviceid;
    UserModel(){

    }

    public UserModel(String username, String useruid, String usercity, String userlocation, String userregisterdate, String userphone, String useraddress, String pincode, String accounttype, String deviceid) {
        this.username = username;
        this.useruid = useruid;
        this.usercity = usercity;
        this.userlocation = userlocation;
        this.userregisterdate = userregisterdate;
        this.userphone = userphone;
        this.useraddress = useraddress;
        this.pincode = pincode;
        Accounttype = accounttype;
        this.deviceid = deviceid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }

    public String getUsercity() {
        return usercity;
    }

    public void setUsercity(String usercity) {
        this.usercity = usercity;
    }

    public String getUserlocation() {
        return userlocation;
    }

    public void setUserlocation(String userlocation) {
        this.userlocation = userlocation;
    }

    public String getUserregisterdate() {
        return userregisterdate;
    }

    public void setUserregisterdate(String userregisterdate) {
        this.userregisterdate = userregisterdate;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAccounttype() {
        return Accounttype;
    }

    public void setAccounttype(String accounttype) {
        Accounttype = accounttype;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
}
