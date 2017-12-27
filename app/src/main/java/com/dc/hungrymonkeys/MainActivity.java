package com.dc.hungrymonkeys;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    //Crear botones
    @BindView(R.id.boton1) Button boton1;
    @BindView(R.id.boton3) Button boton3;
    @BindView(R.id.boton4) Button boton4;
    @BindView(R.id.switch1) Switch switch1;
    static MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.jungle);

        //Capturar los clicks en el botón
        boton1.setOnClickListener(this);
        boton3.setOnClickListener(this);
        boton4.setOnClickListener(this);
        switch1.setOnCheckedChangeListener(this);

    }

    //Configurar el Listener, getTipo un mensaje al clickar en un boton
    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.boton1:
                Intent intent = new Intent(this,PlayActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.boton3:
                finish();
                System.exit(0);
                break;
            case R.id.boton4:
                Intent intent2 = new Intent(this,SettingsActivity.class);
                startActivity(intent2);
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    protected void onStop() {
        System.out.println("OnStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(mp.isPlaying())
            mp.stop();
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.getId() == R.id.switch1){
            if (b){
                mp = MediaPlayer.create(getApplicationContext(), R.raw.jungle);
                mp.setLooping(true);
                mp.start();
            }else{
                if(mp.isPlaying())
                    mp.stop();
            }
        }
    }
}
