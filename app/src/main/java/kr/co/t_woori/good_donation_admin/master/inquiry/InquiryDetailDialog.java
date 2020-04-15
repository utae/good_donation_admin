package kr.co.t_woori.good_donation_admin.master.inquiry;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.HashMap;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.InquiryDetailDialogBinding;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-09-29.
 */

public class InquiryDetailDialog extends DialogFragment {

    private InquiryDetailDialogBinding binding;

    private Inquiry inquiry;

    private OnAnswerListener onAnswerListener;

    public static InquiryDetailDialog create(Inquiry inquiry){
        InquiryDetailDialog inquiryDetailDialog = new InquiryDetailDialog();
        Bundle args = new Bundle();
        args.putSerializable("inquiry", inquiry);
        inquiryDetailDialog.setArguments(args);
        return inquiryDetailDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inquiry = (Inquiry) getArguments().getSerializable("inquiry");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.inquiry_detail_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.titleTextView.setText(inquiry.getTitle());
        binding.questionTextView.setText(inquiry.getQuestion());

        if(inquiry.getAnswer() != null){
            binding.answerEdt.setText(inquiry.getAnswer());
        }

        binding.answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(binding.answerEdt.getText().toString().trim())){
                    Utilities.showToast(getContext(), "답변을 입력하세요.");
                }else if(inquiry.getAnswer() != null && inquiry.getAnswer().equals(binding.answerEdt.getText().toString().trim())){
                    Utilities.showToast(getContext(), "답변이 등록되었습니다.");
                }else{
                    answer(inquiry.getId(), binding.answerEdt.getText().toString().trim());
                }
            }
        });

        return binding.getRoot();
    }

    public InquiryDetailDialog setOnAnswerListener(OnAnswerListener onAnswerListener) {
        this.onAnswerListener = onAnswerListener;
        return this;
    }

    private void answer(String inquiryId, String answer){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(InquiryAPIService.class).answerQuestion(inquiryId, answer)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "답변이 등록되었습니다.");
                if(onAnswerListener != null){
                    onAnswerListener.onAnswer();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    public interface OnAnswerListener{
        void onAnswer();
    }
}
