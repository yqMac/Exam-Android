package com.yqmac.it.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import com.yqmac.it.fragment.BaseFragment;

/**
 * Created by yqmac on 2016/5/24 0024.
 */
public class FragmentUtils {

    public static FragmentManager getFragmentManager(Activity activity) {
        return activity.getFragmentManager();
    }

    public static void replace(Activity activity, Fragment fragment, int replaceId) {

        FragmentManager fragmentManager = getFragmentManager(activity);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(replaceId, fragment);
        transaction.commit();
    }

    public static void add(Activity activity, Fragment fragment, int replaceId) {

        if (fragment.isAdded()) {
            return;
        }

        FragmentManager fragmentManager = getFragmentManager(activity);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(replaceId, fragment);
        transaction.commit();
    }

    public static void show(Activity activity, Fragment fragment) {

        if (!fragment.isHidden()) {
            return;
        }

        FragmentManager fragmentManager = getFragmentManager(activity);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.show(fragment);
        transaction.commit();
    }

    public static void hide(Activity activity, Fragment fragment) {
        if (fragment.isHidden()) {
            return;
        }
        FragmentManager fragmentManager = getFragmentManager(activity);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.hide(fragment);
        transaction.commit();
    }

    // 工厂模式创建fragment实例，内部反射
    public static <T> T getFragment(Class<T> clz) {
        return BaseFragment.newInstance(clz);
    }

    public static <T> T getFragment(Class<T> clz, String param1) {
        return BaseFragment.newInstance(clz, param1);
    }

    public static <T> T getFragment(Class<T> clz, String param1, String param2) {
        return BaseFragment.newInstance(clz, param1, param2);
    }

    public static <T> T getFragment(Class<T> clz, String param1, String param2, String param3) {
        return BaseFragment.newInstance(clz, param1, param2, param3);
    }
}
