package com.agroservices.user.agroservices;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.Despacho;
import model.DespachoAdapter;
import model.DetalleFacturaId;


public class ListarDespachos extends ActionBarActivity {


    ArrayList<Despacho> despachoData = null;

    private DespachoAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_despachos);
        Log.i(ListarDespachos.class.toString(),"Creando Actividad para listar despachos");
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        int idRuta = bundle.getInt("CODIGO_RUTA");
        despachoData = new ArrayList<>();
        loadDespachos(idRuta);
        adapter = new DespachoAdapter(this,R.layout.listview_item_row,
                despachoData);
        final ListView listView1 = (ListView)findViewById(R.id.listView1);
        View header = (View)getLayoutInflater().inflate(R.layout.listview_header,null);
        listView1.addHeaderView(header);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= listView1.getHeaderViewsCount();
                int duration= Toast.LENGTH_LONG;
                Despacho despacho = (Despacho)adapter.getItem(position);
                Intent i = new Intent(getApplicationContext(),DetalleDespacho.class);
                System.out.println(despacho.getIdDespacho()+" "+despacho.getCantidadComprada());
                i.putExtra("CODIGO_DESPACHO",despacho.getIdDespacho());
                i.putExtra("CANTIDAD_PRODUCTO",despacho.getCantidadComprada());
                i.putExtra("CODIGO_PRODUCTO",despacho.getDetalleFacturaId().getProductosEnVentaId());
                i.putExtra("CODIGO_FACTURA",despacho.getDetalleFacturaId().getFacturasId());
                startActivity(i);
            }
        });

    }


    public void loadDespachos(int idRuta){
        Log.d(MainActivity.class.toString(), "Cargando los despachos");
        AsyncTask<String, Void ,String> asyncTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                StringBuilder builder = new StringBuilder();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://agroservices.herokuapp.com/rest/despachos/rutas/"+params[0]);
                Log.i(ListarDespachos.class.toString(),"Hallar los despachos de la ruta");
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
                JSONArray arrayDespachos = null;
                try {
                    arrayDespachos = new JSONArray(s);
                    ArrayList<Despacho> tempData = new ArrayList<Despacho>();
                    for(int i=0;i<arrayDespachos.length();i++){
                        Log.i(MainActivity.class.toString(),"Objetos JSON"+arrayDespachos.get(i).toString());
                        JSONObject tempDespacho = arrayDespachos.getJSONObject(i);
                        JSONObject detalleFactura = tempDespacho.getJSONObject("detalleFactura");
                        JSONObject detalleFacturaKey = detalleFactura.getJSONObject("id");
                        DetalleFacturaId detalleFacturaId = new DetalleFacturaId(detalleFacturaKey.getInt("productosEnVentaIdProductosEnVenta"),
                                detalleFacturaKey.getInt("facturasIdFacturas"));
                        System.out.println(detalleFactura.toString());
                        System.out.println(tempDespacho);
                        tempData.add(new Despacho(tempDespacho.getInt("idDespachos"),detalleFacturaId,detalleFactura.getDouble("cantidadComprada"),
                                detalleFactura.getDouble("precioVenta"),detalleFactura.getBoolean("yaSeEntrego")));
                    }
                    Log.v(MainActivity.class.toString(), "GET DATA " + s);
                    despachoData = tempData;
                    adapter.clear();
                    for(Despacho d:despachoData){
                        adapter.add(d);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        asyncTask.execute(idRuta+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listar_despachos, menu);
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
