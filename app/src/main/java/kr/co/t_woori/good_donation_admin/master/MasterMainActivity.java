package kr.co.t_woori.good_donation_admin.master;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.ImageUploader;
import kr.co.t_woori.good_donation_admin.databinding.MasterMainActivityBinding;
import kr.co.t_woori.good_donation_admin.master.approval.ApprovalPlaceActivity;
import kr.co.t_woori.good_donation_admin.master.charity.ManageCharityActivity;
import kr.co.t_woori.good_donation_admin.master.charity.CharitySignupActivity;
import kr.co.t_woori.good_donation_admin.master.charity.ProjectSignupActivity;
import kr.co.t_woori.good_donation_admin.master.charity.RecommendCharityActivity;
import kr.co.t_woori.good_donation_admin.master.inquiry.InquiryActivity;
import kr.co.t_woori.good_donation_admin.master.place.ManagePlaceActivity;
import kr.co.t_woori.good_donation_admin.setting.SettingActivity;
import kr.co.t_woori.good_donation_admin.utilities.BackPressCloseSystem;

public class MasterMainActivity extends AppCompatActivity {

    private MasterMainActivityBinding binding;
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.master_main_activity);

        init();
    }

    private void init(){
        initDrawer();
        backPressCloseSystem = new BackPressCloseSystem(this);
    }

    private void initDrawer(){
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        initMenu();
    }

    private void initMenu(){
        binding.approvalPlace.setOnClickListener(new OnMenuClickListener());
        binding.charitySignup.setOnClickListener(new OnMenuClickListener());
        binding.projectSignup.setOnClickListener(new OnMenuClickListener());
        binding.recommendCharity.setOnClickListener(new OnMenuClickListener());
        binding.manageCharity.setOnClickListener(new OnMenuClickListener());
        binding.managePlace.setOnClickListener(new OnMenuClickListener());
        binding.homeBanner.setOnClickListener(new OnMenuClickListener());
        binding.manageInquiry.setOnClickListener(new OnMenuClickListener());
        binding.setting.setOnClickListener(new OnMenuClickListener());
    }

    @Override
    public void onBackPressed() {
        backPressCloseSystem.onBackPressed();
    }

    private class OnMenuClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.approval_place :
                    intent = new Intent(MasterMainActivity.this, ApprovalPlaceActivity.class);
                    startActivity(intent);
                    break;

                case R.id.charity_signup :
                    intent = new Intent(MasterMainActivity.this, CharitySignupActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;

                case R.id.project_signup :
                    intent = new Intent(MasterMainActivity.this, ProjectSignupActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;

                case R.id.recommend_charity :
                    intent = new Intent(MasterMainActivity.this, RecommendCharityActivity.class);
                    startActivity(intent);
                    break;

                case R.id.manage_place :
                    intent = new Intent(MasterMainActivity.this, ManagePlaceActivity.class);
                    startActivity(intent);
                    break;

                case R.id.manage_charity :
                    intent = new Intent(MasterMainActivity.this, ManageCharityActivity.class);
                    startActivity(intent);
                    break;

                case R.id.home_banner :
                    intent = new Intent(MasterMainActivity.this, ImageUploader.class);
                    intent.putExtra("type", "H");
                    intent.putExtra("idNum", "0");
                    startActivity(intent);
                    break;

                case R.id.manage_inquiry :
                    intent = new Intent(MasterMainActivity.this, InquiryActivity.class);
                    startActivity(intent);
                    break;

                case R.id.setting :
                    intent = new Intent(MasterMainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
