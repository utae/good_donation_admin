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

import java.util.HashMap;

import kr.co.t_woori.good_donation_admin.CustomObject.Place;
import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.ManageCharityDialogBinding;
import kr.co.t_woori.good_donation_admin.master.MasterAPIService;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-10-16.
 */

public class ManageCharityDialog extends DialogFragment {

    private ManageCharityDialogBinding binding;
    private Charity charity;
    private ManageCharityDialogListener manageCharityDialogListener;

    public static ManageCharityDialog create(Charity charity){
        ManageCharityDialog dialog = new ManageCharityDialog();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.manage_charity_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.modInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manageCharityDialogListener != null){
                    manageCharityDialogListener.onModInfoBtnClick(charity);
                }
                dismiss();
            }
        });

        binding.modImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manageCharityDialogListener != null){
                    manageCharityDialogListener.onModImgBtnClick(charity);
                }
                dismiss();
            }
        });

        binding.delImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(charity.getImgAmount())){
                    Utilities.showToast(getContext(), "삭제할 이미지가 없습니다.");
                }else{
                    delCharityImg(charity.getIdNum(), charity.getImgAmount());
                }
            }
        });

        binding.delCharityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delCharity(charity.getIdNum());
            }
        });

        return binding.getRoot();
    }

    public ManageCharityDialog setManageCharityDialogListener(ManageCharityDialogListener manageCharityDialogListener) {
        this.manageCharityDialogListener = manageCharityDialogListener;
        return this;
    }

    private void delCharityImg(String idNum, String imgAmount){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(MasterAPIService.class).delCharityImg(idNum, imgAmount)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "이미지 삭제 완료");
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void delCharity(String idNum){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(MasterAPIService.class).delCharity(idNum)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "기부처 삭제 완료");
                if(manageCharityDialogListener != null){
                    manageCharityDialogListener.refreshCharityList();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    public interface ManageCharityDialogListener{
        void refreshCharityList();
        void onModImgBtnClick(Charity charity);
        void onModInfoBtnClick(Charity charity);
    }
}
