package com.example.musicc.controle;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicc.modelo.musica;

import java.util.ArrayList;

public class musicaControle {
    private conexao banco;
    private Context com;

    public musicaControle(Context contexto) {
//        this.banco = new conexao(contexto);
        this.com = contexto;
    }


    public musica listaMusicaNome(String n) {
        musica mus = new musica();
        String SQL = "SELECT * FROM musica WHERE nome = '" + n + "'";
        try {
            remote r = new remote(this.com);
            r.prepare("SELECT * FROM musica WHERE nome = :nome");
            r.bindValue(":nome", n);
            Cursor c = r.executeQuery();
            int cursorCont = c.getCount();
            c.moveToFirst();
            if (cursorCont > 0) {

                mus.setId(c.getInt(c.getColumnIndex("id")));
                mus.setNome(c.getString(c.getColumnIndex("nome")));
                mus.setAutor(c.getString(c.getColumnIndex("autor")));
                mus.setAlbum(c.getString(c.getColumnIndex("album")));
                mus.setLetra(c.getString(c.getColumnIndex("letra")));
                mus.setTraducao(c.getString(c.getColumnIndex("traducao")));

            }

        } catch (SQLException e) {
            System.out.println("DEu erro aqui oh:" + e);
        }
        return mus;
    }

    public ArrayList<String> listaMusica() {


        ArrayList<String> nomeMusicas = new ArrayList<String>();
        try {
            remote r = new remote(this.com);
            r.prepare("SELECT * FROM musica ORDER BY nome");
            Cursor c = r.executeQuery();

            c.moveToFirst();
            if (c.getCount() > 0) {
                do {
                    nomeMusicas.add(c.getString(c.getColumnIndex("nome")));
                } while (c.moveToNext());
            } else {
                nomeMusicas.add("Não possui musicas cadastradas");
            }

            c.close();

        } catch (SQLException e) {

        }
        return nomeMusicas;
    }

    public ArrayList<String> pesquisa(String nomeM) {
        String SQL = "SELECT * FROM musica WHERE nome LIKE '%" + nomeM + "%' OR autor LIKE '" + nomeM + "' ORDER BY nome";
        ArrayList<String> nomeMusicas = new ArrayList<String>();
        try {
            remote r = new remote(this.com);
            r.prepare("SELECT * FROM musica WHERE nome LIKE :nome OR autor LIKE :nome ORDER BY nome");
            r.bindValue(":nome", "%"+nomeM+"%");
            Cursor c = r.executeQuery();
            // System.out.println(""+c.getInt(c.getColumnIndex("id")));

            int cursorCont = c.getCount();
            c.moveToFirst();
            if (cursorCont > 0) {
                do {
                    nomeMusicas.add(c.getString(c.getColumnIndex("nome")));
                } while (c.moveToNext());
            } else {
                nomeMusicas = listaMusica();
            }
        } catch (SQLException e) {
        }
        return nomeMusicas;
    }

    public String novaMusica(musica mus) {

        String ret = "";
        try {
            remote r = new remote(this.com);
            r.prepare("INSERT INTO musica(nome, autor, letra, traducao, album) values (:nome, :autor, :letra, :traducao, :album)");
            r.bindValue(":nome", mus.getNome());
            r.bindValue(":autor", mus.getAutor());
            r.bindValue(":letra", mus.getLetra());
            r.bindValue(":traducao", mus.getTraducao());
            r.bindValue(":album", mus.getAlbum());
            r.execute();
            r.executeRemote();
            ret = "Musica adicionada";
        } catch (SQLException e) {
            ret = "Erro em: " + e;
        }
        return ret;
    }


}
