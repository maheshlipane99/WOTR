package com.stack.wotr.activities;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stack.wotr.R;
import com.stack.wotr.adapter.DateAdapter;
import com.stack.wotr.adapter.MonthAdapter;
import com.stack.wotr.adapter.YearAdapter;
import com.stack.wotr.interfaces.OnRecyclerViewClick;
import com.stack.wotr.model.Data;
import com.stack.wotr.model.Item;
import com.stack.wotr.utils.Common;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements OnRecyclerViewClick, View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @BindView(R.id.recyclerViewDate)
    RecyclerView recyclerViewDate;

    @BindView(R.id.recyclerViewMonth)
    RecyclerView recyclerViewMonth;

    @BindView(R.id.recyclerViewYear)
    RecyclerView recyclerViewYear;

    String months[];
    private MonthAdapter mMonthAdapter;
    private DateAdapter mDayAdapter;
    private YearAdapter mYearAdapter;

    @BindView(R.id.textPeriod)
    TextView textPeriod;

    @BindView(R.id.seekPeriod)
    IndicatorSeekBar seekPeriod;

    @BindView(R.id.btnNext)
    Button btnNext;

    String mMonth;
    int mDate;
    int mYear;
    int month = 1;
    String mPeriod = "60 Days";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setTitle("Home");

        mYear = Calendar.getInstance().get(Calendar.YEAR);
        mDate = Calendar.getInstance().get(Calendar.DATE);

        recyclerViewMonth.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerViewYear.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerViewDate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        mMonthAdapter = new MonthAdapter(this, this);
        mDayAdapter = new DateAdapter(this, this);
        mYearAdapter = new YearAdapter(this, this);

        recyclerViewMonth.setAdapter(mMonthAdapter);
        recyclerViewDate.setAdapter(mDayAdapter);
        recyclerViewYear.setAdapter(mYearAdapter);

        btnNext.setOnClickListener(this);

        seekPeriod.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                textPeriod.setText(seekParams.progress + " Days");
                mPeriod = seekParams.progress + " Days";
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });

        initMonthAdapter();
        initYearAdapter();
    }


    private void initYearAdapter() {
        for (int i = 2030; i >= 2010; i--) {
            Item item = new Item(i + "");
            if (mYear == i) {
                item.setCheck(true);
            }
            mYearAdapter.addItem(item);
        }
        recyclerViewYear.scrollToPosition(mYearAdapter.getLastPosition()); //use to focus the item with index
        mYearAdapter.notifyDataSetChanged();
    }


    private void initMonthAdapter() {
        months = getResources().getStringArray(R.array.months);
        for (int i = months.length - 1; i >= 0; i--) {
            Item item = new Item(months[i]);
            if ((Calendar.getInstance().get(Calendar.MONTH)) == i) {
                mMonth = item.getTitle();
                month = i+1;
                item.setCheck(true);
            }
            mMonthAdapter.addItem(item);
        }
        recyclerViewMonth.scrollToPosition(mMonthAdapter.getLastPosition()); //use to focus the item with index
        mMonthAdapter.notifyDataSetChanged();
        initDateAdapter();
    }

    private void initDateAdapter() {
        mDayAdapter.removeAll();
        for (int i = Common.getDays(month, mYear); i >= 1; i--) {
            Item item = new Item(i + "");
            if (mDate == i) {
                item.setCheck(true);
            }
            mDayAdapter.addItem(item);
        }
        recyclerViewDate.scrollToPosition(mDayAdapter.getLastPosition()); //use to focus the item with index
        mDayAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.rootViewMonth: {
                if (position != mMonthAdapter.getLastPosition()) {
                    Item mItemOld = mMonthAdapter.getItem(mMonthAdapter.getLastPosition());
                    Item mItemNew = mMonthAdapter.getItem(position);
                    mItemNew.setCheck(true);
                    mItemOld.setCheck(false);
                    mMonthAdapter.replace(mMonthAdapter.getLastPosition(), mItemOld);
                    mMonthAdapter.replace(position, mItemNew);
                    mMonth = mItemNew.getTitle();
                    month = mMonthAdapter.getItemCount() - position;
                    initDateAdapter();
                }
                break;
            }

            case R.id.rootViewDate: {
                if (position != mMonthAdapter.getLastPosition()) {
                    Item mItemOld = mDayAdapter.getItem(mDayAdapter.getLastPosition());
                    Item mItemNew = mDayAdapter.getItem(position);
                    mItemNew.setCheck(true);
                    mItemOld.setCheck(false);
                    mDayAdapter.replace(mDayAdapter.getLastPosition(), mItemOld);
                    mDayAdapter.replace(position, mItemNew);
                    mDate = Integer.parseInt(mItemNew.getTitle());
                }
                break;
            }

            case R.id.rootViewYear: {
                if (position != mMonthAdapter.getLastPosition()) {
                    Item mItemOld = mYearAdapter.getItem(mYearAdapter.getLastPosition());
                    Item mItemNew = mYearAdapter.getItem(position);
                    mItemNew.setCheck(true);
                    mItemOld.setCheck(false);
                    mYearAdapter.replace(mYearAdapter.getLastPosition(), mItemOld);
                    mYearAdapter.replace(position, mItemNew);
                    mYear = Integer.parseInt(mItemNew.getTitle());
                    initDateAdapter();
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext: {
                Data mData = new Data(mDate, mMonth, mYear, mPeriod);
                Intent mIntent = new Intent(HomeActivity.this, MapActivity.class);
                mIntent.putExtra("data", mData);
                startActivity(mIntent);
                break;
            }
        }
    }
}

