package com.ksn.kraiponn.income_expensesrecord.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ksn.kraiponn.income_expensesrecord.R;

public class MainIncomeExpensesFragment extends Fragment {
    /****************************
     *  variable
     */
    private Button btnViewRecord;
    private Button btnAddRecord;

    public interface openAddRecordFragmentListener{
        void onOpenAddRecordFragment();
    }
    public interface openViewDataFragmentListener{
        void onOpenViewDataFragment();
    }

    public MainIncomeExpensesFragment() {

    }

    public static MainIncomeExpensesFragment newInstance() {
        MainIncomeExpensesFragment fm = new MainIncomeExpensesFragment();
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
                R.layout.fragment_main_income_expenses, container, false
        );

        initInstance(view);
        return view;
    }

    private void initInstance(View view) {
        btnAddRecord = view.findViewById(R.id.btn_add_record);
        btnViewRecord = view.findViewById(R.id.btn_add_record);

        btnViewRecord.setOnClickListener(btnViewRecordClick);
        btnAddRecord.setOnClickListener(btnAddRecordClick);
    }


    /****************************
     *  Listener Zone
     */
    View.OnClickListener btnViewRecordClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openViewDataFragmentListener listener =
                    (openViewDataFragmentListener) getActivity();
            listener.onOpenViewDataFragment();
        }
    };

    View.OnClickListener btnAddRecordClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openAddRecordFragmentListener listener =
                    (openAddRecordFragmentListener) getActivity();
            listener.onOpenAddRecordFragment();
        }
    };


}
