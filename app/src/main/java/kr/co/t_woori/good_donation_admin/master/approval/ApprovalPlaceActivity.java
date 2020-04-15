package kr.co.t_woori.good_donation_admin.master.approval;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.ApprovalPlaceActivityBinding;
import kr.co.t_woori.good_donation_admin.master.MasterAPIService;
import kr.co.t_woori.good_donation_admin.CustomObject.Place;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-10.
 */

public class ApprovalPlaceActivity extends AppCompatActivity {

    private ApprovalPlaceActivityBinding binding;

    private ApprovalPlaceListAdapter listAdapter;

    private ArrayList<Place> placeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.approval_place_activity);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        placeList = new ArrayList<>();

        getPlaceList();

        binding.allSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listAdapter != null){
                    for(int i = 0; i < listAdapter.getCount(); i++){
                        binding.placeListView.setItemChecked(i, true);
                    }
                }
            }
        });

        binding.allUnselectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listAdapter != null){
                    for(int i = 0; i < listAdapter.getCount(); i++){
                        binding.placeListView.setItemChecked(i, false);
                    }
                }
            }
        });

        binding.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listAdapter != null) {
                    ArrayList<String> placeIdList = new ArrayList<>();
                    SparseBooleanArray selectedPlaceList = binding.placeListView.getCheckedItemPositions();
                    for(int i = 0; i < selectedPlaceList.size(); i++){
                        if(selectedPlaceList.valueAt(i)){
                            placeIdList.add(((Place)binding.placeListView.getItemAtPosition(selectedPlaceList.keyAt(i))).getIdNum());
                        }
                    }
                    if(placeIdList.size() > 0){
                        approvePlace(placeIdList);
                    }else{
                        Utilities.showToast(ApprovalPlaceActivity.this, "선택된 기부매장이 없습니다.");
                    }
                }
            }
        });

        binding.disapproveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listAdapter != null) {
                    ArrayList<String> placeIdList = new ArrayList<>();
                    SparseBooleanArray selectedPlaceList = binding.placeListView.getCheckedItemPositions();
                    for(int i = 0; i < selectedPlaceList.size(); i++){
                        if(selectedPlaceList.valueAt(i)){
                            placeIdList.add(((Place)binding.placeListView.getItemAtPosition(selectedPlaceList.keyAt(i))).getIdNum());
                        }
                    }
                    if(placeIdList.size() > 0){
                        disapprovePlace(placeIdList);
                    }else{
                        Utilities.showToast(ApprovalPlaceActivity.this, "선택된 기부매장이 없습니다.");
                    }
                }
            }
        });

    }

    private void initListView(){
        listAdapter = new ApprovalPlaceListAdapter(this, placeList);
        binding.placeListView.setAdapter(listAdapter);

        binding.placeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
    }

    private void getPlaceList(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(MasterAPIService.class).getSimplePlaceInfo("R")
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!placeList.isEmpty()){
                    placeList.clear();
                }
                if(results.get("place") instanceof List){
                    for(Object place : (List)results.get("place")){
                        if(place instanceof Map){
                            Map map = (Map)place;
                            placeList.add(new Place((String)map.get("idNum"), (String)map.get("name")));
                        }
                    }

                    initListView();
                }
            }
        };

        serverCommunicator.execute();
    }

    private void approvePlace(ArrayList<String> placeIdList){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(MasterAPIService.class).approvePlace(placeIdList.toString())
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                getPlaceList();
            }
        };
        serverCommunicator.execute();
    }

    private void disapprovePlace(ArrayList<String> placeIdList){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(MasterAPIService.class).disapprovePlace(placeIdList.toString())
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                getPlaceList();
            }
        };
        serverCommunicator.execute();
    }
}
