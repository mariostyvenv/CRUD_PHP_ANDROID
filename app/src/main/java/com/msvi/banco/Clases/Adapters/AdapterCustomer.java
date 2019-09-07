package com.msvi.banco.Clases.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.msvi.banco.Clases.Cliente;
import com.msvi.banco.R;

import java.util.ArrayList;

public class AdapterCustomer extends RecyclerView.Adapter<AdapterCustomer.ViewHolderCustomers>
{
    ArrayList<Cliente> listaClientes;
    Snackbar snackbar;
    public AdapterCustomer(ArrayList<Cliente> listaClientes)
    {
        this.listaClientes = listaClientes;
    }

    @NonNull
    @Override

    public ViewHolderCustomers onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer,null, false);
        return new ViewHolderCustomers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCustomers holder, final int position)
    {
        holder.asignarClientes(listaClientes.get(position));

        holder.constraintLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Cliente c = listaClientes.get(position);
                snackbar = Snackbar.make (view, "Has seleccionado "+ c.getIdent(), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listaClientes.size();
    }

    public class ViewHolderCustomers extends RecyclerView.ViewHolder
    {
        TextView nombresCliente, idCliente, emailCliente;
        ConstraintLayout constraintLayout;

        public ViewHolderCustomers(@NonNull View itemView)
        {
            super(itemView);
            idCliente = itemView.findViewById(R.id.idCliente);
            nombresCliente = itemView.findViewById(R.id.nombresCliente);
            emailCliente = itemView.findViewById(R.id.emailCliente);
            constraintLayout = itemView.findViewById(R.id.layoutCustomers);
        }

        public void asignarClientes(Cliente cliente)
        {
            idCliente.setText(cliente.getIdent());
            nombresCliente.setText(cliente.getNombres());
            emailCliente.setText(cliente.getEmail());
        }
    }
}
