package kr.co.t_woori.good_donation_admin.signup;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.databinding.SimpleRowBinding;

/**
 * Created by rladn on 2017-08-10.
 */

public class AddressListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AddressItem> addressList;

    public AddressListAdapter(Context context, ArrayList<AddressItem> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Object getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleRowBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.simple_row, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (SimpleRowBinding) convertView.getTag();
        }

        binding.rowTextView.setText(addressList.get(position).getAddress());

        return convertView;
    }
}
