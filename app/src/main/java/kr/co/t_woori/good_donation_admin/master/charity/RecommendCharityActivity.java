package kr.co.t_woori.good_donation_admin.master.charity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.RecommendCharityActivityBinding;
import kr.co.t_woori.good_donation_admin.master.MasterAPIService;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-28.
 */

public class RecommendCharityActivity extends AppCompatActivity {

    private RecommendCharityActivityBinding binding;

    private TabHost tabHost;
    private ArrayList<Charity> allCharityList;
    private ArrayList<Charity> allProjectList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.recommend_charity_activity);

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

        charityPageInit();
        projectPageInit();
    }

    private void addTab(String tag, @IdRes int contentId, String indicator){
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setContent(contentId);
        tabSpec.setIndicator(indicator);
        tabHost.addTab(tabSpec);
    }

    private void charityPageInit(){
        allCharityList = new ArrayList<>();
        binding.charityPage.sortBtn.setOnCheckedChangeListener(new OnSortButtonToggleListener(binding.charityPage.listView));
        binding.charityPage.filterBtn.setOnCheckedChangeListener(new OnFilterButtonToggleListener(binding.charityPage.listView));
        binding.charityPage.saveBtn.setOnClickListener(new OnSaveButtonClickListener(binding.charityPage.listView, false));
        getAllCharity();
    }

    private void projectPageInit(){
        allProjectList = new ArrayList<>();
        binding.projectPage.sortBtn.setOnCheckedChangeListener(new OnSortButtonToggleListener(binding.projectPage.listView));
        binding.projectPage.filterBtn.setOnCheckedChangeListener(new OnFilterButtonToggleListener(binding.projectPage.listView));
        binding.projectPage.saveBtn.setOnClickListener(new OnSaveButtonClickListener(binding.projectPage.listView, true));
        getAllProject();
    }

    private void initListView(ListView listView, ArrayList<Charity> charityList, boolean isProject){
        RecommendCharityListAdapter listAdapter;
        if(isProject){
            listAdapter = new RecommendCharityListAdapter(this, charityList, true);
            listView.setOnItemClickListener(new OnItemClickListener(true));
        }else{
            listAdapter = new RecommendCharityListAdapter(this, charityList, false);
            listView.setOnItemClickListener(new OnItemClickListener(false));
        }
        if(binding.charityPage.sortBtn.isChecked()){
            listAdapter.popularSort();
        }
        if(binding.charityPage.filterBtn.isChecked()){
            listAdapter.showRecommendCharity();
        }
        listView.setAdapter(listAdapter);
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

    private void modRecommendCharity(HashSet<String> addedList, HashSet<String> removedList, final boolean isProject){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(MasterAPIService.class).modRecommendCharity(addedList.toString(), removedList.toString())
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(RecommendCharityActivity.this, "저장되었습니다.");
                if(isProject){
                    getAllProject();
                }else{
                    getAllCharity();
                }
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

    private class OnFilterButtonToggleListener implements CompoundButton.OnCheckedChangeListener{

        private ListView listView;

        public OnFilterButtonToggleListener(ListView listView) {
            this.listView = listView;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(listView.getAdapter() != null){
                if(isChecked){
                    if(listView.getAdapter() instanceof RecommendCharityListAdapter){
                        ((RecommendCharityListAdapter)listView.getAdapter()).showRecommendCharity();
                    }
                }else{
                    if(listView.getAdapter() instanceof RecommendCharityListAdapter){
                        ((RecommendCharityListAdapter)listView.getAdapter()).showAllCharity();
                    }
                }
            }
        }
    }

    private class OnSaveButtonClickListener implements View.OnClickListener{

        private ListView listView;
        boolean isProject;

        public OnSaveButtonClickListener(ListView listView, boolean isProject) {
            this.listView = listView;
            this.isProject = isProject;
        }

        @Override
        public void onClick(View v) {
            if(listView.getAdapter() instanceof RecommendCharityListAdapter){
                modRecommendCharity(((RecommendCharityListAdapter)listView.getAdapter()).getAddedCharityIdNum(), ((RecommendCharityListAdapter)listView.getAdapter()).getRemovedCharityIdNum(), isProject);
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
