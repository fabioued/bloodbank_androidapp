package com.donars.srp.hosp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.donars.srp.hosp.fetcher.Details;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserStats extends AppCompatActivity {
    @BindView(R.id.input_name)
    TextView _nameText;
    @BindView(R.id.input_email) TextView _emailText;
    @BindView(R.id.input_address) TextView _address;
    @BindView(R.id.input_blood_type)
    TextView _bloodtype;
    //@BindView(R.id.input_phone)
   // TextView _phoneNo;
    @BindView(R.id.input_gender)
    TextView _gender;
    @BindView(R.id.input_lastd)
    TextView _lastd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);
        ButterKnife.bind(this);
        init();
        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    public void init()
    {
        _nameText.setText("Name:"+ Details.user.getName());
        _emailText.setText("Email:"+Details.user.getUsername());
        _address.setText("Address:"+Details.user.getAddress());
        _gender.setText("Gender:"+Details.user.getGender());
        _lastd.setText("Last Donation:"+Details.user.getTimestamp());
        //_phoneNo.setText("Phone no:"+Details.user.getPhone());
        _bloodtype.setText("Blood Type:"+Details.user.getBlood_GRP());
    }
}
