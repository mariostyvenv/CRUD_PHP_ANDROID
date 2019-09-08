package com.msvi.banco.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.msvi.banco.Clases.Adapters.AdapterCustomer;
import com.msvi.banco.Clases.Adapters.AdapterTransfer;
import com.msvi.banco.Clases.Transferencia;
import com.msvi.banco.Interfaces.IReturnTransferencia;
import com.msvi.banco.R;
import com.msvi.banco.Repositories.ApiBanco;
import com.msvi.banco.Repositories.PersonalAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListarTransaccionesActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    ApiBanco banco = new ApiBanco();
    ArrayList<String> p;
    PersonalAccount personal = new PersonalAccount();
    AdapterTransfer adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_transacciones);
        bindViews();
    }

    @SuppressLint("WrongConstant")
    private void bindViews()
    {
        recyclerView = findViewById(R.id.recyclerTransfer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        getTransacciones(this);
    }

    private void getTransacciones(Context context)
    {
        p = personal.getPersonal(context);

        banco.consultarTransacciones(this, p.get(0), new IReturnTransferencia()
        {
            @Override
            public void onResponseTransfer(String response)
            {
                ArrayList<Transferencia> transferencias = new ArrayList<>();

                JSONObject resp;
                JSONObject t;
                JSONArray trans;
                Transferencia transferencia;

                try
                {
                    resp = new JSONObject(response);

                    trans = resp.getJSONArray("transacciones"); //Obtengo el arreglo

                    for(int i = 0; i < trans.length(); i++)
                    {
                        transferencia = new Transferencia();
                        t = trans.getJSONObject(i);
                        Log.w("msvi", t.toString());
                        transferencia.setNrotransacc("N°TRANSACCION: ["+t.getString("nrotransacc")+"]");
                        transferencia.setNrocuentaorigen("N°CUENTA ORIGEN: ["+t.getString("nrocuentaorigen")+"]");
                        transferencia.setNrocuentadestino("N°CUENTA DESTINO: ["+t.getString("nrocuentadestino")+"]");
                        transferencia.setFecha("FECHA: ["+t.getString("fecha")+"]");
                        transferencia.setHora("HORA: ["+t.getString("hora")+"]");
                        transferencia.setValor("VALOR: [$"+t.getString("valor")+"]");
                        transferencias.add(transferencia);
                    }
                    adapter = new AdapterTransfer(transferencias);
                    recyclerView.setAdapter(adapter);

                }
                catch (JSONException e)
                {

                }

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, ClientesActivity.class);
        startActivity(intent);
        finish();
    }
}
