package com.example.musicc.controle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.musicc.MainActivity;

public class temNet extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (isConnected(ctx)) {
                new usuarioControle(ctx).sincronizaUsuario();
                new musicaControle(ctx).sincronizaMusica();
                new favoritaControle(ctx).sincronizaFavorita();
                Toast.makeText(ctx, "Conectado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ctx, "Desconectado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
