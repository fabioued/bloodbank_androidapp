package com.donars.srp.hosp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.donars.srp.hosp.fetcher.Details;
import com.google.gson.Gson;

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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lakshman on 10/23/2016.
 */

public class UserDetails extends AppCompatActivity{
    @BindView(R.id.input_name)
    TextView _nameText;
    @BindView(R.id.input_email) TextView _emailText;
    @BindView(R.id.input_address) TextView _address;
    @BindView(R.id.input_blood_type)
    TextView _bloodtype;
    @BindView(R.id.input_phone1)
    TextView _phoneNo;
    @BindView(R.id.input_gender)
    TextView _gender;
    @BindView(R.id.input_age)
    TextView _age;
    @BindView(R.id.input_weight)
    TextView _weight;
    @BindView(R.id.input_dob)
    TextView _dob;
    @BindView(R.id.delete_btn)
    AppCompatButton _deletebtn;
            @BindView(R.id.completed_btn)
            AppCompatButton _compbtn;
    ProgressDialog progress;
    String username;
    int mSelectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_box_donor_details);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        mSelectedItem=intent.getIntExtra("mSelectedItem",-11);
        progress=new ProgressDialog(this);
        _deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("itemposition",mSelectedItem);
                intent.putExtra("option","delete");
                setResult(Activity.RESULT_OK,intent);
                finish();//finishing activity
            }
        });
        _compbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("itemposition",mSelectedItem);
                intent.putExtra("option","accept");
                setResult(Activity.RESULT_OK,intent);
                finish();//finishing activity
            }
        });
        getData();
    }
    public void init()
    {
        _nameText.setText("Name:"+ Details.userinfo.getName());
        _emailText.setText("Email:"+Details.userinfo.getUsername());
        _address.setText("Address:"+Details.userinfo.getAddress());
        _gender.setText("Gender:"+Details.userinfo.getGender());
       // _lastd.setText("Last Donation:"+Details.userinfo.getLastdonation());
        _phoneNo.setText("Phone no:"+Details.userinfo.getPhone());
        _bloodtype.setText("Blood Type:"+Details.userinfo.getBloodgroup());
        _dob.setText("D.O.B :"+Details.userinfo.getDate_of_birth());
      //  _drink.setText("Drinking Habit:"+Details.userinfo.getDrinking_habit());
       // _ndonations.setText("No of Times Donated:"+Details.userinfo.getNo_of_times_donated());
        _age.setText("Age:"+Details.userinfo.getAge());
       // _rating.setText("Rating:"+Details.userinfo.getRating());
        _weight.setText("Weight:"+Details.userinfo.getWeight());
    }
    public void getData(){

        class GetDataJSON extends AsyncTask<String, Void, Boolean> {
            String res;
             String data;
            @Override
            protected void onPreExecute() {
                res="";
                data="";
                progress.setMessage("Loading");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {

                    URL url = new URL("http://lifesaver.net23.net/get_donor_details.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String line="";
                    Integer i=1;
                    Boolean result=false;
                    while((line=bufferedReader.readLine())!=null)
                    {
                        res=res+line;
                        Log.d("res",line);
                    }
                    res = res.substring(0, res.indexOf("<"));
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            protected void onPostExecute(Boolean te){
//                myJSON=te;
//                //t1.setText(myJSON);
//                res="";
                Log.d("resulttt",data+res+(res.equalsIgnoreCase("Failure"))+"");
                //       Toast.makeText(getApplicationContext(),"return"+te,Toast.LENGTH_LONG).show();
                if(res.length()>0 && !(res.equalsIgnoreCase("Failure"))) {
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                    try {
                        Gson gson = new Gson();
                        Details.userinfo = gson.fromJson(res, com.donars.srp.hosp.model.UserStats.class);
                        System.out.println(gson.toJson(Details.userinfo));
                        init();
                    }catch (Exception e){e.printStackTrace();}

                }
                progress.dismiss();
//                la=showList();
//                if (te != null) return te;
//                // t1.setText(te);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

}
