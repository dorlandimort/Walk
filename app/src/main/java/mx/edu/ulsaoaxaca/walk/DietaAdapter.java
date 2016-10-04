package mx.edu.ulsaoaxaca.walk;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mx.edu.ulsaoaxaca.walk.pojos.Comida;

/**
 * Created by Dorlan on 04/10/2016.
 */
public class DietaAdapter extends ArrayAdapter<Comida> {

    private Activity context;
    private List<Comida> comidas;

    public DietaAdapter(Activity context, int resource, List<Comida> objects) {
        super(context, resource, objects);
        this.context = context;
        this.comidas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.dieta_layout, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.nombre = (TextView) rowView.findViewById(R.id.txtNombre);
            viewHolder.cantidad = (TextView) rowView.findViewById(R.id.txtCantidad);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Comida comida = this.comidas.get(position);
        holder.nombre.setText(comida.getNombre());
        holder.cantidad.setText(comida.getCantidad());
        return rowView;
    }

    class ViewHolder {
        private TextView nombre;
        private TextView cantidad;
    }
}
