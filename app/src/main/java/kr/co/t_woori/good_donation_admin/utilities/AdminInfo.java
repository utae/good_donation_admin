package kr.co.t_woori.good_donation_admin.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.util.HashMap;

/**
 * Created by Utae on 2015-11-06.
 */
public class AdminInfo {

    static private HashMap<String, Object> userInfo = new HashMap<>();

    @SuppressLint("HardwareIds")
    static public String getUserAndroidId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    static public void setAdminClass(String adminClass){
        userInfo.put("class", adminClass);
    }

    static public String getAdminClass(){
        if(userInfo.get("class") == null){
            return null;
        }else{
            return (String)userInfo.get("class");
        }
    }

    static public void setToken(String token){
        userInfo.put("token", token);
    }

    static public String getToken(){
        if(userInfo.get("token") == null){
            return null;
        }else{
            return (String)userInfo.get("token");
        }
    }

    static public void clearAdminInfo(){
        userInfo.clear();
    }
}
