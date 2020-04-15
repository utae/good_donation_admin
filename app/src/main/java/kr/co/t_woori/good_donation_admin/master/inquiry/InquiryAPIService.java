package kr.co.t_woori.good_donation_admin.master.inquiry;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface InquiryAPIService {

    @GET("/common/inquiry/all")
    Call<HashMap<String, Object>> getAllInquiry();

    @FormUrlEncoded
    @POST("/common/inquiry/a")
    Call<HashMap<String, Object>> answerQuestion(@Field("inquiryId") String inquiryId, @Field("answer") String answer);
}
