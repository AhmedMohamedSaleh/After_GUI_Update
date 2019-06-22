package com.example.android.demo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.demo.remote.Upload_Image_Request_Config;
import com.example.android.demo.remote.Establish_API_Connection;


/*import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
*/


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Environment.getExternalStorageState;
import static android.util.Log.d;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class Upload extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView imageView;
    private Button upload_button,select_button,cancel_button= null;
    EditText editText;
    TextView result;
    Call_Api_And_Get_Response callApigetResponse = null;
    Uri selectedImage;
    String img_path = null;
    Boolean img_detection = true;
    Upload_Image_Request_Config uploadImageAPIConfig;
    ProgressBar progressBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_upload);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        result = (TextView) findViewById(R.id.result);
        result.setVisibility(View.GONE);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon));
        upload_button = (Button) findViewById(R.id.button1);
        select_button = (Button) findViewById(R.id.select);
        cancel_button = (Button) findViewById(R.id.cancel);
        upload_button.setVisibility(View.GONE);
        cancel_button.setVisibility(View.GONE);

        editText = (EditText) findViewById(R.id.text);
        uploadImageAPIConfig = Establish_API_Connection.getFileService();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setEnabled(false);
        progressBar.setVisibility( INVISIBLE);


    }

    public void select_image(View v) {
        result.setVisibility(View.GONE);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            if (selectedImage == null)
            {
                d("image ss" , "null");
                Toast.makeText(this, ( "invalid image ." ) , Toast.LENGTH_LONG).show();
                return;
            }
            else{
                d("image ss" , "not null");
            }

            Bitmap bitmap2 = null;
            Bitmap circleBitmap = null;
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);


                Bitmap bitmap = Bitmap.createScaledBitmap(bitmap2, 100, 100, true);

                circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
                //Log.d("img_path : ", String.valueOf(bitmap.getWidth()));

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, ( "invalid image ." ) , Toast.LENGTH_LONG).show();
                return;
            }
            imageView.setImageBitmap(circleBitmap);
            //imageView.setImageURI(selectedImage);
            img_path = getRealPathFromURI(this, selectedImage);
            d("img_path : ", img_path);
            select_button.setVisibility(View.GONE);
            upload_button.setVisibility(VISIBLE);
            cancel_button.setVisibility(VISIBLE);

        }
        else {
            d("imgage : ", "it is not selected .");
            Toast.makeText(this, ( "image path didn't establish ." ) , Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void cancel_image(View v){
        result.setVisibility(View.GONE);
        selectedImage = null;
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon));
        select_button.setVisibility(VISIBLE);
        upload_button.setVisibility(View.GONE);
        cancel_button.setVisibility(View.GONE);
    }

    public void upload(View v) {
        result.setVisibility(View.GONE);
        if (!img_detection)
        {
            img_detection = !img_detection;
            d("image path " , "didn't establish .");
            Toast.makeText(this, ( "image path didn't establish ." ) , Toast.LENGTH_LONG).show();
            return;
        }
        upload_button.setEnabled(false);
        imageView.setEnabled(false);
        cancel_button.setEnabled(false);

        progressBar.setEnabled(true);
        progressBar.setVisibility( VISIBLE);
        callAsync();
        if (callApigetResponse != null && callApigetResponse.isCancelled())
            d("thread ", "cancel final");
        d("confirmInputData : ", "true");

        return;

    }

    public void Validate_Response(String response) {

        d("Validate_Response : ", "true");
        cancel_button.setEnabled(true);
        upload_button.setEnabled(true);
        imageView.setEnabled(true);
        if (response !=null ){
            d("Validate_Response : ", response);
            if(!response.contains("error"))
                d("Server response","response hasn't contain error message");
            else if(!response.contains("class"))
                d("Server response","response hasn't contain class message");
            else if(!response.contains("accuracy"))
                d("Server response","response hasn't contain accuracy message");
            else {
                String response22[];
                response22 = (response.split(","));

                String response_accuracy = response22[0].split(":")[1];
                String response_class = response22[1].split(":")[1];
                String response_error = response22[2].split(":")[1].split(",")[0];

                if (response_error.contains("Nothing"))
                {
                    d("class message" , response_class );
                    d("accuracy message" , response_accuracy );
                    //Toast.makeText(this, ( response_class + ( response_accuracy) ) , Toast.LENGTH_LONG).show();
                    result.setVisibility(VISIBLE);
                    String response_accuracy_output ;
                    DecimalFormat df = new DecimalFormat("##.###");
                    response_accuracy = response_accuracy.substring(1,response_accuracy.length()-1);
                    response_class = response_class.substring(1,response_class.length()-1);
                    response_accuracy_output = df.format(Double.valueOf( response_accuracy ) ) + " % .";

                    result.setText( "Disease : " + response_class + " .\nProbability : " + response_accuracy_output );
                }
                else
                    d("error message" , response_error );
            }
        }
        else{
            if (Get_Connection_Status.getAPIConnection()) {
                d("Server :", "didn't recieve response .");
                Toast.makeText(this, ( "Server :- didn't recieve response ." ) , Toast.LENGTH_LONG).show();
            }
            else if (!Get_Connection_Status.getInternetConnection()) {
                Log.d("Connection ", "didn't establish ,please check your internet .");
                Toast.makeText(this, ("please check your internet connection and try again ."), Toast.LENGTH_LONG).show();
                Get_Connection_Status.setAPIConnection(!Get_Connection_Status.getAPIConnection());
                Get_Connection_Status.setInternetConnection(!Get_Connection_Status.getInternetConnection());
            }else {
                Log.d("Connection ", "didn't establish ");
                Toast.makeText(this, ("Connection didn't establish ."), Toast.LENGTH_LONG).show();
                Get_Connection_Status.setAPIConnection(!Get_Connection_Status.getAPIConnection());
            }
        }
        progressBar.setVisibility( INVISIBLE);
    }

    public void callAsync() {
        callApigetResponse = new Call_Api_And_Get_Response();
        try {

            callApigetResponse.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        callApigetResponse.cancel(true);
        d("callAsync ", "true");
    }

    public String getStringFromRetrofitResponse(Response<ResponseBody> response) {
        //Try to get response body
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        Boolean check_respoonseBody = false;
        if (response == null)
        {
            d("response = " , " null ." );
            Toast.makeText(this, ( "response == null " ) , Toast.LENGTH_LONG).show();
            return null;
        }
        else if (response.body() == null)
        {
            check_respoonseBody = !check_respoonseBody;
            d("response.body()  " , " null ." );
            Toast.makeText(this, ( "failed to upload , please try again ." ) , Toast.LENGTH_LONG).show();
            reader = new BufferedReader(new InputStreamReader((InputStream) response.errorBody().byteStream()));
        }
        else if (response.body().byteStream() == null)
        {
            d("byteStream() = " , " null ." );
            Toast.makeText(this, ( "response.body().byteStream() == null " ) , Toast.LENGTH_LONG).show();
            return null;
        }
        else {
            reader = new BufferedReader(new InputStreamReader((InputStream) response.body().byteStream()));

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            d("response.body()  ", sb.toString());
        }
        if (check_respoonseBody)
        {
            check_respoonseBody = false;
            return null;
        }
        return sb.toString();
    }

    class Call_Api_And_Get_Response extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            d("thread ", "begin");
        }

        @Override
        protected String doInBackground(final Void... params) {

            if (img_path == null) {
                img_detection = !img_detection;
                return "";
            }
            String sourceFileUri = img_path;
            String fileName = sourceFileUri;
            File sourceFile = new File(sourceFileUri);

            if (!sourceFile.isFile()) {
                Log.e("uploadFile", "Source File not exist :" + img_path);
            } else {

                    File file = new File(img_path);
                    d("file.getName() " , file.getName() );
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("pic" , file.getName() ,requestBody);
                    MultipartBody.Part token = MultipartBody.Part.createFormData("token", Session_Token.getToken());
                    d("token :-  " , Session_Token.getToken() );
                    retrofit2.Call<ResponseBody> call ;
                if ( uploadImageAPIConfig.upload( body, token) == null){
                    d("info :- " , "null");
                    return "";
                }
                else{
                    Log.d("call :- " , String.valueOf(uploadImageAPIConfig.upload( body, token)));
                    call = uploadImageAPIConfig.upload(
                            body,
                            token);
                }

                call.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response1) {
                            long tx = response1.raw().sentRequestAtMillis();
                            long rx = response1.raw().receivedResponseAtMillis();

                            d("sentRequest time " ,(  ( (tx / 1000) % 60) +" s" ) );
                            d("receivedResponse time " ,( (rx / 1000) % 60) +" s" );
                            progressBar.setVisibility( INVISIBLE);
                            String response = getStringFromRetrofitResponse(response1);
                            if (response == null)
                            {
                                upload_button.setEnabled(true);
                                imageView.setEnabled(true);
                                return;
                            }
                            Validate_Response(response);
                        }
                        @Override
                        public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                            d("response fail" , t.toString() );
                            upload_button.setEnabled(true);
                            imageView.setEnabled(true);
                            cancel_button.setEnabled(true);
                            Get_Connection_Status.setAPIConnection( !Get_Connection_Status.getAPIConnection() );
                            Validate_Response(null);
                        }

                    });
                    // Responses from the server (code and message)
            }
            return "";
        }
    }
}
