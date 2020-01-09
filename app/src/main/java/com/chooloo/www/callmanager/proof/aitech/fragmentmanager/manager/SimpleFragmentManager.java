package com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.parceler.Parcels;


public class SimpleFragmentManager {

    public static final String KEY_TAGS = "key_tags";
    private final FragmentManager fragmentManager;
    private final int fragmentContainerId;
    private FragmentTagStack fragmentTagStack = new FragmentTagStack();
    private static String STACK_LAST_FRAGMENT_NAME = "";

    public SimpleFragmentManager(FragmentManager fragmentManager, int fragmentContainerId) {

        this.fragmentManager = fragmentManager;
        this.fragmentContainerId = fragmentContainerId;
    }

    public void addFragment(IFragment fragment) {
        fragmentTagStack.push(fragment.getFragmentName());
        fragmentManager.beginTransaction()
                .add(fragmentContainerId, (Fragment) fragment, fragment.getFragmentName())
                .addToBackStack(fragment.getFragmentName())
                .commitAllowingStateLoss();
    }

    public void replaceFragment(IFragment fragment) {
        fragmentTagStack.push(fragment.getFragmentName());
        fragmentManager.beginTransaction()
                .replace(fragmentContainerId, (Fragment) fragment, fragment.getFragmentName())
                .addToBackStack(fragment.getFragmentName())
                .commitAllowingStateLoss();
    }

    public boolean onBackPressed() {
        IFragment currentFragmentBeforePop = getCurrentFragment();
        if (currentFragmentBeforePop != null)
            STACK_LAST_FRAGMENT_NAME = currentFragmentBeforePop.getFragmentName();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            popUp();
            IFragment currentFragment = getCurrentFragment();
            if (currentFragment != null) {
                currentFragment.setTitle();
                STACK_LAST_FRAGMENT_NAME = currentFragment.getFragmentName();
                return true;
            }
        }

        return false;
    }

    public String getFragmentName() {
        return STACK_LAST_FRAGMENT_NAME;
    }
     /*Name: AIT-Android , Date: 20-02-2019 , Feature ID: merging issues
     Desc: Removing the unused code, Release: Project-Skoda*/

    public void popUp() {
        fragmentManager.popBackStackImmediate();
        fragmentTagStack.pop();
    }

    public void popUpAll() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTagStack.popUpAll();
    }

    public IFragment getCurrentFragment() {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(fragmentTagStack.getActiveTag());
        if (fragmentByTag != null) {
            return ((IFragment) fragmentByTag);
        } else {
            return null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data, null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, String targetTag) {
        String tag = targetTag;
        if (TextUtils.isEmpty(tag)) {
            tag = fragmentTagStack.getActiveTag();
        }

        if (!TextUtils.isEmpty(tag)) {
            Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);
            if (fragmentByTag != null) {
                fragmentByTag.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void dispose() {
        IFragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {
            currentFragment.dispose();
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        /*Date: 07-05-2019 , Name: AIT - ANDROID  , Release Version: 2.4.2 Hotfix, Bug ID  : bug-641 ,
  Desc : Modified the condition to wrap the data as per Object by using Parcels*/
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable(KEY_TAGS, Parcels.wrap(fragmentTagStack));
        }
    }

    public void onRestoreInstanceState(Bundle outState) {
        if (outState != null) {
            /*Date: 07-05-2019 , Name: AIT - ANDROID  , Release Version: 2.4.2 Hotfix, Bug ID  : bug-641 ,
  Desc : Modified the condition to unwrap the data as per Object by using Parcels*/
            fragmentTagStack = Parcels.unwrap(outState.getParcelable(KEY_TAGS));
        } else {
            fragmentTagStack = new FragmentTagStack();
        }
    }

    public void enableLogs(boolean logsEnabled) {
        fragmentTagStack.setShowLogs(logsEnabled);
    }
}
