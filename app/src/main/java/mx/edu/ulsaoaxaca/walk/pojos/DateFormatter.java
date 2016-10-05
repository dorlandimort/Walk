package mx.edu.ulsaoaxaca.walk.pojos;

import com.jjoe64.graphview.DefaultLabelFormatter;

/**
 * Created by Dorlan on 05/10/2016.
 */
public class DateFormatter extends DefaultLabelFormatter {

    @Override
    public String formatLabel(double value, boolean isValueX) {
        if (isValueX) {
            return super.formatLabel(value, isValueX);
        } else {
            // show currency for y values
            return super.formatLabel(value, isValueX) + " €";
        }
    }
}
