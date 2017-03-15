package com.donars.srp.hosp;

/**
 * Created by Lakshman on 10/23/2016.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.donars.srp.hosp.fetcher.Details;
import com.donars.srp.hosp.model.NotificationModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;


public class FragmentPendingNotification extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int mSelectedItem=-11;
    ListView lV;
    NotificationListAdapter adapter;
    // TODO: Rename and change types of parameters
    private String TAG="PendingNotif";
    private String mParam2;
    String res,rate;
    private ArrayList<NotificationModel> notificationList;
    ProgressDialog progressDialog;
    AppCompatButton cancel_btn,rate_btn;
    LinearLayout btm_bar;
    public FragmentPendingNotification() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static FragmentPendingNotification newInstance(String param1, String param2) {
        FragmentPendingNotification fragment = new FragmentPendingNotification();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_pending, container, false);
        cancel_btn=(AppCompatButton)v.findViewById(R.id.delete_btn);
        rate_btn=(AppCompatButton)v.findViewById(R.id.completed_btn);
        btm_bar=(LinearLayout)v.findViewById(R.id.btm_bar);
        notificationList=new ArrayList<>();
        lV = (ListView) v.findViewById(android.R.id.list);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedItem!=-11)
                    ShowCancelRequestDialog();
            }
        });
        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedItem!=-11)
                    showAcceptDialog();
            }
        });
        lV.setEmptyView(v.findViewById(R.id.emptyElement));
        getData();
        adapter = new NotificationListAdapter(getContext(), notificationList);
        //

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        lV.setAdapter(adapter);
        lV = getListView();
        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getActivity(),UserDetails.class);
                intent.putExtra("username",notificationList.get(position).getPatient_id());
                intent.putExtra("mSelectedItem",position);
                startActivityForResult(intent,111);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == 111) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String data1=data.getStringExtra("option");
                int pos=data.getIntExtra("itemposition",-11);
                System.out.println(data1 + pos);
                mSelectedItem=pos;
                if(data1.equals("delete"))
                    cancel_btn.performClick();
                if(data1.equals("accept"))
                    rate_btn.performClick();
            }
        }
    }

    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            String data;
            @Override
            protected void onPreExecute() {
                Log.d(TAG, "started");data="";
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/pending_req.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "iso-8859-1"));

                    data = URLEncoder.encode("hospital_name", "UTF-8") + "=" + URLEncoder.encode(Details.hospital.getName(), "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String line = "", res = "";
                    Integer i = 1;
                    while ((line = bufferedReader.readLine()) != null) {

                        res = res + line;

                    }
                    res = res.substring(0, res.indexOf("<"));


                    Log.d(TAG+"response", data);
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
            protected void onPostExecute(String te) {
                String myJSON = te;
                //t1.setText(myJSON);
                Log.d(TAG+"ans", te);
                //  Toast.makeText(getApplicationContext(), te, Toast.LENGTH_LONG).show();

                Gson gson = new Gson();

                try {
                    Type type = new TypeToken<ArrayList<NotificationModel>>() {
                    }.getType();
                    ArrayList<NotificationModel> arr=gson.fromJson(te, type);
                    notificationList.clear();
                    notificationList.addAll(arr);

                    Log.d("notification", notificationList.get(0).getHospital_name() + notificationList.size());

                    adapter.notifyDataSetChanged();
                    btm_bar.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        GetDataJSON getDataJSON=new GetDataJSON();
        getDataJSON.execute();
    }
    public void acceptRequest(final int mSelectedItem,final String time)
    {
        class GetDataJSON extends AsyncTask<String, Void, Boolean>{
            String data="";
            @Override
            protected void onPreExecute() {
                res="";
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/update_accept_status.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("doctor_name","UTF-8")+"="+URLEncoder.encode(notificationList.get(mSelectedItem).getDoctor_name(),"UTF-8")+"&"
                            +URLEncoder.encode("patient_id","UTF-8")+"="+URLEncoder.encode(notificationList.get(mSelectedItem).getPatient_id(),"UTF-8")+"&"
                            +URLEncoder.encode("time","UTF-8")+"="+URLEncoder.encode(time,"UTF-8");

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
                    Log.d("resulttt",data);
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

                notificationList.remove(mSelectedItem);
                adapter.notifyDataSetChanged();
                btm_bar.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();
//                la=showList();
//              if (te != null) return te;
//                // t1.setText(te);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
    public void showAcceptDialog() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity());

// Setting Dialog Title
        alertDialog2.setTitle("Confirm Accept...");

// Setting Dialog Message
        alertDialog2.setMessage("Are you sure you want the accept Donor request?");

        final EditText editText=new EditText(getActivity());
        alertDialog2.setView(editText);
        editText.setHint("Enter Time");
        editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar now = Calendar.getInstance();
                        TimePickerDialog tpd = TimePickerDialog.newInstance(
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                        String time = hourOfDay+":"+minute;
                                        editText.setText(time);
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
                        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
                    }
                });
// Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        acceptRequest(mSelectedItem,editText.getText().toString());
                    }
                });

// Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();


    }
    public void ShowCancelRequestDialog()
    {
        final Dialog popDialog = new Dialog(getActivity());
        popDialog.setContentView(R.layout.dialog_box_cancel_user);;
        final EditText editText=(EditText)popDialog.findViewById(R.id.edit_text1);
        final AppCompatButton btn_ok,btn_cancel;
        btn_ok=(AppCompatButton)popDialog.findViewById(R.id.ok_btn);
        btn_cancel=(AppCompatButton)popDialog.findViewById(R.id.cancel_btn);
        popDialog.setTitle("Cancel the Patient Request");

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popDialog.hide();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback=editText.getText().toString();
                deleteNotif(feedback);
                popDialog.hide();
            }
        });


        popDialog.show();

    }
    public void deleteNotif(final String feedback_c)
    {
        class GetDataJSON extends AsyncTask<String, Void, Boolean>{

            @Override
            protected void onPreExecute() {
                res="";
                progressDialog.show();
                progressDialog.setTitle("Canceling User request");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/cancelnot.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("doctor_name","UTF-8")+"="+URLEncoder.encode(notificationList.get(mSelectedItem).getDoctor_name(),"UTF-8")+"&"
                            +URLEncoder.encode("patient_id","UTF-8")+"="+URLEncoder.encode(notificationList.get(mSelectedItem).getPatient_id(),"UTF-8")+"&"
                            +URLEncoder.encode("feedback","UTF-8")+"="+URLEncoder.encode(feedback_c,"UTF-8");
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
                Log.d("resulttt",res+(res.equalsIgnoreCase("Failure"))+"");
                //progressDialog.dismiss();
                notificationList.remove(mSelectedItem);
                adapter.notifyDataSetChanged();
                btm_bar.setVisibility(View.INVISIBLE);
                Hosp2Activity.fab.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();

    }


}
