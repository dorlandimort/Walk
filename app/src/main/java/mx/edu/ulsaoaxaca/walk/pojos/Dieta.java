package mx.edu.ulsaoaxaca.walk.pojos;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dorlan on 04/10/2016.
 */
public class Dieta implements Serializable {
    private List<Comida> comidas;


    public List<Comida> getComidas() {
        return comidas;
    }

    public void setComidas(List<Comida> comidas) {
        this.comidas = comidas;
    }
}
