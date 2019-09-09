package com.msvi.banco.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.msvi.banco.Interfaces.IConsultarCliente;
import com.msvi.banco.Interfaces.IReturnCreateAcount;
import com.msvi.banco.Interfaces.IReturnTransferencia;
import com.msvi.banco.R;
import com.msvi.banco.Repositories.ApiBanco;
import com.msvi.banco.Repositories.PersonalAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VerClienteActivity extends AppCompatActivity implements View.OnClickListener
{

    TextView etIdentCliente, etNombreCliente, etEmailCliente, etNroCuentaCliente, etSaldoCliente;
    Button btnCrearCuenta, btnTransferir;
    ApiBanco banco = new ApiBanco();
    String saldoObtenido, ident, cuentaDestino;

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
        btnTransferir = findViewById(R.id.btnTransferir);

        btnCrearCuenta.setOnClickListener(this);
        btnTransferir.setOnClickListener(this);

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
                        btnTransferir.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        retornarCuentaDestino(cliente.getString("nrocuenta"));

                        etIdentCliente.setText(cliente.getString("ident"));
                        etNombreCliente.setText(cliente.getString("nombres"));
                        etEmailCliente.setText(cliente.getString("email"));
                        etNroCuentaCliente.setText(cliente.getString("nrocuenta"));
                        etSaldoCliente.setText("$"+cliente.getString("saldo"));
                        btnCrearCuenta.setVisibility(View.INVISIBLE);
                        //Aquí validar que el cliente tiene cuenta
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    public void dialogSaldo(final String estado, final View view)
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
                if(estado.equals("crear"))
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
                else if(estado.equals("transferir"))
                {
                    retornarSaldo(saldo.getText().toString());

                    PersonalAccount personal = new PersonalAccount();
                    ArrayList<String> p = personal.getPersonal(getApplicationContext());

                    banco.realizarTransaccion(VerClienteActivity.this, p.get(1), cuentaDestino, saldoObtenido, new IReturnTransferencia() {
                        @Override
                        public void onResponseTransfer(String response)
                        {
                            try
                            {
                                JSONObject r = new JSONObject(response);
                                Log.w("msvi", r.toString());
                                if(r.getString("estatus").equals("ok"))
                                {
                                    Intent intent = new Intent(VerClienteActivity.this, ClientesActivity.class);
                                    intent.putExtra("tipo","transferencia");
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Log.w("msvi",r.getString("message"));
                                    if(r.getString("message").equals("saldo insuficiente"))
                                    {
                                        Snackbar.make(view, "Saldo Insuficiente", Snackbar.LENGTH_SHORT).show();
                                    }
                                    else if(r.getString("message").equals("no puede enviar cantidades menores a 1"))
                                    {
                                        Snackbar.make(view, "No puede enviar menos que 1$", Snackbar.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Snackbar.make(view, r.getString("message"), Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });
                }
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
    public void retornarSaldo(String saldo) { this.saldoObtenido = saldo; }

    public void retornarIdent(String ident) { this.ident = ident; }

    public void retornarCuentaDestino(String cuenta){this.cuentaDestino = cuenta;}

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
                dialogSaldo("crear", view);
                break;

            case R.id.btnTransferir:
                dialogSaldo("transferir", view);
                break;
        }
    }
}
