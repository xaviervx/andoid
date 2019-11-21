package com.example.musicc.modelo;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class musica {

    private int id;
    private String nome;
    private String autor;
    private String album;
    private String letra;
    private String traducao;

    public musica() {
    }

    public musica(JSONObject json) {
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

    public musica(Cursor c){
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

    public musica(int id, String nome, String autor, String letra) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.letra = letra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getTraducao() {
        return traducao;
    }

    public void setTraducao(String traducao) {
        this.traducao = traducao;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
