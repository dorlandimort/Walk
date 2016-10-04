package mx.edu.ulsaoaxaca.walk.pojos;

import java.io.Serializable;

/**
 * Created by Dorlan on 04/10/2016.
 */
public class Comida implements Serializable {

    private String nombre;
    private String cantidad;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
