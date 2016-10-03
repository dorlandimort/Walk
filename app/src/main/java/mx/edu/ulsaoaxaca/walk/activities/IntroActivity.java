package mx.edu.ulsaoaxaca.walk.activities;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import mx.edu.ulsaoaxaca.walk.R;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("splash", 0);
        boolean seen = false;
        this.addSlides();
        /*if (sp.contains("seen")) {
            seen = sp.getBoolean("seen", false);
        }
        if (seen) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }*/

    }

    private void addSlides() {
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorPrimaryDark)
                .image(R.drawable.heart_10)
                .title("Walk!")
                .description("Una aplicación que mide tus pasos y calorías.")
                .build()
        );

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide2Primary)
                .buttonsColor(R.color.slide2Dark)
                .image(R.drawable.dieta)
                .title("Come saludable")
                .description("Te recomendaremos algunas dietas para nutrirte bien :)")
                .build()
        );

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide3Primary)
                .buttonsColor(R.color.slide3Dark)
                .neededPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(R.drawable.avatar)
                .title("Espera")
                .description("Antes de continuar necesitamos que nos des permisos para acceder a tu ubicación.")
                .build()
        );
    }

    @Override
    public void onFinish() {
        super.onFinish();
        SharedPreferences sp = getSharedPreferences("splash", 0);
        sp.edit().putBoolean("seen", true).commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
