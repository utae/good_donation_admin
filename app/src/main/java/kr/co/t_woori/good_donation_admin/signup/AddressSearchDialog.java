package kr.co.t_woori.good_donation_admin.signup;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation_admin.CustomObject.Place;
import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.AddressSearchDialogBinding;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-10.
 */

public class AddressSearchDialog extends DialogFragment {

    private AddressSearchDialogBinding binding;
    private ArrayList<AddressItem> addressItemList;
    private AddressListAdapter addressListAdapter;
    private AdapterView.OnItemClickListener onItemClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.address_search_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        addressItemList = new ArrayList<>();

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAddress();
            }
        });

        return binding.getRoot();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void initListView(){
        addressListAdapter = new AddressListAdapter(getContext(), addressItemList);
        binding.addressListView.setAdapter(addressListAdapter);
        if(onItemClickListener != null){
            binding.addressListView.setOnItemClickListener(onItemClickListener);
        }
        Utilities.hideKeyboard(getContext(), binding.addressEdt);
    }

    private void searchAddress(){
        String address = binding.addressEdt.getText().toString().trim();
        if("".equals(address)){
            Utilities.showToast(getContext(), "검색할 주소를 입력해주세요.");
        }else if(!Utilities.isValidAddress(address)){
            Utilities.showToast(getContext(), "번지수 또는 건물주소를 입력해주세요.\n예)둔산동 1424(O) 둔산동 1424번지(X)");
        }else{
            ServerCommunicator serverCommunicator = new ServerCommunicator(
                    getContext(), APICreator.create(SignupAPIService.class).searchAddress(address)
            ) {
                @Override
                protected void onSuccess(HashMap<String, Object> results) {
                    if(!addressItemList.isEmpty()){
                        addressItemList.clear();
                    }
                    if(results.get("location") instanceof List){
                        for(Object address : (List)results.get("location")){
                            if(address instanceof Map){
                                Map map = (Map)address;
                                addressItemList.add(new AddressItem((String)map.get("address"), (String)map.get("coordinate")));
                            }
                        }

                        initListView();
                    }
                }
            };

            serverCommunicator.execute();
        }
    }
}
