package kr.co.t_woori.good_donation_admin.signup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.HashMap;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.databinding.SignupFirstActivityBinding;
import kr.co.t_woori.good_donation_admin.login.LoginActivity;
import kr.co.t_woori.good_donation_admin.utilities.ScrollView;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by utae on 2017-07-26.
 */

public class SignupFirstActivity extends AppCompatActivity {

    private SignupFirstActivityBinding binding;

    private HashMap<String, String> signupInfo;

    private AddressSearchDialog addressSearchDialog;

    private String coordinate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signup_first_activity);

        initScrollView();

        signupInfo = new HashMap<>();

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllFormFilled()){
                    signupInfo.put("placeName", binding.placeNameEdt.getText().toString().trim());
                    signupInfo.put("placePhone", binding.placePhoneEdt.getText().toString().trim());
                    signupInfo.put("address", binding.addressTextView.getText().toString().trim() + " " + binding.detailAddressEdt.getText().toString().trim());
                    //TODO 네이버 api에서 받아온 좌표넣기
                    signupInfo.put("location", coordinate);
                    Intent intent = new Intent(SignupFirstActivity.this, SignupSecondActivity.class);
                    intent.putExtra("signupInfo", signupInfo);
                    startActivity(intent);
                }
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupFirstActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        binding.addressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAddress();
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

    private void searchAddress(){
        addressSearchDialog = new AddressSearchDialog();
        addressSearchDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressItem addressItem = (AddressItem)parent.getItemAtPosition(position);
                binding.addressTextView.setText(addressItem.getAddress());
                coordinate = addressItem.getCoordinate();
                addressSearchDialog.dismiss();
            }
        });
        addressSearchDialog.show(getSupportFragmentManager(), "addressSearchDialog");
    }

    private boolean isAllFormFilled(){
        if(!isFilled(binding.placeNameEdt)){
            Utilities.showToast(this, "플레이스 이름을 입력하세요.");
        }else if(!isFilled(binding.placePhoneEdt, 9)){
            Utilities.showToast(this, "플레이스 전화번호를 확인해주세요.");
        }else if("플레이스 주소".equals(binding.addressTextView.getText().toString().trim())){
            Utilities.showToast(this, "플레이스 주소를 입력하세요.");
        }else if(coordinate == null || "".equals(coordinate)){
            Utilities.showToast(this, "주소의 좌표를 가져오는데 실패하였습니다.\n다시 시도해주세요.");
        }else{
            return true;
        }
        return false;
    }

    private boolean isFilled(EditText editText){
        return isFilled(editText, 1);
    }

    private boolean isFilled(EditText editText, int length){
        return editText.getText().toString().trim().length() >= length;
    }

    @Override
    public void onBackPressed() {
        binding.cancelBtn.performClick();
    }
}
