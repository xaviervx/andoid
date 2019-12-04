package com.example.musicc.controle;

import android.content.Context;
import android.database.Cursor;

import com.android.volley.VolleyError;
import com.example.musicc.modelo.favorita;

import org.json.JSONArray;
import org.json.JSONException;

public class favoritaControle {

    private Context com;

    public favoritaControle(Context contexto) {
        this.com = contexto;
    }

    public boolean efavorita(favorita favo) {

        remote r = new remote(this.com);
        r.prepare("SELECT * FROM favorita WHERE id_usuario = :idUsuario AND id_musica = :idMusica");
        r.bindValue(":idUsuario", favo.getId_usuario());
        r.bindValue(":idMusica", favo.getId_musica());
        Cursor c = r.executeQuery();

        if (c.getCount() > 0) {

            return true;

        } else {
            return false;
        }

    }

    public String inserirfavorita(favorita favo) {
        try {
            remote r = new remote(this.com){
                @Override
                public void responseError(VolleyError response) {
                    super.responseError(response);
                    this.simpleReplace(" favorita", " favoritaclone");
                    this.execute();
                }
            };
            r.prepare("insert into favorita(id_usuario, id_musica) values (:idUsuario, :idMusica)");
            r.bindValue(":idUsuario", favo.getId_usuario());
            r.bindValue(":idMusica", favo.getId_musica());
            r.execute();
            r.executeRemote();
            return "Musica adicionada as favoritas";
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }


    }

    public String tirarfavorita (favorita favo){
        try {
            remote r = new remote(this.com);
            r.prepare("delete from favorita where id_usuario = :idUsuario AND id_musica = :idMusica");
            r.bindValue(":idUsuario", favo.getId_usuario());
            r.bindValue(":idMusica", favo.getId_musica());
            r.execute();
            r.executeRemote();
            return "Musica removida das favoritas";
        }catch (Exception e){
            return "Erro: " +e.getMessage();
        }
    }

    public void sincronizaFavorita(){

        remote r = new remote(this.com);
        r.prepare("SELECT * FROM favoritaclone");
        Cursor c = r.executeQuery();

        if(c.getCount() > 0){
            c.moveToFirst();
            do {
                remote re = new remote(this.com){
                    @Override
                    public void responseError(VolleyError response) {
                        super.responseError(response);
                        this.simpleReplace(" favorita", " favoritaclone");
                        this.execute();
                    }
                };
                favorita favo = new favorita(c);
                re.prepare("insert into favorita(id_usuario, id_musica) values (:idUsuario, :idMusica)");
                re.bindValue(":idUsuario", favo.getId_usuario());
                re.bindValue(":idMusica", favo.getId_musica());
                re.executeRemote();
                remote rem = new remote(this.com);
                rem.prepare("delete from favoritaclone where id_usuario = :idUsuario AND id_musica = :idMusica");
                rem.bindValue(":idUsuario", favo.getId_usuario());
                rem.bindValue(":idMusica", favo.getId_musica());
                rem.execute();
            }while (c.moveToNext());
        }

        r = new remote(this.com){
            @Override
            public void success(JSONArray jsonArray) {
                super.success(jsonArray);
                this.prepare("delete from favorita");
                this.execute();
                for(int i = 0; i < jsonArray.length(); i++){
                    try {

                        favorita favo = new favorita(jsonArray.getJSONObject(i));
                        this.prepare("INSERT INTO favorita(id_usuario, id_musica) values (:idUsuario, :idMusica)");
                        this.bindValue(":idUsuario", favo.getId_usuario());
                        this.bindValue(":idMusica", favo.getId_musica());
                        this.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        r.prepare("SELECT * FROM favorita");
        r.executeRemote();

    }

}
