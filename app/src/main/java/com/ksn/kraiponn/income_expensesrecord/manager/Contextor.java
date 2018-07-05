package com.ksn.kraiponn.income_expensesrecord.manager;

import android.content.Context;

public class Contextor {
    private static Contextor sInstance;

    public static Contextor getsInstance() {
        if (sInstance == null) {
            sInstance = new Contextor();
        }

        return sInstance;
    }

    private Context mContext;
    private Contextor() {}

    public void init(Context context) {
        mContext = context;
    }

}
