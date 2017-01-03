package com.example.aggarwal.ridealong;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Aggarwal on 27-06-2016.
 */
public class MainFragment extends Fragment {
    Button btn1=null;
    Button btn2=null;
    Button btn3=null;
    Button btn4=null;
    DrawerLayout drawerLayout = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activitymain,container,false);
        btn1=(Button)v.findViewById(R.id.button1);
        btn2=(Button)v.findViewById(R.id.button2);
        btn3=(Button)v.findViewById(R.id.button3);
        btn4=(Button)v.findViewById(R.id.button4);
        drawerLayout = (DrawerLayout) v.findViewById(R.id.drawf);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame,new GiveRide()).commit();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame,new TakeRide()).commit();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MapActivity.class);
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MapActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
