package kr.co.t_woori.good_donation_admin.signup;

/**
 * Created by rladn on 2017-08-10.
 */

public class AddressItem {

    private String address;
    private String coordinate;

    public AddressItem(String address, String coordinate) {
        this.address = address;
        this.coordinate = coordinate;
    }

    public String getAddress() {
        return address;
    }

    public String getCoordinate() {
        return coordinate;
    }
}
