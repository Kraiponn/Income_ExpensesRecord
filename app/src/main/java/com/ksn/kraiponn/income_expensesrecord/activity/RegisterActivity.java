package com.ksn.kraiponn.income_expensesrecord.activity;

import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ksn.kraiponn.income_expensesrecord.R;
import com.ksn.kraiponn.income_expensesrecord.dao.SQLiteIncomeExpensesHelper;
import com.ksn.kraiponn.income_expensesrecord.fragment.RegisterFragment;

public class RegisterActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private EditText edtUserName;
    private EditText edtPswd;
    private Button btnRegister;

    private SQLiteIncomeExpensesHelper helper;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setOrientationMobileOrTable();
        initInstance(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
        helper = SQLiteIncomeExpensesHelper.newInstance(this);
        db = helper.getWritableDatabase();
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
                R.anim.from_top, R.anim.to_bottom
        );
    }

    /*******************************
     *  Method Zone
     */
    private void initInstance(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        edtUserName = findViewById(R.id.edt_register_userName);
        edtPswd = findViewById(R.id.edt_register_password);
        btnRegister = findViewById(R.id.btn_register_confirm);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(fabClick);
        btnRegister.setOnClickListener(btnRegisterClick);
    }

    private void setOrientationMobileOrTable() {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            //
        }
    }

    private void showToast(String text) {
        Toast.makeText(getApplicationContext(),
                text,
                Toast.LENGTH_SHORT).show();
    }

    private void InsertNewUser() {
        String errMsg = "";
        if (edtUserName.getText().toString().equals("")) {
            errMsg = "Please enter user!!";
        } else if (edtPswd.getText().toString().equals("")) {
            errMsg = "Please enter password!!";
        }

        if (!errMsg.equals("")) {
            showToast(errMsg);
            return;
        }

        String user = edtUserName.getText().toString().trim();
        String pswd = edtPswd.getText().toString().trim();

        ContentValues cv = new ContentValues();
        cv.put("user_name", user);
        cv.put("password", pswd);

        try {
            db.insert(
                    "user_list",
                    null,
                    cv
            );

            showToast("Register successfully");
            finish();
            overridePendingTransition(
                    R.anim.from_top, R.anim.to_bottom
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /*******************************
     *  Listener Zone
     */
    View.OnClickListener fabClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    View.OnClickListener btnRegisterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InsertNewUser();
        }
    };


}
