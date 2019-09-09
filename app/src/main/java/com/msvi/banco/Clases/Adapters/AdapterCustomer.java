package com.msvi.banco.Clases.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.msvi.banco.Activities.FormClienteActivity;
import com.msvi.banco.Activities.VerClienteActivity;
import com.msvi.banco.Clases.Cliente;
import com.msvi.banco.Interfaces.IEliminarCliente;
import com.msvi.banco.Interfaces.IReturnDelete;
import com.msvi.banco.R;
import com.msvi.banco.Repositories.ApiBanco;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class AdapterCustomer extends RecyclerView.Adapter<AdapterCustomer.ViewHolderCustomers>
{
    ArrayList<Cliente> listaClientes;
    Snackbar snackbar;
    Cliente cliente;
    Activity activity;
    ApiBanco banco = new ApiBanco();

    public AdapterCustomer(ArrayList<Cliente> listaClientes, Activity activity)
    {
        this.listaClientes = listaClientes;
        this.activity = activity;
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
                Intent intent = new Intent(view.getContext(), VerClienteActivity.class);
                intent.putExtra("ident",c.getIdent());
                view.getContext().startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listaClientes.size();
    }

    public class ViewHolderCustomers extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
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
            constraintLayout.setOnCreateContextMenuListener(this);
        }

        public void asignarClientes(Cliente cliente)
        {
            idCliente.setText(cliente.getIdent());
            nombresCliente.setText(cliente.getNombres());
            emailCliente.setText(cliente.getEmail());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
        {
            contextMenu.add(this.getAdapterPosition(),1,0,"Actualizar");
            contextMenu.add(this.getAdapterPosition(),2,1,"Eliminar");
        }

    }
    public void actualizar(int position, Context context, Activity activity)
    {
        cliente = listaClientes.get(position);
        Intent intent = new Intent(context, FormClienteActivity.class);
        intent.putExtra("tipo","actualizar");
        intent.putExtra("id",cliente.getIdent());
        context.startActivity(intent);
        activity.finish();
    }

    public void eliminar(int position, final View view, final Context context, final IReturnDelete callback)
    {
        cliente = listaClientes.get(position);
        banco.eliminarCliente(context, cliente.getIdent(), new IEliminarCliente()
        {
            @Override
            public void onResponseStatus(String response)
            {
                try
                {
                    JSONObject resp = new JSONObject(response);
                    if(resp.getString("status").equals("ok"))
                    {
                        callback.onReturnDelete(true);

                        Snackbar.make(view, "ELIMINACION CORRECTA", Snackbar.LENGTH_LONG).show();
                    }
                    else
                    {
                        callback.onReturnDelete(false);
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
