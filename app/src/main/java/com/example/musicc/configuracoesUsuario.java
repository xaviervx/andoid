package com.example.musicc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicc.controle.usuarioControle;
import com.example.musicc.modelo.usuario;

public class configuracoesUsuario extends AppCompatActivity {

    TextView campoNome;
    EditText edNome, edSenhaAntiga, edNovaSenha, edReNovaSenha;
    Button btSalvar, btVoltar;
    final usuario usu = new usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        campoNome = (TextView) findViewById(R.id.textNome);
        final Intent intent = getIntent();


        usu.setId(intent.getStringExtra("id"));
        usu.setNome(intent.getStringExtra("nome"));
        usu.setSenha(intent.getStringExtra("senha"));

        campoNome.setText(intent.getStringExtra("nome"));
        btSalvar = (Button) findViewById(R.id.btSalvar);
        btVoltar = (Button) findViewById(R.id.btVoltar);
        edNome = (EditText) findViewById(R.id.editTextName);
        edSenhaAntiga = (EditText) findViewById(R.id.editTextSenhaAntiga);
        edNovaSenha = (EditText) findViewById(R.id.editTextNovaSenha);
        edReNovaSenha = (EditText) findViewById(R.id.editTextReNovaSenha);

        //Ação de salvar os dados do usuario
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarDados(usu);
            }
        });

        // Ação do botão de voltar
        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void salvarDados(usuario usu){
        String mensagem = null;
        usuarioControle uc = new usuarioControle(configuracoesUsuario.this);


        if(!edNome.getText().toString().equals("")){
            usu.setNome(edNome.getText().toString());
            if(edSenhaAntiga.getText().toString().equals("")){
                mensagem = uc.atualiza_usuario(usu);
            }else if (edSenhaAntiga.getText().toString().equals(usu.getSenha())){
                if (edNovaSenha.getText().toString().equals(edReNovaSenha.getText().toString())){
                    usu.setSenha(edNovaSenha.getText().toString());
                    mensagem = uc.atualiza_usuario(usu);
                    finish();
                }else{
                    mensagem = "A nova senha não confere";
                }
            }else {
                mensagem =  "A senha antiga não confere";
            }
        }else {
            mensagem = "O campo nome não pode estar vazio";
        }

        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();

    }
}
