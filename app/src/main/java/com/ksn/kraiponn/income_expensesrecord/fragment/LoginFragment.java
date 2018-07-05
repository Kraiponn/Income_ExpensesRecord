package com.ksn.kraiponn.income_expensesrecord.fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ksn.kraiponn.income_expensesrecord.R;
import com.ksn.kraiponn.income_expensesrecord.dao.SQLiteIncomeExpensesHelper;

public class LoginFragment extends Fragment {
    private EditText edtUserName;
    private EditText edtPswd;
    private Button btnConfirm;
    private Button btnScanBarCode;
    private TextView tvLoginToRegister;

    private SQLiteIncomeExpensesHelper helper;
    private SQLiteDatabase db;

    public interface onOpenRegisterFormListener{
        void onOpenRegisterForm();
    }
    public interface loginPassListener{
        void onLoginPass();
    }
    public interface scanningForLogInListener{
        void onScanningForLogin();
    }

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        LoginFragment fm = new LoginFragment();
        Bundle args = new Bundle();
        fm.setArguments(args);
        return fm;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_login_layout, container, false
        );

        initInstance(view);
        return view;
    }



    /*******************************
     *  Method Zone
     */
    private void initInstance(View view) {
        edtUserName = view.findViewById(R.id.edt_login_userName);
        edtPswd = view.findViewById(R.id.edt_login_password);
        btnConfirm = view.findViewById(R.id.btn_login_confirm);
        btnScanBarCode = view.findViewById(R.id.btn_login_scan_code);
        tvLoginToRegister = view.findViewById(R.id.tv_login_to_register);

        btnConfirm.setOnClickListener(btnConfirmClick);
        tvLoginToRegister.setOnClickListener(tvLogInToRegisterClick);
        btnScanBarCode.setOnClickListener(btnScanBarcodeClick);
        helper = SQLiteIncomeExpensesHelper.newInstance(getContext());
        db = helper.getWritableDatabase();
    }

    private void showToast(String text) {
        Toast.makeText(getContext(),
                text,
                Toast.LENGTH_SHORT).show();
    }


    /*******************************
     *  Listener Zone
     */
    View.OnClickListener btnConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //
        }
    };

    View.OnClickListener tvLogInToRegisterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onOpenRegisterFormListener listener =
                    (onOpenRegisterFormListener) getActivity();
            listener.onOpenRegisterForm();
        }
    };

    View.OnClickListener btnScanBarcodeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            scanningForLogInListener listener =
                    (scanningForLogInListener) getActivity();
            listener.onScanningForLogin();
        }
    };


}
