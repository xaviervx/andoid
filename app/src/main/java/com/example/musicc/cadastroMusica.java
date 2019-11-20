package com.example.musicc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicc.controle.musicaControle;
import com.example.musicc.modelo.musica;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class cadastroMusica extends AppCompatActivity {

    EditText nomemusica, nomealtor, nomealbum;
    TextInputEditText letra, traducao;
    Button btadicionar, btcancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_musica);

        nomemusica = (EditText) findViewById(R.id.nomemusica);
        nomealtor = (EditText) findViewById(R.id.nomeautor);
        nomealbum = (EditText) findViewById(R.id.nomealbum);

        letra = (TextInputEditText) findViewById(R.id.letra);
        traducao = (TextInputEditText) findViewById(R.id.campotraducao);

        btadicionar = (Button) findViewById(R.id.adicionar);
        btcancelar = (Button) findViewById(R.id.cancelar);

        btadicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarMusica();
            }
        });
    }

    public void cadastrarMusica(){




        musica mus = new musica();
        mus.setNome(nomemusica.getText().toString());
        mus.setAutor(nomealtor.getText().toString());
        mus.setAlbum(nomealbum.getText().toString());
        mus.setLetra(nomemusica.getText().toString().replace(" ", "").trim());
        mus.setTraducao(nomemusica.getText().toString().replace(" ", "").trim() + "Traducao");


        // Faz os arquivos correspondentes a letra e tradução

        try {
            FileOutputStream letra = openFileOutput(mus.getLetra()+".txt", MODE_PRIVATE);
            FileOutputStream traducao = openFileOutput(mus.getTraducao()+".txt", MODE_PRIVATE);
            OutputStreamWriter escrita = new OutputStreamWriter(letra);
            BufferedWriter bw = new BufferedWriter(escrita);
            bw.write(this.letra.getText().toString());
            bw.close();
            escrita.close();
            letra.close();

            OutputStreamWriter escritaTraducao = new OutputStreamWriter(traducao);
            bw = new BufferedWriter(escritaTraducao);
            bw.write(this.traducao.getText().toString());
            bw.close();
            escrita.close();
            letra.close();


        }catch (Exception e){

        }

        musicaControle muscontrole = new musicaControle(cadastroMusica.this);

        if (!mus.getNome().equals("")){
            Toast.makeText(this, muscontrole.novaMusica(mus), Toast.LENGTH_SHORT).show();
            finish();

        }else{
            Toast.makeText(this, "Ao menos insira um nome para a musica", Toast.LENGTH_SHORT).show();
        }
    }


}
