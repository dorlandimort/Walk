package mx.edu.ulsaoaxaca.walk.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import im.dacer.androidcharts.LineView;
import mx.edu.ulsaoaxaca.walk.R;
import mx.edu.ulsaoaxaca.walk.Utilities;

public class HistorialActivity extends AppCompatActivity {

    private LineView lineView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);
        lineView = (LineView)findViewById(R.id.line_view);
        lineView.setDrawDotLine(false); //optional
        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional

        HistorialTask task = new HistorialTask();
        task.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }




    public void setData(ArrayList<ArrayList<Integer>> data) {

        lineView.setDataList(data);
    }

    public void setLabels(List<String> labels) {
        lineView.setBottomTextList((ArrayList<String>) labels);
    }

    public class HistorialTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder result = new StringBuilder();

            try {
                SharedPreferences sp = getSharedPreferences("persona", 0);
                String id = sp.getString("id", "");
                URL url = new URL(Utilities.SERVER_URL + "bitacora/historial/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "plain/text");
                conn.setUseCaches(false);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.toString();

        }

        @Override
        protected void onPostExecute(String result) {
            if (! result.isEmpty()) {
                try {
                    JSONObject json = new JSONObject(result);
                    JSONArray array = json.getJSONArray("historialSemana");

                    ArrayList<String> labels = new ArrayList<>();
                    ArrayList<ArrayList<Integer>> d = new ArrayList<>();
                    ArrayList<Integer> pasos = new ArrayList<>();
                    ArrayList<Integer> calorias = new ArrayList<>();
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject j = array.getJSONObject(i);
                        labels.add(j.getString("fecha"));
                        pasos.add(j.getInt("pasos"));
                        calorias.add(j.getInt("calorias"));

                    }
                    d.add(pasos);
                    d.add(calorias);
                    setLabels(labels);
                    setData(d);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
