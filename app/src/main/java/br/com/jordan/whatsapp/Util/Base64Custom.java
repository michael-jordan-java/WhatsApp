package br.com.jordan.whatsapp.Util;

import android.util.Base64;

/**
 * Created by User on 20/08/2017.
 */

public class Base64Custom {

    public static String toBase64(String encode){
        return Base64.encodeToString(encode.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String fromBase64(String decode){
        return new String(Base64.decode(decode,Base64.DEFAULT));
    }
}
