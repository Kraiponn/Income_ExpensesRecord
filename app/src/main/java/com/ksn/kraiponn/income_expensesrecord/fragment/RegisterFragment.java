package com.ksn.kraiponn.income_expensesrecord.fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ksn.kraiponn.income_expensesrecord.R;
import com.ksn.kraiponn.income_expensesrecord.dao.SQLiteIncomeExpensesHelper;
import com.ksn.kraiponn.income_expensesrecord.manager.Contextor;

public class RegisterFragment extends Fragment {
    private EditText edtUserName;
    private EditText edtPswd;
    private Button btnRegister;

    private SQLiteIncomeExpensesHelper helper;
    private SQLiteDatabase db;

    public interface registerFinishListener{
        void onRegisterFinish();
    }

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance() {
        RegisterFragment fm = new RegisterFragment();
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
                R.layout.fragment_register_layout, container, false
        );

        initInstance(view);
        return view;
    }



    /*******************************
     *  Method Zone
     */
    private void initInstance(View view) {
        edtUserName = view.findViewById(R.id.edt_register_userName);
        edtPswd = view.findViewById(R.id.edt_register_password);
        btnRegister = view.findViewById(R.id.btn_register_confirm);

        btnRegister.setOnClickListener(btnRegisterClick);
        helper = SQLiteIncomeExpensesHelper.newInstance(getContext());
        db = helper.getWritableDatabase();
    }

    private void showToast(String text) {
        Toast.makeText(getContext(),
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
            getFragmentManager().popBackStack();

            registerFinishListener listener =
                    (registerFinishListener) getActivity();
            listener.onRegisterFinish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /*******************************
     *  Listener Zone
     */
    View.OnClickListener btnRegisterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InsertNewUser();
        }
    };


}
