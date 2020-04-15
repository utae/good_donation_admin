package kr.co.t_woori.good_donation_admin.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.good_donation_admin.master.MasterMainActivity;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.place.PlaceMainActivity;
import kr.co.t_woori.good_donation_admin.utilities.AdminInfo;
import retrofit2.Response;

/**
 * Created by rladn on 2017-08-07.
 */

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAutoLogin();
    }

    private void checkAutoLogin(){
        prefs = getSharedPreferences("good_donation_admin", MODE_PRIVATE);

        if(prefs != null && prefs.getString("token", null) != null){
            autoLogin();
        }else{
            goToLogin();
        }
    }

    private void autoLogin(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(LoginAPIService.class).autoLogin(prefs.getString("token", null))
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                String adminClass = (String)results.get("class");
                if(adminClass != null){
                    AdminInfo.setToken(prefs.getString("token", null));
                    AdminInfo.setAdminClass(adminClass);
                    Intent intent = null;
                    switch (adminClass){
                        case "M" :
                            intent = new Intent(SplashActivity.this, MasterMainActivity.class);
                            break;
                        case "P" :
                            intent = new Intent(SplashActivity.this, PlaceMainActivity.class);
                            break;
                    }
                    if(intent != null){
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        goToLogin();
                    }
                }else{
                    goToLogin();
                }
            }

            @Override
            protected void onServerError(Response<HashMap<String, Object>> response) {
                super.onServerError(response);
                if(response != null && response.code() == 401){
                    goToLogin();
                }
            }
        };
        serverCommunicator.execute();
    }

    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
