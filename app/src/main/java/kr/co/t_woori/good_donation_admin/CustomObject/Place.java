package kr.co.t_woori.good_donation_admin.CustomObject;

import java.io.Serializable;

/**
 * Created by rladn on 2017-08-10.
 */

public class Place implements Serializable {

    private String idNum;
    private String id;
    private String placeName;
    private String placePhone;
    private String address;
    private String ownerName;
    private String ownerPhone;
    private String balance;
    private String imgAmount;

    public Place(String idNum, String placeName) {
        this.idNum = idNum;
        this.placeName = placeName;
    }

    public Place(String idNum, String id, String placeName, String address, String balance, String imgAmount) {
        this.idNum = idNum;
        this.id = id;
        this.placeName = placeName;
        this.address = address;
        this.balance = balance;
        this.imgAmount = imgAmount;
    }

    public String getIdNum() {
        return idNum;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlacePhone() {
        return placePhone;
    }

    public String getAddress() {
        return address;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public String getId() {
        return id;
    }

    public String getBalance() {
        return balance;
    }

    public void setPlacePhone(String placePhone) {
        this.placePhone = placePhone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getImgAmount() {
        return imgAmount;
    }
}
