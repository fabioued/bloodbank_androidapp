package com.donars.srp.hosp;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;
    @BindView(R.id.input_address) TextView _address;
    @BindView(R.id.input_phone) TextView _phone;
    @BindView(R.id.input_weight) EditText _weight;
    @BindView(R.id.input_ndonations) EditText _nooftimes;

    @BindView(R.id.input_lastd)TextView _lastd;
    @BindView(R.id.input_dob)TextView _dob;
    @BindView(R.id.input_age)EditText _age;
    @BindView(R.id.input_drink)AppCompatCheckBox _drink;
    RadioGroup radioSexGroup;
    String gender,name,pass,email,myJSON,res,blood_grp,address,phone;

     ProgressDialog progressDialog=null;
    String weight,drunk,lastd,dob,age,nooftimes;
    Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        // Spinner element

        spinner = (Spinner) findViewById(R.id.spinner1);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);



        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
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
                                _dob.setText("D.O.B :"+dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
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

        _lastd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                _lastd.setText("Last Donated Donated :"+ dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
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

    public void signup() {
        Log.d(TAG, "Signup");


        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this);


        onSignupSuccess();

    }


    public void onSignupSuccess() {
        if(validate()) {
            _signupButton.setEnabled(false);
            getData();
            System.out.println("Success in validating");
        }
        else {
            onSignupFailed();
        }
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Enter Valid Details ", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        // get selected radio button from radioGroup
        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
        gender=radioSexButton.getText().toString();
        weight = _weight.getText().toString();
        drunk=_drink.isChecked()?"Yes":"No";
        lastd=_lastd.getText().toString();
        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        pass = _passwordText.getText().toString();
        address=_address.getText().toString();
        phone=_phone.getText().toString();

        dob=_dob.getText().toString();

        age=_age.getText().toString();
        nooftimes=_nooftimes.getText().toString();
        // TODO: Implement your own signup logic here .
        blood_grp = spinner.getSelectedItem().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        if (phone.isEmpty() || phone.length()!=10) {
            _phone.setError("enter a valid phone Number");
            valid = false;
        } else {
            _phone.setError(null);
        }
        if (address.isEmpty() ) {
            _address.setError("enter a valid Address");
            valid = false;
        } else {
            _address.setError(null);
        }

        if (weight.isEmpty() || (Integer.valueOf(weight)<45) ) {
            _weight.setError("Minimum Weight is 45");
            valid = false;
        } else {
            _weight.setError(null);
        }

        if (age.isEmpty() || (Integer.valueOf(age)<18 && Integer.valueOf(age)>61) ) {
            _age.setError("Age Limit is 18-60");
            valid = false;
        } else {
            _age.setError(null);
        }
        if(drunk.isEmpty())
        {
            _drink.setError("enter a valid status");
            valid = false;
        } else {
            _drink.setError(null);
        }
        if(nooftimes.isEmpty())
        {
            _nooftimes.setError("enter a valid number");
            valid = false;
        } else {
            _nooftimes.setError(null);
        }
        if(dob.isEmpty())
        {
            _dob.setError("enter a valid dob");
            valid = false;
        } else {
            _dob.setError(null);

        }
        try {
            dob = lastd.substring(dob.indexOf('B'));
            lastd = lastd.substring(lastd.lastIndexOf('d')+1);
        }catch (Exception e){e.getMessage();}
        if(valid) {
            Log.d("request ", name + email + pass + address + gender + blood_grp + drunk + dob + nooftimes + weight + age + lastd);
            System.out.println("valid");
        }
        return valid;
    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/register.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                            +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8")+"&"
                            +URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8")+"&"
                            +URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                            +URLEncoder.encode("blood_group","UTF-8")+"="+URLEncoder.encode(blood_grp,"UTF-8")+"&"
                            +URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"
                            +URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"
                            +URLEncoder.encode("drinking_habit","UTF-8")+"="+URLEncoder.encode(drunk,"UTF-8")+"&"
                            +URLEncoder.encode("weight","UTF-8")+"="+URLEncoder.encode(weight,"UTF-8")+"&"
                            +URLEncoder.encode("no_of_times","UTF-8")+"="+URLEncoder.encode(nooftimes ,"UTF-8")+"&"
                            +URLEncoder.encode("lastdonationdate","UTF-8")+"="+URLEncoder.encode(lastd,"UTF-8")+"&"
                            +URLEncoder.encode("date_of_birth","UTF-8")+"="+URLEncoder.encode(dob,"UTF-8");

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
                 Toast.makeText(getApplicationContext(),"Signed up Successfully",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                onBackPressed();
                te="";
                // t1.setText(te);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
}