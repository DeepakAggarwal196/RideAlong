package com.example.aggarwal.ridealong;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Aggarwal on 23-06-2016.
 */
public class SignUp extends Fragment {
    Button sign=null;
    EditText name=null;
    EditText email=null;
    RelativeLayout lay=null;
    EditText contact=null;
    EditText password=null;
    EditText confirm=null;
//http://ridealong.freeoda.com/login.php
    String url="http://ridealong.esy.es/login.php";
    RequestQueue requestQueue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.signup,container,false);
        requestQueue= Volley.newRequestQueue(getActivity());
        name=(EditText) v.findViewById(R.id.iname);
        email=(EditText)v.findViewById(R.id.imail);
        contact=(EditText)v.findViewById(R.id.inumber);
        password=(EditText)v.findViewById(R.id.ipassword);
        confirm=(EditText)v.findViewById(R.id.iconfirmpassword);
        sign=(Button)v.findViewById(R.id.signupbtn);
        if (!isInternetAvailable()) {
            final AlertDialog.Builder builders = new AlertDialog.Builder(getActivity());
            builders.setMessage("Enable Internet first");
            builders.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getFragmentManager().beginTransaction().replace(R.id.frame,new MainFragment()).commit();
                }
            });
            AlertDialog dialog = builders.create();
            dialog.show();
        }
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkvalid()) {
                    createacc();
                } else {
                    Toast.makeText(getActivity(), "Enter Valid Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
    public boolean checkvalid()
    {
        boolean flag=true;
        String inputname=name.getText().toString();
        String inputmail=email.getText().toString();
        String number=contact.getText().toString();
        String inputpassword=password.getText().toString();
        String inputconfirmpssword=confirm.getText().toString();
        if(inputname.isEmpty()||inputmail.isEmpty()||number.isEmpty()||number.length()<10||number.length()>13||inputpassword.isEmpty()||inputconfirmpssword.isEmpty()||!inputpassword.equals(inputconfirmpssword))
        {
            flag=false;
        }
        return flag;
    }
    public void createaccount()
    {
        boolean flag=false;
        DbClass obj=new DbClass(getActivity());
        flag=obj.add(name.getText().toString(),email.getText().toString(),password.getText().toString(),contact.getText().toString());
        if(flag==true)
        {
            Toast.makeText(getActivity(),"Account created Successfully",Toast.LENGTH_SHORT).show();
            getFragmentManager().beginTransaction().replace(R.id.frame,new Login()).commit();
        }
        else
        {
            Toast.makeText(getActivity(),"Account Already Exist",Toast.LENGTH_SHORT).show();
        }
    }
    public void createacc()
    {
        Boolean flag=false;
//        String names,emails,passwords,contacts;
        final String names=name.getText().toString();
        final String emails=email.getText().toString();
        final String passwords=password.getText().toString();
        final String contacts=contact.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Toast.makeText(getActivity(), "" +s, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Failed to process.TRY AGAIN" , Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> myparams = new HashMap<String, String>();
                myparams.put("name", names);
                myparams.put("email", emails);
                myparams.put("password", passwords);
                myparams.put("contact", contacts);
                return myparams;
            }
        };
        requestQueue.add(stringRequest);
        flag=true;
//                    flag = dbClassNext.add(name, contact, src, des, date, time, car, capacity);
        if (flag) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Account created Successfully");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getFragmentManager().beginTransaction().replace(R.id.frame, new Login()).commit();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            Toast.makeText(getActivity(),"Failed to process.TRY AGAIN",Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isInternetAvailable() {
        boolean isWifiAvailable = false;
        boolean isMobileInternetAvailable = false;
        ConnectivityManager connectManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netwrkInfo = connectManager.getAllNetworkInfo();
        for (NetworkInfo networkInfor : netwrkInfo) {
            if (networkInfor.getTypeName().equalsIgnoreCase("WIFI")) {
                if (networkInfor.isConnected()) {
                    isWifiAvailable = true;
                }
            }
            if (networkInfor.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (networkInfor.isConnected()) {
                    isMobileInternetAvailable = true;
                }
            }
        }
        return isMobileInternetAvailable || isWifiAvailable;
    }
}
