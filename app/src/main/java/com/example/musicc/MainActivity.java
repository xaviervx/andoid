package com.example.musicc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicc.controle.conexao;
import com.example.musicc.controle.favoritaControle;
import com.example.musicc.controle.musicaControle;
import com.example.musicc.controle.remote;
import com.example.musicc.controle.usuarioControle;
import com.example.musicc.modelo.usuario;

public class MainActivity extends AppCompatActivity {

    private EditText ednome, edsenha;
    private Button btentrar, btcadastrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        remote r = new remote(this.getBaseContext());
        r.createDB();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        usuarioControle uc = new usuarioControle(MainActivity.this);
        uc.sincronizaUsuario();

        musicaControle mc = new musicaControle(MainActivity.this);
        mc.sincronizaMusica();

        favoritaControle fc = new favoritaControle(MainActivity.this);
        fc.sincronizaFavorita();

        ednome = (EditText) findViewById(R.id.ednome);
        edsenha = (EditText) findViewById(R.id.edsenha);
        btentrar =  (Button) findViewById(R.id.btentrar);
        btcadastrarse = (Button) findViewById(R.id.btcadastrarse);


        // Ação do botão de entrar
        btentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();
            }
        });

        // Ação do botão de cadastrar se
        btcadastrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cadastrar();

//                conexao c = new conexao(MainActivity.this);
//                Toast.makeText(MainActivity.this, "OI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void cadastrar() {
        usuario us = new usuario();
        us.setNome(ednome.getText().toString());
        us.setSenha(edsenha.getText().toString());

        usuarioControle uc = new usuarioControle(MainActivity.this);

        Toast.makeText(this, uc.inserirusuario(us), Toast.LENGTH_SHORT).show();

    }

    private void login(){
        usuario us = new usuario();
        us.setNome(ednome.getText().toString());
        us.setSenha(edsenha.getText().toString());

        usuarioControle uc = new usuarioControle(MainActivity.this);

        if(uc.login(us)){
            Intent i = new Intent(this, principal.class);
            us.setId(uc.loginId(us).getId());
            i.putExtra("id", us.getId());
            i.putExtra("nome", us.getNome());
            i.putExtra("senha", us.getSenha());

            startActivity(i);

        }else{
            Toast.makeText(this, "Usuario ou senha incorretos", Toast.LENGTH_SHORT).show();
        }


    }
}
