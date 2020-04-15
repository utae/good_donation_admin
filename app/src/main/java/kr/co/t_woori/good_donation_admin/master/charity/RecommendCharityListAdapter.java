package kr.co.t_woori.good_donation_admin.master.charity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.databinding.CharityChooseRowBinding;

/**
 * Created by rladn on 2017-08-28.
 */

public class RecommendCharityListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Charity> allCharityList;
    private ArrayList<Charity> charityList;
    private HashSet<Charity> selectedCharitySet;
    private boolean isProject;

    public RecommendCharityListAdapter(Context context, ArrayList<Charity> allCharityList, boolean isProject) {
        this.context = context;
        this.allCharityList = allCharityList;
        this.isProject = isProject;
        this.selectedCharitySet = new HashSet<>();
        this.charityList = new ArrayList<>();
        this.charityList.addAll(allCharityList);
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
        CharityChooseRowBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.charity_choose_row, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (CharityChooseRowBinding) convertView.getTag();
        }
        Charity charity = charityList.get(position);
        binding.nameTextView.setText(charity.getName());
        binding.introTextView.setText(charity.getIntroduction());
        binding.registrationTextView.setText(charity.getRegistration());
        binding.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(charity));
        binding.checkBox.setChecked(charity.isRecommend());

        if(isProject){
            int progress = Integer.parseInt(charity.getAccumulation())*100 / Integer.parseInt(charity.getGoal());
            binding.progressBar.setProgress(progress);
            binding.progressTextView.setText(charity.getAccumulation() + " / " + charity.getGoal());
            binding.progressContainer.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public HashSet<String> getAddedCharityIdNum() {
        HashSet<String> addedCharitySet = new HashSet<>();
        for(Charity charity : selectedCharitySet){
            if(!charity.isRecommend()){
                addedCharitySet.add(charity.getIdNum());
            }
        }
        return addedCharitySet;
    }

    public HashSet<String> getRemovedCharityIdNum() {
        HashSet<String> removedCharitySet = new HashSet<>();
        for(Charity charity : allCharityList){
            if(!selectedCharitySet.contains(charity) && charity.isRecommend()){
                removedCharitySet.add(charity.getIdNum());
            }
        }
        return removedCharitySet;
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

    public void showAllCharity(){
        if(!charityList.isEmpty()){
            charityList.clear();
        }
        charityList.addAll(allCharityList);
        notifyDataSetChanged();
    }

    public void showRecommendCharity(){
        if(!charityList.isEmpty()){
            charityList.clear();
        }
        for(Charity charity : allCharityList){
            if(charity.isRecommend()){
                charityList.add(charity);
            }
        }
        notifyDataSetChanged();
    }

    private class OnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{

        private Charity charity;

        public OnCheckedChangeListener(Charity charity) {
            this.charity = charity;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                selectedCharitySet.add(charity);
            }else{
                selectedCharitySet.remove(charity);
            }
        }
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
