package kr.co.t_woori.good_donation_admin.master.approval;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.databinding.SimpleToggleRowBinding;
import kr.co.t_woori.good_donation_admin.CustomObject.Place;

/**
 * Created by rladn on 2017-08-10.
 */

public class ApprovalPlaceListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Place> placeList;

    public ApprovalPlaceListAdapter(Context context, ArrayList<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
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
        return Integer.parseInt(placeList.get(position).getIdNum());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleToggleRowBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.simple_toggle_row, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (SimpleToggleRowBinding) convertView.getTag();
        }

        binding.rowTextView.setText(placeList.get(position).getPlaceName());

        return convertView;
    }
}
