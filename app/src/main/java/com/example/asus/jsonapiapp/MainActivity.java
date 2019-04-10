package com.example.asus.jsonapiapp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private ProgressDialog pDialog;

    private static String url = "https:http://api.banghasan.com/sholat/kota/"
    ArrayList<HashMap<String, String>> kotaList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kotaList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url:" + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray kota = jsonObj.getJSONArray("kota");

                    for (int i = 0; i < kota.length(); i++) {
                        JSONObject c = kota.getJSONObject(i);

                        String id = c.getString("id");
                        String nama = c.getString("nama");

                        HashMap<String, String> kota = new HashMap<>();

                        kota.put("id", id);
                        kota.put("nama", nama);

                        kotaList.add(kota);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error:" + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error:" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            id(pDialog.isShowing())
            pDialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, kotaList,
                    R.layout.list_item,
                    new String[]{"id", "nama"},
                    new int {
            } {
                R.id.id, R.id.nama
            });
            lv.setAdapter(adapter);
        }
    }
}

