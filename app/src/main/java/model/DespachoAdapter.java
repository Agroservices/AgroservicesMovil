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
 * Created by User on 09/11/2015.
 */
public class DespachoAdapter extends ArrayAdapter {

    Context context;
    int layoutResourceId;
    ArrayList<Despacho> despachos = null;

    public DespachoAdapter(Context context, int resource, ArrayList<Despacho> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.despachos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DespachoHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DespachoHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }else{
            holder = (DespachoHolder)row.getTag();
        }
        Despacho despacho = despachos.get(position);
        holder.txtTitle.setText(despacho.idDespacho+"");
        return row;
    }

    public void updateData(ArrayList<Despacho> data){
        //clear();
        //addAll(data);
        despachos = data;
        this.notifyDataSetChanged();
    }

    static class DespachoHolder{
        ImageView imgIcon;
        TextView txtTitle;
    }

}
