package kr.co.t_woori.good_donation_admin.place.tag;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.good_donation_admin.R;
import kr.co.t_woori.good_donation_admin.communication.APICreator;
import kr.co.t_woori.good_donation_admin.communication.ServerCommunicator;
import kr.co.t_woori.good_donation_admin.databinding.QrActivityBinding;
import kr.co.t_woori.good_donation_admin.place.PlaceAPIService;
import kr.co.t_woori.good_donation_admin.utilities.Utilities;

/**
 * Created by rladn on 2017-08-11.
 */

public class QrActivity extends AppCompatActivity{

    private QrActivityBinding binding;
    private Bitmap qrCode;
    private final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.qr_activity);

        binding.generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = binding.amountEdt.getText().toString().trim();
                if(!"".equals(amount) && Integer.parseInt(amount) >= 100){
                    Utilities.hideKeyboard(QrActivity.this, binding.amountEdt);
                    generateQr(amount);
                }else{
                    Utilities.showToast(QrActivity.this, "최소금액은 100원입니다.");
                }
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    saveQrToGallery(qrCode);
                }else{
                    requestPermission();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    saveQrToGallery(qrCode);
                }else{
                    Utilities.showToast(this, "권한이 없습니다.");
                }
        }
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(QrActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(QrActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }else{
            ActivityCompat.requestPermissions(QrActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    private boolean isPermissionGranted(String permission){
        return ContextCompat.checkSelfPermission(QrActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private Bitmap getQrBitmap(String contents) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Bitmap bitmap = null;
        try {
            bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 500, 500));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    private void saveQrToGallery(Bitmap bitmap){
        if(bitmap != null){
            String exStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            String dirName = "/donation/";
            String fileName = Utilities.convertTimeMillisToTimeFormat(System.currentTimeMillis()) + ".jpg";

            try{
                File dir = new File(exStorage + dirName);
                if(!dir.isDirectory()){
                    boolean a = dir.mkdirs();
                    Utilities.logD("Test", Boolean.toString(a));
                }
                FileOutputStream fileOutputStream = new FileOutputStream(exStorage + dirName + fileName);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Utilities.showToast(this, "이미지를 저장할 수 없습니다.");
            }
            Utilities.showToast(this, "이미지를 저장하였습니다.");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + exStorage + dirName + fileName)));
        }
    }

    private void generateQr(String amount){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(PlaceAPIService.class).getTagToken(amount)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                String token = (String)results.get("tagToken");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("provider", "donation");
                    jsonObject.put("token", token);
                    qrCode = getQrBitmap(jsonObject.toString());
                    if(qrCode != null){
                        binding.qrImgView.setImageBitmap(qrCode);
                        binding.saveBtn.setVisibility(View.VISIBLE);
                    }else{
                        throw new JSONException("JSON Exception");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utilities.showToast(QrActivity.this, "QR코드 생성 실패");
                }
            }
        };

        serverCommunicator.execute();
    }
}
