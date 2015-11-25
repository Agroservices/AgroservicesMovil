package com.agroservices.user.agroservices;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Ubicacion;


public class Mapa extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener{

    GoogleMap map;
    GoogleApiClient mGoogleApiClient;
    LatLng origen = new LatLng(4.3364751,-74.3697546);
    Ubicacion destino;
    Location mLastLocation;
    private static final String TAG = Mapa.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        destino = (Ubicacion)bundle.get("DESTINO");
        MapFragment fragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        map = fragment.getMap();
        buildGoogleApiClient();
    }


    private String getUrl(LatLng origen, LatLng destino){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("maps.googleapis.com")
                .appendPath("maps")
                .appendPath("api")
                .appendPath("directions")
                .appendPath("json")
                .appendQueryParameter("origin",origen.latitude+","+origen.longitude)
                .appendQueryParameter("destination",destino.latitude+","+destino.longitude)
                .appendQueryParameter("key", "AIzaSyDnnmgi40JjFpgC_tEywJ07ZZQiaoQ3Swk");
        return builder.build().toString();
    }

    private void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation!=null){
            AsyncTask<LatLng,Void,String> task = new AsyncTask<LatLng, Void, String>() {

                @Override
                protected String doInBackground(LatLng... params) {
                    StringBuilder builder = new StringBuilder();
                    HttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(getUrl(params[0],params[1]));
                    System.out.println(httpGet.getURI());
                    try {
                        HttpResponse response = client.execute(httpGet);
                        StatusLine statusLine = response.getStatusLine();
                        HttpEntity entity = response.getEntity();
                        InputStream content = entity.getContent();
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(content));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                        Log.e(MainActivity.class.toString(),
                                "GET request failed" + e.getLocalizedMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(MainActivity.class.toString(),
                                "GET request failed"+e.getLocalizedMessage());
                    }
                    System.out.println(builder.toString());
                    return builder.toString();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //txt.setText(s);
                    Log.i(TAG,s);
                    ParserTask parserTask = new ParserTask();

                    // Invokes the thread for parsing the JSON data
                    parserTask.execute(s);
                }
            };
            origen = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            LatLng destinoMapa = new LatLng(destino.getLatitude(),destino.getLongitude());
            task.execute(origen, destinoMapa);
            CameraPosition camera = new CameraPosition.Builder()
                    .zoom(13)
                    .target(origen)
                    .build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG,"Connection Suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG,"Connection Failed");
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }
            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }

}
