package kr.co.t_woori.good_donation_admin.master.charity;

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
import android.widget.EditText;

import java.util.HashMap;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.CharityInfoDialogBinding;
import kr.co.t_woori.good_donation_admin.master.MasterAPIService;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-10-11.
 */

public class CharityInfoDialog extends DialogFragment {

    private CharityInfoDialogBinding binding;
    private Charity charity;
    private OnCharityInfoModifiedListener onCharityInfoModifiedListener;

    public static CharityInfoDialog create(Charity charity){
        CharityInfoDialog dialog = new CharityInfoDialog();
        Bundle args = new Bundle();
        args.putSerializable("charity", charity);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        charity = (Charity)getArguments().getSerializable("charity");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.charity_info_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        initCharityData();

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllDataValid()){
                    if(isDataModified()){
                        saveCharityInfo(charity.getIdNum(),
                                binding.nameEdt.getText().toString().trim(),
                                binding.introEdt.getText().toString().trim(),
                                binding.appreciationEdt.getText().toString().trim());
                    }else{
                        Utilities.showToast(getContext(), "수정 완료");
                        dismiss();
                    }
                }
            }
        });

        return binding.getRoot();
    }

    private void initCharityData(){
        binding.nameEdt.setText(charity.getName());
        binding.introEdt.setText(charity.getIntroduction());
        binding.appreciationEdt.setText(charity.getAppreciationPhrase());
    }

    private boolean isFilled(EditText editText){
        return !"".equals(editText.getText().toString().trim());
    }

    private boolean isAllDataValid(){
        if(!isFilled(binding.nameEdt)){
            Utilities.showToast(getContext(), "기부처명을 입력해주세요.");
        }else if(!isFilled(binding.introEdt)){
            Utilities.showToast(getContext(), "기부처 한줄 소개를 입력해주세요.");
        }else if(!isFilled(binding.appreciationEdt)){
            Utilities.showToast(getContext(), "기부처 감사 문구를 입력해주세요.");
        }else{
            return true;
        }
        return false;
    }

    private boolean isDataModified(){
        if(!charity.getName().equals(binding.nameEdt.getText().toString().trim())){
            return true;
        }else if(!charity.getIntroduction().equals(binding.introEdt.getText().toString().trim())){
            return true;
        }else if(!charity.getAppreciationPhrase().equals(binding.appreciationEdt.getText().toString().trim())){
            return true;
        }
        return false;
    }

    private void saveCharityInfo(String idNum, String name, String intro, String appreciation){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(MasterAPIService.class).modCharityInfo(idNum, name, intro, appreciation)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "수정 완료");
                if(onCharityInfoModifiedListener != null){
                    onCharityInfoModifiedListener.onCharityInfoModified();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    public CharityInfoDialog setOnCharityInfoModifiedListener(OnCharityInfoModifiedListener onCharityInfoModifiedListener) {
        this.onCharityInfoModifiedListener = onCharityInfoModifiedListener;
        return this;
    }

    public interface OnCharityInfoModifiedListener{
        void onCharityInfoModified();
    }
}
