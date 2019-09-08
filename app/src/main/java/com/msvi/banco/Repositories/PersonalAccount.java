package com.msvi.banco.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class PersonalAccount
{
    private SharedPreferences sharedPref;


    public void createPersonal(Context context, String ident, String cuenta)
    {
        sharedPref = context.getSharedPreferences("personal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ident",ident);
        editor.putString("nrocuenta", cuenta);
        editor.commit();
    }

    public ArrayList<String> getPersonal(Context context)
    {
        ArrayList<String> listado = new ArrayList<>();
        sharedPref = context.getSharedPreferences("personal", Context.MODE_PRIVATE);
        listado.add(sharedPref.getString("ident",""));
        listado.add(sharedPref.getString("nrocuenta",""));
        return listado;
    }

}
