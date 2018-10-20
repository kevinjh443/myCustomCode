package com.hogee.myapplicationdemo.viepagerswitchcard;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hogee.myapplicationdemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchFragment();
    }


    /** * Fragment界面切换 */
    private void switchFragment(){
        Fragment fragment = new SwitchFragment();;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_switch_ll,fragment);
        transaction.commitAllowingStateLoss();
    }

}
