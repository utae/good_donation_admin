package kr.co.t_woori.good_donation_admin.master.charity;

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

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ImageUploader;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.CharitySignupActivityBinding;
import kr.co.t_woori.good_donation_admin.master.MasterAPIService;
import kr.co.t_woori.good_donation_admin.utilities.ScrollView;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-22.
 */

public class CharitySignupActivity extends AppCompatActivity {

    private CharitySignupActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.charity_signup_activity);

        initScrollView();

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllDataValid()){
                    charitySignup(getCharityInfo());
                }
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

    private boolean isFilled(EditText editText){
        return !"".equals(editText.getText().toString().trim());
    }

    private boolean isAllDataValid(){
        if(!isFilled(binding.nameEdt)){
            Utilities.showToast(this, "기부처명을 입력해주세요.");
        }else if(!isFilled(binding.introEdt)){
            Utilities.showToast(this, "기부처 한줄 소개를 입력해주세요.");
        }else if(!isFilled(binding.appreciationEdt)){
            Utilities.showToast(this, "기부처 감사 문구를 입력해주세요.");
        }else{
            return true;
        }
        return false;
    }

    private HashMap<String, String> getCharityInfo(){
        HashMap<String, String> charityInfo = new HashMap<>();
        charityInfo.put("name", binding.nameEdt.getText().toString().trim());
        charityInfo.put("introduction", binding.introEdt.getText().toString().trim());
        charityInfo.put("appreciation", binding.appreciationEdt.getText().toString().trim());
        return charityInfo;
    }

    private void charitySignup(HashMap<String, String> charityInfo){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(MasterAPIService.class).charitySignup(charityInfo)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                final String idNum = (String)results.get("idNum");
                AlertDialog.Builder builder = new AlertDialog.Builder(CharitySignupActivity.this);
                builder.setTitle("기부처 등록 완료")
                        .setMessage("기부처 이미지를 바로 등록하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(CharitySignupActivity.this, ImageUploader.class);
                                intent.putExtra("idNum", idNum);
                                intent.putExtra("type", "C");
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }
        };
        serverCommunicator.execute();
    }
}
