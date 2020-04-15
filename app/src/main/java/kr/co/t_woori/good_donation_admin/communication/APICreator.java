package kr.co.t_woori.good_donation_admin.communication;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by Utae on 2017-07-20.
 */

public class APICreator {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://221.160.225.186/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(new OkHttpClient.Builder().addInterceptor(new AddTokenInterceptor()).build())
            .build();

    public static <T> T create(Class<T> APIService){
        return retrofit.create(APIService);
    }
}
