package kr.co.t_woori.good_donation_admin.master.charity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ImageUploader;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.ManageCharityActivityBinding;
import kr.co.t_woori.good_donation_admin.master.MasterAPIService;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-30.
 */

public class ManageCharityActivity extends AppCompatActivity {

    private ManageCharityActivityBinding binding;

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private TabHost tabHost;
    private ArrayList<Charity> allCharityList;
    private ArrayList<Charity> allProjectList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.manage_charity_activity);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabHost = binding.tabHost;

        tabHost.setup();

        addTab("charity tab", R.id.charity_page, "기부처");
        addTab("project tab", R.id.project_page, "프로젝트");

        for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++){
            ((TextView)((ViewGroup) tabHost.getTabWidget().getChildTabViewAt(i)).getChildAt(1)).setTextColor(Color.BLACK);
        }

        pageInit();
    }

    private void addTab(String tag, @IdRes int contentId, String indicator){
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setContent(contentId);
        tabSpec.setIndicator(indicator);
        tabHost.addTab(tabSpec);
    }

    private void pageInit(){
        allCharityList = new ArrayList<>();
        allProjectList = new ArrayList<>();
        binding.charityPage.sortBtn.setOnCheckedChangeListener(new OnSortButtonToggleListener(binding.charityPage.listView));
        binding.projectPage.sortBtn.setOnCheckedChangeListener(new OnSortButtonToggleListener(binding.projectPage.listView));
        getAllCharity();
        getAllProject();
    }

    private void initListView(ListView listView, ArrayList<Charity> charityList, final boolean isProject){
        ManageCharityListAdapter listAdapter = new ManageCharityListAdapter(this, getSupportFragmentManager(), charityList, new ManageCharityDialog.ManageCharityDialogListener() {
            @Override
            public void refreshCharityList() {
                getAllCharity();
                getAllProject();
            }

            @Override
            public void onModImgBtnClick(Charity charity) {
                if(isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    uploadImg(charity.getIdNum());
                }else{
                    requestPermission();
                }
            }

            @Override
            public void onModInfoBtnClick(Charity charity) {
                CharityInfoDialog.create(charity).setOnCharityInfoModifiedListener(new CharityInfoDialog.OnCharityInfoModifiedListener() {
                    @Override
                    public void onCharityInfoModified() {
                        getAllCharity();
                        getAllProject();
                    }
                }).show(getSupportFragmentManager(), "charity info dialog");
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener(isProject));
        if(binding.charityPage.sortBtn.isChecked()){
            listAdapter.popularSort();
        }
        listView.setAdapter(listAdapter);
    }

    private void uploadImg(String charityIdNum){
        Intent intent = new Intent(this, ImageUploader.class);
        intent.putExtra("idNum", charityIdNum);
        intent.putExtra("type", "C");
        startActivity(intent);
    }

    private boolean isPermissionGranted(String permission){
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //pass
                }else{
                    Utilities.showToast(this, "권한이 없습니다.");
                }
        }
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    private void getAllCharity(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(MasterAPIService.class).getAllCharity()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!allCharityList.isEmpty()){
                    allCharityList.clear();
                }
                if(results.get("allCharity") instanceof List){
                    Charity charity;
                    for(Object json : (List)results.get("allCharity")){
                        if(json instanceof Map){
                            Map map = (Map)json;
                            String idNum = (String)map.get("idNum");
                            String name = (String)map.get("name");
                            String introduction = (String)map.get("introduction");
                            String appreciation = (String)map.get("appreciation");
                            String registration = (String)map.get("registration");
                            String follow = (String)map.get("follow");
                            String today = (String)map.get("today");
                            String accumulation = (String)map.get("accumulation");
                            boolean recommend = "Y".equals(map.get("recommend"));
                            String imgAmount = (String)map.get("img");
                            charity = new Charity(idNum, name, introduction, appreciation, registration, follow, today, accumulation, recommend, imgAmount);
                            allCharityList.add(charity);
                        }
                    }
                    initListView(binding.charityPage.listView, allCharityList, false);
                }
            }
        };
        serverCommunicator.execute();
    }

    private void getAllProject(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(MasterAPIService.class).getAllProject()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!allProjectList.isEmpty()){
                    allProjectList.clear();
                }
                if(results.get("allProject") instanceof List){
                    if(Integer.parseInt((String)results.get("allCount")) > 0){
                        for(Object project : (List)results.get("allProject")){
                            if(project instanceof Map){
                                Map map = (Map)project;
                                String idNum = (String)map.get("idNum");
                                String name = (String)map.get("name");
                                String introduction = (String)map.get("introduction");
                                String appreciation = (String)map.get("appreciation");
                                String registration = (String)map.get("registration");
                                String follow = (String)map.get("follow");
                                String today = (String)map.get("today");
                                String accumulation = (String)map.get("accumulation");
                                boolean recommend = "Y".equals(map.get("recommend"));
                                String imgAmount = (String)map.get("img");
                                String goal = (String)map.get("goal");
                                allProjectList.add(new Charity(idNum, name, introduction, appreciation, registration, follow, today, accumulation, recommend, imgAmount, goal));
                            }
                        }
                    }
                    initListView(binding.projectPage.listView, allProjectList, true);
                }
            }
        };
        serverCommunicator.execute();
    }

    private void delCharityImg(String idNum, String imgAmount){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(MasterAPIService.class).delCharityImg(idNum, imgAmount)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(ManageCharityActivity.this, "이미지 삭제 완료");
            }
        };
        serverCommunicator.execute();
    }

    private class OnSortButtonToggleListener implements CompoundButton.OnCheckedChangeListener{

        private ListView listView;

        public OnSortButtonToggleListener(ListView listView) {
            this.listView = listView;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(listView.getAdapter() != null){
                if(isChecked){
                    if(listView.getAdapter() instanceof RecommendCharityListAdapter){
                        ((RecommendCharityListAdapter)listView.getAdapter()).popularSort();
                    }
                }else{
                    if(listView.getAdapter() instanceof RecommendCharityListAdapter){
                        ((RecommendCharityListAdapter)listView.getAdapter()).latestSort();
                    }
                }
            }
        }
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener{

        private boolean isProject;

        public OnItemClickListener(boolean isProject) {
            this.isProject = isProject;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getAdapter() != null && parent.getAdapter().getItem(position) instanceof Charity){
                if(isProject){
                    ProjectDetailDialog.create((Charity)parent.getAdapter().getItem(position)).show(getSupportFragmentManager(), "ProjectDetailDialog");
                }else{
                    CharityDetailDialog.create((Charity)parent.getAdapter().getItem(position)).show(getSupportFragmentManager(), "CharityDetailDialog");
                }
            }
        }
    }
}
