package mx.edu.ulsaoaxaca.walk.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import mx.edu.ulsaoaxaca.walk.DietaAdapter;
import mx.edu.ulsaoaxaca.walk.R;
import mx.edu.ulsaoaxaca.walk.pojos.Comida;
import mx.edu.ulsaoaxaca.walk.pojos.Dieta;

public class InicioActivity extends Activity {

    private ListView comidaList;
    private TextView txtPasos;
    private TextView txtCalorias;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        this.init();

    }

    private void init() {


        List<String> items = new LinkedList<>();
        items.add("Inicio");
        items.add("Estadisticas");
        items.add("Editar datos");
        items.add("Cerrar Sesion");


        this.comidaList = (ListView) findViewById(R.id.listDieta);
        this.txtPasos = (TextView) findViewById(R.id.txtPasos);
        this.txtCalorias = (TextView) findViewById(R.id.txtCalorias);
        SharedPreferences sp = getSharedPreferences("persona", 0);
        this.txtPasos.setText(sp.getString("pasos", "-"));
        this.txtCalorias.setText(sp.getString("calorias", "-"));

        Intent i = getIntent();
        Bundle b = i.getExtras();
        Dieta dieta = (Dieta) b.get("dieta");
        DietaAdapter adapter = new DietaAdapter(this, dieta.getComidas());
        this.comidaList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            // Create a new fragment and specify the planet to show based on position

        }


    }

}
