package kr.co.t_woori.good_donation_admin.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by utae on 2017-07-25.
 */

public interface MasterAPIService {

    @FormUrlEncoded
    @POST("/admin/place/info/simple")
    Call<HashMap<String, Object>> getSimplePlaceInfo(@Field("state") String state);

    @FormUrlEncoded
    @POST("/admin/place/approve")
    Call<HashMap<String, Object>> approvePlace(@Field("placeIdList")String placeIdList);

    @FormUrlEncoded
    @POST("/admin/place/disapprove")
    Call<HashMap<String, Object>> disapprovePlace(@Field("placeIdList")String placeIdList);

    @FormUrlEncoded
    @POST("/admin/charity")
    Call<HashMap<String, Object>> charitySignup(@FieldMap HashMap<String, String> charityInfo);

    @GET("/common/charity/all")
    Call<HashMap<String, Object>> getAllCharity();

    @GET("/common/project/all")
    Call<HashMap<String, Object>> getAllProject();

    @FormUrlEncoded
    @POST("/admin/charity/recommend")
    Call<HashMap<String, Object>> modRecommendCharity(@Field("addedList") String addedList, @Field("removedList") String removedList);

    @FormUrlEncoded
    @POST("/admin/place/charge")
    Call<HashMap<String, Object>> chargePlaceBalance(@Field("idNum") String idNum, @Field("amount") String amount);

    @FormUrlEncoded
    @POST("/admin/place/img/del")
    Call<HashMap<String, Object>> delPlaceImg(@Field("idNum") String idNum, @Field("imgAmount") String imgAmount);

    @FormUrlEncoded
    @POST("/admin/place/del")
    Call<HashMap<String, Object>> delPlace(@Field("idNum") String idNum);

    @FormUrlEncoded
    @POST("/admin/charity/img/del")
    Call<HashMap<String, Object>> delCharityImg(@Field("idNum") String idNum, @Field("imgAmount") String imgAmount);

    @FormUrlEncoded
    @POST("/admin/charity/info")
    Call<HashMap<String, Object>> modCharityInfo(@Field("idNum") String idNum, @Field("name") String name, @Field("intro") String intro, @Field("appreciation") String appreciation);

    @FormUrlEncoded
    @POST("/admin/charity/del")
    Call<HashMap<String, Object>> delCharity(@Field("idNum") String idNum);
}
