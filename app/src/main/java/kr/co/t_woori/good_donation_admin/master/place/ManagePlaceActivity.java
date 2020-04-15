package kr.co.t_woori.good_donation_admin.master.place;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation_admin.CustomObject.Place;
import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ImageUploader;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.ManagePlaceActivityBinding;
import kr.co.t_woori.good_donation_admin.master.MasterAPIService;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-29.
 */

public class ManagePlaceActivity extends AppCompatActivity{

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private ManagePlaceActivityBinding binding;
    private ArrayList<Place> placeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.manage_place_activity);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        placeList = new ArrayList<>();

        getPlaceList();
    }

    private void initListView(){
        PlaceListAdapter listAdapter = new PlaceListAdapter(this, getSupportFragmentManager(), placeList, new ManagePlaceDialog.ManagePlaceDialogListener() {
            @Override
            public void refreshPlaceList() {
                getPlaceList();
            }

            @Override
            public void onModImgBtnClick(Place place) {
                if(isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    uploadImg(place.getIdNum());
                }else{
                    requestPermission();
                }
            }
        });
        binding.listView.setAdapter(listAdapter);
    }

    private void uploadImg(String idNum){
        Intent intent = new Intent(this, ImageUploader.class);
        intent.putExtra("idNum", idNum);
        intent.putExtra("type", "P");
        startActivity(intent);
    }

    private void getPlaceList(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(MasterAPIService.class).getSimplePlaceInfo("Y")
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
                            placeList.add(new Place((String)map.get("idNum"), (String)map.get("id"), (String)map.get("name"), (String)map.get("address"), (String)map.get("balance"), (String)map.get("img")));
                        }
                    }
                    initListView();
                }
            }
        };

        serverCommunicator.execute();
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
}
