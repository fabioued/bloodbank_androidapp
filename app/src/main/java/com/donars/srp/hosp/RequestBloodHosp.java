package com.donars.srp.hosp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.donars.srp.hosp.fetcher.Details;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestBloodHosp extends AppCompatActivity {
    public final String TAG="RequestBloodHosp";
    @BindView(R.id.input_name)
    EditText _inputname;
    @BindView(R.id.input_phone)
    EditText _inputphone;
    @BindView(R.id.input_age)
    EditText _inputage;
    @BindView(R.id.input_spl)
    EditText _inputspl;
    @BindView(R.id.btn_submit)
    Button _submit;
    @BindView(R.id.input_date)
    Button _dob;
    String name,blood_qtn,blood_type,res,myJSON,hosp_name="empty",hosp_addr="empty",age,phone,spl,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood_hosp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Doctor");
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBlood();
            }
        });
        _dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                _dob.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                Calendar calendar=Calendar.getInstance();
                calendar.set(1900,1,1);
                dpd.setMinDate(calendar);
                dpd.setMaxDate(Calendar.getInstance());
                dpd.setThemeDark(true);
                dpd.setAccentColor(Color.parseColor("#9C27B0"));

                dpd.show(getFragmentManager(), "Datepickerdialog");
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
        age = _inputage.getText().toString();
        spl=_inputspl.getText().toString();
        phone=_inputphone.getText().toString();
        date=_dob.getText().toString();
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



      public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/add_doc.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                            +URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8")+"&"
                            +URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"
                            +URLEncoder.encode("special","UTF-8")+"="+URLEncoder.encode(spl,"UTF-8")+"&"
                            +URLEncoder.encode("hospital","UTF-8")+"="+URLEncoder.encode(Details.hospital.getName(),"UTF-8")
                            +"&"+URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8");

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
               // Toast.makeText(getApplicationContext(),te,Toast.LENGTH_LONG).show();
                te="";
                // t1.setText(te);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

}
