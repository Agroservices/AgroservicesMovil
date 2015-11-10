package model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agroservices.user.agroservices.R;

import java.util.ArrayList;

/**
 * Created by User on 10/11/2015.
 */
public class RutaAdapter extends ArrayAdapter {

    Context context;
    int layoutResourceId;
    ArrayList<Ruta> rutas = null;

    public RutaAdapter(Context context, int resource, ArrayList<Ruta> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.rutas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RutaHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RutaHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIconRuta);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitleRuta);

            row.setTag(holder);
        }else{
            holder = (RutaHolder)row.getTag();
        }
        Ruta ruta = rutas.get(position);
        holder.txtTitle.setText(ruta.getIdRutas()+"");
        return row;
    }

    public void updateData(ArrayList<Ruta> data){
        //clear();
        //addAll(data);
        rutas = data;
        this.notifyDataSetChanged();
    }

    static class RutaHolder{
        ImageView imgIcon;
        TextView txtTitle;
    }
}
