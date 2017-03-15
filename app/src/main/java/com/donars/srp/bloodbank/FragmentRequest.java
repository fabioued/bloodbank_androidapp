package com.donars.srp.bloodbank;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

import com.donars.srp.bloodbank.fetcher.Details;
import com.donars.srp.bloodbank.model.NotificationModel;
import com.donars.srp.bloodbank.model.RequestModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Admin1 on 10/16/2016.
 */

public class FragmentRequest extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int mSelectedItem=-11;
    private final  String TAG="FragmentRequest";
    ListView lV;
    RequestListAdapter adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String rate;

    private ArrayList<RequestModel> requestList;
    ProgressDialog progressDialog;

    public FragmentRequest() {
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
    public static FragmentRequest newInstance(String param1, String param2) {
        FragmentRequest fragment = new FragmentRequest();
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
        View v= inflater.inflate(R.layout.fragment_request, container, false);
        requestList=new ArrayList<>();
        lV = (ListView) v.findViewById(android.R.id.list);

        lV.setEmptyView(v.findViewById(R.id.emptyElement));
        getData();
        adapter = new RequestListAdapter(getActivity(), requestList);
        //




        //   bottomBar.setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog=new ProgressDialog(getActivity());
        lV.setAdapter(adapter);
        lV=getListView();
        lV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Succs"+position);
                //view.setSelected(true);

                mSelectedItem = position;
                lV.invalidateViews();
                ShowDialog();
                //  Hosp2Activity.fab.setVisibility(View.GONE);

                return false;
            }
        });

    }

    public void getData() {

        class GetDataJSON extends AsyncTask<String, Void, String> {
            String data;
            String res;
            @Override
            protected void onPreExecute() {
                Log.d("tete", "started");
                data="";
                res="";
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/HospgetReq.php");
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

                    Log.d("res", res);

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

                //  Toast.makeText(getApplicationContext(), te, Toast.LENGTH_LONG).show();


                Log.d("tetereq", myJSON);
                JsonReader jsonReader=new JsonReader(new StringReader(myJSON));
                try {

                    Type type = new TypeToken<ArrayList<RequestModel>>() {
                    }.getType();
                    System.out.println(jsonReader.toString());
                    System.out.println(type);
                    Gson gson = new Gson();
                    System.out.println(gson.fromJson(jsonReader, type));
                    ArrayList<RequestModel> arr=gson.fromJson(myJSON, type);
                    requestList.clear();
                    requestList.addAll(arr);

                    Log.d("requestfrag", arr.get(0).getHospital_name() + arr.size()+ data);


                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        GetDataJSON getDataJSON=new GetDataJSON();
        getDataJSON.execute();
    }
    public void deleteNotif(final String feedback)
    {

        class GetDataJSON extends AsyncTask<String, Void, Boolean>{
            String res;
            String data;
            @Override
            protected void onPreExecute() {
                res="";
                data="";
                progressDialog.show();
                System.out.println(mSelectedItem);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    URL url = new URL("http://lifesaver.net23.net/deletereq.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    data = URLEncoder.encode("patient_id","UTF-8")+"="+URLEncoder.encode(requestList.get(mSelectedItem).getPatient_id(),"UTF-8")+"&"
                            +URLEncoder.encode("feedback","UTF-8")+"="+URLEncoder.encode(feedback+"","UTF-8");
//\\
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
                Log.d("resulttt",res+(res.equalsIgnoreCase("Failure"))+""+data);
                //progressDialog.dismiss();
                /*
                * notificationList.remove(mSelectedItem);
                adapter.notifyDataSetChanged();
                * */
                mSelectedItem=-11;
                lV.invalidateViews();
                progressDialog.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();

    }
    public void ShowDialog()
    {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());
        popDialog.setTitle("Delete request");
        popDialog.setMessage("Are u sure to delete the request?");
        final EditText editText=new EditText(popDialog.getContext());
        editText.setHint("Feedback");
        editText.setLines(3);
        popDialog.setView(editText);
        // Button OK
        popDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNotif(editText.getText().toString());
                        try {
                            requestList.remove(mSelectedItem);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        //notificationList.remove(mSelectedItem);
                        //adapter.notifyDataSetChanged();

                    }

                })

                // Button Cancel
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                mSelectedItem=-11;
                                lV.invalidateViews();
                                dialog.cancel();
                            }
                        });

        popDialog.create();
        popDialog.show();

    }

}
