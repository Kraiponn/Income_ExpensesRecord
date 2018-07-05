package com.ksn.kraiponn.income_expensesrecord.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksn.kraiponn.income_expensesrecord.R;
import com.ksn.kraiponn.income_expensesrecord.manager.ChildItem;
import com.ksn.kraiponn.income_expensesrecord.manager.SectionItem;

import java.util.List;

public class IncomeExpensesAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List mItem;
    private static final int SECTION_ITEM = 0;
    private static final int CHILD_ITEM = 1;
    private boolean mIsFirstChild = true;
    private onItemSelectListener mCallBack;

    public interface onItemSelectListener{
        void onItemSelect(View view, int position, int id);
    }



    public IncomeExpensesAdapter(Context mContext, List mItem) {
        this.mContext = mContext;
        this.mItem = mItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (viewType == SECTION_ITEM) {
            View view = inflater.inflate(
                    R.layout.section_item_layout, parent, false
            );

            SectionHolder holder = new SectionHolder(view);
            return holder;
        } else if (viewType == CHILD_ITEM) {
            View view = inflater.inflate(
                    R.layout.child_item_layout, parent, false
            );

            ChildHolder holder = new ChildHolder(view);
            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        int type = getItemViewType(position);
        if (type == SECTION_ITEM) {
            mIsFirstChild = true;
            SectionItem item = (SectionItem) mItem.get(position);
            SectionHolder vHolder = (SectionHolder) holder;
            vHolder.tvSectionDateMonth.setText(item.getTextSection());
        } else if (type == CHILD_ITEM) {
            ChildItem item = (ChildItem) mItem.get(position);
            ChildHolder vHolder = (ChildHolder) holder;

            vHolder.imgIconId.setImageResource(item.getIconID());
            vHolder.tvTitle.setText(item.getTitle());
            vHolder.tvAmount.setText(item.getAmount());

            if (item.getIconID() == R.drawable.ic_minus_circle) {
                vHolder.tvAmount.setTextColor(Color.parseColor("#990000"));
            }

            boolean isLastOfSection =
                    position < mItem.size() - 1 && getItemViewType(position + 1) == SECTION_ITEM;
            boolean isLastOfAll = position == mItem.size() - 1;
            boolean isLastChild = isLastOfSection || isLastOfAll;

            if (mIsFirstChild && isLastChild) {
                vHolder.tvTitle.getRootView()
                        .setBackgroundResource(R.drawable.one_item_state);
                mIsFirstChild = false;
            } else if (mIsFirstChild || position == 0) {
                vHolder.tvTitle.getRootView()
                        .setBackgroundResource(R.drawable.top_item_state);
                mIsFirstChild = false;
            } else if (isLastChild) {
                vHolder.tvTitle.getRootView()
                        .setBackgroundResource(R.drawable.bottom_item_state);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItem.get(position) instanceof SectionItem) {
            return SECTION_ITEM;
        } else if (mItem.get(position) instanceof ChildItem) {
            return CHILD_ITEM;
        }
        return -1;
    }

    public void setOnItemSelectListener(onItemSelectListener listener) {
        mCallBack = listener;
    }


    /********************************
     *  Sub Class
     */
    public class SectionHolder extends RecyclerView.ViewHolder {
        public TextView tvSectionDateMonth;

        public SectionHolder(View cv) {
            super(cv);
            tvSectionDateMonth = cv.findViewById(R.id.tv_section_date_month);
        }

    }

    public class ChildHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvAmount;
        public ImageView imgIconId;

        public ChildHolder(View cv) {
            super(cv);
            imgIconId = cv.findViewById(R.id.img_child_icon);
            tvTitle = cv.findViewById(R.id.tv_child_title);
            tvAmount = cv.findViewById(R.id.tv_child_amount);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    ChildItem item = (ChildItem) mItem.get(pos);
                    mCallBack.onItemSelect(v, pos, item.get_id());
                }
            });
        }
    }
}
