package com.example.android.demo;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.android.demo.remote.Establish_API_Connection;
import com.example.android.demo.remote.User_Images_Request_Config;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.R.attr.id;
import static android.view.View.generateViewId;

public class User_Photos extends AppCompatActivity {

    ProgressBar progressBar;
    User_Photos.AsyncT asyncT = null;
    TextView textView_1;
    //DownloadImageFromInternet asyncT_download = null;
    User_Images_Request_Config info;
    private Button show = null;
    String response_from_Asynct;
    int id = 75465;
    ArrayList<String> urls = new ArrayList<>();
    Toolbar toolbar;
    static String evalue ;
    static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.userphoto);
        info = Establish_API_Connection.getUser_infos();
        textView_1 = (TextView) findViewById(R.id.TV_1);
        textView_1.setVisibility(View.GONE);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        show();
    }

    public void show() {

//        progressBar.setEnabled(true);
        //      progressBar.setVisibility( VISIBLE);
        callAsync();
        //    progressBar.setEnabled(false);
        //  progressBar.setVisibility( INVISIBLE);

        if (asyncT !=null && asyncT.isCancelled())
            Log.d("thread " , "cancel final" );
        else
            Log.d("callAsync" , "success" );
        ArrayList<String> urls = confirm_response(response_from_Asynct);

        //getImages(urls);
        Add_All_Images_with_info();
        Log.d("get images" , "success" );
    }

    public void callAsync() {

        Log.d("callAsync ", "begin");
        asyncT = new AsyncT();
        try {
            String response = asyncT.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        asyncT.cancel(true);
        Log.d("callAsync ", "true");
    }

    class AsyncT extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("thread ", "begin");
        }

        @Override
        protected String doInBackground(final Void... params) {

            final Boolean[] check_response = {false};

            MultipartBody.Part token = MultipartBody.Part.createFormData("token", Session_Token.getToken());
            Call<ResponseBody> call = null;
            if ( info.get_images(token) == null){
                Log.d("info :- " , "null");
            }
            else
                call = info.get_images(token);

            Response<ResponseBody> response1 = null;

            try {
                response1 = call.execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response1.isSuccessful()){
                check_response[0] = true ;
                long tx = response1.raw().sentRequestAtMillis();
                long rx = response1.raw().receivedResponseAtMillis();

                Log.d("sentRequest time " ,(  ( (tx / 1000) % 60) +" s" ) );
                Log.d("receivedResponse time " ,( (rx / 1000) % 60) +" s" );

                response_from_Asynct = getStringFromRetrofitResponse(response1);
                // Validate_Response(response);
            } else {
                Log.d("wait :- " , "sfsdf");
            }
            return "";
        }
    }

    public String getStringFromRetrofitResponse(Response<ResponseBody> response) {
        //Try to get response body
        BufferedReader reader ;
        StringBuilder sb = new StringBuilder();
        Boolean check_respoonseBody = false;
        if (response == null)
        {
            Log.d("response = " , " null ." );
            Toast.makeText(this, ( "response == null " ) , Toast.LENGTH_LONG).show();
            return null;
        }
        else if (response.body() == null)
        {
            check_respoonseBody = !check_respoonseBody;
            Log.d("response.body() = " , " null ." );
            Toast.makeText(this, ( "response.body() == null " ) , Toast.LENGTH_LONG).show();
            reader = new BufferedReader(new InputStreamReader((InputStream) response.errorBody().byteStream()));
        }
        else if (response.body().byteStream() == null)
        {
            Log.d("byteStream() = " , " null ." );
            Toast.makeText(this, ( "response.body().byteStream() == null " ) , Toast.LENGTH_LONG).show();
            return null;
        }
        else
            reader = new BufferedReader(new InputStreamReader((InputStream) response.body().byteStream()));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d("response.body() = " , sb.toString() );
        if (check_respoonseBody)
            return null;
        return sb.toString();
    }

    class Information {
        String acc;
        String prediction;
        String url;
        public Information(String acc ,String prediction,String url){
            this.acc = acc;
            this.prediction = prediction;
            this.url = url;
        }
    }
    ArrayList<Information> informations = new ArrayList<>();

    public ArrayList<String> confirm_response(String response){
        if (response != null)
        {
            try {

                response = response.substring(9,response.length()-2);
                Log.d("response " , response);
                if (!response.contains("error")) {

                    ArrayList<String>response22 = new ArrayList<>();

                    if (!response.isEmpty()) {
                        Pattern ptn = Pattern.compile("\\{[^\\}|]*\\}");
                        Matcher matcher = ptn.matcher(response);
                        while (matcher.find())
                        {
                            response22.add(matcher.group());
                        }
                        for (int i = 0; i < response22.size(); i++) {
                            //    final_response.add( response22.get(i).substring(2, response22.get(i).length() - 1) );

                            String information[] = response22.get(i).substring(2, response22.get(i).length() - 1).split(",");

                            String acc = information[0].split(":")[1];
                            acc = acc.substring(1,acc.length()-1);
                            Log.d(String.valueOf((i + 1)), acc);
                            String prediction = information[1].split(":")[1];
                            prediction = prediction.substring(1,prediction.length()-1);
                            Log.d(String.valueOf((i + 1)), prediction);
                            String url = information[2].split("\":\"")[1];
                            url = url.substring(0,url.length()-1);
                            Information information1 = new Information(acc,prediction,(url) );
                            informations.add(information1);
                            urls.add( url);
                        }
                        if (urls.size() == 0)
                            Log.d("size :- " , String.valueOf(0));
                        else
                            Log.d("size :- " , "yes");

                        return urls;
                    }
                    else
                    {
                        Log.d("data " , "arr empty");
                        textView_1.setVisibility(View.VISIBLE);
                        textView_1.setText("there are not any image .");
                    }
                }else Log.d("error :- " , response);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

//////////////////////////// Validate_Inputs_And_Enable_Buttons

    public void Add_All_Images_with_info() {

        ScrollView scrollView = new ScrollView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(layoutParams);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearParams);

        scrollView.addView(linearLayout);
        int index = 0;
        for (int j = 0; j < urls.size(); j++) {
            for (int i = 0; i < informations.size(); i++) {
                if (informations.get(i).url.equals(urls.get(j))) {
                    index = i;
                    break;
                }
            }

            Log.d("src ", urls.get(index));
            Log.d("acc " + (index + 1), informations.get(index).acc);
            Log.d("prediction " + (index + 1), informations.get(index).prediction);

            Add_Image_With_URL_And_Text(linearLayout,urls.get(index), informations.get(index).acc,informations.get(index).prediction);
        }

        LinearLayout linearLayoutx = (LinearLayout) findViewById(R.id.rootContainer);
        if (linearLayoutx != null) {
            linearLayoutx.addView(scrollView);
        }
    }


    public void Add_Image_With_URL_And_Text(LinearLayout linearLayout, String Url, String Prob, String Disease) {

        RelativeLayout linearLayout1 = new RelativeLayout(this);
        LinearLayout.LayoutParams linearParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(linearParams1);
        linearParams1.setMargins(15, 15, 15, 0);
        linearLayout1.setBackgroundResource(R.drawable.profile_background5);
        linearLayout.addView(linearLayout1);

        final de.hdodenhof.circleimageview.CircleImageView imageView1 = new de.hdodenhof.circleimageview.CircleImageView(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(280, 280,1f);
        params1.setMargins(30, 30, 0, 30);
        //  params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        imageView1.setLayoutParams(params1);
        //imageView1.setImageResource(R.drawable.salh);
        Picasso.with(this).load(Url).centerCrop().resize(50,50).into(imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(User_Photos.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_dialog_custom, null);
                PhotoView photoView = (PhotoView) mView.findViewById(R.id.imageView);
                photoView.setImageDrawable(imageView1.getDrawable());
                //  photoView.setMaxHeight(330);
                photoView.setMinimumHeight(3000);

                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        linearLayout1.addView(imageView1);

        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params3.setMargins(350, 80, 0, 00);
        TextView text1 = new TextView(getApplicationContext());
        text1.setTextSize(22);

        text1.setTypeface(null, Typeface.BOLD);
        text1.setText("Disease: " + Disease);
        text1.setLayoutParams(params3);
        text1.setId(id++);
        linearLayout1.addView(text1);

        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params4.setMargins(350, 50, 0, 30);
        TextView text2 = new TextView(getApplicationContext());
        String test = Prob;
        Prob = test.substring(0,4);
        text2.setText("Probability: " + Prob +"%");
        text2.setTextSize(22);
        text2.setTypeface(null, Typeface.BOLD);
        params4.addRule(RelativeLayout.BELOW, text1.getId());
        text2.setLayoutParams(params4);
        linearLayout1.addView(text2);
    }



    /*public void getImages(ArrayList<String> urls){
        final ImageView imageView=(ImageView)findViewById(R.id.image);
        if (urls !=null)
            for (int i=0;i<urls.size();i++)
            {
                Log.d("yes " , "yes");
                //Download(imageView,"https://api-skin-dis.herokuapp.com"+urls.get(i));
                printt(urls.get(i));

            }
        else
        {
            Log.d("image arr " , "there is not any image" );
            Toast.makeText(getApplicationContext(), "there is not any image", Toast.LENGTH_LONG).show();
        }
    }

    public void printt(String srcc)
    {
        final ImageView imageView2=(ImageView)findViewById(R.id.image);
        final ImageView imageView222=(ImageView)findViewById(R.id.image2);
        int index =0;

        for (int i=0;i<informations.size();i++)
        {
            if (informations.get(i).url.equals(srcc) )
            {
                index = i ;
                break;
            }
        }
        Log.d("src " , srcc);
        Log.d("acc "+(index+1) , informations.get(index).acc);
        Log.d("prediction "+(index+1) , informations.get(index).prediction);
        if (srcc !=" ")
        {
            if (index==0)
                Picasso.with(this).load(srcc).centerCrop().resize(20,20).into(imageView2);
            else
                Picasso.with(this).load(srcc).centerCrop().resize(20,20).into(imageView222);
        }
        else Log.d(" errror " , "null");

    }
    */


    /*
    public void Download(ImageView imageView , String url) {
        String srcc = " ";
        asyncT_download = new DownloadImageFromInternet(imageView);
        try {
            srcc = asyncT_download.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        asyncT_download.cancel(true);
        asyncT_download=null;

        if(srcc != "")
            printt(srcc);
        else {
            Uri imageUri = Uri.parse("android.resource://"+this.getPackageName()+"/drawable/saleh");
            ImageView imageView22 = (ImageView) findViewById(R.id.image2);
            imageView22.setImageURI(imageUri);
            return;
        }
        Log.d("Download ", "true");
    }

    class DownloadImageFromInternet extends AsyncTask<String , Void ,String> {

        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
            Log.d("thread ", "begin");
        }

        @Override
        protected String doInBackground(String... params) {
            String imageURL = params[0];
            Log.d("sleep " , imageURL);
            String srcc="";
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect(imageURL).get();
                Elements img = doc.getElementsByTag("img");
                for (org.jsoup.nodes.Element el: img){
                    srcc= el.absUrl("src");
                    if (srcc != "")
                    {
                        Log.d("src  " , srcc);
                        for (int x=0;x<informations.size();x++)
                        {
                            if (informations.get(x).url.equals(imageURL))
                            {
                                informations.get(x).url = srcc;
                                break;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            return srcc;

        }


    }
    */
    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }
}
