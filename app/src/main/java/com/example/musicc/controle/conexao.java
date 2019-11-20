package com.example.musicc.controle;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class conexao extends SQLiteOpenHelper {
    // versao banco
    private static final int VERSAO= 4;

    // nome banco
    private static final String NOME_BANCO = "musicc";
    // User table name
    private static final String TABELA_USUARIO = "usuario";

    // atributos
    private static final String COLUNA_USUARIO_ID = "id";
    private static final String COLUNA_USUARIO_NOME = "nome";
    private static final String COLUNA_USUARIO_SENHA = "usuario_senha";

    String CRIA_TABELAS = "CREATE TABLE IF NOT EXISTS usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR(100) NOT NULL, senha VARCHAR(100) NOT NULL)";


    String CRIA_MUSICAS = "CREATE TABLE IF NOT EXISTS musica (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR(100) NOT NULL, autor VARCHAR(100), letra VARCHAR(100), traducao VARCHAR(100), album VARCHAR(100))";

    String CRIA_favo = "CREATE TABLE IF NOT EXISTS favorita (id_usuario INTEGER NOT NULL, id_musica INTEGER NOT NULL, FOREIGN KEY (id_usuario) REFERENCES usuario(id), FOREIGN KEY (id_musica) REFERENCES musica(id))";

    public conexao(Context context) {
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CRIA_TABELAS);
        sqLiteDatabase.execSQL(CRIA_MUSICAS);
        sqLiteDatabase.execSQL(CRIA_favo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String RECRIA="DROP TABLE IF EXISTS "+TABELA_USUARIO;
        sqLiteDatabase.execSQL(RECRIA);
        onCreate(sqLiteDatabase);
    }
}
