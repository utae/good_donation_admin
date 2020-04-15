package kr.co.t_woori.good_donation_admin.place;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.PlaceMainActivityBinding;
import kr.co.t_woori.good_donation_admin.place.tag.NfcActivity;
import kr.co.t_woori.good_donation_admin.place.tag.QrActivity;
import kr.co.t_woori.good_donation_admin.setting.SettingActivity;
import kr.co.t_woori.good_donation_admin.utilities.BackPressCloseSystem;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-11.
 */

public class PlaceMainActivity extends AppCompatActivity {

    private PlaceMainActivityBinding binding;
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.place_main_activity);

        getMyBalance();

        init();
    }

    private void init(){
        backPressCloseSystem = new BackPressCloseSystem(this);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        binding.generateQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceMainActivity.this, QrActivity.class);
                startActivity(intent);
            }
        });

        binding.registNfcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNfcExisting()){
                    Intent intent = new Intent(PlaceMainActivity.this, NfcActivity.class);
                    startActivity(intent);
                }else{
                    Utilities.showToast(PlaceMainActivity.this, "NFC를 지원하지 않는 단말기입니다.");
                }
            }
        });

        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceMainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseSystem.onBackPressed();
    }

    private boolean isNfcExisting(){
        return NfcAdapter.getDefaultAdapter(this) != null;
    }

    private void getMyBalance(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(PlaceAPIService.class).getMyBalance()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                String balance = (String)results.get("balance");
                binding.balanceTextView.setText("플레이스 기부 잔액 : " + balance + "원");
            }
        };

        serverCommunicator.execute();
    }
}
