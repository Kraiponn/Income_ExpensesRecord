package com.ksn.kraiponn.income_expensesrecord.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ksn.kraiponn.income_expensesrecord.R;
import com.ksn.kraiponn.income_expensesrecord.adapter.IncomeExpensesAdapter;
import com.ksn.kraiponn.income_expensesrecord.dao.SQLiteIncomeExpensesHelper;
import com.ksn.kraiponn.income_expensesrecord.dialog.ConfirmDialog;
import com.ksn.kraiponn.income_expensesrecord.manager.ChildItem;
import com.ksn.kraiponn.income_expensesrecord.manager.SectionItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DataViewIncomeExpenseActivity extends AppCompatActivity {
    /****************************************/
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private Spinner spinnerMonth;
    private Spinner spinnerYear;
    private TextView tvIncome;
    private TextView tvExpenses;
    private TextView tvBalance;
    private RecyclerView rcv;
    private LinearLayout layoutTotal;

    private SQLiteIncomeExpensesHelper helper;
    private SQLiteDatabase db;
    private IncomeExpensesAdapter mAdapter;

    private final String[] mMonthEn =  {
            "มกราคม", "กุมภาพันธ์", "มีนาคม",
            "เมษายน", "พฤษภาคม", "มิถุนายนต์", "กรกฎาคม",
            "กันยายน", "ตุลาคม", "พฤษจิกายน",
            "ธันวาคม"
    };

    /***************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view_income_expense);

        setOrientationMobileOrTable();
        initInstance(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
        helper = SQLiteIncomeExpensesHelper.newInstance(this);
        db = helper.getWritableDatabase();

        Calendar cal = Calendar.getInstance();
        int cMonth = cal.get(Calendar.MONTH);
        int cYear = cal.get(Calendar.YEAR)+543;

        ArrayAdapter<String> Adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, mMonthEn
        );
        spinnerMonth.setAdapter(Adapter);
        spinnerMonth.setSelection(cMonth);

        ArrayList year = new ArrayList();
        for (int i=0; i<5; i++) {
            year.add(cYear - i);
        }
        Adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, year
        );
        spinnerYear.setAdapter(Adapter);
        spinnerYear.setSelection(0);

        spinnerMonth.setOnItemSelectedListener(spinnerMonthSelect);
        spinnerYear.setOnItemSelectedListener(spinnerYearSelect);

        createItem();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(
                R.anim.slide_in_left, R.anim.slide_out_right
        );
    }

    /*********************************
     *  Method Zone
     */
    private void initInstance(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        spinnerMonth = findViewById(R.id.spinner_month);
        spinnerYear = findViewById(R.id.spinner_year);
        tvIncome = findViewById(R.id.tv_view_income);
        tvExpenses = findViewById(R.id.tv_view_expenses);
        tvBalance = findViewById(R.id.tv_view_balance);
        rcv = findViewById(R.id.recyclerView);
        layoutTotal = findViewById(R.id.layout_total_income_expenses);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fab.setOnClickListener(fabClick);
    }

    private void setOrientationMobileOrTable() {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            //
        }
    }

    private void createItem() {
        String sql = "SELECT * FROM income_expenses " +
                " WHERE month = ? AND year = ? " +
                " ORDER BY date ";
        int month = spinnerMonth.getSelectedItemPosition() + 1;
        int year = Integer.valueOf(spinnerYear.getSelectedItem().toString())-543;


        int _id = 0;
        int date = 0;
        int lastDate = 0;
        String dateMonth = "";
        String title = "";
        int amount = 0;
        int total_income = 0;
        int total_expenses = 0;
        String type = "";
        int drawer = 0;
        ArrayList item = new ArrayList();

        Cursor cursor = db.rawQuery(sql,
                new String[]{month+"", year+""});
        if (cursor.getCount() <= 0) {
            Toast.makeText(this,
                    "ไม่มีข้อมูลตามที่ร้องขอ!", Toast.LENGTH_SHORT).show();
            rcv.setVisibility(View.GONE);
            layoutTotal.setVisibility(View.GONE);
            return;
        }

        while (cursor.moveToNext()) {
            _id = cursor.getInt(0);
            date = cursor.getInt(1);
            if (date != lastDate) {
                dateMonth = (date < 10 ? "0" : "") + date;
                dateMonth += " " + mMonthEn[cursor.getInt(2) - 1];
                item.add(new SectionItem(dateMonth));
                lastDate = date;
            }

            title = cursor.getString(4);
            type = cursor.getString(5);
            amount = cursor.getInt(6);

            if (type.equals("+")) {
                total_income += amount;
                drawer = R.drawable.ic_plus_circle;
            } else {
                total_expenses += amount;
                drawer = R.drawable.ic_minus_circle;
            }


            item.add(new ChildItem(_id, drawer, title, amount+""));

        }

        cursor.close();
        rcv.setVisibility(View.VISIBLE);
        layoutTotal.setVisibility(View.VISIBLE);
        String ic = NumberFormat.getIntegerInstance().format(total_income);
        String exp = NumberFormat.getIntegerInstance().format(total_expenses);
        String bl = NumberFormat.getIntegerInstance()
                .format(total_income - total_expenses);
        tvIncome.setText(ic);
        tvExpenses.setText(exp);
        tvBalance.setText(bl);

        mAdapter = new IncomeExpensesAdapter(this, item);
        rcv.setAdapter(mAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false
        ));

        mAdapter.setOnItemSelectListener(new IncomeExpensesAdapter.onItemSelectListener() {
            @Override
            public void onItemSelect(View view, int position, int id) {
                    /*Toast.makeText(DataViewIncomeExpenseActivity.this,
                            "Item " + id, Toast.LENGTH_SHORT).show();*/
                TextView tvTitle = view.findViewById(R.id.tv_child_title);
                String text = tvTitle.getText().toString().trim();
                openConfirmDialog(id, text, position);
            }
        });
    }


    private void openConfirmDialog(final int id,
                                   final String title,
                                   final int position) {
        ConfirmDialog dialog = ConfirmDialog.newInstance(
                "คุณต้องการแก้ไขหรือลบ?",
                "แก้ไข", "ลบ"
        );
        dialog.show(getSupportFragmentManager(), null);
        dialog.setOnFinishDialogListener(new ConfirmDialog.onFinishDialogListener() {
            @Override
            public void onFinishDialog(int index) {
                if (index < 0) {
                    return;
                }

                if (index == 1) {
                    //Go to AddIncomeExpensesActivity For Edit Data
                    Intent itn = new Intent(
                            DataViewIncomeExpenseActivity.this,
                            AddIncomeExpensesActivity.class
                    );
                    itn.putExtra("_id", id);
                    startActivity(itn);
                } else if (index == 2) {
                    //Delete Item As Id
                    String sql = "DELETE FROM income_expenses " +
                            " WHERE _id = ?";
                    db = helper.getWritableDatabase();
                    db.execSQL(sql, new String[]{String.valueOf(id)});
                    showToast("ลบข้อมูล " + title + " เรียบร้อบแล้ว");
                    mAdapter.notifyItemRemoved(position);
                    //rcv.scrollToPosition(position-1);
                    createItem();
                }

            }
        });
    }


    private void showToast(String text) {
        Toast.makeText(DataViewIncomeExpenseActivity.this,
                text, Toast.LENGTH_SHORT).show();
    }



    /*********************************
     *  Listener Zone
     */
    View.OnClickListener fabClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(
                    DataViewIncomeExpenseActivity.this,
                    AddIncomeExpensesActivity.class
            ));
            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        }
    };

    AdapterView.OnItemSelectedListener spinnerMonthSelect
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            createItem();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinnerYearSelect
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            createItem();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
