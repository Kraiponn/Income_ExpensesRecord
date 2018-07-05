package com.ksn.kraiponn.income_expensesrecord.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ksn.kraiponn.income_expensesrecord.R;

import java.util.List;

public class MainIncomeExpenseActivity extends AppCompatActivity {

    /******************************
     *  Variable Zone
     */
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private Button btnAddIncomeExpenses;
    private Button btnViewRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_income_expense);
        if (Build.VERSION.SDK_INT >= 23) {
            requestRuntimePermission();
        } else {
            //
        }

        setOrientationMobileOrTable();
        initInstance();
        if (savedInstanceState == null) {
           //
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(
                    R.anim.from_top, R.anim.to_bottom
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(
                R.anim.from_top,
                R.anim.to_bottom
        );
    }


    /******************************
     *  Method Zone
     ******************************/
    private void initInstance() {
        toolbar = findViewById(R.id.toolbar);
        btnViewRecord = findViewById(R.id.btn_view_record);
        btnAddIncomeExpenses = findViewById(R.id.btn_add_record);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnViewRecord.setOnClickListener(btnViewRecordClick);
        btnAddIncomeExpenses.setOnClickListener(btnAddIncomeExpensesClick);
    }

    private void setOrientationMobileOrTable() {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            //
        }
    }

    private void requestRuntimePermission() {
        final boolean[] result = {false};
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(
                            MultiplePermissionsReport report) {
                        //result[0] = true;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(
                            List<PermissionRequest> permissions,
                            PermissionToken token) {
                        token.continuePermissionRequest();
                        //result[0] = false;
                    }
                }).check();
    }


    /*******************************
     *  Listener Zone
     */
    View.OnClickListener btnViewRecordClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(
                    MainIncomeExpenseActivity.this,
                    DataViewIncomeExpenseActivity.class
            ));
            overridePendingTransition(
                    R.anim.slide_in_right, R.anim.slide_out_left
            );
        }
    };

    View.OnClickListener btnAddIncomeExpensesClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(
                    MainIncomeExpenseActivity.this,
                    AddIncomeExpensesActivity.class
            ));
            overridePendingTransition(
                    R.anim.slide_in_right, R.anim.slide_out_left
            );
        }
    };


}
