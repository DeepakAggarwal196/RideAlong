package com.example.aggarwal.ridealong;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by Aggarwal on 25-06-2016.
 */
public class HelpFragment extends Fragment {
    SharedPreferences sharedPreferences=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.help,container,false);
        return v;
    }
}

