package kr.co.t_woori.good_donation_admin.login;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface LoginAPIService {

    @FormUrlEncoded
    @POST("/admin/login")
    Call<HashMap<String, Object>> login(@Field("id") String id, @Field("pw") String pw);

    @FormUrlEncoded
    @POST("/admin/login/auto")
    Call<HashMap<String, Object>> autoLogin(@Field("token") String token);
}
