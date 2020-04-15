package kr.co.t_woori.good_donation_admin.master.charity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.databinding.CharityDetailDialogBinding;
import kr.co.t_woori.good_donation_admin.databinding.ProjectDetailDialogBinding;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-25.
 */

public class ProjectDetailDialog extends DialogFragment {

    private ProjectDetailDialogBinding binding;

    private Charity project;

    static public ProjectDetailDialog create(Charity project){
        ProjectDetailDialog charityDetailDialog = new ProjectDetailDialog();
        Bundle args = new Bundle();
        args.putSerializable("project", project);
        charityDetailDialog.setArguments(args);
        return charityDetailDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        project = (Charity) getArguments().getSerializable("project");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.project_detail_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.titleTextView.setText(project.getName());

        binding.introTextView.setText(project.getIntroduction());

        int progress = (Integer.parseInt(project.getAccumulation())*100)/Integer.parseInt(project.getGoal());

        binding.progressBar.setProgress(progress);

        SpannableStringBuilder builder = new SpannableStringBuilder(progress+"%");

        builder.setSpan(new AbsoluteSizeSpan(14, true), builder.length()-1, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.progressTextView.setText(builder);

        binding.accumTextView.setText(Utilities.convertStringToNumberFormat(project.getAccumulation()));

        binding.goalTextView.setText(Utilities.convertStringToNumberFormat(project.getGoal()));

        binding.followTextView.setText(project.getFollow());

        binding.todayTextView.setText(project.getToday());

        binding.registrationTextView.setText(Utilities.convertStringToDateFormat(project.getRegistration()));

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
