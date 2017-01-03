package com.example.aggarwal.ridealong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aggarwal on 08-07-2016.
 */
public class LocationDetail extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail);
        String names= getIntent().getStringExtra("name");
        String location= getIntent().getStringExtra("loc");
        String rating= getIntent().getStringExtra("rat");
        String[] name = {"Name", "Location", "Rating"};
        String[] values = {names,location,rating};
        ArrayList<DataPlaceClass> data = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            DataPlaceClass d = new DataPlaceClass(name[i], values[i]);
            data.add(d);
        }
        MyAdapter adapter = new MyAdapter(this, R.layout.list_loc, data);
        ListView list = (ListView) findViewById(R.id.list_loc_det);
        list.setAdapter(adapter);
    }
    public class DataPlaceClass {
        String nameL, locationL;

        DataPlaceClass( String nameL, String locationL) {
            this.nameL= nameL;
            this.locationL = locationL;
        }

        public String getName() {
            return nameL;
        }

        public String getValue() {
            return locationL;
        }
    }
    public class MyAdapter extends ArrayAdapter<DataPlaceClass> {
        Context context;
        int ResId;
        ArrayList<DataPlaceClass> data;
        MyAdapter(Context context, int ResId, ArrayList data) {
            super(context, ResId, data);
            this.context = context;
            this.ResId = ResId;
            this.data = data;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(ResId, parent, false);
            DataPlaceClass obj = data.get(position);
            String name = obj.getName();
            String number = obj.getValue();
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(name) ;
            TextView textView1 = (TextView) view.findViewById(R.id.number);
            textView1.setText(number);
            return view;
        }
    }
}
