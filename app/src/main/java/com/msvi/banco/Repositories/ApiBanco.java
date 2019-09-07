package com.msvi.banco.Repositories;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msvi.banco.Clases.Cliente;
import com.msvi.banco.Interfaces.IAcceso;
import com.msvi.banco.Interfaces.IAgregarCliente;
import com.msvi.banco.Interfaces.IListarClientes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ApiBanco
{

    private String url = "http://apibanco.tk/api.php";
    private String correo;
    private String clave;
    private String receive;

    public void acceso(String email, String password, Context context, final IAcceso callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        this.correo = email;
        this.clave = password;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccessResponse(response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onSuccessResponse(error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipo", "acceso");
                params.put("email", correo);
                params.put("password", clave);
                return params;
            }
        };

        queue.add(strRequest);
    }

    public void listarClientes(Context context, final IListarClientes callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                ArrayList<Cliente> customers = new ArrayList<>();
                JSONObject cliente;
                JSONObject resp;
                JSONArray clientes;
                Cliente clienteObtenido;

                try
                {
                    resp = new JSONObject(response);
                    clientes = resp.getJSONArray("users");

                    for(int i = 0; i < clientes.length(); i++)
                    {
                        clienteObtenido = new Cliente();
                        cliente = clientes.getJSONObject(i);
                        clienteObtenido.setIdent(cliente.getString("ident"));
                        clienteObtenido.setNombres(cliente.getString("nombres"));
                        clienteObtenido.setEmail(cliente.getString("email"));
                        clienteObtenido.setClave(cliente.getString("clave"));
                        customers.add(clienteObtenido);
                    }
                    callback.onReturnCustomers(customers);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipo", "listarClientes");
                return params;
            }
        };

        queue.add(strRequest);
    }

    public void AgregarCliente(Context context, final Cliente cliente, final IAgregarCliente callback)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onResponseAddCustomer(response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                params.put("tipo", "insertar");
                params.put("ident", cliente.getIdent());
                params.put("nombres", cliente.getNombres());
                params.put("email", cliente.getEmail());
                params.put("clave", cliente.getClave());

                return params;
            }
        };

        queue.add(strRequest);
    }
}
