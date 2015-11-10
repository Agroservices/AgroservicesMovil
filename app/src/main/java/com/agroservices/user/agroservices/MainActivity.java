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
import android.widget.EditText;
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
import java.util.Date;

import model.Ruta;
import model.RutaAdapter;


public class MainActivity extends ActionBarActivity {


    private ArrayList<Ruta> rutasData = null;

    private RutaAdapter adapterRuta = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(MainActivity.class.toString(),"Creando la actividad principal");
        rutasData = new ArrayList<Ruta>();
        rutasData.add(new Ruta(1,null,null));
        rutasData.add(new Ruta(2,null,null));
        rutasData.add(new Ruta(3,null,null));
        rutasData.add(new Ruta(4,null,null));
        rutasData.add(new Ruta(5,null,null));
        adapterRuta = new RutaAdapter(this, R.layout.listview_item_ruta_row,
                rutasData);
        final ListView listViewRuta = (ListView)findViewById(R.id.listViewRuta);
        View header = (View)getLayoutInflater().inflate(R.layout.listview_rutas_header,null);
        listViewRuta.addHeaderView(header);
        listViewRuta.setAdapter(adapterRuta);
        listViewRuta.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= listViewRuta.getHeaderViewsCount();
                int duration= Toast.LENGTH_LONG;
                //Toast.makeText(view," ",duration).show();
                Ruta ruta = (Ruta)adapterRuta.getItem(position);
                Intent i = new Intent(getApplicationContext(),ListarDespachos.class);
                System.out.println(ruta.getIdRutas()+" "+ruta.getFechaInicio());
                i.putExtra("CODIGO_RUTA",ruta.getIdRutas());
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void cargarRutas(View View){
        EditText text = (EditText)findViewById(R.id.id_transportista);
        int idTransportista = Integer.parseInt(text.getText().toString());
        Log.i(MainActivity.class.toString(),"Cargando rutas");
        AsyncTask<String,Void,String>task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                StringBuilder builder = new StringBuilder();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://agroservices.herokuapp.com/rest/rutas/transportista/"+params[0]);
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
                JSONArray arrayRutas = null;
                try {
                    arrayRutas = new JSONArray(s);
                    ArrayList<Ruta> tempData = new ArrayList<Ruta>();
                    for(int i=0;i<arrayRutas.length();i++){
                        Log.i(MainActivity.class.toString(),"Objetos JSON"+arrayRutas.get(i).toString());
                        JSONObject tempRuta = arrayRutas.getJSONObject(i);
                        System.out.println(tempRuta.toString());
                        tempData.add(new Ruta(tempRuta.getInt("idRutas"),new Date()
                                ,new Date()));
                    }
                    Log.v(MainActivity.class.toString(), "GET DATA " + s);
                    rutasData = tempData;
                    adapterRuta.clear();
                    for(Ruta r:rutasData){
                        adapterRuta.add(r);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute(idTransportista+"");
    }

}
