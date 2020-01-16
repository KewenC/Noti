package com.kewenc.noti.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kewenc.noti.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static TextView tv_flag;
    private static final String[] FLAG={"四级","六级","考研","雅思"};
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        tv_flag=(TextView)view.findViewById(R.id.tv_flag);
        SharedPreferences sp=getContext().getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
        int access_flag=sp.getInt("ACCESS_FLAG",0)==4?0:sp.getInt("ACCESS_FLAG",0);
        tv_flag.setText(FLAG[access_flag]);
        Change(access_flag,getChildFragmentManager());//初始化fragment
        return view;
    }
    /**
     * 词库选取对话框
     */
    public static void SelectAccess(final FragmentManager fragmentManager,Context context) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle("词库选取：");
        mBuilder.setItems(FLAG,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_flag.setText(FLAG[which]);
                        Change(which,fragmentManager);
                    }
                });
        mBuilder.create().show();
    }

    private static void Change(int which,FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction;
//        FragmentManager fragmentManager=context.getChildFragmentManager();
        switch (which){
            case 0:
                Cet4Fragment cet4Fragment=new Cet4Fragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_home,cet4Fragment);
                fragmentTransaction.commit();
                break;
            case 1:
                Cet6Fragment cet6Fragment=new Cet6Fragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_home,cet6Fragment);
                fragmentTransaction.commit();
                break;
            case 2:
                TeefpsFragment teefpsFragment=new TeefpsFragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_home,teefpsFragment);
                fragmentTransaction.commit();
                break;
            case 3:
                IeltsFragment ieltsFragment=new IeltsFragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_home,ieltsFragment);
                fragmentTransaction.commit();
                break;
        }
    }
}
