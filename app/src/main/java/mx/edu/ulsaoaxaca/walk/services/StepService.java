package mx.edu.ulsaoaxaca.walk.services;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.edu.ulsaoaxaca.walk.Utilities;
import mx.edu.ulsaoaxaca.walk.pojos.Point;


public class StepService extends Service implements LocationListener {
    private LocationManager lom;
    private Point point1;
    private Point point2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getLocation();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
    }

    private void getLocation() {
        this.lom = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.lom.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
    }
/****************************************/
    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Toast.makeText(getApplicationContext(),
                "Latitud: " + lat + " Longitud: " + lng, Toast.LENGTH_LONG
        ).show();

        Point point = new Point();
        point.setLat(lat);
        point.setLn(lng);

        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        Log.e("", date);
        point.setFecha(date);
        this.setPoint(point);
    }

    private void setPoint(Point point) {
        if (this.point1 == null)
            this.point1 = point;
        else {
            this.point2 = point;
            SendPoints send = new SendPoints(this.point1, this.point2);
            send.execute();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public class SendPoints extends AsyncTask<Void, Void, String> {
        private Point pointA;
        private Point pointB;

        public SendPoints(Point pointA, Point pointB) {
            this.pointA = pointA;
            this.pointB = pointB;
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder result = new StringBuilder();
            JSONObject json = new JSONObject();
            SharedPreferences sp = getSharedPreferences("persona", 0);
            try {
                json.put("id", sp.getString("id", "1"));
                json.put("lat1", this.pointA.getLat());
                json.put("ln1", this.pointA.getLn());
                json.put("lat2", this.pointB.getLat());
                json.put("ln2", this.pointB.getLn());
                json.put("hora1", this.pointA.getFecha());
                json.put("hora2", this.pointB.getFecha());

                URL url = new URL(Utilities.SERVER_URL + "bitacora/submit");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "plain/text");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                DataOutputStream output;

                output = new DataOutputStream(conn.getOutputStream());
                output.writeBytes(json.toString());
                output.flush();
                output.close();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                } else {
                    InputStream in = new BufferedInputStream(conn.getErrorStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        Log.e("Error:", line);
                    }
                    reader.close();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(! result.isEmpty()) {
                Log.d("resultdao", result);
                try {
                    JSONObject json = new JSONObject(result);
                    SharedPreferences sp = getSharedPreferences("datos", 0);
                    sp.edit().putString("totalPasosHoy", json.getString("totalPasosHoy")).commit();
                    sp.edit().putString("totalCaloriasHoy", json.getString("totalCaloriasHoy")).commit();
                    sp.edit().putString("totalPasosSemana", json.getString("totalPasosSemana")).commit();
                    sp.edit().putString("totalCaloriasSemana", json.getString("totalCaloriasSemana")).commit();
                    Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_SHORT);
                    /* guardar el historial*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            point1 = point2;
        }
    }
}
