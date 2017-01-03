package com.example.aggarwal.ridealong;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Aggarwal on 23-06-2016.
 */
public class UserAccountDetail extends Fragment{
    SharedPreferences sharedPreferences=null;
    TextView txt=null;
    Button btn=null;
    ImageView imageView = null;
    DrawerLayout drawerLayout=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.useraccount,container,false);
        btn=(Button)v.findViewById(R.id.logout);
        ListView list = (ListView) v.findViewById(R.id.l_list_1);
        sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("name", "");
//        final String usermail = sharedPreferences.getString("mail", "");
        final String usercontact = sharedPreferences.getString("contact", "");
//        DbClass db=new DbClass(getActivity());
        String[] name = {"NAME", "CONTACT", "EMAIL"};
        try {
            String mail=sharedPreferences.getString("mail", "");
//            DataClass dbClass=db.fetchNameByEmail(mail);
            String[] contact = {username, usercontact,mail};
            ArrayList<DataClass> data = new ArrayList<>();
            for (int i = 0; i < name.length; i++) {
                DataClass d = new DataClass(name[i], contact[i]);
                data.add(d);
            }
            MyAdapter adapter = new MyAdapter(getActivity(), R.layout.list, data);

            list.setAdapter(adapter);
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),""+e,Toast.LENGTH_SHORT).show();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("loginTrue", "false");
                editor.remove("name");
                editor.remove("mail");
                editor.remove("contact");
                editor.commit();
                Intent intent=new Intent(getActivity(),FirstActivity.class);
                Activity activity=getActivity();        //problem
                startActivity(intent);
                activity.finish();                      //problem
            }
        });
        return v;
    }

    public class MyAdapter extends ArrayAdapter<DataClass> {
        Context context;
        int ResId;
        ArrayList<DataClass> data;

        MyAdapter(Context context, int ResId, ArrayList data) {
            super(context, ResId, data);
            this.context = context;
            this.ResId = ResId;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getActivity().getLayoutInflater().inflate(ResId, parent, false);    //problem 1
            DataClass obj = data.get(position);
            String name = obj.getName();
            String number = obj.getNumber();
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(name) ;
            TextView textView1 = (TextView) view.findViewById(R.id.number);
            textView1.setText(number);
            return view;
        }
    }
}
