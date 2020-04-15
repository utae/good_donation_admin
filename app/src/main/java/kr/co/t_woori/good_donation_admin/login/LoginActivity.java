package kr.co.t_woori.good_donation_admin.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.good_donation_admin.master.MasterMainActivity;
import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.LoginActivityBinding;
import kr.co.t_woori.good_donation_admin.place.PlaceMainActivity;
import kr.co.t_woori.good_donation_admin.signup.SignupAgreementActivity;
import kr.co.t_woori.good_donation_admin.utilities.BackPressCloseSystem;
import kr.co.t_woori.good_donation_admin.utilities.ScrollView;
import kr.co.t_woori.good_donation_admin.utilities.AdminInfo;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by utae on 2017-07-25.
 */

public class LoginActivity extends AppCompatActivity {

    private LoginActivityBinding binding;
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity);

        initScrollView();

        backPressCloseSystem = new BackPressCloseSystem(this);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllFormFilled()){
                    login();
                }else{
                    Utilities.showToast(LoginActivity.this, "닉네임(또는 이메일)과 비밀번호를 입력해주세요.");
                }
            }
        });

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupAgreementActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initScrollView(){
        binding.scrollView.setOnSizeChangeListener(new ScrollView.OnSizeChangeListener() {
            @Override
            public void onSizeChanged(boolean smaller) {
                if(smaller){
                    View curFocusView = getCurrentFocus();
                    binding.scrollView.fullScroll(View.FOCUS_DOWN);
                    if(curFocusView != null){
                        curFocusView.requestFocus();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseSystem.onBackPressed();
    }

    private boolean isAllFormFilled(){
        return isFilled(binding.idEdt) && isFilled(binding.pwEdt);
    }

    private boolean isFilled(EditText editText){
        return !"".equals(editText.getText().toString().trim());
    }

    private void login(){
        String id = binding.idEdt.getText().toString().trim();
        String pw = binding.pwEdt.getText().toString().trim();

        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(LoginAPIService.class).login(id, pw)
        ) {

            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                String token = (String)results.get("token");
                String adminClass = (String)results.get("class");
                if(token != null && adminClass != null){
                    if(binding.autoLoginChkBox.isChecked()){
                        SharedPreferences prefs = getSharedPreferences("good_donation_admin", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("token", token);
                        editor.apply();
                    }
                    AdminInfo.setToken(token);
                    AdminInfo.setAdminClass(adminClass);
                    Intent intent = null;
                    switch (adminClass){
                        case "M" :
                            intent = new Intent(LoginActivity.this, MasterMainActivity.class);
                            break;
                        case "P" :
                            intent = new Intent(LoginActivity.this, PlaceMainActivity.class);
                            break;
                    }
                    if(intent != null){
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }else{
                    Utilities.showToast(LoginActivity.this, "서버오류 : 토큰을 받아올 수 없습니다.");
                }
            }
        };
        serverCommunicator.execute();
    }
}
