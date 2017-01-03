package com.example.aggarwal.ridealong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FirstActivity extends Activity {
    DrawerLayout drawerLayout = null;
    ImageView imageView = null;
    SharedPreferences sharedPreferences=null;
    TextView txt=null;
    ArrayList<DatasClass> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
//      AIzaSyBv1kPA8JT3r5HQjwJput3t6cM60SztFdI api key
        sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);
        ListView list = (ListView) findViewById(R.id.nav_list);
        String[] names = {"HOME", "GIVE RIDE", "TAKE RIDE", "FIND PETROL PUMP", "FIND GARAGE","ABOUT US","HELP"};
        Integer[] imageId={R.drawable.home,R.drawable.give,R.drawable.take,R.drawable.petrol,R.drawable.garage,R.drawable.about,R.drawable.help};
        imageView = (ImageView) findViewById(R.id.img);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawf);
        for (int i = 0; i < names.length; i++) {
            DatasClass d = new DatasClass(names[i], imageId[i]);
            datas.add(d);
        }
        MyAdapter adapter = new MyAdapter(this, R.layout.list_for_headers, datas);
        list.setAdapter(adapter);
        list.addFooterView(getLayoutInflater().inflate(R.layout.footer, null, false));
        txt=(TextView) findViewById(R.id.footertext);
        try {
            String isLogin = sharedPreferences.getString("loginTrue", "false");
            if (isLogin.equalsIgnoreCase("true")) {
                String name = sharedPreferences.getString("name", "");
                txt.setText(name);
            } else if (isLogin.equalsIgnoreCase("false")) {
                txt.setText("GUEST");
            }
        }
        catch (Exception E) {
            Toast.makeText(FirstActivity.this, "" + E, Toast.LENGTH_SHORT).show();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        getFragmentManager().beginTransaction().replace(R.id.frame, new MainFragment()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 1: {
                        getFragmentManager().beginTransaction().replace(R.id.frame, new GiveRide()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 2: {
                        getFragmentManager().beginTransaction().replace(R.id.frame, new TakeRide()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 3: {
                        Intent intent=new Intent(FirstActivity.this, MapActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent=new Intent(FirstActivity.this, MapActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 5: {
                        getFragmentManager().beginTransaction().replace(R.id.frame, new AboutFragment()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 6: {
                        getFragmentManager().beginTransaction().replace(R.id.frame, new HelpFragment()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 7: {
                        try {
                            String isLogin = sharedPreferences.getString("loginTrue", "false");
                            if (isLogin.equalsIgnoreCase("true")) {
                                getFragmentManager().beginTransaction().replace(R.id.frame, new UserAccountDetail()).commit();
                                drawerLayout.closeDrawers();
                            } else if (isLogin.equalsIgnoreCase("false")) {
                                getFragmentManager().beginTransaction().replace(R.id.frame, new Login()).commit();
                                drawerLayout.closeDrawers();
                            }
                        } catch (Exception E) {
                            Toast.makeText(FirstActivity.this, "" + E, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        drawerLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.iconheaders));
                } else {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.icons));
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                super.onBackPressed();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        //add pop up here
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFragmentManager().beginTransaction().replace(R.id.frame, new MainFragment()).commit();
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
            View view = getLayoutInflater().inflate(ResId, parent, false);
            ImageView img = (ImageView) view.findViewById(R.id.drawerimage);
            DatasClass obj = datas.get(position);
            String name = obj.getName();
            Integer number = obj.getNumber();
            img.setImageDrawable(getDrawable(number));
            TextView textView = (TextView) view.findViewById(R.id.drawertext);
            textView.setText(name) ;
            return view;
        }
    }
}
