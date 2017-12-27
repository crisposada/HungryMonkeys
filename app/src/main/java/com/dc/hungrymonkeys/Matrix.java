package com.dc.hungrymonkeys;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Thread.sleep;

/**
 * Created by CristinaPosada on 13/11/2017.
 */

public class Matrix extends View implements View.OnTouchListener {

    final static int VALOR_0_0= 1;
    final static int VALOR_1_0= 4;
    final static int VALOR_2_0= 0;
    final static int VALOR_1_1= 4;


    PlayActivity activity;
    private final int ROWS=9;
    private final int COLUMNS=7;
    private int width, height;
    private final double VIEWPORT=1;
    private final int REL_DEP_TP=8;
    private int posXInicial, posXFinal;
    private int posYInicial, posYFinal;
    protected static int posX;
    protected static int posY;
    private int tamPuntos;
    protected static int altoCelda;
    protected Imprimible matrix [][] = new Imprimible [ROWS-1][COLUMNS-1];
    int [] Manzanas_Naranjas_Peras = new int [3];
    int fila=0;
    int columna=0;
    ArrayList<Fruit> Frutas = new ArrayList<Fruit>();
    Map<String, Player> Jugadores = new TreeMap<String, Player>();
    float xTouch, yTouch;
    Canvas canvas;
    int turno =0;
    boolean partida_finalizada = false;
    boolean turno_maquina=false;
    private boolean ganada;

    public Matrix(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        activity = (PlayActivity)context;
        inicializarElementos();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        //System.out.println("Ancho: "+width+" Alto: "+height);
        System.out.println("TURNO: "+turno%2);
        this.canvas =canvas;

        dibujarMatrix();
        if(!partida_finalizada){
            if (turno % 2 == 0) {
                System.out.println("ONTOUCH");
            } else {
                turno_maquina = true;
                turnoMaquina(Algoritmo("1"));
                turno++;
                turno_maquina = false;

            }
        }else{
            activity.partidaFinalizada(ganada);
        }
    }

    private void turnoMaquina(int []movimientos) {

        System.out.println("Hola turno Maquina");
        //TURNO DE LA MÁQUINA.
        if(movimientos[1]!=Jugadores.get("1").getPosicionX())
        {
            if(matrix[movimientos[1]][movimientos[2]] instanceof Fruit)
            {
                String fruit = matrix[movimientos[1]][movimientos[2]].getTipo();
                actualizarPuntuación("1",fruit);
                Frutas.remove(matrix[movimientos[1]][movimientos[2]]);
                Calcular_valor_fruta(movimientos[1],movimientos[2]);
            }
            matrix[movimientos[1]][movimientos[2]]= Jugadores.get("1");
            matrix[Jugadores.get("1").getPosicionX()][Jugadores.get("1").getPosicionY()]=new Casilla();
            Jugadores.get("1").setPosicionX(movimientos[1]);
            Jugadores.get("1").setPosicionY(movimientos[2]);
        }
        if(movimientos[2]!=Jugadores.get("1").getPosicionY())
        {
            if(matrix[movimientos[1]][movimientos[2]] instanceof Fruit)
            {
                String fruit = matrix[movimientos[1]][movimientos[2]].getTipo();
                actualizarPuntuación("1",fruit);
                Frutas.remove(matrix[movimientos[1]][movimientos[2]]);
                Calcular_valor_fruta(movimientos[1],movimientos[2]);
            }
            matrix[movimientos[1]][movimientos[2]]= Jugadores.get("1");
            matrix[Jugadores.get("1").getPosicionX()][Jugadores.get("1").getPosicionY()]=new Casilla();
            Jugadores.get("1").setPosicionX(movimientos[1]);
            Jugadores.get("1").setPosicionY(movimientos[2]);
        }
        //FIN DEL TURNO DE LA MÁQUINA.
        if(Jugadores.get("0").categorias!=2 && Jugadores.get("1").categorias!=2) {
            invalidate();
        }
        else{
            ganada=false;
            partida_finalizada=true;
            invalidate();
        }
    }
    private int[] Algoritmo(String jugador)
    {

        TreeMap<Integer, int[]> Costos = new TreeMap<Integer,int[]>();
        int fila_actual=Jugadores.get(jugador).posicionX;
        int columna_actual=Jugadores.get(jugador).posicionY;

        for(int contador=0;contador<Frutas.size();contador++)
        {
            if(Frutas.get(contador).Valor!=0 )
            {
                //System.out.println("Valor: 100-("+Dijkstra_valor(fila_actual,columna_actual,Frutas.get(contador).getFila(),Frutas.get(contador).getColumna())[0]+"*"+Frutas.get(contador).getValor()+")");
                Costos.put(100-(Dijkstra_valor(fila_actual,columna_actual,Frutas.get(contador).getFila(),Frutas.get(contador).getColumna())[0]*Frutas.get(contador).getValor()),Dijkstra_valor(fila_actual,columna_actual,Frutas.get(contador).getFila(),Frutas.get(contador).getColumna()));
            }
        }
        Iterator it =Costos.entrySet().iterator();
        Integer contador=0;
        Integer fruta=1000;

        while(it.hasNext())
        {
            Map.Entry e= (Map.Entry)it.next();
            contador= (Integer)e.getKey();

            if(fruta>contador)
            {
                fruta=contador;
            }
            //System.out.println("indice mapa:"+contador);
        }
        System.out.println("Valor: "+fruta+" "+"MOVIMIENTO: "+(Costos.get(fruta)[1]+1)+" "+(Costos.get(fruta)[2]+1));

        return Costos.get(fruta);
    }
    private  int[] Dijkstra_valor(int fila_actual, int columna_actual, Integer fila_fruta, Integer columna_fruta)
    {
        int fila= fila_actual-fila_fruta;
        int columna=columna_actual-columna_fruta;
        int costo=Math.abs(fila)+Math.abs(columna);

        if(fila<0||fila>0)
        {
            System.out.println("MOVIMIENTO FILA");
            return fila(costo,fila,columna,fila_actual,columna_actual,false);
        }
        else
        {
            System.out.println("MOVIMIENTO COLUMNA");
            return columna(costo,fila,columna,fila_actual,columna_actual,false);
        }
    }
    private  int[] fila(int costo,int fila,int columna,int fila_actual,int columna_actual,boolean re)
    {
        int[] movimiento= new int[3];
        movimiento[0]=100/costo;
        if(re)
        {
            if(fila_actual+1<7)
            {
                movimiento[1]=fila_actual+1;
                movimiento[2]=columna_actual;
            }
            else
            {
                movimiento[1]=fila_actual-1;
                movimiento[2]=columna_actual;
            }

        }
        else
        {
            if(fila<0)
            {
                movimiento[1]=fila_actual+1;
                movimiento[2]=columna_actual;
                if(matrix[movimiento[1]][movimiento[2]] instanceof Player)
                {
                    System.out.println("REMOVIMIENTO COLUMNA");
                    movimiento= columna(costo+2,fila,columna,fila_actual,columna_actual,true);
                }
                return movimiento;
            }
            if(fila>0)
            {
                movimiento[1]=fila_actual-1;
                movimiento[2]=columna_actual;
                if(matrix[movimiento[1]][movimiento[2]] instanceof Player)
                {
                    System.out.println("REMOVIMIENTO COLUMNA");
                    movimiento= columna(costo+2,fila,columna,fila_actual,columna_actual,true);
                }
                return movimiento;
            }
        }

        System.out.println("FALLE");
        return movimiento;
    }
    private int [] columna(int costo,int fila,int columna,int fila_actual,int columna_actual,boolean re)
    {
        int[] movimiento= new int[3];
        movimiento[0]=100/costo;
        if(re)
        {
            if(columna_actual+1<6)
            {
                movimiento[1]=fila_actual;
                movimiento[2]=columna_actual+1;
            }
            else
            {
                movimiento[1]=fila_actual;
                movimiento[2]=columna_actual-1;
            }
        }
        else
            {
            if (columna < 0) {
                movimiento[1] = fila_actual;
                movimiento[2] = columna_actual + 1;
                if (matrix[movimiento[1]][movimiento[2]] instanceof Player) {
                    System.out.println("REMOVIMIENTO FILA");
                    movimiento = fila(costo + 2, fila, columna, fila_actual, columna_actual, true);
                }
                return movimiento;
            }
            if (columna > 0) {
                movimiento[1] = fila_actual;
                movimiento[2] = columna_actual - 1;
                if (matrix[movimiento[1]][movimiento[2]] instanceof Player) {
                    System.out.println("REMOVIMIENTO FILA");
                    movimiento = fila(costo + 2, fila, columna, fila_actual, columna_actual, true);
                }
                return movimiento;
            }
        }
        System.out.println("FALLE");
        return movimiento;
    }

    private int Calcular_valor_fruta(Integer fila_fruta, Integer columna_fruta)
    {
        String fruta_comida=matrix[fila_fruta][columna_fruta].getTipo();
        System.out.println("RECALCULAMOS VALOR DE UNA FRUTA DEL TIPO: "+ fruta_comida);
        int valor=0;
        int frutas_jug0=0;
        int frutas_jug1=0;
        switch(fruta_comida)
        {
            case "M":
                frutas_jug0=Jugadores.get("0").getManzanas_Naranjas_Peras()[0];
                frutas_jug1=Jugadores.get("1").getManzanas_Naranjas_Peras()[0];
                break;
            case "N":
                frutas_jug0=Jugadores.get("0").getManzanas_Naranjas_Peras()[1];
                frutas_jug1=Jugadores.get("1").getManzanas_Naranjas_Peras()[1];
                break;
            case "P":
                frutas_jug0=Jugadores.get("0").getManzanas_Naranjas_Peras()[2];
                frutas_jug1=Jugadores.get("1").getManzanas_Naranjas_Peras()[2];
                break;
            default:
                break;
        }

        if(frutas_jug0==0)
        {
            if(frutas_jug1==0)
            {
                valor=VALOR_0_0;
            }
            if(frutas_jug1==1)
            {
                valor=VALOR_1_0;
            }
            if(frutas_jug1==2 || frutas_jug1==3)
            {
                valor=VALOR_2_0;
            }

        }
        if(frutas_jug0==1)
        {
            if(frutas_jug1==0)
            {
                valor=VALOR_1_0;
            }
            if(frutas_jug1==1)
            {
                valor=VALOR_1_1;
            }
            if(frutas_jug1==2 )
            {
                valor=VALOR_2_0;
            }

        }
        if(frutas_jug0==2 || frutas_jug0==3)
        {
            valor=VALOR_2_0;
        }

        for(int contador=0;contador<Frutas.size();contador++)
        {
            if(Frutas.get(contador).Nombre.equals(fruta_comida))
            {
                System.out.println(fruta_comida+ " valor: "+valor);
                Frutas.get(contador).setValor(valor);
            }
        }
        return 0;
    }
    private void inicializarElementos(){

        //LOGICA:

        //CREAMOS LA MATRIZ LOGICA :
        for (int x=0;x<ROWS-1;x++)
        {
            for (int y=0; y < COLUMNS-1; y++)
            {
                matrix[x][y]=new Casilla(); //METEMOS LAS CASILLAS.
            }
        }

        //COLOCAMOS LAS FRUTAS-----------------------------------------------
        for(int i=0; i < Manzanas_Naranjas_Peras.length; i++)
        {
            Manzanas_Naranjas_Peras[i]=3;

            while(Manzanas_Naranjas_Peras[i] > 0)
            {
                Manzanas_Naranjas_Peras[i]--;
                do
                {
                    fila=(int)Math.floor(Math.random()*(8));
                    columna=(int)Math.floor(Math.random()*(6));

                }while(comprobar_posicion(fila,columna));

                if(i==0)
                {
                    Fruit fruta=new Fruit("M",fila,columna);
                    matrix[fila][columna]= fruta;
                    Frutas.add(fruta);
                }
                if(i==1)
                {
                    Fruit fruta=new Fruit("N",fila,columna);
                    matrix[fila][columna]= fruta;
                    Frutas.add(fruta);
                }
                if(i==2)
                {
                    Fruit fruta=new Fruit("P",fila,columna);
                    matrix[fila][columna]= fruta;
                    Frutas.add(fruta);
                }
            }
        }
        for(int i=0; i <Manzanas_Naranjas_Peras.length; i++)
        {
            Manzanas_Naranjas_Peras[i]=3;
        }
        //COLOCAMOS A LOS JUGADORES------------------------------------------------
        for(Integer i=0; i < 2; i++)
        {
            do
            {
                fila=(int)Math.floor(Math.random()*(8));
                columna=(int)Math.floor(Math.random()*(6));
            }while(comprobar_posicion(fila,columna));

            Player jugador_nuevo=new Player(i.toString(),fila,columna);
            matrix[fila][columna]= jugador_nuevo;
            Jugadores.put(i.toString(),jugador_nuevo);
        }
    }

    private void dibujarMatrix (){



        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        //tamPuntos=(int) ((height*(VIEWPORT))/((REL_DEP_TP+1)*ROWS-REL_DEP_TP));
        tamPuntos=10;
        altoCelda =(int)(height-height*0.05)/(ROWS-1);
        posXInicial = (width-((COLUMNS-1)* altoCelda))/2;
        posYInicial =  (height-((ROWS-1)* altoCelda))/2;

        posX = posXInicial;
        posY = posYInicial;

        paint.setStrokeWidth(8);
        canvas.drawRect(new Rect(posX,posY,posX+(COLUMNS-1)* altoCelda,posY+(int)(height-height*0.05)),paint);
        paint.setColor(Color.BLACK);
        for(int i=0; i<ROWS; i++){
            for (int j=0; j<COLUMNS; j++){
                if(i==0){
                    //Líneas verticales
                    canvas.drawLine(posX,posY,posX,posY+(int)(height-height*0.05),paint);
                }
                if(j==0){
                    //Líneas horizontales
                    canvas.drawLine(posX,posY,posX+(COLUMNS-1)* altoCelda,posY,paint);
                }
                if(i<ROWS-1 && j<COLUMNS-1) {
                    if (this.matrix[i][j] instanceof Fruit) {
                        if (this.matrix[i][j].getTipo().equals("M")) {
                            Bitmap manzanaGrande = BitmapFactory.decodeResource(getResources(), R.drawable.apple);
                            Bitmap manzana = Bitmap.createScaledBitmap(manzanaGrande, altoCelda, altoCelda, false);
                            canvas.drawBitmap(manzana, posX, posY, paint);
                        }
                        if (this.matrix[i][j].getTipo().equals("N")) {
                            Bitmap naranjaGrande = BitmapFactory.decodeResource(getResources(), R.drawable.orange);
                            Bitmap naranja = Bitmap.createScaledBitmap(naranjaGrande, altoCelda, altoCelda, false);
                            canvas.drawBitmap(naranja, posX, posY, paint);
                        }
                        if (this.matrix[i][j].getTipo().equals("P")) {
                            Bitmap peraGrande = BitmapFactory.decodeResource(getResources(), R.drawable.pear);
                            Bitmap pera = Bitmap.createScaledBitmap(peraGrande, altoCelda, altoCelda, false);
                            canvas.drawBitmap(pera, posX, posY, paint);
                        }

                    }
                    if (this.matrix[i][j] instanceof Player) {
                        if (this.matrix[i][j].getTipo().equals("0")) {
                            Bitmap jugadorGrande = BitmapFactory.decodeResource(getResources(), R.drawable.pink3);
                            Bitmap jugador = Bitmap.createScaledBitmap(jugadorGrande, altoCelda, altoCelda, false);
                            canvas.drawBitmap(jugador, posX, posY, paint);
                        }
                        if (this.matrix[i][j].getTipo().equals("1")) {
                            Bitmap jugadorGrande = BitmapFactory.decodeResource(getResources(), R.drawable.blue2);
                            Bitmap jugador = Bitmap.createScaledBitmap(jugadorGrande, altoCelda, altoCelda, false);
                            canvas.drawBitmap(jugador, posX, posY, paint);
                        }
                    }

                }
                if(i==ROWS-1 && j==COLUMNS-1){
                    posXFinal = posX;
                    posYFinal = posY;
                }
                canvas.drawCircle(posX,posY,tamPuntos,paint);
                posX= posX+ altoCelda;
            }
            posY = posY+ altoCelda;
            posX = posXInicial;
        }

    }

    private boolean comprobar_posicion(int fila, int columna)
    {
        boolean respuesta=false;
        Log.i("comprobar_posicion","hola"+this.matrix[fila][columna].getClass().toString());

        if(this.matrix[fila][columna] instanceof Fruit){
            respuesta=true;
            //String fruit = this.matrix[fila][columna].getTipo();
        }else if(this.matrix[fila][columna] instanceof Player){
            respuesta=true;
         }else{
            respuesta=false;
        }
        return respuesta;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (turno_maquina != true) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                xTouch = motionEvent.getX();
                yTouch = motionEvent.getY();
                Log.i("touchEvent", "X= " + xTouch + ", Y= " + yTouch);

                int columna = (int) Math.ceil((xTouch - posXInicial) / altoCelda) - 1;
                int fila = (int) Math.ceil((yTouch - posYInicial) / altoCelda) - 1;
                Log.i("touchEvent", "Fila= " + fila + ", Columna= " + columna);
                Log.i("playerPosition", "");
                if (comprobarMovimiento(fila, columna)) {
                    Player jugador = Jugadores.get(("0"));
                    this.matrix[jugador.getPosicionX()][jugador.getPosicionY()] = new Casilla();
                    this.matrix[fila][columna] = jugador;
                    Jugadores.get("0").setPosicionX(fila);
                    Jugadores.get("0").setPosicionY(columna);
                    turno++;
                }
                if (Jugadores.get("0").categorias != 2 && Jugadores.get("1").categorias != 2) {
                    invalidate();
                }
                else{
                    ganada=true;
                    partida_finalizada=true;
                    invalidate();
                }

             }
         }
        return false;
    }

    public boolean comprobarMovimiento(int fila, int columna){
        if(fila<0 || fila>7 || columna < 0 || columna>5) {
            return false;
        }else{
            Player jugador = Jugadores.get("0");
            if(matrix[fila][columna] instanceof Player){
                activity.confundirse.start();
                return false;
            }
            if(fila == jugador.getPosicionX() && (columna == jugador.getPosicionY()+1 || columna == jugador.getPosicionY()-1)){
                if(matrix[fila][columna] instanceof Fruit) {
                    String fruit = matrix[fila][columna].getTipo();
                    actualizarPuntuación("0",fruit);
                    Frutas.remove(matrix[fila][columna]);
                    Calcular_valor_fruta(fila,columna);
                    activity.fruta.start();
                }else{
                    activity.casilla.start();
                }
                return true;
            } else if (columna == jugador.getPosicionY() && (fila == jugador.getPosicionX()+1 || fila == jugador.getPosicionX()-1)){
                if(matrix[fila][columna] instanceof Fruit) {
                    String fruit = matrix[fila][columna].getTipo();
                    actualizarPuntuación("0",fruit);
                    Frutas.remove(matrix[fila][columna]);
                    Calcular_valor_fruta(fila, columna);
                    activity.fruta.start();
                }else{
                    activity.casilla.start();
                }
                return true;
            }else{
                return false;
            }
        }
    }

    public void actualizarPuntuación(String id, String tipoFruta){
        System.out.println("ENTRE ACTUALIZAR PUNTUACION");
        switch(tipoFruta){
            case "M":
                Manzanas_Naranjas_Peras[0]--;
                Jugadores.get(id).Manzanas_Naranjas_Peras[0]+=1;
                if(Jugadores.get(id).Manzanas_Naranjas_Peras[0]==2)
                {
                    Jugadores.get(id).categorias++;
                    if(id.equals("0")){
                        activity.player1_apple_row.setBackgroundColor(getResources().getColor(R.color.pink_row));
                    }
                    else{
                        activity.playerAI_apple_row.setBackgroundColor(getResources().getColor(R.color.blue_row));

                    }
                }
                if (id.equals("0"))
                    activity.mApple1.setText(String.format("  %d", Jugadores.get(id).getManzanas_Naranjas_Peras()[0]));
                else
                    activity.mApple2.setText(String.format("    %d", Jugadores.get(id).getManzanas_Naranjas_Peras()[0]));
                break;
            case "N":
                Manzanas_Naranjas_Peras[1]--;
                Jugadores.get(id).Manzanas_Naranjas_Peras[1]+=1;
                if(Jugadores.get(id).Manzanas_Naranjas_Peras[1]==2)
                {
                    Jugadores.get(id).categorias++;
                    if(id.equals("0")){
                        activity.player1_orange_row.setBackgroundColor(getResources().getColor(R.color.pink_row));
                    }
                    else{
                        activity.playerAI_orange_row.setBackgroundColor(getResources().getColor(R.color.blue_row));
                    }
                }
                if(id.equals("0"))
                    activity.mOrange1.setText(String.format("  %d", Jugadores.get(id).getManzanas_Naranjas_Peras()[1]));
                else
                    activity.mOrange2.setText(String.format("    %d", Jugadores.get(id).getManzanas_Naranjas_Peras()[1]));
                break;
            case "P":
                Manzanas_Naranjas_Peras[2]--;
                Jugadores.get(id).Manzanas_Naranjas_Peras[2]+=1;
                if(Jugadores.get(id).Manzanas_Naranjas_Peras[2]==2)
                {
                    Jugadores.get(id).categorias++;
                    if(id.equals("0")){
                        activity.player1_pear_row.setBackgroundColor(getResources().getColor(R.color.pink_row));
                    }
                    else{
                        activity.playerAI_pear_row.setBackgroundColor(getResources().getColor(R.color.blue_row));
                    }
                }
                if(id.equals("0"))
                    activity.mPear1.setText(String.format("  %d", Jugadores.get(id).getManzanas_Naranjas_Peras()[2]));
                else
                    activity.mPear2.setText(String.format("    %d", Jugadores.get(id).getManzanas_Naranjas_Peras()[2]));
                break;
        }
    }
}
