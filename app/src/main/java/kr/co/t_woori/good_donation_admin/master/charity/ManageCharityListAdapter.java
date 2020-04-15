package kr.co.t_woori.good_donation_admin.master.charity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.databinding.ManageCharityRowBinding;

/**
 * Created by rladn on 2017-08-28.
 */

public class ManageCharityListAdapter extends BaseAdapter {

    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<Charity> allCharityList;
    private ArrayList<Charity> charityList;
    private ManageCharityDialog.ManageCharityDialogListener manageCharityDialogListener;

    public ManageCharityListAdapter(Context context, FragmentManager fragmentManager, ArrayList<Charity> allCharityList, ManageCharityDialog.ManageCharityDialogListener manageCharityDialogListener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.allCharityList = allCharityList;
        this.charityList = new ArrayList<>();
        this.charityList.addAll(allCharityList);
        this.manageCharityDialogListener = manageCharityDialogListener;
    }

    @Override
    public int getCount() {
        return charityList.size();
    }

    @Override
    public Object getItem(int position) {
        return charityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(charityList.get(position).getIdNum());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ManageCharityRowBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.manage_charity_row, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (ManageCharityRowBinding) convertView.getTag();
        }
        final Charity charity = charityList.get(position);
        binding.nameTextView.setText(charity.getName());

        binding.manageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageCharityDialog.create(charity).setManageCharityDialogListener(manageCharityDialogListener).show(fragmentManager, "manage charity dialog");
            }
        });

        return convertView;
    }

    public void latestSort(){
        Collections.sort(charityList, new LatestSort());
        Collections.sort(allCharityList, new LatestSort());
        notifyDataSetChanged();
    }

    public void popularSort(){
        Collections.sort(charityList, new PopularSort());
        Collections.sort(allCharityList, new PopularSort());
        notifyDataSetChanged();
    }

    private class LatestSort implements Comparator<Charity> {

        @Override
        public int compare(Charity o1, Charity o2) {
            return o2.getRegistration().compareTo(o1.getRegistration());
        }
    }

    private class PopularSort implements Comparator<Charity>{

        @Override
        public int compare(Charity o1, Charity o2) {
            return o2.getFollow().compareTo(o1.getFollow());
        }
    }
}
