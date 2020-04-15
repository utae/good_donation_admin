package kr.co.t_woori.good_donation_admin.signup;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.SignupThirdActivityBinding;
import kr.co.t_woori.good_donation_admin.login.LoginActivity;
import kr.co.t_woori.good_donation_admin.utilities.ScrollView;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by utae on 2017-07-26.
 */

public class SignupThirdActivity extends AppCompatActivity{

    private SignupThirdActivityBinding binding;

    private HashMap<String, String> signupInfo;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.signup_third_activity);

        initScrollView();

        signupInfo = (HashMap<String, String>) getIntent().getSerializableExtra("signupInfo");

        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initScrollView(){
        binding.scrollView.setOnSizeChangeListener(new ScrollView.OnSizeChangeListener() {
            @Override
            public void onSizeChanged(boolean smaller) {
                if(smaller) {
                    View curFocusView = getCurrentFocus();
                    binding.scrollView.fullScroll(View.FOCUS_DOWN);
                    if (curFocusView != null) {
                        curFocusView.requestFocus();
                    }
                }
            }
        });
    }

    private boolean isAllDataValid(){
        if(!isFilled(binding.idEdt)){
            Utilities.showToast(this, "닉네임을 입력해주세요.");
        }else if(!Utilities.isValidPassword(binding.pwEdt.getText().toString().trim())){
            Utilities.showToast(this, "비밀번호가 기준에 맞는 지 확인해주세요.");
        }else if(!isPwConfirmCorrect()){
            Utilities.showToast(this, "비밀번호 확인이 일치하지 않습니다.");
        }else{
            return true;
        }
        return false;
    }

    private boolean isPwConfirmCorrect(){
        return binding.pwEdt.getText().toString().equals(binding.pwCfmEdt.getText().toString());
    }

    private boolean isFilled(EditText editText){
        return isFilled(editText, 1);
    }

    private boolean isFilled(EditText editText, int length){
        return editText.getText().toString().trim().length() >= length;
    }

    private void signup(){
        if(isAllDataValid()){
            signupInfo.put("id", binding.idEdt.getText().toString().trim());
            signupInfo.put("pw", binding.pwEdt.getText().toString().trim());

            ServerCommunicator serverCommunicator = new ServerCommunicator(
                    this, APICreator.create(SignupAPIService.class).signup(signupInfo)
            ) {

                @Override
                protected void onSuccess(HashMap<String, Object> results) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupThirdActivity.this);
                    builder.setMessage("가입신청이 완료되었습니다.\n관리자의 승인을 기다려주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(SignupThirdActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                }
                            }).show();
                }
            };

            serverCommunicator.execute();
        }
    }
}
