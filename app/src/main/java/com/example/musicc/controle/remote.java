package com.example.musicc.controle;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class remote<E> {

    private SQLiteDatabase db;
    private String sql;
    private Context activity;
    private String remoteAdress = "localhost";
    private String remoteDb = "musicc";
    private String remoteUser = "root";
    private String remotePass = "";
    private String apiURL = "http://200.132.17.142/musicc/remoto.php";
    private E obj;

    public remote(Context activity, String apiURL, String remoteAdress, String remoteDb, String remoteUser, String remotePass) {
        this.remoteAdress = remoteAdress;
        this.remoteDb = remoteDb;
        this.remoteUser = remoteUser;
        this.remotePass = remotePass;
        this.activity = activity;
        this.apiURL = apiURL;
        this.db = this.activity.openOrCreateDatabase( "musicc.db", Context.MODE_PRIVATE, null);
    }

    public remote(Context activity) {
        this.activity = activity;
        this.db = this.activity.openOrCreateDatabase( "musicc.db", Context.MODE_PRIVATE, null);
    }

    public remote(Context activity, String dbName) {
        this.activity = activity;
        this.db = this.activity.openOrCreateDatabase( "musicc.db", Context.MODE_PRIVATE, null);

    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public Context getActivity() {
        return activity;
    }

    public void setActivity(Context activity) {
        this.activity = activity;
    }

    public String getRemoteDb() {
        return remoteDb;
    }

    public void setRemoteDb(String remoteDb) {
        this.remoteDb = remoteDb;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public String getRemotePass() {
        return remotePass;
    }

    public void setRemotePass(String remotePass) {
        this.remotePass = remotePass;
    }

    public String getapiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public E getObj() {
        return obj;
    }

    public void setObj(E obj) {
        this.obj = obj;
    }

    public String getRemoteAdress() {
        return remoteAdress;
    }

    public void setRemoteAdress(String remoteAdress) {
        this.remoteAdress = remoteAdress;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public boolean createDB() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.activity.getAssets().open("bancoSchema.sql")));
            String sql = "";
            String lstrlinha;
            while ((lstrlinha = br.readLine()) != null) {
                sql = sql + lstrlinha;
            }
            String[] comandos = sql.split(";");
            for (int i = 0; i < comandos.length; i++) {
                this.db.execSQL(comandos[i]);
            }
            return true;
        } catch (Exception e) {
            Log.d("avanco", e.getMessage());
            return false;
        }
    }

    public void prepare(String sql) {
        this.sql = sql;
    }


    public void bindValue(String param, String value) {
        this.sql = this.sql.replaceAll(param, "\"" + value + "\"");
    }

    public void bindValue(String param, Double value) {
        this.sql = this.sql.replaceAll(param, "" + value);
    }

    public void bindValue(String param, int value) {
        this.sql = this.sql.replaceAll(param, "" + value);
    }

    public boolean execute() {
        try {
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            Log.d("avanco", e.getMessage());
            return false;
        }
    }

    public Cursor executeQuery() {
        try {
            Cursor c = this.db.rawQuery(this.sql, null);
            return c;

        } catch (Exception e) {
            Log.d("avanco", e.getMessage());
            return null;
        }
    }



    public void success(JSONArray jsonArray) {

    }

    public void responseError(VolleyError response) {
        Log.d("avanco", "4");
    }

    public void executeRemote() {
        if (!this.remoteAdress.equals("")) {
            Log.d("avanco", "1");
            Thread t = new Thread() {
                @Override
                public void run() {
                    Log.d("avanco", "2");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("avanco", response);
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            success(jsonArray);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            responseError(error);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parametros = new Hashtable<String, String>();
                            parametros.put("sql", sql);
                            parametros.put("endereco", remoteAdress);
                            parametros.put("nomeDB", remoteDb);
                            parametros.put("user", remoteUser);
                            parametros.put("password", remotePass);
                            parametros.put("token_acesso","c-m35jf7k2gh34x6c2j45h7v3k56");
                            return parametros;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(stringRequest);
                }
            };
            t.start();

        } else {
            Log.d("avanco", "Configurações incorretas para remoto!");
        }
    }

//
    //Metodo para consulta retornando lista encadeada do objeto especificado na chamada da função e na construção do objeto
//    public void executeRemote(E objet) {
//        this.obj = objet;
//        if (!this.remoteAdress.equals("")) {
//            Log.d("avanco", "1");
//            Thread t = new Thread() {
//                @Override
//                public void run() {
//                    Log.d("avanco", "2");
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            JSONArray jsonArray = null;
//                            try {
//                                jsonArray = new JSONArray(response);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            List<E> lista = new ArrayList<>();
//                            Field[] fields = obj.getClass().getDeclaredFields();
//                            Log.d("avanco", "3");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject json = null;
//                                try {
//                                    json = (JSONObject) jsonArray.get(i);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                                for (int j = 0, m = fields.length; j < m; j++) {
//                                    fields[j].setAccessible(true);
//                                    try {
//                                        fields[j].set(obj, json.get(fields[j].getName()));
//                                    } catch (IllegalAccessException e) {
//                                        e.printStackTrace();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                lista.add(obj);
//                            }
//                            success(lista);
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            responseError(error);
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> parametros = new Hashtable<String, String>();
//                            parametros.put("sql", sql);
//                            parametros.put("endereco", remoteAdress);
//                            parametros.put("nomeDB", remoteDb);
//                            parametros.put("user", remoteUser);
//                            parametros.put("password", remotePass);
//                            return parametros;
//                        }
//                    };
//                    RequestQueue requestQueue = Volley.newRequestQueue(activity.getBaseContext());
//                    requestQueue.add(stringRequest);
//                }
//            };
//            t.start();
//
//        } else {
//            Log.d("bdErro", "Configurações incorretas para remoto!");
//        }
//    }
//
    //Ciador de url para consulta via get
//    private void createGetUrl() {
//        this.apiURL = this.apiURL + "?sql=" + urlEnconde(this.sql) + "&endereco=" + urlEnconde(this.remoteAdress) + "&nomeDB=" + urlEnconde(this.remoteDb) +
//                "&user=" + this.remoteUser + "&password=" + this.remotePass;
//    }
//    private String urlEnconde(String string) {
//        try {
//            return URLEncoder.encode(string, StandardCharsets.UTF_8.toString());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public void success(List<E> lista) {
//
//    }
}