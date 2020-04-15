package kr.co.t_woori.good_donation_admin.communication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.MimeTypeMap;

import com.andremion.louvre.Louvre;
import com.andremion.louvre.home.GalleryActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.t_woori.good_donation_admin.utilities.Utilities;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rladn on 2017-08-30.
 */

public class ImageUploader extends AppCompatActivity{

    private static final int SELECT_PICTURE = 1;

    private String type; // "C" : Charity, "P" : Place, "H" : HomeBanner
    private String idNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");
        idNum = getIntent().getStringExtra("idNum");
        getFileFromGallery();
    }

    private void getFileFromGallery() {
        Louvre.init(this).setRequestCode(SELECT_PICTURE).setMaxSelection(20).open();
    }


    private void readyToUpload(String idNum, List<Uri> uriList) {

        List<MultipartBody.Part> fileList = new ArrayList<>();

        RequestBody idNumBody = RequestBody.create(MediaType.parse("text/plain"), idNum);

        int count = 0;
        for(Uri uri : uriList){
            count++;
            // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
            // use the FileUtils to get the actual file by uri
            File file = new File(uri.getPath());

            // create RequestBody instance from file
            String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            RequestBody requestFile = RequestBody.create(MediaType.parse(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("image_"+count, file.getName(), requestFile);

            fileList.add(body);
        }

        if("C".equals(type)){
            uploadCharityImg(idNumBody, fileList);
        }else if("P".equals(type)){
            uploadPlaceImg(idNumBody, fileList);
        }else if("H".equals(type)){
            uploadHomeBanner(idNumBody, fileList);
        }
    }

    private void uploadCharityImg(RequestBody idNum, List<MultipartBody.Part> fileList){
        APICreator.create(CommunicationAPIService.class).uploadCharityImg(idNum, fileList).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utilities.showToast(ImageUploader.this, "업로드 성공");
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utilities.showToast(ImageUploader.this, "업로드 실패");
                finish();
            }
        });
    }

    private void uploadPlaceImg(RequestBody idNum, List<MultipartBody.Part> fileList){
        APICreator.create(CommunicationAPIService.class).uploadPlaceImg(idNum, fileList).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utilities.showToast(ImageUploader.this, "업로드 성공");
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utilities.showToast(ImageUploader.this, "업로드 실패 " + t.getMessage());
                finish();
            }
        });
    }

    private void uploadHomeBanner(RequestBody idNum, List<MultipartBody.Part> fileList){
        APICreator.create(CommunicationAPIService.class).uploadHomeBanner(idNum, fileList).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utilities.showToast(ImageUploader.this, "업로드 성공");
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utilities.showToast(ImageUploader.this, "업로드 실패");
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE && data != null) {
                readyToUpload(idNum, GalleryActivity.getSelection(data));
            }
        }else{
            finish();
        }
    }
}