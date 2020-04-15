package kr.co.t_woori.good_donation_admin.communication;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by utae on 2017-07-25.
 */

public interface CommunicationAPIService {

    @Multipart
    @POST("/admin/charity/img/upload")
    Call<ResponseBody> uploadCharityImg(@Part("idNum") RequestBody idNum, @Part List<MultipartBody.Part> fileList);

    @Multipart
    @POST("/admin/place/img/upload")
    Call<ResponseBody> uploadPlaceImg(@Part("idNum") RequestBody idNum, @Part List<MultipartBody.Part> fileList);

    @Multipart
    @POST("/common/home/banner")
    Call<ResponseBody> uploadHomeBanner(@Part("idNum") RequestBody idNum, @Part List<MultipartBody.Part> fileList);
}
