package com.example.musicc.modelo;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class usuario {

    private String id;
    private String nome;
    private String senha;

    public usuario() {
    }

    public usuario(usuario usuario){
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.senha = usuario.getSenha();
    }

    public usuario(JSONObject json) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);
            try {
                switch (fields[j].getGenericType().toString()) {
                    case "int":
                        fields[j].set(this, json.getInt(fields[j].getName()));
                        break;
                    case "class java.lang.String":
                        fields[j].set(this, json.getString(fields[j].getName()));
                        break;
                    case "class java.lang.Double":
                        fields[j].set(this, json.getDouble(fields[j].getName()));
                        break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public usuario(Cursor c){
        Field[] fields = this.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);
            try {
                switch (fields[j].getGenericType().toString()) {
                    case "int":
                        fields[j].set(this,  c.getInt(c.getColumnIndex(fields[j].getName())));
                        break;
                    case "class java.lang.String":
                        fields[j].set(this, c.getString(c.getColumnIndex(fields[j].getName())));
                        break;
                    case "class java.lang.Double":
                        fields[j].set(this, c.getDouble(c.getColumnIndex(fields[j].getName())));
                        break;
                }
//                fields[j].set(this, c.(fields[j].getName()));
            } catch (Exception e) {
                Log.d("avanco", e.getMessage());
                Log.d("avanco", "Deu merda no construtor la");
            }
        }
    }

    public usuario(String id, String nome, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
