package com.msvi.banco.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.msvi.banco.Interfaces.IConsultarCliente;
import com.msvi.banco.Interfaces.IReturnCreateAcount;
import com.msvi.banco.R;
import com.msvi.banco.Repositories.ApiBanco;

import org.json.JSONException;
import org.json.JSONObject;

public class VerClienteActivity extends AppCompatActivity implements View.OnClickListener
{

    TextView etIdentCliente, etNombreCliente, etEmailCliente, etNroCuentaCliente, etSaldoCliente;
    Button btnCrearCuenta;
    ApiBanco banco = new ApiBanco();
    String saldoObtenido, ident;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cliente);
        bindViews();
        getClientes(this, getIntent().getStringExtra("ident"));
    }

    private void bindViews()
    {
        etIdentCliente = findViewById(R.id.etIdentCliente);
        etNombreCliente = findViewById(R.id.etNombreCliente);
        etEmailCliente = findViewById(R.id.etEmailCliente);
        etNroCuentaCliente = findViewById(R.id.etNroCuentaCliente);
        etSaldoCliente = findViewById(R.id.etSaldoCliente);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        btnCrearCuenta.setOnClickListener(this);
    }

    private void getClientes(Context context, final String ident)
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

                    retornarIdent(cliente.getString("ident"));

                    if(cliente.getString("nrocuenta").equals("error"))
                    {
                        etIdentCliente.setText(cliente.getString("ident"));
                        etNombreCliente.setText(cliente.getString("nombres"));
                        etEmailCliente.setText(cliente.getString("email"));
                        etNroCuentaCliente.setText("NO TIENE CUENTA");
                        etSaldoCliente.setText("NO TIENE SALDO");
                    }
                    else
                    {
                        etIdentCliente.setText(cliente.getString("ident"));
                        etNombreCliente.setText(cliente.getString("nombres"));
                        etEmailCliente.setText(cliente.getString("email"));
                        etNroCuentaCliente.setText(cliente.getString("nrocuenta"));
                        etSaldoCliente.setText("$"+cliente.getString("saldo"));
                        btnCrearCuenta.setVisibility(View.INVISIBLE);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    public void showChangeLangDialog()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.crear_cuenta_alert, null);
        final EditText saldo =  dialogView.findViewById(R.id.etSaldoCuenta);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Crear cuenta");
        dialogBuilder.setMessage("Saldo:");

        dialogBuilder.setPositiveButton("Crear", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                retornarSaldo(saldo.getText().toString());
                banco.crearCuenta(VerClienteActivity.this, ident, saldoObtenido, new IReturnCreateAcount() {
                    @Override
                    public void onReturnCreateAcount(String response)
                    {
                        try
                        {
                            JSONObject r = new JSONObject(response);
                            if(r.getString("status").equals("ok"))
                            {
                                Intent intent = new Intent(VerClienteActivity.this, ClientesActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        dialogBuilder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {

            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    public void retornarSaldo(String saldo)
    {
        this.saldoObtenido = saldo;
    }

    public  void retornarIdent(String ident)
    {
        this.ident = ident;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, ClientesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnCrearCuenta:
                showChangeLangDialog();
                break;
        }
    }
}
