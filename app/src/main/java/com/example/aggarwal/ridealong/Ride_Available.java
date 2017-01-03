package com.example.aggarwal.ridealong;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;

/**
 * Created by Aggarwal on 08-07-2016.
 */
public class Ride_Available extends Fragment {
    SharedPreferences sharedPreferences=null;
    String url = "http://ridealong.esy.es/take_ride.php";
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v=inflater.inflate(R.layout.rider_available,container,false);
        requestQueue = Volley.newRequestQueue(getActivity());
        sharedPreferences=getActivity().getSharedPreferences("search", Context.MODE_PRIVATE);
        final String src=sharedPreferences.getString("source","");
        final String des=sharedPreferences.getString("destination","");
        final String date=sharedPreferences.getString("date","");
        final String source[]={"null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null"};
        final String destination[]={"null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null"};
        final String date1[]={"null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null"};
        final String name[]={"null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null"};
        final String contact[]={"null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null"};
        final String time[]={"null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null"};
        final String car[]={"null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null"};
        final String seats[]={"null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null"};
//        final DbClassNext dbClassNext=new DbClassNext(getActivity());
//        Cursor cursor=dbClassNext.fetchDatabase(src,des,date);
//        cursor.moveToFirst();
        final ArrayList<Bin> data = new ArrayList<>();
//        do {
//            String name=cursor.getString(cursor.getColumnIndex("name"));
//            String contact=cursor.getString(cursor.getColumnIndex("contact"));
//            String time=cursor.getString(cursor.getColumnIndex("time"));
//            String car=cursor.getString(cursor.getColumnIndex("car"));
//            String seats=cursor.getString(cursor.getColumnIndex("capacity"));
//            Bin bin=new Bin(name,contact,time,car,seats);
//            data.add(bin);
//            System.out.println(">>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<");
//        }while (cursor.moveToNext());


        jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override

            public void onResponse(JSONObject response) {
                ArrayList<Bin> data = new ArrayList<>();
                //first take out JSON array from inside of JSON Object
                try {
                    JSONArray jsonArray = response.getJSONArray("data");//results is the name of the JSON array
                    int f=0;
//                    ArrayList<Bin> datas = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);//i is the index of the objects
                        source[i]=obj.getString("source");
                        destination[i]=obj.getString("destination");
                        date1[i]=obj.getString("dates");
//                        //name[i] = obj.getString("name");//name is same as written in the JSOn url
//                        //contact[i] = obj.getString("contact");
//                        //time[i]=obj.getString("time");
//                        //car[i]=obj.getString("car");
//                        //seats[i]=obj.getString("capacity");
//                        //Toast.makeText(Log_In.this, ""+name, Toast.LENGTH_SHORT).show();
                        if (false||(source[i].equalsIgnoreCase(src) && destination[i].equalsIgnoreCase(des) && date1[i].equalsIgnoreCase(date))) {
//                            Intent intent=new Intent(Log_In.this,Welcome.class);
//                            startActivity(intent);
                            name[f] = obj.getString("name");//name is same as written in the JSOn url
                            contact[f] = obj.getString("contact");
                            time[f]=obj.getString("time");
                            car[f]=obj.getString("car");
                            seats[f]=obj.getString("capacity");
                            f++;
//                            Toast.makeText(getActivity(), ""+name[f], Toast.LENGTH_SHORT).show();
//                            Bin bin=new Bin(name,contact,time,car,seats);
//                            data.add(bin);
                        }

                    }
                    for(int j=0;j<f;j++)
                    {
//                        Toast.makeText(getActivity(), ""+name[j], Toast.LENGTH_SHORT).show();
                        Bin bin=new Bin(name[j],contact[j],time[j],car[j],seats[j]);
                        data.add(bin);
//                        dataSet(bin,v);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("SIZE: "+data.size());
                MyAdapter adapter = new MyAdapter(getActivity(), R.layout.list_for_rider, data);
                ListView list = (ListView) v.findViewById(R.id.list_for_share);
                list.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);



//        System.out.println("SIZE: "+data.size());
//        MyAdapter adapter = new MyAdapter(getActivity(), R.layout.list_for_rider, data);
//        ListView list = (ListView) v.findViewById(R.id.list_for_share);
//        list.setAdapter(adapter);
        return v;
    }
    public void dataSet(Bin bin,View v)
    {
        final ArrayList<Bin> data = new ArrayList<>();
        data.add(bin);
        System.out.println("SIZE: "+data.size());
        MyAdapter adapter = new MyAdapter(getActivity(), R.layout.list_for_rider, data);
        ListView list = (ListView) v.findViewById(R.id.list_for_share);
        list.setAdapter(adapter);
    }
    class Bin
    {
        String name;
        String contact;
        String time;
        String car;
        String seats;
        Bin(String name,String contact,String time,String car,String seats)
        {
            this.name=name;
            this.contact=contact;
            this.time=time;
            this.car=car;
            this.seats=seats;
        }
        public String getName() {
            return name;
        }
        public String getCar() {
            return car;
        }

        public String getContact() {
            return contact;
        }

        public String getTime() {
            return time;
        }

        public String getSeats() {
            return seats;
        }
    }
    public class MyAdapter extends ArrayAdapter<Bin> {
        Context context;
        int ResId;
        View view=null;
        ArrayList<Bin> data;
        MyAdapter(Context context, int ResId, ArrayList data) {
            super(context, ResId, data);
            this.context = context;
            this.ResId = ResId;
            this.data = data;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           if(convertView==null)
           {
               view = getActivity().getLayoutInflater().inflate(ResId, parent, false);

           }
            Bin obj = data.get(position);
            System.out.println("ADAPTER: "+obj);
            String name=obj.getName();
            String contact=obj.getContact();
            String time=obj.getTime();
            String car=obj.getCar();
            String seats=obj.getSeats();
            TextView textView = (TextView) view.findViewById(R.id.name);
            textView.setText("Name") ;

            TextView textView1 = (TextView) view.findViewById(R.id.name_value);
            textView1.setText(name);
            TextView textView2 = (TextView) view.findViewById(R.id.contact);
            textView2.setText("Contact") ;
            TextView textView3 = (TextView) view.findViewById(R.id.contact_value);
            textView3.setText(contact);
            TextView textView4 = (TextView) view.findViewById(R.id.dept_time);
            textView4.setText("Departure Time (24hr format)") ;
            TextView textView5 = (TextView) view.findViewById(R.id.dept_time_value);
            textView5.setText(time);
            TextView textView6 = (TextView) view.findViewById(R.id.car);
            textView6.setText("Car") ;
            TextView textView7 = (TextView) view.findViewById(R.id.car_value);
            textView7.setText(car);
            TextView textView8 = (TextView) view.findViewById(R.id.seats);
            textView8.setText("Seats Available") ;
            TextView textView9 = (TextView) view.findViewById(R.id.seats_value);
            textView9.setText(seats);
            return view;
        }
    }
}
