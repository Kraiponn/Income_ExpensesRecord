package com.ksn.kraiponn.income_expensesrecord.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ksn.kraiponn.income_expensesrecord.R;
import com.ksn.kraiponn.income_expensesrecord.dialog.CustomDatePickerDialog;
import com.ksn.kraiponn.income_expensesrecord.dao.SQLiteIncomeExpensesHelper;
import com.ksn.kraiponn.income_expensesrecord.dialog.ItemDialog;

public class AddIncomeExpensesActivity extends AppCompatActivity {
    /****************************************/
    private RadioButton radIncome;
    private RadioButton radExpenses;
    private Button btnAddEdit;
    private ImageButton imgBtnDate;
    private ImageButton imgBtnTitle;
    private EditText edtDate;
    private EditText edtTitle;
    private EditText edtAmount;

    private SQLiteIncomeExpensesHelper helper;
    private SQLiteDatabase db;
    private int m_id = 0;
    private String mDate = "";
    private String mMonth = "";
    private String mYear = "";
    private String mTitle = "";
    private String mType = "";
    private int mAmount = 0;


    /***************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income_expenses);

        setOrientationMobileOrTable();
        initInstance(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper = SQLiteIncomeExpensesHelper.newInstance(this);
        db = helper.getWritableDatabase();

        Intent itn = getIntent();
        int id = itn.getIntExtra("_id", 0);
        if (id > 0) {
            readDataFromDB();
            //showToast(id+"");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(
                R.anim.slide_in_left, R.anim.slide_out_right
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(
                    R.anim.slide_in_left, R.anim.slide_out_right
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*********************************
     *  Method Zone
     */
    private void initInstance(Bundle savedInstanceState) {
        edtTitle = findViewById(R.id.edt_title);
        edtDate = findViewById(R.id.edt_date);
        edtAmount = findViewById(R.id.edt_amount);
        radIncome = findViewById(R.id.rad_income);
        radExpenses = findViewById(R.id.rad_expenses);
        btnAddEdit = findViewById(R.id.btn_add_edit);
        imgBtnDate = findViewById(R.id.imgButton_get_date);
        imgBtnTitle = findViewById(R.id.imgButton_get_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAddEdit.setOnClickListener(btnAddEditClick);
        imgBtnDate.setOnClickListener(imgBtndateClick);
        this.imgBtnTitle.setOnClickListener(imgBtnTitleClick);
    }

    private void setOrientationMobileOrTable() {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            //
        }
    }

    private void showToast(String text) {
        Toast.makeText(this,
                text, Toast.LENGTH_SHORT).show();
    }

    private void setTitleName() {
        final String[] incomeItem = {"เงินเดือน", "รายได้พิเศษ","โบนัส"};
        final String[] expensesItem = {
                "ค่าอาหาร", "ค่ารถประจำทาง","ค่าน้ำมันรถ",
                "ค่าบ้าน", "ค่าไฟ","ค่าน้ำ",
                "ค่าอินเตอร์เน็ต", "ค่าโทรศัพท์","ค่าอื่นๆ",
        };

        ItemDialog dialog;
        if (radIncome.isChecked()) {
            dialog = ItemDialog.newInstance(
                    "กรุณาเลือกรายการ รายรับของคุณ", incomeItem
            );
            dialog.show(getSupportFragmentManager(), null);
            dialog.setOnFinishDialogListener(new ItemDialog.onFinishDialogListener() {
                @Override
                public void onFinishDialog(int index) {
                    edtTitle.setText(incomeItem[index]);
                }
            });
        } else {
            dialog = ItemDialog.newInstance(
                    "กรุณาเลือกรายการ รายจ่ายของคุณ", expensesItem
            );
            dialog.show(getSupportFragmentManager(), null);
            dialog.setOnFinishDialogListener(new ItemDialog.onFinishDialogListener() {
                @Override
                public void onFinishDialog(int index) {
                    edtTitle.setText(expensesItem[index]);
                }
            });
        }
    }

    private void setDateMonthYearToEditBox() {
        CustomDatePickerDialog dialog = CustomDatePickerDialog.newInstance();
        dialog.show(getSupportFragmentManager(), null);
        dialog.setOnFinishDialogListener(new CustomDatePickerDialog.onFinishDialogListener() {
            @Override
            public void onFinishDialog(int[] date) {
                String dmy = "";
                dmy = ((date[0] < 10) ? "0" : "") + date[0] + "-";
                dmy += ((date[1] < 10) ? "0" : "") + date[1] + "-";
                dmy += ((date[2] < 10) ? "0" : "") + date[2];
                edtDate.setText(dmy);
            }
        });
    }

    private void clearView() {
        edtDate.setText("");
        edtTitle.setText("");
        edtAmount.setText("");
        radIncome.setChecked(true);
    }

    private void readDataFromDB() {
        Intent itn = getIntent();
        m_id = itn.getIntExtra("_id",0);

        String sql = "SELECT * FROM income_expenses " +
                " WHERE _id = " + m_id;
        //db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        mDate = (cursor.getInt(1) < 10 ? "0" : "")
                + cursor.getInt(1);
        mMonth = (cursor.getInt(2) < 10 ? "0" : "")
                + cursor.getInt(2);
        mYear = String.valueOf(cursor.getInt(3));
        mTitle = cursor.getString(4);
        mType = cursor.getString(5);
        mAmount = cursor.getInt(6);

        if (mType.equals("+")) {
            radIncome.setChecked(true);
        } else {
            radExpenses.setChecked(true);
        }

        String dmy = mDate + "-" + mMonth + "-" + mYear;
        edtDate.setText(dmy);
        edtTitle.setText(mTitle);
        edtAmount.setText(mAmount + "");
    }


    private void readDataFromView() {
        String errMessage = "";
        if (!radIncome.isChecked() && !radExpenses.isChecked()) {
            errMessage = "คุณยังไม่ได้เลือกชนิดรายการ";
        } else if (edtDate.getText().toString().equals("")) {
            errMessage = "คุณยังไม่ได้ระบุวัน เดือน ปี ที่จะบันทึก";
        } else if (edtTitle.getText().toString().equals("")) {
            errMessage = "คุณยังไม่ได้ใส่ชื่อรายการที่จะบันทึก";
        } else if (edtAmount.getText().toString().equals("")) {
            errMessage = "คุณยังไม่ได้ระบุจำนวนเงิน?";
        }

        if (!errMessage.equals("")) {
            showToast(errMessage);
            return;
        }

        String[] arrDMY = edtDate.getText().toString().trim().split("-");
        mDate = arrDMY[0];
        mMonth = arrDMY[1];
        mYear = arrDMY[2];
        mTitle = edtTitle.getText().toString().trim();
        double amt = Double.valueOf(
                edtAmount.getText().toString());
        mAmount = (int) amt;
        //showToast(mAmount+"");
        if (radIncome.isChecked()) {
            mType = "+";
        } else {
            mType = "-";
        }

        ContentValues cv = new ContentValues();
        cv.put("date", Integer.valueOf(mDate));
        cv.put("month", Integer.valueOf(mMonth));
        cv.put("year", Integer.valueOf(mYear));
        cv.put("title", mTitle);
        cv.put("type", mType);
        cv.put("amount", mAmount);

        Intent itn = getIntent();
        int id = itn.getIntExtra("_id", 0);
        try {
            long res = 0;
            if (id > 0) {
                res = db.update(
                        "income_expenses",
                        cv,
                        "_id = ?",
                        new String[]{String.valueOf(id)}
                );
                if (res != 0) {
                    showToast("แก้ไขข้อมูลสำเร็จ");
                    clearView();
                    backToDataViewIncomeExpenses();
                } else {
                    showToast("เกิดข้อผิดพลาด! ไม่สามารถแก้ไขข้อมูลได้");
                }
            }else{
                res = db.insert("income_expenses", null, cv);
                if (res != 0) {
                    showToast("เพิ่มข้อมูลใหม่สำเร็จ");
                    clearView();
                } else {
                    showToast("เกิดข้อผิดพลาด! ไม่สามารถเพิ่มข้อมูลได้");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void backToDataViewIncomeExpenses() {
        finish();
    }


    /*********************************
     *  Listener Zone
     */
    View.OnClickListener btnAddEditClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            readDataFromView();
        }
    };

    View.OnClickListener imgBtndateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setDateMonthYearToEditBox();
        }
    };

    View.OnClickListener imgBtnTitleClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setTitleName();

        }
    };



}
