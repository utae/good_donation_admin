package kr.co.t_woori.good_donation_admin.place;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface PlaceAPIService {

    @FormUrlEncoded
    @POST("/admin/tagToken")
    Call<HashMap<String, Object>> getTagToken(@Field("amount") String amount);

    @GET("/admin/balance")
    Call<HashMap<String, Object>> getMyBalance();
}
