package kr.co.t_woori.good_donation_admin.master.charity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.databinding.CharityDetailDialogBinding;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-25.
 */

public class CharityDetailDialog extends DialogFragment {

    private CharityDetailDialogBinding binding;

    private Charity charity;

    static public CharityDetailDialog create(Charity charity){
        CharityDetailDialog charityDetailDialog = new CharityDetailDialog();
        Bundle args = new Bundle();
        args.putSerializable("charity", charity);
        charityDetailDialog.setArguments(args);
        return charityDetailDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        charity = (Charity) getArguments().getSerializable("charity");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.charity_detail_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.titleTextView.setText(charity.getName());

        binding.introTextView.setText(charity.getIntroduction());

        binding.accumTextView.setText(Utilities.convertStringToNumberFormat(charity.getAccumulation()));

        binding.followTextView.setText(charity.getFollow());

        binding.todayTextView.setText(charity.getToday());

        binding.registrationTextView.setText(Utilities.convertStringToDateFormat(charity.getRegistration()));

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getDialog() == null){
            return;
        }
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
