package com.msvi.banco.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;

import com.msvi.banco.Clases.Adapters.AdapterCustomer;
import com.msvi.banco.Clases.Cliente;
import com.msvi.banco.Interfaces.IListarClientes;
import com.msvi.banco.R;
import com.msvi.banco.Repositories.ApiBanco;

import java.util.ArrayList;

public class ClientesActivity extends AppCompatActivity
{
    ApiBanco listarCliente = new ApiBanco();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        bindViews();
        getCustomers(getApplicationContext());
    }

    @SuppressLint("WrongConstant")
    private void bindViews()
    {
        recyclerView = findViewById(R.id.recyclerCustomer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
    }

    private void getCustomers(Context context)
    {
        listarCliente.listarClientes(context, new IListarClientes()
        {
            @Override
            public void onReturnCustomers(ArrayList<Cliente> customers)
            {
                AdapterCustomer adapter = new AdapterCustomer(customers);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_customers, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.agregarCliente:
                Intent intent = new Intent(this, FormClienteActivity.class);
                intent.putExtra("tipo","agregar");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}