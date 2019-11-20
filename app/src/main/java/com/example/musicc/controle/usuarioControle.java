package com.example.musicc.controle;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicc.modelo.usuario;

import org.json.JSONArray;
import org.json.JSONException;

public class usuarioControle {

    private SQLiteDatabase db;
    private conexao banco;
    private String senhaU;
    private String nomeUs;
    private Context com;

    public usuarioControle(Context contexto) {
//        this.banco = new conexao(contexto);
        this.com = contexto;
    }

    public String inserirusuario(usuario us) {
        try {
            remote r = new remote(this.com);
            r.prepare("INSERT INTO usuario(nome, senha) values (:nome, :senha)");
            r.bindValue(":nome", us.getNome());
            r.bindValue(":senha", us.getSenha());
            r.execute();
            r.executeRemote();
            return "Usuário inserido";
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }


    }

    public boolean login(usuario us) {

        remote r = new remote(this.com);
        r.prepare("SELECT * FROM usuario WHERE nome = :nome AND senha = :senha");
        r.bindValue(":nome", us.getNome());
        r.bindValue(":senha", us.getSenha());
        Cursor c = r.executeQuery();

        if (c.getCount() > 0) {

            return true;

        } else {
            return false;
        }

    }

    public usuario loginId(usuario us) {

//        String SQL = "SELECT * FROM usuario WHERE nome ='" + us.getNome() + "' AND senha = '" + us.getSenha() + "'";
        try {
            remote r = new remote(this.com);
            r.prepare("SELECT * FROM usuario WHERE nome ='" + us.getNome() + "' AND senha = '" + us.getSenha() + "'");
            Cursor c =  r.executeQuery();


            int cursorCont = c.getCount();


            c.moveToFirst();
            if (cursorCont > 0) {
                System.out.println("" + c.getInt(c.getColumnIndex("id")));
                us.setId("" + c.getInt(c.getColumnIndex("id")));

            } else {

            }
            c.close();

        } catch (SQLException e) {

        }
        return us;
    }

    public String atualiza_usuario(usuario usr) {
        int idd = Integer.parseInt(usr.getId());

        String ATUALIZA_USUARIO = "UPDATE usuario SET nome = '" + usr.getNome() + "',senha ='" + usr.getSenha() + "' WHERE id = " + idd;
        try {
            SQLiteDatabase db = banco.getWritableDatabase();
            db.execSQL(ATUALIZA_USUARIO);
            db.close();
        } catch (Exception ex) {
            return "Erro ao atualizar: " + ex.getMessage();
            //("Erro (criação tabela)",ex.getMessage());
        }
        return "Dados Atualizados ";
    }

    public void sincronizaUsuario(){
        remote r = new remote(this.com){
            @Override
            public void success(JSONArray jsonArray) {
                super.success(jsonArray);
                for(int i = 0; i < jsonArray.length(); i++){
                    try {
                        this.prepare("delete from usuario");
                        this.execute();
                        usuario us = new usuario(jsonArray.getJSONObject(i));
                        this.prepare("INSERT INTO usuario(nome, senha) values (:nome, :senha)");
                        this.bindValue(":nome", us.getNome());
                        this.bindValue(":senha", us.getSenha());
                        this.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        r.prepare("SELECT * FROM usuario");
        r.executeRemote();
    }
}
