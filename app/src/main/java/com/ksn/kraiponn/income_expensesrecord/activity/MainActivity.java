package com.ksn.kraiponn.income_expensesrecord.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ksn.kraiponn.income_expensesrecord.R;
import com.ksn.kraiponn.income_expensesrecord.dao.SQLiteIncomeExpensesHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /******************************
     *  Variable Zone
     */
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private NavigationView navMenu;
    private EditText edtUserName;
    private EditText edtPswd;
    private Button btnConfirm;
    private Button btnScanBarCode;
    private TextView tvLoginToRegister;

    private final int QR_BAR_CODE_RESULT = 99;
    private SQLiteIncomeExpensesHelper helper;
    private SQLiteDatabase db;
    private String mUserFromBarCode = "";

    /*******************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setOrientationMobileOrTable();
        initInstance();
        if (savedInstanceState == null) {
            //
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper = SQLiteIncomeExpensesHelper.newInstance(this);
        db = helper.getWritableDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_BAR_CODE_RESULT && resultCode == RESULT_OK) {
            String result = data.getStringExtra("barcode");
            //showToast(result);
            loginByScanningCode(result);
        }else{
            showToast("Login fail");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /******************************
     *  Method Zone
     ******************************/
    private void initInstance() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestRuntimePermission();
        } else {
            //
        }

        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navMenu = findViewById(R.id.nav_menu);
        edtUserName = findViewById(R.id.edt_login_userName);
        edtPswd = findViewById(R.id.edt_login_password);
        btnConfirm = findViewById(R.id.btn_login_confirm);
        btnScanBarCode = findViewById(R.id.btn_login_scan_code);
        tvLoginToRegister = findViewById(R.id.tv_login_to_register);

        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.open_menu, R.string.close_menu
        );
        drawer.addDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navMenu.setNavigationItemSelectedListener(navMenuItemSelectListener);
        btnConfirm.setOnClickListener(btnConfirmClick);
        tvLoginToRegister.setOnClickListener(tvLogInToRegisterClick);
        btnScanBarCode.setOnClickListener(btnScanBarcodeClick);
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

    private void showToast(String result) {
        Toast.makeText(this,
                result,
                Toast.LENGTH_SHORT).show();
    }

    private void logInByManual() {
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

        String userName = edtUserName.getText().toString().trim();
        String password = edtPswd.getText().toString().trim();

        String sql = "SELECT * FROM user_list " +
                "WHERE user_name = ? AND password = ? ";
        Cursor cursor = db.rawQuery(
                            sql,
                            new String[]{userName, password});
        if (cursor.getCount() <= 0) {
            showToast("ชื่อของคุณหรือรหัสผ่านไม่ถูกต้อง โปรดลองอีกครั้ง");
            return;
        } else {
            showToast("ลงชื่อเข้าใช้งานสำเร็จ");
            startActivity(new Intent(
                    MainActivity.this,
                    MainIncomeExpenseActivity.class
            ));
            overridePendingTransition(
                    R.anim.from_bottom, R.anim.to_top
            );

            edtPswd.setText("");
        }

        cursor.close();

    }

    private void loginByScanningCode(String user) {
        String sql = "SELECT * FROM user_list " +
                     "WHERE user_name = ? ";

        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{user} );
        if (cursor.getCount() <= 0) {
            showToast("ชื่อของคุณหรือรหัสผ่านไม่ถูกต้อง โปรดลองอีกครั้ง");
            return;
        } else {
            showToast("ลงชื่อเข้าใช้งานสำเร็จ");

            startActivity(new Intent(
                    MainActivity.this,
                    MainIncomeExpenseActivity.class
            ));
            overridePendingTransition(
                    R.anim.from_bottom, R.anim.to_top
            );
            edtPswd.setText("");
        }

        cursor.close();
        //showToast(user);
    }


    /******************************
     *  Listener Zone
     ******************************/
    NavigationView.OnNavigationItemSelectedListener navMenuItemSelectListener =
            new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    };

    View.OnClickListener btnConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logInByManual();
            /*startActivity(new Intent(
                    MainActivity.this,
                    MainIncomeExpenseActivity.class
            ));
            overridePendingTransition(
                    R.anim.from_bottom, R.anim.to_top
            );*/
        }
    };

    View.OnClickListener tvLogInToRegisterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(
                    MainActivity.this,
                    RegisterActivity.class
            ));
            overridePendingTransition(
                    R.anim.from_bottom, R.anim.to_top
            );
        }
    };

    View.OnClickListener btnScanBarcodeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent itn = new Intent(
                    MainActivity.this,
                    ScanningCodeActivity.class
            );
            startActivityForResult(itn, QR_BAR_CODE_RESULT);
        }
    };

}
