package com.example.aggarwal.ridealong;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Aggarwal on 22-06-2016.
 */
public class TakeRide extends Fragment {
    EditText innputSrc;
    EditText innputDes;
    TextView innputDate;
    SharedPreferences sharedPreferences=null;
    String src;
    String des;
    String date;
    Button btn;
    int day;
    int month;
    int years;
    boolean flag;
    String url = "http://ridealong.esy.es/take_ride.php";
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.take_ride,container,false);
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
        innputSrc=(EditText)v.findViewById(R.id.input_source_take_ride);
        final Date currentDate = new Date();
        requestQueue = Volley.newRequestQueue(getActivity());
        innputDes=(EditText)v.findViewById(R.id.input_destination_take_ride);
        innputDate=(TextView)v.findViewById(R.id.input_dept_date_take_ride);
        sharedPreferences=getActivity().getSharedPreferences("search", Context.MODE_PRIVATE);
        innputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        day = dayOfMonth;
                        month = monthOfYear;
                        years = year;
                        innputDate.setText(day + "/" + month + "/" + year);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, 2016, currentDate.getMonth(), currentDate.getDay());
                if (datePickerDialog != null && !datePickerDialog.isShowing()) {
                    datePickerDialog.show();
                }
            }
        });
        btn=(Button)v.findViewById(R.id.submit_take_ride);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                src=innputSrc.getText().toString();
                src=src.toUpperCase();
                des=innputDes.getText().toString();
                des=des.toUpperCase();
                date=innputDate.getText().toString();
                if((!src.isEmpty())&&(!des.isEmpty())&&(date.isEmpty()))
                {
                    Toast.makeText(getActivity(), "Input Date First", Toast.LENGTH_SHORT).show();
                }
//                final DbClassNext dbClassNext=new DbClassNext(getActivity());
//                Boolean available=dbClassNext.checkDatabase(src, des, date);
//                if(available==true)
//                {
//                    Toast.makeText(getActivity(),"Rider Available",Toast.LENGTH_SHORT).show();
//                    SharedPreferences.Editor editor=sharedPreferences.edit();
//                    editor.putString("source", src);
//                    editor.putString("destination", des);
//                    editor.putString("date", date);
//                    editor.commit();
//                    getFragmentManager().beginTransaction().replace(R.id.frame,new Ride_Available()).commit();
//                }
//                else
//                {
//                    Toast.makeText(getActivity(),"No Ride Available",Toast.LENGTH_SHORT).show();
//                }
                else{
                    jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                            //first take out JSON array from inside of JSON Object
                            try {
                                JSONArray jsonArray = response.getJSONArray("data");//data is the name of the JSON array
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);//i is the index of the objects
                                    String src1 = obj.getString("source");//name is same as written in the JSOn url
                                    String des1 = obj.getString("destination");
                                    String date1=obj.getString("dates");
                                    //Toast.makeText(Log_In.this, ""+name, Toast.LENGTH_SHORT).show();
                                    if (src1.equalsIgnoreCase(src) && des1.equalsIgnoreCase(des)&& date1.equalsIgnoreCase(date)) {
//                                        Intent intent=new Intent(Log_In.this,Welcome.class);
//                                        startActivity(intent);
                                        Toast.makeText(getActivity(),"Rider Available",Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("source", src);
                                        editor.putString("destination", des);
                                        editor.putString("date", date);
                                        editor.commit();
                                        flag=true;
                                        getFragmentManager().beginTransaction().replace(R.id.frame,new Ride_Available()).commit();
                                        break;
                                    }
                                    }
                                if(!flag)
                                {
                                    Toast.makeText(getActivity(),"No Ride Available",Toast.LENGTH_SHORT).show();
                                }
                                } catch (JSONException e) {
                                e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "failed to connect to server, try again later...", Toast.LENGTH_SHORT).show();
                        }
                    });

                    requestQueue.add(jsonObjectRequest);
                }
            }
        });
        return v;
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
