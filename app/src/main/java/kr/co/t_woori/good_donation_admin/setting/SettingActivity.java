package kr.co.t_woori.good_donation_admin.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.databinding.SettingActivityBinding;
import kr.co.t_woori.good_donation_admin.login.LoginActivity;
import kr.co.t_woori.good_donation_admin.utilities.AdminInfo;

/**
 * Created by rladn on 2017-08-07.
 */

public class SettingActivity extends AppCompatActivity{

    SettingActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.setting_activity);

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout(){
        SharedPreferences prefs = getSharedPreferences("good_donation_admin", MODE_PRIVATE);
        if(prefs != null){
            prefs.edit().clear().apply();
        }
        AdminInfo.clearAdminInfo();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
