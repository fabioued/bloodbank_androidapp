package com.donars.srp.bloodbank.fetcher;

/**
 * Created by Lakshman on 9/19/2016.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.donars.srp.bloodbank.MapsActivity;
import com.donars.srp.bloodbank.model.BloodModel;
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
import java.util.List;

public class Fetcher {
    public static List<BloodModel> detailsList;
    String res;
    public void getData() {
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
    private class GetDataJSON extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Log.d("tete","started");
        }

        @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://lifesaver.net23.net/getReq.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"iso-8859-1"));
            String data= URLEncoder.encode("blood_grp","UTF-8")+"="+URLEncoder.encode(Details.user.getBlood_GRP(),"UTF-8");
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
            Type type = new TypeToken<List<BloodModel>>() {
            }.getType();
            detailsList = gson.fromJson(te, type);
        }
        catch (Exception e)
        {e.printStackTrace();}
        MapsActivity.addExtraMarkers();
    }
}
}