package br.com.jordan.whatsapp.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import static android.R.id.edit;

/**
 * Created by User on 16/08/2017.
 */

public class Preference {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private final String NOME_ARQUIVO = "Token";

    public Preference(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(NOME_ARQUIVO, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void salvarUsuarioPreferences(String identificadorUsuario) {
        editor.putString("identificadorUsuario", identificadorUsuario);
        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString("identificadorUsuario","");
    }



}
