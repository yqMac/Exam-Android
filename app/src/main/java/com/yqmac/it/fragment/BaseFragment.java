package com.yqmac.it.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    protected String mParam1;
    protected String mParam2;
    protected String mParam3;

    public BaseFragment() {
    }

    public static <T> T newInstance(Class<T> clz) {
        Bundle args = new Bundle();

        return getInstance(clz, args);
    }

    public static <T> T newInstance(Class<T> clz, String param1) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        return getInstance(clz, args);
    }

    public static <T> T newInstance(Class<T> clz, String param1, String param2) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        return getInstance(clz, args);
    }

    public static <T> T newInstance(Class<T> clz, String param1, String param2, String param3) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);

        return getInstance(clz, args);
    }

    private static <T> T getInstance(Class<T> clz, Bundle args) {
        T fragment = null;

        try {
            fragment = clz.newInstance();

            Method setArguments = clz.getMethod("setArguments", Bundle.class);

            setArguments.invoke(fragment, args);

        } catch (InvocationTargetException | NoSuchMethodException | java.lang.InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("BaseFragment", "--->onCreate");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);

            if (!TextUtils.isEmpty(mParam1)) {
                Log.w("mParam1", mParam1);
            }
            if (!TextUtils.isEmpty(mParam2)) {
                Log.w("mParam2", mParam2);
            }
            if (!TextUtils.isEmpty(mParam3)) {
                Log.w("mParam3", mParam3);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w("BaseFragment", "--->onCreateView");

        return initView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.w("BaseFragment", "--->onActivityCreated");

        initData();

        initListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.w("BaseFragment", "--->onDetach");

        try {

            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            // 还是反射爽
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void initListener();

}
