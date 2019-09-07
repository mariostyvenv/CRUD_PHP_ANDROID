package com.msvi.banco.Interfaces;

import com.msvi.banco.Clases.Cliente;

import java.util.ArrayList;

public interface IListarClientes
{
    void onReturnCustomers(ArrayList<Cliente> customers);
}
