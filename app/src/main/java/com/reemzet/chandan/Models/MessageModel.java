package com.reemzet.chandan.Models;

public class MessageModel {
    String message, sendername, messageid, senderaccounttype;
    String senderuid;
    String timestamp, date;

    public MessageModel() {

    }

    public MessageModel(String message, String sendername, String messageid, String senderaccounttype, String senderuid, String timestamp, String date) {
        this.message = message;
        this.sendername = sendername;
        this.messageid = messageid;
        this.senderaccounttype = senderaccounttype;
        this.senderuid = senderuid;
        this.timestamp = timestamp;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getSenderaccounttype() {
        return senderaccounttype;
    }

    public void setSenderaccounttype(String senderaccounttype) {
        this.senderaccounttype = senderaccounttype;
    }

    public String getSenderuid() {
        return senderuid;
    }

    public void setSenderuid(String senderuid) {
        this.senderuid = senderuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}