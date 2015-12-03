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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
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
    LatLng destinoLatLng;
    boolean esCampesino;
    String direccion;
    boolean viaje;
    int idDespacho;
    private static final String TAG = Mapa.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        destino = (Ubicacion)bundle.get("DESTINO");
        esCampesino = bundle.getBoolean("ES_CAMPESINO");
        direccion = bundle.getString("DIRECCION");
        viaje = bundle.getBoolean("VIAJE");
        idDespacho = bundle.getInt("ID_DESPACHO");
        MapFragment fragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        map = fragment.getMap();
        buildGoogleApiClient();
        Button button = (Button)findViewById(R.id.postEnvio);
        if(esCampesino){
            button.setText("Recoger Producto");
        }else{
            button.setText("Entregar Producto");
        }
        if(viaje)
            button.setEnabled(false);
    }


    public void postViaje(View view){
        AsyncTask<Void,Void,String>task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void ... params) {
                JSONObject despacho = new JSONObject();
                String reqResponse = null;
                String url = "";
                try{
                    despacho.put("idDespachos",0);
                    despacho.put("detalleFactura",null);
                    despacho.put("rutas",null);
                    despacho.put("estimacionRecoleccion",null);
                    despacho.put("estimacionEntrega",null);
                    if(esCampesino){
                        despacho.put("seRecogio",true);
                        despacho.put("seEntrego",false);
                        url = "https://agroservices.herokuapp.com/rest/despachos/"+idDespacho+"/seRecogio";
                    }else{
                        despacho.put("seRecogio",false);
                        despacho.put("seEntrego",true);
                        url = "https://agroservices.herokuapp.com/rest/despachos/"+idDespacho+"/seEntrego";
                    }
                    DefaultHttpClient dhhtpc=new DefaultHttpClient();
                    HttpPost postreq=new HttpPost(url);
                    //agregar la versión textual del documento jSON a la petición
                    StringEntity se=new StringEntity(despacho.toString());
                    se.setContentType("application/json;charset=UTF-8");
                    se.setContentEncoding(new
                            BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                    postreq.setEntity(se);
                    //ejecutar la petición
                    HttpResponse httpr=dhhtpc.execute(postreq);
                    //Para obtener la respuesta:
                    reqResponse= EntityUtils.toString(httpr.getEntity());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(Mapa.class.toString(),"Error en la peticion post");
                }
                return reqResponse;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast toast = null;
                if(s!=null) {
                    Log.i(Mapa.class.toString(), "Post Exitoso");
                    toast = Toast.makeText(getApplicationContext(),"Se ha actualizado exitosamente la informacion",
                            Toast.LENGTH_LONG);
                }else{
                    Log.i(Mapa.class.toString(),"Error en el POST");
                    toast = Toast.makeText(getApplicationContext(),"No se ha podido actualizar la información",
                            Toast.LENGTH_LONG);
                }
                toast.show();
            }
        };
        task.execute();
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
            destinoLatLng = new LatLng(destino.getLatitude(),destino.getLongitude());
            task.execute(origen, destinoLatLng);
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
            MarkerOptions home = new MarkerOptions()
                    .position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()))
                    .title("Tu posicion");
            String title ="Direccion Entrega: ";
            if(esCampesino)
                title = "Destino Recogida: ";
            title+=direccion;
            MarkerOptions goal = new MarkerOptions()
                    .position(destinoLatLng)
                    .title(title);
            map.addMarker(home);
            map.addMarker(goal);
        }
    }

}
