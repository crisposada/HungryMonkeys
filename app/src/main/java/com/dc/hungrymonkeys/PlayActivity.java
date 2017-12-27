package com.dc.hungrymonkeys;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by CristinaPosada on 11/11/2017.
 */

public class PlayActivity extends Activity {

    @BindView(R.id.Matrix)
    View mMatrix;
    @BindView(R.id.Number_apple)
    public TextView mApple1;
    @BindView(R.id.Number_apple2)
    TextView mApple2;
    @BindView(R.id.Number_orange)
    TextView mOrange1;
    @BindView(R.id.Number_orange2)
    TextView mOrange2;
    @BindView(R.id.Number_pear)
    TextView mPear1;
    @BindView(R.id.Number_pear2)
    TextView mPear2;
    @BindView(R.id.animacion)
    LottieAnimationView animacion;
    @BindView(R.id.pantalla_de_juego)
    RelativeLayout pantalla_de_juego;
    @BindView(R.id.Fondo)
    RelativeLayout fondo;
    @BindView(R.id.player1_apple_row)
    TableRow player1_apple_row;
    @BindView(R.id.player1_pear_row)
    TableRow player1_pear_row;
    @BindView(R.id.player1_orange_row)
    TableRow player1_orange_row;
    @BindView(R.id.playerAI_apple_row)
    TableRow playerAI_apple_row;
    @BindView(R.id.playerAI_pear_row)
    TableRow playerAI_pear_row;
    @BindView(R.id.playerAI_orange_row)
    TableRow playerAI_orange_row;




    public MediaPlayer win, lose, casilla, fruta, confundirse, mono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        win = MediaPlayer.create(this,R.raw.win);
        lose = MediaPlayer.create(this,R.raw.lose);
        casilla = MediaPlayer.create(this,R.raw.casilla);
        fruta = MediaPlayer.create(this,R.raw.fruta);
        confundirse = MediaPlayer.create(this,R.raw.confundirse);
        mono= MediaPlayer.create(this,R.raw.mono);

    }

    public void partidaFinalizada(boolean ganada){
        LottieComposition.Factory.fromAssetFileName(this, ganada?"trophy.json":"crying.json", new OnCompositionLoadedListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                animacion.setComposition(composition);
                animacion.loop(true);
                animacion.playAnimation();
                fondo.setBackgroundColor(getResources().getColor(R.color.white));
                pantalla_de_juego.setVisibility(View.GONE);
                fondo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        onBackPressed();
                        return false;
                    }
                });
            }
        });

        if(ganada) {
            if(MainActivity.mp.isPlaying()){
                MainActivity.mp.pause();
                win.start();
                win.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        MainActivity.mp.start();
                    }
                });
            }else
                win.start();
        } else {
            if(MainActivity.mp.isPlaying()) {
                MainActivity.mp.pause();
                lose.start();
                lose.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        MainActivity.mp.start();
                    }
                });
            }else{
                lose.start();
            }
        }
    }

    @Override
    public void onBackPressed() {

        if(lose.isPlaying()){
            lose.stop();
        }
        if(win.isPlaying()){
            win.stop();
        }

        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
    @Override
    protected void onPause() {
        finish();
        super.onPause();

    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();

    }
}
