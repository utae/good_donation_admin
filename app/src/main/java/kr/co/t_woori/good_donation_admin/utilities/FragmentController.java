package kr.co.t_woori.good_donation_admin.utilities;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * Created by rladn on 2017-08-01.
 */

public class FragmentController {

    private FragmentManager fragmentManager;

    @IdRes private int containerId;

    private ArrayList<Fragment> fragments;

    private int curMenuNum;

    public FragmentController(FragmentManager fragmentManager, int containerId, ArrayList<Fragment> fragments) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.fragments = fragments;
        this.curMenuNum = -1;
    }

    public void selectMenu(int menuNum){
        if(fragments.size() > menuNum){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if(curMenuNum >= 0){
                transaction.hide(fragmentManager.findFragmentByTag(Integer.toString(curMenuNum)));
            }
            if(fragmentManager.findFragmentByTag(Integer.toString(menuNum)) == null){
                transaction.add(containerId, fragments.get(menuNum), Integer.toString(menuNum));
            }else{
                transaction.show(fragmentManager.findFragmentByTag(Integer.toString(menuNum)));
            }
            curMenuNum = menuNum;
            transaction.commit();
        }
    }


}
