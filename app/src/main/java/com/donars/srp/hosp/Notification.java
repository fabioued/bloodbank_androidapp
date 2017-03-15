package com.donars.srp.hosp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donars.srp.hosp.fetcher.Details;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Notification extends AppCompatActivity {

    LinearLayout timerLayout,acceptLayout,deleteLayout;
    CountDownTimer countDownTimer;
    TextView tvSecond;
    TextView tvMinute;
    ProgressDialog progressDialog;
    Button cancel_btn,cancel_ap_btn;
    Boolean loadCompleted=true;
    int check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_accept);
        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        check=getIntent().getIntExtra("starttimer",-11);

        progressDialog=new ProgressDialog(this);
        countDownTimer = new MyCountDownTimer(1000*100, 1000);
        tvSecond = (TextView) findViewById(R.id.tvSecond);
        tvMinute = (TextView) findViewById(R.id.tvMinute);
        cancel_btn=(Button)findViewById(R.id.cancel_btn);
        cancel_ap_btn=(Button)findViewById(R.id.cancel_ap_btn);
        timerLayout=(LinearLayout)findViewById(R.id.timer_layout);
        acceptLayout=(LinearLayout)findViewById(R.id.accept_layout);
        deleteLayout=(LinearLayout)findViewById(R.id.reject_layout);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest();
            }
        });
        cancel_ap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest();
            }
        });
        if(Details.status==0)
        {
            acceptedData();
            rejectedData();
        }
        if(check==11)
        {
            startTimer();
        }
    }
    public void changeView()
    {

        switch (Details.status)
        {
            case 0:{timerLayout.setVisibility(View.INVISIBLE);
                acceptLayout.setVisibility(View.INVISIBLE);
                deleteLayout.setVisibility(View.INVISIBLE);
                break;}
            case 2:{timerLayout.setVisibility(View.INVISIBLE);
                acceptLayout.setVisibility(View.VISIBLE);
                deleteLayout.setVisibility(View.INVISIBLE);
                break;}
            case 3:{timerLayout.setVisibility(View.INVISIBLE);
                acceptLayout.setVisibility(View.INVISIBLE);
                deleteLayout.setVisibility(View.VISIBLE);
                break;}
        }
    }
    public  void startTimer()
    {
        timerLayout.setVisibility(View.VISIBLE);
        acceptLayout.setVisibility(View.INVISIBLE);
        deleteLayout.setVisibility(View.INVISIBLE);
        countDownTimer.start();
    }
    public void cancelRequest()
    {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            String res,myJSON;
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                res="";
                if(timerLayout.getVisibility()==View.VISIBLE)
                {
                    countDownTimer.onFinish();
                    timerLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/user_delete_req.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("donor_id","UTF-8")+"="+URLEncoder.encode(Details.user.getUsername(),"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    Integer i=1;
                    while((line=bufferedReader.readLine())!=null)
                    {
                        res=res+line;
                    }
                    res=res.substring(0,res.indexOf("<"));
                    Log.d("data",data);
                    Log.d("response",res);
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return res;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String te){
                myJSON=te;
                //t1.setText(myJSON);
                Log.d("res",res);
//                Toast.makeText(getApplicationContext(),te,Toast.LENGTH_LONG).show();
                te="";
                // t1.setText(te);
                // Use the AlertDialog.Builder to configure the AlertDialog.
                progressDialog.hide();
                if(res.equalsIgnoreCase("Success") ) {
                    AlertDialog.Builder alertDialogBuilder =
                            new AlertDialog.Builder(Notification.this)
                                    .setTitle("Success")
                                    .setMessage("You Request has been canceled");

                    AlertDialog alertDialog = alertDialogBuilder.show();
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.dismiss();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            long second = millisUntilFinished / 1000;

            tvSecond.setText(String.valueOf(second%60));
            long min = second / 60;
            tvMinute.setText(String.valueOf(min));

            // if()) {
             //
              if((second % 60)%30==0 && loadCompleted)
                  acceptedData();

         //   }
        }
        @Override
        public void onFinish() {
            System.out.println("Finished");
          if(Details.status==0)
              noResponse();
            //onBackPressed();
        }
    }
    public void noResponse()
    {
        AlertDialog.Builder alertDialogBuilder =
            new AlertDialog.Builder(Notification.this)
                    .setTitle("Failure")
                    .setMessage("Sorry Your Request wasn't accepted by the hospital");
        cancelRequest();
        AlertDialog alertDialog = alertDialogBuilder.show();
        alertDialog.setCanceledOnTouchOutside(true);
        timerLayout.setVisibility(View.GONE);

    }

    public void acceptedData()
    {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog progressDialog;
            String res,myJSON;
            @Override
            protected void onPreExecute()
            {
                progressDialog=new ProgressDialog(Notification.this);
                super.onPreExecute();
                res="";
                loadCompleted=false;
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/user_accepted.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("donor_id","UTF-8")+"="+URLEncoder.encode(Details.user.getUsername(),"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    Integer i=1;
                    while((line=bufferedReader.readLine())!=null)
                    {
                        res=res+line;
                    }
                    res=res.substring(0,res.indexOf("<"));
                    Log.d("data",data);
                    Log.d("response",res);
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return res;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String te){
                myJSON=te;
                //t1.setText(myJSON);
                Log.d("res",res);
//                Toast.makeText(getApplicationContext(),te,Toast.LENGTH_LONG).show();
                te="";
                // t1.setText(te);
                // Use the AlertDialog.Builder to configure the AlertDialog.
                progressDialog.hide();
                loadCompleted=true;
                if(res.length()>10 ) {
                    Details.status=2;
                    AlertDialog.Builder alertDialogBuilder =
                            new AlertDialog.Builder(Notification.this)
                                    .setTitle("Success")
                                    .setMessage("You Request has been accepted");

                    AlertDialog alertDialog = alertDialogBuilder.show();
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.dismiss();
                    acceptLayout.setVisibility(View.VISIBLE);
                    timerLayout.setVisibility(View.INVISIBLE);
                    deleteLayout.setVisibility(View.INVISIBLE);
                    changeView();
                }

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
    public void rejectedData()
    {

        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog progressDialog;
            String res,myJSON;
            @Override
            protected void onPreExecute()
            {
                progressDialog=new ProgressDialog(Notification.this);
                super.onPreExecute();
                res="";
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/user_deleted.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("donor_id","UTF-8")+"="+URLEncoder.encode(Details.user.getUsername(),"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    Integer i=1;
                    while((line=bufferedReader.readLine())!=null)
                    {
                        res=res+line;
                    }
                    res=res.substring(0,res.indexOf("<"));
                    Log.d("data",data);
                    Log.d("response",res);
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return res;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String te){
                myJSON=te;
                Log.d("res",res);
                progressDialog.hide();
                if(res.length()>10 ) {
                    Details.status=3;
                    AlertDialog.Builder alertDialogBuilder =
                            new AlertDialog.Builder(Notification.this)
                                    .setTitle("Failure")
                                    .setMessage("Sorry You Request has been Rejected");

                    AlertDialog alertDialog = alertDialogBuilder.show();
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.dismiss();
                    acceptLayout.setVisibility(View.INVISIBLE);
                    timerLayout.setVisibility(View.INVISIBLE);
                    deleteLayout.setVisibility(View.VISIBLE);
                    final TextView editText=(TextView)findViewById(R.id.feedback);
                    //editText.setText();
                    changeView();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

}
