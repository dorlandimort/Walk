package mx.edu.ulsaoaxaca.walk.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import mx.edu.ulsaoaxaca.walk.DietaAdapter;
import mx.edu.ulsaoaxaca.walk.R;
import mx.edu.ulsaoaxaca.walk.pojos.Comida;

public class InicioActivity extends Activity {

    private ListView comidaList;
    private TextView txtPasos;
    private TextView txtCalorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        this.init();
        ListView comidas = (ListView) findViewById(R.id.listDieta);
    }

    private void init() {
        SharedPreferences sp = getSharedPreferences("persona", 0);
        this.txtPasos.setText(sp.getString("pasos", "-"));
        this.txtCalorias.setText(sp.getString("calorias", "-"));
    }

}
