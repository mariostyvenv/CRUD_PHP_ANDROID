package com.msvi.banco.Clases.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.msvi.banco.Clases.Transferencia;
import com.msvi.banco.R;
import java.util.ArrayList;

public class AdapterTransfer extends RecyclerView.Adapter<AdapterTransfer.ViewHolderCustomers>
{
    ArrayList<Transferencia> listarTransacciones;

    public AdapterTransfer(ArrayList<Transferencia> listarTransacciones)
    {
        this.listarTransacciones = listarTransacciones;
    }

    @NonNull
    @Override
    public ViewHolderCustomers onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer,null, false);
        return new ViewHolderCustomers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCustomers holder, final int position)
    {
        holder.asignarTransaccion(listarTransacciones.get(position));
    }

    @Override
    public int getItemCount()
    {
        return listarTransacciones.size();
    }

    public class ViewHolderCustomers extends RecyclerView.ViewHolder
    {
        TextView nrotransacc, nrocuentaorigen, nrocuentadestino, tvfecha, tvhora, tvvalor;

        public ViewHolderCustomers(@NonNull View itemView)
        {
            super(itemView);
            nrotransacc = itemView.findViewById(R.id.nrotransacc);
            nrocuentaorigen = itemView.findViewById(R.id.nrocuentaorigen);
            nrocuentadestino = itemView.findViewById(R.id.nrocuentadestino);
            tvfecha = itemView.findViewById(R.id.tvfecha);
            tvhora = itemView.findViewById(R.id.tvhora);
            tvvalor = itemView.findViewById(R.id.tvvalor);
        }

        public void asignarTransaccion(Transferencia transferencia)
        {
            nrotransacc.setText(transferencia.getNrotransacc());
            nrocuentaorigen.setText(transferencia.getNrocuentaorigen());
            nrocuentadestino.setText(transferencia.getNrocuentadestino());
            tvfecha.setText(transferencia.getFecha());
            tvhora.setText(transferencia.getHora());
            tvvalor.setText(transferencia.getValor());
        }
    }
}