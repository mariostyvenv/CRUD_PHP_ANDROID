package com.msvi.banco.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.msvi.banco.Interfaces.IAcceso;
import com.msvi.banco.R;
import com.msvi.banco.Repositories.ApiBanco;
import com.msvi.banco.Repositories.PersonalAccount;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnIngresar;
    private ProgressBar progressBar;
    private Snackbar snackbar;
    private EditText etCorreo, etClave;
    private String correo, clave;
    private ApiBanco login = new ApiBanco();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnAcceso:
                correo = etCorreo.getText().toString();
                clave = etClave.getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                checkSesion(correo, clave, view);
                break;
        }
    }

    private void bindViews()
    {
        btnIngresar = findViewById(R.id.btnAcceso);
        btnIngresar.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
        etCorreo = findViewById(R.id.etCorreo);
        etClave = findViewById(R.id.etClaveForm);
    }

    private void checkSesion(String correo, String clave, final View view)
    {
        login.acceso(correo, clave, getApplicationContext(), new IAcceso() {
            @Override
            public void onSuccessResponse(String result)
            {
                try
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject obj = new JSONObject(result);
                    JSONObject client = obj.getJSONObject("user");
                    Log.w("msvi", result);

                    PersonalAccount personal = new PersonalAccount();
                    personal.createPersonal(LoginActivity.this, client.getString("ident"), client.getString("nrocuenta"));
                    String status = obj.getString("estatus");

                    if(status.equals("ok"))
                    {
                        snackbar = Snackbar.make (view, "Acceso Correcto", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        Intent intent = new Intent(getApplicationContext(), ClientesActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(status.equals("error"))
                    {
                        snackbar = Snackbar.make (view, "Correo o contraseña incorrectos", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                    else
                    {
                        snackbar = Snackbar.make (view, "Ocurrió un error", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
                catch (JSONException e)
                {
                    snackbar = Snackbar.make (view, "Ocurrió un error", Snackbar.LENGTH_LONG);
                    Log.w("msvi", e.toString());
                    snackbar.show();
                }
            }
        });
    }
}
