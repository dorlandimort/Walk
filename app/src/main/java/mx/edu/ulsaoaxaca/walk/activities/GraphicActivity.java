package mx.edu.ulsaoaxaca.walk.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import android.app.Activity;
import android.util.Log;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import im.dacer.androidcharts.LineView;
import mx.edu.ulsaoaxaca.walk.R;
import mx.edu.ulsaoaxaca.walk.Utilities;


public class GraphicActivity extends Activity {


    private LineView lineView;
    Viewport viewport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);

        //graph = (GraphView) findViewById(R.id.graph);


// set date label formatter
        /*graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        graph.getGridLabelRenderer().setHumanRounding(false);

        // data
        series = new LineGraphSeries<>();
        graph.addSeries(series);
        // customize a little bit viewport
        viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(7);
        viewport.setScrollable(true);
        // set date label formatter
*/
        //this.addEntry();
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
