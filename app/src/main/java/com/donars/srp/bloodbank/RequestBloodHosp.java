package com.donars.srp.bloodbank;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.donars.srp.bloodbank.fetcher.Details;

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

public class RequestBloodHosp extends AppCompatActivity {
    public final String TAG="RequestBloodHosp";
    @BindView(R.id.input_name)
    EditText _inputname;
    @BindView(R.id.input_bloodrequ)
    EditText _inputblood;
    @BindView(R.id.btn_submit)
    Button _submit;
    @BindView(R.id.spinner1)
    Spinner spinner;
    String name,blood_qtn,blood_type,res,myJSON,hosp_name="empty",hosp_addr="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood_hosp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Request Blood");
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        _inputblood.setFilters(new InputFilter[]{new InputFilterMinMax(1,7)});
        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBlood();
            }
        });

    }
    public void requestBlood() {
        Log.d(TAG, "submit");


        _submit.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RequestBloodHosp.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting...");
        progressDialog.show();

        name = _inputname.getText().toString();
        blood_qtn = _inputblood.getText().toString();

        blood_type = spinner.getSelectedItem().toString();

        Log.d("request ",name+blood_qtn+blood_type);


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
        getData();
    }



    public boolean validate() {
        boolean valid = true;

        String name = _inputname.getText().toString();
        String blood_qtn = _inputblood.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _inputname.setError("at least 3 characters");
            valid = false;
        } else {
            _inputname.setError(null);
        }

        if (blood_qtn.isEmpty()) {
            _inputblood.setError("enter blood quantity");
            valid = false;
        } else {
            _inputblood.setError(null);
        }


        return valid;
    }

    public void getData(){
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
                            +URLEncoder.encode("Blood_group","UTF-8")+"="+URLEncoder.encode(blood_type,"UTF-8")+"&"
                            +URLEncoder.encode("quantity","UTF-8")+"="+URLEncoder.encode(blood_qtn,"UTF-8")+"&"
                            +URLEncoder.encode("Hospital_name","UTF-8")+"="+URLEncoder.encode(Details.hospital.getName(),"UTF-8")+"&"
                            +URLEncoder.encode("Hosp_address","UTF-8")+"="+URLEncoder.encode(Details.hospital.getAddress(),"UTF-8")+"&"
                            +URLEncoder.encode("latitude","UTF-8")+"="+URLEncoder.encode(Details.hospital.getLatitude(),"UTF-8")+"&"
                            +URLEncoder.encode("longitude","UTF-8")+"="+URLEncoder.encode(Details.hospital.getLongitude(),"UTF-8");

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
            @Override
            protected void onPostExecute(String te){
                myJSON=te;
                //t1.setText(myJSON);
                res="";
                Toast.makeText(getApplicationContext(),te,Toast.LENGTH_LONG).show();
                te="";
                // t1.setText(te);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

}
