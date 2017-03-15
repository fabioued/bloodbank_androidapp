package com.donars.srp.bloodbank;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.donars.srp.bloodbank.fetcher.Details;
import com.donars.srp.bloodbank.model.NotificationModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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


public class FragmentNotification extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int mSelectedItem=-11;
    ListView lV;
    NotificationListAdapter adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String res,rate;
    private ArrayList<NotificationModel> notificationList;
    ProgressDialog progressDialog;
    AppCompatButton cancel_btn,rate_btn;
    LinearLayout btm_bar;
    public FragmentNotification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNotification.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNotification newInstance(String param1, String param2) {
        FragmentNotification fragment = new FragmentNotification();
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
        View v= inflater.inflate(R.layout.fragment_notification, container, false);
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
                showRatingDialog();
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

        lV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.printf("Succs\n" + position);
                //view.setSelected(true);
                lV.invalidateViews();
                mSelectedItem = position;
                adapter.notifyDataSetChanged();
                  Hosp2Activity.fab.setVisibility(View.INVISIBLE);
                  btm_bar.setVisibility(View.VISIBLE);
                return false;
            }
        });

        lV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mSelectedItem = -11;
                lV.invalidateViews();
                adapter.notifyDataSetChanged();
                  Hosp2Activity.fab.setVisibility(View.VISIBLE);
                btm_bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }


    public void getData() {
         class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                Log.d("tete", "started");
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/notret.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "iso-8859-1"));
                    String data = URLEncoder.encode("hospital_name", "UTF-8") + "=" + URLEncoder.encode(Details.hospital.getName(), "UTF-8");
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


                    Log.d("response", res);
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
                Log.d("tete", te);
                //  Toast.makeText(getApplicationContext(), te, Toast.LENGTH_LONG).show();

                Gson gson = new Gson();

                try {
                    Type type = new TypeToken<ArrayList<NotificationModel>>() {
                    }.getType();
                    ArrayList<NotificationModel> arr=gson.fromJson(te, type);
                    notificationList.clear();
                    notificationList.addAll(arr);

                    Log.d("notification", notificationList.get(0).getDonor_id() + notificationList.size());


                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        GetDataJSON getDataJSON=new GetDataJSON();
        getDataJSON.execute();
    }
    public void setRating(final int rating1,final String feedback)
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
                    URL url = new URL("http://lifesaver.net23.net/rating.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                     data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(notificationList.get(mSelectedItem).getDonor_id(),"UTF-8")+"&"
                            +URLEncoder.encode("rating","UTF-8")+"="+URLEncoder.encode(rating1+"","UTF-8")+"&"
                             +URLEncoder.encode("feedback","UTF-8")+"="+URLEncoder.encode(feedback+"","UTF-8")+"&"
                             +URLEncoder.encode("patient_id","UTF-8")+"="+URLEncoder.encode(notificationList.get(mSelectedItem).getPatient_id()+"","UTF-8");

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
                notificationList.remove(mSelectedItem);
                adapter.notifyDataSetChanged();
                btm_bar.setVisibility(View.INVISIBLE);
                Hosp2Activity.fab.setVisibility(View.VISIBLE);

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
    public void showRatingDialog() {
        final Dialog popDialog = new Dialog(getActivity());
        popDialog.setContentView(R.layout.dialog_box);;
        final EditText editText=(EditText)popDialog.findViewById(R.id.edit_text1);
        final AppCompatButton btn_ok,btn_cancel;
        final AppCompatRatingBar ratingBar=(AppCompatRatingBar)popDialog.findViewById(R.id.rating_bar) ;
        btn_ok=(AppCompatButton)popDialog.findViewById(R.id.ok_btn);
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        ratingBar.setBackgroundColor(getResources().getColor(R.color.md_amber_400));
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        btn_cancel=(AppCompatButton)popDialog.findViewById(R.id.cancel_btn);
        popDialog.setTitle("Rate the Donor");

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

                setRating(ratingBar.getNumStars(),feedback);
                popDialog.hide();
            }
        });


        popDialog.show();


    }
    public void ShowCancelRequestDialog()
    {
        final Dialog popDialog = new Dialog(getActivity());
        popDialog.setContentView(R.layout.dialog_box_cancel_user);;
        final EditText editText=(EditText)popDialog.findViewById(R.id.edit_text1);
        final AppCompatButton btn_ok,btn_cancel;
        btn_ok=(AppCompatButton)popDialog.findViewById(R.id.ok_btn);
        btn_cancel=(AppCompatButton)popDialog.findViewById(R.id.cancel_btn);
        popDialog.setTitle("Cancel the Donor Request");
        final CheckBox checkBox=(CheckBox)popDialog.findViewById(R.id.chk_drunk);

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
                Boolean blocked=checkBox.isChecked();
                deleteNotif(feedback,blocked+"");
                popDialog.hide();
            }
        });


        popDialog.show();

    }
    public void deleteNotif(final String feedback_c,final String drunk_c)
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
                    String data= URLEncoder.encode("donor_id","UTF-8")+"="+URLEncoder.encode(notificationList.get(mSelectedItem).getDonor_id(),"UTF-8")+"&"
                            +URLEncoder.encode("patient_id","UTF-8")+"="+URLEncoder.encode(notificationList.get(mSelectedItem).getPatient_id(),"UTF-8")+"&"
                            +URLEncoder.encode("feedback","UTF-8")+"="+URLEncoder.encode(feedback_c,"UTF-8")+"&"
                            +URLEncoder.encode("drunk","UTF-8")+"="+URLEncoder.encode(drunk_c,"UTF-8");
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
                /*
                * notificationList.remove(mSelectedItem);
                adapter.notifyDataSetChanged();
                * */notificationList.remove(mSelectedItem);
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
