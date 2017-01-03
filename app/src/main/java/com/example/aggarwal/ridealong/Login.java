package com.example.aggarwal.ridealong;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aggarwal on 22-06-2016.
 */
public class Login extends Fragment {
    Button login =null;
    SharedPreferences sharedPreferences=null;
    Button signup=null;
    EditText usermail=null;
    EditText password=null;
    String mail=null;
    String url = "http://ridealong.esy.es/logincheck.php";
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    boolean flag;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.login,container,false);
        sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        password = (EditText) v.findViewById(R.id.enterPassword);
        usermail = (EditText) v.findViewById(R.id.enterMail);
        requestQueue = Volley.newRequestQueue(getActivity());
        login =(Button)v.findViewById(R.id.login);
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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datValid()){
//                    mail = usermail.getText().toString();
//                    DbClass dbClass=new DbClass(getActivity());
//                    if (loginTrue()) {
//                        final String name=dbClass.fetchName(mail);
//                        final String number=dbClass.fetchContact(mail);
//                        Toast.makeText(getActivity(), "welcome " + name, Toast.LENGTH_SHORT).show();
//                        SharedPreferences.Editor editor=sharedPreferences.edit();
//                        editor.putString("loginTrue","true");
//                        editor.putString("name", name);
//                        editor.putString("mail", mail);
//                        editor.putString("contact", number);
//                        editor.commit();
//                        Intent intent=new Intent(getActivity(),FirstActivity.class);
//                        Activity activity=getActivity();        //problem
//                        startActivity(intent);
//                        activity.finish();          //problem
//                    } else {
//                        Toast.makeText(getActivity(), "Username and password do not match", Toast.LENGTH_SHORT).show();
//                    }
                    jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
                        @Override

                        public void onResponse(JSONObject response) {
                            //first take out JSON array from inside of JSON Object
                            try {
                                JSONArray jsonArray = response.getJSONArray("data");//results is the name of the JSON array
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);//i is the index of the objects
                                    String name1 = obj.getString("name");//name is same as written in the JSOn url
                                    String email1 = obj.getString("email");
                                    String pass1=obj.getString("password");
                                    String cont1=obj.getString("contact");
                                    //Toast.makeText(Log_In.this, ""+name, Toast.LENGTH_SHORT).show();
                                    if (email1.equalsIgnoreCase(usermail.getText().toString()) && pass1.equalsIgnoreCase(password.getText().toString())) {
//                                        Intent intent=new Intent(Log_In.this,Welcome.class);
//                                        startActivity(intent);
                                        Toast.makeText(getActivity(),"Welcome "+name1,Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("loginTrue","true");
                                        editor.putString("name", name1);
                                        editor.putString("mail", email1);
                                        editor.putString("contact",cont1);
                                        editor.commit();
                                        flag=true;
//                                        getFragmentManager().beginTransaction().replace(R.id.frame,new Ride_Available()).commit();
                                        Intent intent=new Intent(getActivity(),FirstActivity.class);
                                        Activity activity=getActivity();        //problem
                                        startActivity(intent);
                                        activity.finish();
                                        break;
                                    }
                                }
                                if(!flag)
                                {
                                    Toast.makeText(getActivity(),"Account does not exist, Create Account first...",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "login failed...Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });

                    requestQueue.add(jsonObjectRequest);
                }
                else
                {
                    Toast.makeText(getActivity(), "Invalid Username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signup=(Button)v.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame,new SignUp()).commit();
            }
        });
        return v;
    }
    public boolean loginTrue()
    {
        boolean flag=false;
        String mail=null,pass=null;
        mail = usermail.getText().toString();
        pass = password.getText().toString();
        DbClass dbDemo=new DbClass(getActivity());
        flag=dbDemo.fetchDatabase(mail,pass);
        return flag;
    }
    public boolean datValid()
    {
        boolean flag=true;
        String mail=usermail.getText().toString();
        String pass=password.getText().toString();
        if(mail.isEmpty()||pass.isEmpty())
        {
            flag=false;
        }
        return flag;
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
