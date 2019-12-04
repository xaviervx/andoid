package com.example.musicc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicc.controle.favoritaControle;
import com.example.musicc.controle.musicaControle;
import com.example.musicc.modelo.favorita;
import com.example.musicc.modelo.musica;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class vermusica extends AppCompatActivity {

    TextView camponomemusica, camponomeautor, campoalbum, campoletra, campotraducao;
    ImageButton favoritar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vermusica);

        final Intent intent = getIntent();

        String nomeMusica = intent.getStringExtra("nome").toString();

//        int a = R.drawable.es;
        camponomemusica = (TextView) findViewById(R.id.camponomemusica);
        camponomemusica.setText(nomeMusica);

        camponomeautor = (TextView) findViewById(R.id.camponomeautor);
        campoalbum = (TextView) findViewById(R.id.camponomealbum);
        campoletra = (TextView) findViewById(R.id.campoletra);
        campotraducao = (TextView) findViewById(R.id.campotraducao);

        favoritar = findViewById(R.id.btfavoritar);
        musicaControle mscontrole = new musicaControle(vermusica.this);

        final musica mus = mscontrole.listaMusicaNome(nomeMusica);

        camponomeautor.setText("Autor - " + mus.getAutor());
        campoalbum.setText("Album - " + mus.getAlbum());
        campoletra.setText(mus.getLetra());
        campotraducao.setText(mus.getTraducao());

        final favorita favo = new favorita();
        favo.setId_usuario(Integer.parseInt(intent.getStringExtra("idUsuario")));
        favo.setId_musica(mus.getId());
        final favoritaControle favoControl = new favoritaControle(vermusica.this);

        if(favoControl.efavorita(favo)){
//            favoritar.setImageDrawable(R.drawable.a);
        }

        favoritar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(vermusica.this, favoControl.inserirfavorita(favo), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String buscaLetra(String letra) {

        String letraEscrita = "";

        try{

            FileInputStream arquivo = openFileInput(letra + ".txt");
            InputStreamReader leitura = new InputStreamReader(arquivo);
            BufferedReader leitor = new BufferedReader(leitura);


            while (leitor.ready()){
                String linha = leitor.readLine();
                letraEscrita += linha+"\n";
            }

            leitor.close();
            leitura.close();
            arquivo.close();

        } catch (Exception ex){
            Toast.makeText(this, "Erro aqui: " + ex, Toast.LENGTH_SHORT).show();
        }

        return letraEscrita;
    }

    private String buscaTraducao(String traducao){
        String traducaoEscrita = "";

        try{

            FileInputStream arquivo = openFileInput(traducao+".txt");
            InputStreamReader leitura = new InputStreamReader(arquivo);
            BufferedReader leitor = new BufferedReader(leitura);


            while (leitor.ready()){
                String linha = leitor.readLine();
                traducaoEscrita += linha+"\n";
            }

            leitor.close();
            leitura.close();
            arquivo.close();

        } catch (Exception ex){
            Toast.makeText(this, "Erro aqui: " + ex, Toast.LENGTH_SHORT).show();
        }

        return traducaoEscrita;
    }
}
