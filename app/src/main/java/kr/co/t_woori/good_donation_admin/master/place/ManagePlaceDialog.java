package kr.co.t_woori.good_donation_admin.master.place;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import java.util.HashMap;

import kr.co.t_woori.good_donation_admin.CustomObject.Place;
import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.ManagePlaceDialogBinding;
import kr.co.t_woori.good_donation_admin.master.MasterAPIService;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-10-16.
 */

public class ManagePlaceDialog extends DialogFragment {

    private ManagePlaceDialogBinding binding;
    private Place place;
    private ManagePlaceDialogListener managePlaceDialogListener;

    public static ManagePlaceDialog create(Place place){
        ManagePlaceDialog dialog = new ManagePlaceDialog();
        Bundle args = new Bundle();
        args.putSerializable("place", place);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        place = (Place)getArguments().getSerializable("place");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.manage_place_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.chargeBtn.setOnClickListener(new OnChargeBtnClickListener(place));

        binding.modImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(managePlaceDialogListener != null){
                    managePlaceDialogListener.onModImgBtnClick(place);
                }
                dismiss();
            }
        });

        binding.delImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(place.getImgAmount())){
                    Utilities.showToast(getContext(), "삭제할 이미지가 없습니다.");
                }else{
                    delPlaceImg(place.getIdNum(), place.getImgAmount());
                }
            }
        });

        binding.delPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delPlace(place.getIdNum());
            }
        });

        return binding.getRoot();
    }

    public ManagePlaceDialog setManagePlaceDialogListener(ManagePlaceDialogListener managePlaceDialogListener) {
        this.managePlaceDialogListener = managePlaceDialogListener;
        return this;
    }

    private void delPlaceImg(String idNum, String imgAmount){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(MasterAPIService.class).delPlaceImg(idNum, imgAmount)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "이미지 삭제 완료");
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void delPlace(String idNum){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(MasterAPIService.class).delPlace(idNum)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "기부매장 삭제 완료");
                if(managePlaceDialogListener != null){
                    managePlaceDialogListener.refreshPlaceList();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void chargeBalance(String idNum, String amount){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(MasterAPIService.class).chargePlaceBalance(idNum, amount)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "충전 성공");
                if(managePlaceDialogListener != null){
                    managePlaceDialogListener.refreshPlaceList();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private class OnChargeBtnClickListener implements View.OnClickListener{

        private Place place;

        public OnChargeBtnClickListener(Place place) {
            this.place = place;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setMessage("충전할 금액을 입력해주세요.")
                    .setView(R.layout.charge_amount_dialog)
                    .setPositiveButton("충전", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(dialog instanceof AlertDialog){
                                EditText editText = (EditText)((AlertDialog)dialog).findViewById(R.id.edit_text);
                                if(editText != null && Integer.parseInt(editText.getText().toString().trim()) >= 100){
                                    chargeBalance(place.getIdNum(), editText.getText().toString().trim());
                                }else{
                                    Utilities.showToast(getContext(), "100원 이상의 금액을 입력해주세요.");
                                }
                            }
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    public interface ManagePlaceDialogListener{
        void refreshPlaceList();
        void onModImgBtnClick(Place place);
    }
}
