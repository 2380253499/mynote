package com.zr.note.ui.main.entity;

/**
 * Created by Administrator on 2016/9/28.
 */
public class Login2 {
 /*log-key[price		: ]########[0-800]
log-key[other		: ]########[你啊好噶哈哈哈]
log-key[area		: ]########[]
log-key[fromToRoom	: ]########[1-2]
log-key[token			: ]########[21570f33e2414267bc2eb4298ec80229]
log-key[custCode		: ]########[C98040000041]
log-key[reqType		: ]########[rent]
log-key[distCode		: ]########[null]
log-key[acreage		: ]########[0-50]
log-key[randomTime	: ]########[1475982728]
:http://61.152.255.241:8082/sales-web/mobile/cust/addCustomerdelMobile?pric*/
    private String price;
    private String other;
    private String area;
    private String fromToRoom;
    private String token;
    private String custCode;
    private String reqType;
    private String distCode;
    private String acreage;
    private String randomTime;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFromToRoom() {
        return fromToRoom;
    }

    public void setFromToRoom(String fromToRoom) {
        this.fromToRoom = fromToRoom;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getAcreage() {
        return acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public String getRandomTime() {
        return randomTime;
    }

    public void setRandomTime(String randomTime) {
        this.randomTime = randomTime;
    }
}
