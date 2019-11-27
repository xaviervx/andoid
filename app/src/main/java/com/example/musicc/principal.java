package com.example.musicc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicc.controle.musicaControle;
import com.example.musicc.controle.usuarioControle;
import com.example.musicc.modelo.usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Principal;
import java.util.ArrayList;

public class principal extends AppCompatActivity {

    TextView campoNome;
    EditText pesquisa;
    ImageButton config, pesquisar;
    ListView lista;
    FloatingActionButton btadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        pesquisa = (EditText) findViewById(R.id.txpesquisa);

        lista = (ListView) findViewById(R.id.listasimples);

        campoNome = (TextView) findViewById(R.id.textNome);
        final Intent intent = getIntent();

        usuarioControle uc = new usuarioControle(principal.this);
        final usuario usu = new usuario(uc.monta(Integer.parseInt(intent.getStringExtra("id"))));


        campoNome.setText(usu.getNome());
        pesquisar = (ImageButton) findViewById(R.id.btPesquisar);

        config = (ImageButton) findViewById(R.id.btConfig);

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(principal.this, configuracoesUsuario.class);

//                Toast.makeText(principal.this, "Teste do botão de configuração", Toast.LENGTH_SHORT).show();

                i.putExtra("id", usu.getId());
                i.putExtra("nome", usu.getNome());
                i.putExtra("senha", usu.getSenha());

                startActivity(i);
            }
        });
        final musicaControle mu = new musicaControle(principal.this);
        final ArrayList<String> itens = mu.listaMusica();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itens);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(!itens.get(i).equals("Não possui musicas cadastradas")){
                    Intent tela = new Intent(principal.this, vermusica.class);
                    tela.putExtra("nome", itens.get(i).toString() );

                    startActivity(tela);
                }else{
                    Toast.makeText(principal.this, "Comece cadastrando uma musica", Toast.LENGTH_SHORT).show();
                }


            }
        });


        pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pesquisa.getText().toString().equals("")){
                    ArrayList<String> itensn = mu.pesquisa(pesquisa.getText().toString());

                    ArrayAdapter<String> adaptern=new ArrayAdapter<String>(principal.this, android.R.layout.simple_list_item_1, android.R.id.text1, itensn);
                    lista.setAdapter(adaptern);
                }else{

                }

            }
        });

        btadd = (FloatingActionButton) findViewById(R.id.btadd);

        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Vai char a tela de adicionar nova musica
                Intent i = new Intent(principal.this, cadastroMusica.class);
                startActivity(i);
            }
        });

    }
    @Override
    protected void onRestart() {

        super.onRestart();
        recreate();
    }
}
