package com.example.musicc.controle;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.VolleyError;
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
            remote r = new remote(this.com){
                @Override
                public void responseError(VolleyError response) {
                    super.responseError(response);
                    this.simpleReplace(" usuario", " usuarioclone");
                    this.execute();
                }
            };

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

//        String ATUALIZA_USUARIO = "UPDATE usuario SET nome = '" + usr.getNome() + "',senha ='" + usr.getSenha() + "' WHERE id = " + idd;
        try {
            remote r = new remote(this.com);
            r.prepare("UPDATE usuario SET nome = :nome, senha = :senha WHERE id = :id");
            r.bindValue(":nome", usr.getNome());
            r.bindValue(":senha", usr.getSenha());
            r.bindValue(":id", usr.getId());
            r.execute();
            r.executeRemote();
        } catch (Exception ex) {
            return "Erro ao atualizar: " + ex.getMessage();
            //("Erro (criação tabela)",ex.getMessage());
        }
        return "Dados Atualizados";
    }

    public usuario monta(int aidi){
        usuario us = new usuario();
        try {
            remote r = new remote(this.com);
            r.prepare("SELECT * FROM usuario WHERE id = :id");
            r.bindValue(":id", aidi);
            Cursor c =  r.executeQuery();


            int cursorCont = c.getCount();


            c.moveToFirst();
            if (cursorCont > 0) {
                System.out.println("" + c.getInt(c.getColumnIndex("id")));
                us.setId("" + c.getInt(c.getColumnIndex("id")));
                us.setNome(c.getString(c.getColumnIndex("nome")));
                us.setSenha(c.getString(c.getColumnIndex("senha")));
            } else {

            }
            c.close();

        } catch (SQLException e) {

        }
        return us;
    }

    public void sincronizaUsuario(){

        remote r = new remote(this.com);
        r.prepare("SELECT * FROM usuarioclone");
        Cursor c = r.executeQuery();

        if(c.getCount() > 0){
            c.moveToFirst();
            do {
                remote re = new remote(this.com){
                    @Override
                    public void responseError(VolleyError response) {
                        super.responseError(response);
                        this.simpleReplace(" usuario", " usuarioclone");
                        this.execute();
                    }
                };
                usuario us = new usuario(c);
                re.prepare("INSERT INTO usuario(nome, senha) values (:nome, :senha)");
                re.bindValue(":nome", us.getNome());
                re.bindValue(":senha", us.getSenha());
                re.executeRemote();
                remote rem = new remote(this.com);
                rem.prepare("delete from usuarioclone where id = :id");
                rem.bindValue(":id", us.getId());
                rem.execute();
            }while (c.moveToNext());
        }

        r = new remote(this.com){
            @Override
            public void success(JSONArray jsonArray) {
                super.success(jsonArray);
                this.prepare("delete from usuario");
                this.execute();
                for(int i = 0; i < jsonArray.length(); i++){
                    try {

                        usuario us = new usuario(jsonArray.getJSONObject(i));
                        this.prepare("INSERT INTO usuario(id, nome, senha) values (:id, :nome, :senha)");
                        this.bindValue(":id", us.getId());
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
