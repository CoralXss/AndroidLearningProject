package com.xss.mobile.fragment.tab;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by xss on 2019/8/7.
 */
public class TabFragmentManager {

    private FragmentActivity activity;
    private int currentPosition = -1;
    //使用tablayout来反向决定有哪些fragment
    private TabLayout tabLayout;
    private int contentRes;

    private Fragment[] newFragments() {
        return new Fragment[] {
                new AFragment(),
                new BFragment(),
                new CFragment(),
                new DFragment()
        };
    }

    public TabFragmentManager(FragmentActivity fragmentActivity, int position, TabLayout tabs, int res) {
        currentPosition = position;
        activity = fragmentActivity;
        tabLayout = tabs;
        contentRes = res;
    }

    public void selectTab(int position) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        Fragment fragment = null;
        int tabp = 0;
        boolean isAdded = false;
        Fragment[] contentFragments = null;
        while (tabp < tabLayout.getTabCount()) {
            Fragment fragmentTemp = getFragment(tabp);
            if (position == tabp) {
                if (fragmentTemp == null) {
                    if (contentFragments == null) {
                        contentFragments = newFragments();
                    }
                    if (tabp >= contentFragments.length) {
                        tabp = 0;
                    }
                    fragmentTemp = contentFragments[tabp];
                }
                isAdded = fragmentTemp.isAdded();
                if (!isAdded) {
                    beginTransaction.add(contentRes, fragmentTemp, getFragmentName(tabp));
                    isAdded = true;
                }
                beginTransaction.show(fragmentTemp);
                this.currentPosition = tabp;
                fragment = fragmentTemp;
            } else {
                if (fragmentTemp != null) {
                    beginTransaction.hide(fragmentTemp);
                }
            }
            tabp++;
        }
        beginTransaction.commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
        //tab被修改后，重新走ready方法
        if (isAdded && fragment.getView() != null) {
//            if (fragment instanceof BaseDataBindingFragment) {
//                if (intent != null) {
//                    ((BaseDataBindingFragment) fragment).onReceiverIntent(intent);
//                }
//            }
        }
    }


    public Fragment getFragment(int position) {
        return activity.getSupportFragmentManager().findFragmentByTag(getFragmentName(position));
    }

    public Fragment getCurrentTabFragment() {
        return activity.getSupportFragmentManager().findFragmentByTag(getFragmentName(currentPosition));
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public static String getFragmentName(int position) {
        switch (position) {
            case 0:
                return "AFragment";
            case 1:
                return "BFragment";
            case 2:
                return "CFragment";
            case 3:
                return "DFragment";
            default:
                return "AFragment";
        }
    }



}
