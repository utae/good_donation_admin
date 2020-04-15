package kr.co.t_woori.good_donation_admin.master.place;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.t_woori.good_donation_admin.CustomObject.Place;
import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.ChargeAmountDialogBinding;
import kr.co.t_woori.good_donation_admin.databinding.PlaceRowBinding;
import kr.co.t_woori.good_donation_admin.master.MasterAPIService;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-29.
 */

public class PlaceListAdapter extends BaseAdapter{

    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<Place> placeList;
    private ManagePlaceDialog.ManagePlaceDialogListener managePlaceDialogListener;


    public PlaceListAdapter(Context context, FragmentManager fragmentManager, ArrayList<Place> placeList, ManagePlaceDialog.ManagePlaceDialogListener managePlaceDialogListener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.placeList = placeList;
        this.managePlaceDialogListener = managePlaceDialogListener;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(placeList.get(position).getIdNum());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PlaceRowBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.place_row, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (PlaceRowBinding) convertView.getTag();
        }

        Place place = placeList.get(position);

        binding.nameTextView.setText(place.getPlaceName() + " (" + place.getId() + ")");
        binding.addressTextView.setText(place.getAddress());
        binding.balanceTextView.setText(Utilities.convertStringToNumberFormat(place.getBalance()) + "원");
        binding.manageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagePlaceDialog.create(placeList.get(position)).setManagePlaceDialogListener(managePlaceDialogListener).show(fragmentManager, "manage place dialog");
            }
        });

        return convertView;
    }
}
