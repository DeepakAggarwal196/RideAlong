package com.example.aggarwal.ridealong;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GiveRide extends Fragment {
    SharedPreferences sharedPreferences = null;
    EditText innputSrc;
    EditText innputDes;
    TextView innputDate;
    TextView innputTime;
    EditText innputCar;
    EditText innputCapacity;
    String name;
    String contact;
    String car;
    String capacity;
    String src;
    String des;
    String date;
    String time;
    Button btn;
    int day;
    int month;
    int years;
    int hh;
    int mm;
    String url="http://ridealong.esy.es/insert_ride.php";
    RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.give_ride, container, false);
        requestQueue= Volley.newRequestQueue(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String isLogin = sharedPreferences.getString("loginTrue", "false");
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
        else if (isLogin.equalsIgnoreCase("false")) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Please Login First");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getFragmentManager().beginTransaction().replace(R.id.frame, new MainFragment()).commit();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        final Date currentDate = new Date();
        innputSrc = (EditText) v.findViewById(R.id.input_source_give_ride);
        innputDes = (EditText) v.findViewById(R.id.input_destination_give_ride);
        innputDate = (TextView) v.findViewById(R.id.input_dept_date_give_ride);
        innputTime = (TextView) v.findViewById(R.id.input_dept_time_give_ride);
        innputCar = (EditText) v.findViewById(R.id.input_car_give_ride);
        innputCapacity = (EditText) v.findViewById(R.id.input_capacity_give_ride);
        innputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter=0;
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        day = dayOfMonth;
                        month = monthOfYear;
                        years = year;
                        innputDate.setText(day + "/" + month + "/" + year);
                    }
                }, 2016, currentDate.getMonth(), currentDate.getDay());
                if (datePickerDialog != null && !datePickerDialog.isShowing()&&counter<=1) {
                    datePickerDialog.show();
                    counter++;
                }
            }
        });
        innputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hh = hourOfDay;
                        mm = minute;
                        innputTime.setText(hh + ":" + mm);
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), listener, 12,0, true);
                if(timePickerDialog!=null&&!timePickerDialog.isShowing())
                {
                    timePickerDialog.show();
                }
            }
        });
        btn = (Button) v.findViewById(R.id.submit_give_ride);
        final DbClassNext dbClassNext = new DbClassNext(getActivity());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag;
                src = innputSrc.getText().toString();
                src=src.toUpperCase();
                des = innputDes.getText().toString();
                des=des.toUpperCase();
                date = innputDate.getText().toString();
                time = innputTime.getText().toString();
                car = innputCar.getText().toString();
                capacity = innputCapacity.getText().toString();
                name = sharedPreferences.getString("name", "");
                contact = sharedPreferences.getString("contact", "");
                if(src.isEmpty()||des.isEmpty()||date.isEmpty()||time.isEmpty()||car.isEmpty()||capacity.isEmpty())
                {
                    Toast.makeText(getActivity(),"Entered Data is Invalid",Toast.LENGTH_SHORT).show();
                }
                else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
//                            Toast.makeText(getActivity(), "" + s, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "failed to coonect to server, Try again later" , Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> myparams = new HashMap<String, String>();
                            myparams.put("name", name);
                            myparams.put("contact", contact);
                            myparams.put("source", src);
                            myparams.put("destination", des);
                            myparams.put("dates", date);
                            myparams.put("time", time);
                            myparams.put("car", car);
                            myparams.put("capacity", capacity);
                            return myparams;
                        }
                    };
                    requestQueue.add(stringRequest);
                    flag=true;
//                    flag = dbClassNext.add(name, contact, src, des, date, time, car, capacity);
                    if (flag) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Information added Successfully");
                        builder.setMessage("Wait for the user to contact");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getFragmentManager().beginTransaction().replace(R.id.frame, new MainFragment()).commit();
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
