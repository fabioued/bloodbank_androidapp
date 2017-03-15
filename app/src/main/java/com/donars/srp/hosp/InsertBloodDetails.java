package com.donars.srp.hosp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
 * Created by Rahul Satish on 28-08-2016.
 */
public class InsertBloodDetails extends AppCompatActivity {
    @BindView(R.id.input_name) EditText _name;
    @BindView(R.id.input_hos) EditText _hos;
    @BindView(R.id.input_address) EditText _address;
    @BindView(R.id.input_bg) EditText _bg;
    @BindView(R.id.button_loc) Button _loc;

    String name,hos,address,bg,res,bloodg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_blood);
        ButterKnife.bind(this);

        _loc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                insertDetails();
            }
        });
    }
        public void insertDetails()
        {
            name=_name.getText().toString();
            hos=_hos.getText().toString();
            address=_address.getText().toString();
            bg=_bg.getText().toString();
            Spinner spinner = (Spinner) findViewById(R.id.spinner1);
            bloodg=spinner.getSelectedItem().toString();
            Toast.makeText(getApplicationContext(),bloodg,Toast.LENGTH_LONG).show();
            class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/Bloodreq.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("Patient_name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                            +URLEncoder.encode("Hospital_name","UTF-8")+"="+URLEncoder.encode(hos,"UTF-8")+"&"
                            +URLEncoder.encode("Hosp_address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"
                            +URLEncoder.encode("Blood_group","UTF-8")+"="+URLEncoder.encode(bloodg,"UTF-8");

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
                protected void onPostExecute(String te){
                    //myJSON=te;
                    //t1.setText(myJSON);
                   // res="";
                    Toast.makeText(getApplicationContext(),te,Toast.LENGTH_LONG).show();
                    te="";
                    // t1.setText(te);
                }
        }
    }

}
