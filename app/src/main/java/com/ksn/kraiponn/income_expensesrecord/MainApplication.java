package com.ksn.kraiponn.income_expensesrecord;

import android.app.Application;

import com.ksn.kraiponn.income_expensesrecord.manager.Contextor;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getsInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
