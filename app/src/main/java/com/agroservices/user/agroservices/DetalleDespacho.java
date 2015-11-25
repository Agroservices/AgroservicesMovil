package com.agroservices.user.agroservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import model.Despacho;


public class DetalleDespacho extends ActionBarActivity {

    Despacho despacho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_despacho);
        Log.i(DetalleDespacho.class.toString(),"Crear la Actividad para ver detalle despachos");
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        despacho = (Despacho)i.getSerializableExtra("DESPACHO");
        printDespacho(despacho);
    }


    private void printDespacho(Despacho despacho){
        TextView campoCodigo = (TextView)findViewById(R.id.codigo_despacho);
        campoCodigo.setText(despacho.getIdDespacho()+"");
        TextView campoNombre = (TextView)findViewById(R.id.nombre_producto);
        campoNombre.setText(despacho.getNombreProducto());
        TextView campoCantidad = (TextView)findViewById(R.id.cantidad_producto);
        campoCantidad.setText(despacho.getCantidadComprada()+"");
        TextView campoDireccionRecogida = (TextView)findViewById(R.id.direccion_recogida);
        campoDireccionRecogida.setText(despacho.getDireccionRecogida());
        TextView campoCiudadRecogida = (TextView)findViewById(R.id.ciudad_recogida);
        campoCiudadRecogida.setText(despacho.getCiudadRecogida());
        TextView campoDireccionEntrega = (TextView)findViewById(R.id.direccion_entrega);
        campoDireccionEntrega.setText(despacho.getDireccionEntrega());
        TextView campoCiudadEntrega = (TextView)findViewById(R.id.ciudad_entrega);
        campoCiudadEntrega.setText(despacho.getCiudadEntrega());
    }

    public void llegarCampesino(View view){
        Intent i = new Intent(getApplicationContext(),Mapa.class);
        i.putExtra("DESTINO",despacho.getCoordenadasRecogida());
        i.putExtra("ES_CAMPESINO",true);
        startActivity(i);
    }

    public void llegarMinorista(View view){
        Intent i = new Intent(getApplicationContext(),Mapa.class);
        i.putExtra("DESTINO",despacho.getCoordenadasEntrega());
        i.putExtra("ES_CAMPESINO",false);
        startActivity(i);
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
