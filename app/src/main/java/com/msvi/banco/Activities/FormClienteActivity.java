package com.msvi.banco.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import com.msvi.banco.Clases.Cliente;
import com.msvi.banco.Interfaces.IActualizarCliente;
import com.msvi.banco.Interfaces.IAgregarCliente;
import com.msvi.banco.Interfaces.IConsultarCliente;
import com.msvi.banco.R;
import com.msvi.banco.Repositories.ApiBanco;
import org.json.JSONException;
import org.json.JSONObject;

public class FormClienteActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText etIdentForm, etNombresForm, etEmailForm, etClaveForm;
    Button btnEnviarForm;
    TextView tvForm;
    String entorno;

    ApiBanco banco = new ApiBanco();
    Cliente clienteNuevo = new Cliente();
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cliente);
        bindViews();

        entorno = getIntent().getStringExtra("tipo");
        if(entorno.equals("agregar"))
        {
            entornoAgregar();
        }
        else if(entorno.equals("actualizar"))
        {
            entornoActualizar();
        }
    }

    private void bindViews()
    {
        etIdentForm = findViewById(R.id.etIdentForm);
        etNombresForm = findViewById(R.id.etNombresForm);
        etEmailForm = findViewById(R.id.etEmailForm);
        etClaveForm = findViewById(R.id.etClaveForm);
        btnEnviarForm = findViewById(R.id.btnEnviarForm);
        tvForm = findViewById(R.id.tvForm);
    }

    private void entornoAgregar()
    {
        tvForm.setText("Agregar");
        btnEnviarForm.setOnClickListener(this);
    }

    private void entornoActualizar()
    {
        tvForm.setText("Actualizar");
        etIdentForm.setFocusable(false);
        etIdentForm.setEnabled(false);
        etIdentForm.setCursorVisible(false);
        etIdentForm.setKeyListener(null);
        etIdentForm.setBackgroundColor(Color.TRANSPARENT);

        btnEnviarForm.setOnClickListener(this);
        String ident = getIntent().getStringExtra("id");
        consultarCliente(this, ident);
    }

    private void agregarCliente(Cliente cliente, final View view)
    {
        banco.agregarCliente(this, cliente, new IAgregarCliente() {
            @Override
            public void onResponseAddCustomer(String respuesta)
            {
                try
                {
                    JSONObject resp = new JSONObject(respuesta);
                    if (resp.getString("status").equals("ok"))
                    {
                        snackbar = Snackbar.make (view, "Agregado correctamente", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        Intent intent = new Intent(FormClienteActivity.this, ClientesActivity.class);
                        intent.putExtra("tipo","agregado");
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        snackbar = Snackbar.make (view, "Ocurrio un error", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void actualizarCliente(Context context, Cliente cliente, final View view)
    {
        banco.actualizarCliente(context, cliente, new IActualizarCliente() {
            @Override
            public void onResponseStatus(String response) {
                try
                {
                    JSONObject resp = new JSONObject(response);
                    if(resp.getString("status").equals("ok"))
                    {
                        Intent intent = new Intent(FormClienteActivity.this, ClientesActivity.class);
                        intent.putExtra("tipo","actualizacion");
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        snackbar = Snackbar.make (view, "Ocurrio un error", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    private void consultarCliente(Context context, String ident)
    {
        banco.consultarCliente(context, ident, new IConsultarCliente()
        {
            @Override
            public void onResponseCustomer(String response)
            {
                try
                {
                    JSONObject resp = new JSONObject(response);
                    JSONObject cliente = resp.getJSONObject("user");

                    etIdentForm.setText(cliente.getString("ident"));
                    etNombresForm.setText(cliente.getString("nombres"));
                    etEmailForm.setText(cliente.getString("email"));
                    etClaveForm.setText(cliente.getString("clave"));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnEnviarForm:
                if(entorno.equals("agregar"))
                {
                    clienteNuevo.setIdent(etIdentForm.getText().toString());
                    clienteNuevo.setNombres(etNombresForm.getText().toString());
                    clienteNuevo.setEmail(etEmailForm.getText().toString());
                    clienteNuevo.setClave(etClaveForm.getText().toString());
                    agregarCliente(clienteNuevo, view);
                }
                else if(entorno.equals("actualizar"))
                {
                    clienteNuevo.setIdent(etIdentForm.getText().toString());
                    clienteNuevo.setNombres(etNombresForm.getText().toString());
                    clienteNuevo.setEmail(etEmailForm.getText().toString());
                    clienteNuevo.setClave(etClaveForm.getText().toString());
                    actualizarCliente(this, clienteNuevo, view);
                }
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(FormClienteActivity.this, ClientesActivity.class);
        startActivity(intent);
        finish();
    }
}
