package com.donars.srp.hosp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.donars.srp.hosp.fetcher.Details;
import com.donars.srp.hosp.fetcher.Fetcher;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

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
import java.sql.Timestamp;
import java.util.Calendar;

public class BloodDonationActivity extends AppCompatActivity {
    private int position;
   // EditText quantity;
    Button _submit;
    String donorid,donorname,blood_group,patient_id,patient_name,t_o_s,hosiptal_name,phone,res,myJSON,t_o_a;
    ProgressDialog progressDialog;
    TextView tv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donation);
        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent=getIntent();
        progressDialog=new ProgressDialog(this);
        position=intent.getIntExtra("position",-11);

        TextView tv = (TextView) findViewById(R.id.item);
        TextView tv1 = (TextView) findViewById(R.id.item1);
        TextView tv2 = (TextView)findViewById(R.id.item2);
        TextView tv3 = (TextView) findViewById(R.id.item3);
        TextView tv4 = (TextView) findViewById(R.id.item4);

        tv5 = (TextView) findViewById(R.id.item5);
        //quantity=(EditText)findViewById(R.id.quantity);
        //quantity.setFilters(new InputFilter[]{ new InputFilterMinMax("1", Fetcher.detailsList.get(position).getQuantity()+"")});

        _submit=(Button)findViewById(R.id.btn_submit);
        tv.setText(Fetcher.detailsList.get(position).getHop_name());
        tv1.setText(Fetcher.detailsList.get(position).getAddress());
        tv2.setText(Fetcher.detailsList.get(position).getBlood_group());
        tv3.setText(Fetcher.detailsList.get(position).getQuantity()+"");
        tv4.setText(Fetcher.detailsList.get(position).getName());
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                String time = ""+hourOfDay+":"+minute;
                                tv5.setText(time);
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd.setThemeDark(true);
                tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),
                        now.get(Calendar.SECOND));
                tpd.vibrate(true);
                tpd.dismissOnPause(true);
                tpd.enableSeconds(false);
                    tpd.setAccentColor(Color.parseColor("#9C27B0"));
                    tpd.setTitle("TimePicker");
                    tpd.setTimeInterval(1, 5);
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showTermsandConditionsDialog();
                init();
                getData();

            }
        });
    }
    public void  init()
    {
        donorid= Details.user.getUsername();
        donorname=Details.user.getName();
        blood_group=Fetcher.detailsList.get(position).getBlood_group();
        patient_id=Fetcher.detailsList.get(position).getPatient_id();
        patient_name=Fetcher.detailsList.get(position).getName();
        t_o_s= new Timestamp(System.currentTimeMillis()).toString();
       // System.out.println(t_o_s);
        hosiptal_name=Fetcher.detailsList.get(position).getHop_name();
        phone=Details.user.getPhone();
        t_o_a=tv5.getText().toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                res="";

                progressDialog.show();
                progressDialog.setMessage("Loading");
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/notify.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("donor_id","UTF-8")+"="+URLEncoder.encode(donorid,"UTF-8")+"&"
                            +URLEncoder.encode("donor_name","UTF-8")+"="+URLEncoder.encode(donorname,"UTF-8")+"&"
                            +URLEncoder.encode("patient_id","UTF-8")+"="+URLEncoder.encode(patient_id,"UTF-8")+"&"
                            +URLEncoder.encode("patient_name","UTF-8")+"="+URLEncoder.encode(patient_name,"UTF-8")+"&"
                            +URLEncoder.encode("donor_phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"
                            +URLEncoder.encode("patient_blood_group","UTF-8")+"="+URLEncoder.encode(blood_group,"UTF-8")+"&"
                            +URLEncoder.encode("time_of_submit","UTF-8")+"="+URLEncoder.encode(t_o_s,"UTF-8")+"&"
                            +URLEncoder.encode("hospital_name","UTF-8")+"="+URLEncoder.encode(hosiptal_name,"UTF-8")+"&"
                            +URLEncoder.encode("time_to_arrive","UTF-8")+"="+URLEncoder.encode(t_o_a,"UTF-8");


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
                progressDialog.dismiss();
                if(res.equalsIgnoreCase("Success")) {
                    AlertDialog.Builder alertDialogBuilder =
                            new AlertDialog.Builder(BloodDonationActivity.this)
                                    .setTitle("Success")
                                    .setMessage("You Request has been accepted");

                    AlertDialog alertDialog = alertDialogBuilder.show();
                    alertDialog.setCanceledOnTouchOutside(true);
                    Intent intent=new Intent(BloodDonationActivity.this,Notification.class);
                    intent.putExtra("starttimer",11);
                    alertDialog.dismiss();
                    startActivity(intent);

                    finish();
                }
                else
                {

                    AlertDialog.Builder alertDialogBuilder =
                            new AlertDialog.Builder(BloodDonationActivity.this)
                                    .setTitle("Failure")
                                    .setMessage("You cant donate blood more than one hospital at same time");

                    AlertDialog alertDialog = alertDialogBuilder.show();
                    alertDialog.setCanceledOnTouchOutside(true);

                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


}
