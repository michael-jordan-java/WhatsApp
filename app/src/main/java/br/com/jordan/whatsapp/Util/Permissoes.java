package br.com.jordan.whatsapp.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 17/08/2017.
 */

public class Permissoes {
    public static boolean validaPermissoes(Activity activity, String[] permissoes, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> listaPermissoes = new ArrayList<>();

            //Percorrer as permissões passadas, verificando uma a uma se já tem a permissão liberada
            for (String permissao : permissoes) {
                boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!validaPermissao) {
                    listaPermissoes.add(permissao);
                }
            }

            //Caso a lista esteja vazia não é necessário solicitar permissão
            if(listaPermissoes.isEmpty()){
                return  true;
            }

            //Convertendo o List<> em Vetor
            String[] novasPermissoes = new String[listaPermissoes.size()];
            //Passando os dados do List para o Vetor
            listaPermissoes.toArray(novasPermissoes);
            //Solicitando permissão
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }

        return true;
    }
}
