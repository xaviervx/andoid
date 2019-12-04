package com.example.musicc.modelo;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class favorita {
    private int id_usuario;
    private int id_musica;

    public favorita() {
    }

    public favorita(JSONObject json) {
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

    public favorita(Cursor c){
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

    public favorita(int id_usuario, int id_musica) {
        this.id_usuario = id_usuario;
        this.id_musica = id_musica;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_musica() {
        return id_musica;
    }

    public void setId_musica(int id_musica) {
        this.id_musica = id_musica;
    }
}
