package kr.co.t_woori.good_donation_admin.place.tag;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.NfcActivityBinding;
import kr.co.t_woori.good_donation_admin.place.PlaceAPIService;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-11.
 */

public class NfcActivity extends AppCompatActivity{

    private NfcActivityBinding binding;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private Tag tag = null;
    private String amount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.nfc_activity);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        checkNfcStatus(nfcAdapter);

        binding.generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = binding.amountEdt.getText().toString().trim();
                if(!"".equals(amount) && Integer.parseInt(amount) >= 100){
                    Utilities.hideKeyboard(NfcActivity.this, binding.amountEdt);
                    binding.messageTextView.setVisibility(View.VISIBLE);
                }else{
                    Utilities.showToast(NfcActivity.this, "최소금액은 100원입니다.");
                }
            }
        });

        Intent intent = new Intent(NfcActivity.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(NfcActivity.this, 0, intent, 0);
    }

    private void checkNfcStatus(NfcAdapter nfcAdapter){
        if(!nfcAdapter.isEnabled()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("NFC를 활성화해주세요.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
                        startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                    }else{
                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
            }).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if(tag != null && amount != null){
            getNfcToken(amount);
        }
    }

    private NdefMessage getTextAsNdef(String token) {

        byte[] textBytes = token.getBytes();

        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                "text/plain".getBytes(),
                new byte[] {},
                textBytes);

        return new NdefMessage(new NdefRecord[] {textRecord});
    }

    public void writeTag(NdefMessage message, Tag tag) {

        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Utilities.showToast(this, "쓰기 불가능한 태그입니다.");
                }else if(ndef.getMaxSize() < size){
                    Utilities.logD("Test", "Max Size : " + ndef.getMaxSize());
                    Utilities.logD("Test", "Tag Size : " + size);
                    Utilities.showToast(this, "태그의 용량이 부족합니다.");
                }else{
                    ndef.writeNdefMessage(message);
                    Utilities.showToast(this, "태그 쓰기 성공");
                    finish();
                }
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        Utilities.showToast(this, "태그 쓰기 성공");
                        finish();

                    } catch (IOException e) {
                        Utilities.showToast(this, "NFC 쓰기 실패");
                    }
                }else{
                    Utilities.showToast(this, "NFC 쓰기 실패");
                }
            }
        } catch (Exception e) {
            Utilities.showToast(this, "NFC 쓰기 실패");
        }
    }

    private void getNfcToken(String amount){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(PlaceAPIService.class).getTagToken(amount)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                String token = (String)results.get("tagToken");
                JSONObject jsonObject = new JSONObject();
                try {
                    if(token != null){
                        jsonObject.put("provider", "donation");
                        jsonObject.put("token", token);
                        writeTag(getTextAsNdef(jsonObject.toString()), tag);
                    }else{
                        throw new JSONException("JSON Exception");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utilities.showToast(NfcActivity.this, "NFC 태그 등록 실패");
                }
            }
        };

        serverCommunicator.execute();
    }

    @Override
    protected void onPause() {
        if(nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(nfcAdapter != null){
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }
}
