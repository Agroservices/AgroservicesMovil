package com.agroservices.user.agroservices;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DetalleDespacho extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_despacho);
        Log.i(DetalleDespacho.class.toString(),"Crear la Actividad para ver detalle despachos");
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        int codigoDespacho = bundle.getInt("CODIGO_DESPACHO");
        double cantidadProducto = bundle.getDouble("CANTIDAD_PRODUCTO");
        int idProductoEnVenta = bundle.getInt("CODIGO_PRODUCTO");
        int idFactura = bundle.getInt("CODIGO_FACTURA");
        TextView campoCantidad = (TextView)findViewById(R.id.cantidad_producto);
        campoCantidad.setText(cantidadProducto+"");
        TextView campoCodigo = (TextView)findViewById(R.id.codigo_despacho);
        campoCodigo.setText(codigoDespacho+"");

        AsyncTask<String,Void,String> task1 = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                StringBuilder builder = new StringBuilder();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://agroservices.herokuapp.com/rest/productosEnVenta/"+params[0]+"/ubicacion");
                Log.i(DetalleDespacho.class.toString(),"Hallar la ubicacion de recogida del producto");
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
                return builder.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject ubicacion = null;
                try {
                    ubicacion = new JSONObject(s);
                    String direccionRecogida = ubicacion.getString("direccion");
                    String ciudadRecogida = ubicacion.getString("ciudad");
                    TextView text =(TextView)findViewById(R.id.direccion_recogida);
                    text.setText(direccionRecogida);
                    text = (TextView)findViewById(R.id.ciudad_recogida);
                    text.setText(ciudadRecogida);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        task1.execute(idProductoEnVenta+"");

        AsyncTask<String,Void,String>task2 = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                StringBuilder builder = new StringBuilder();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://agroservices.herokuapp.com/rest/ventas/factura/"+params[0]+"/ubicacion");
                Log.i(DetalleDespacho.class.toString(),"Hallar la ubicacion de entrega del producto");
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
                return builder.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject ubicacion = null;
                try {
                    ubicacion = new JSONObject(s);
                    String direccionEntrega = ubicacion.getString("direccion");
                    String ciudadEntrega = ubicacion.getString("ciudad");
                    TextView text =(TextView)findViewById(R.id.direccion_entrega);
                    text.setText(direccionEntrega);
                    text = (TextView)findViewById(R.id.ciudad_entrega);
                    text.setText(ciudadEntrega);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        task2.execute(idFactura+"");

        AsyncTask<String,Void,String>task3 = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                StringBuilder builder = new StringBuilder();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://agroservices.herokuapp.com/rest/productosEnVenta/"+params[0]+"/nombre");
                Log.i(DetalleDespacho.class.toString(),"Hallar el producto del despacho");
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
                return builder.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String nombre = s;
                TextView text =(TextView)findViewById(R.id.nombre_producto);
                text.setText(s);
            }
        };

        task3.execute(idProductoEnVenta+"");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle_despacho, menu);
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
}
