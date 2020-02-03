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
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity implements OnRecyclerViewClick {

    private static final String TAG = HomeActivity.class.getSimpleName();
    RecyclerView recyclerViewDate, recyclerViewMonth, recyclerViewYear;

    String month[];
    private MonthAdapter mMonthAdapter;
    private DateAdapter mDayAdapter;
    private YearAdapter mYearAdapter;
    private TextView textPeriod;
    IndicatorSeekBar seekPeriod;
    private Button btnNext;
    String mMonth, mDate, mYear, mPeriod = "60 Days";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        mYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        mDate = Calendar.getInstance().get(Calendar.DATE) + "";

        textPeriod = findViewById(R.id.textPeriod);
        seekPeriod = findViewById(R.id.seekPeriod);
        btnNext = findViewById(R.id.btnNext);
        recyclerViewDate = findViewById(R.id.recyclerViewDate);
        recyclerViewMonth = findViewById(R.id.recyclerViewMonth);
        recyclerViewYear = findViewById(R.id.recyclerViewYear);
        recyclerViewMonth.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerViewYear.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerViewDate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        mMonthAdapter = new MonthAdapter(this, this);
        mDayAdapter = new DateAdapter(this, this);
        mYearAdapter = new YearAdapter(this, this);

        recyclerViewMonth.setAdapter(mMonthAdapter);
        recyclerViewDate.setAdapter(mDayAdapter);
        recyclerViewYear.setAdapter(mYearAdapter);

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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data mData = new Data(mDate, mMonth, mYear, mPeriod);
                Intent mIntent = new Intent(HomeActivity.this, MapActivity.class);
                mIntent.putExtra("data", mData);
                startActivity(mIntent);
            }
        });

        initDateAdapter();
        initMonthAdapter();
        initYearAdapter();
    }


    private void initYearAdapter() {
        for (int i = 2030; i >= 2010; i--) {
            Item item = new Item(i + "");
            if (Integer.parseInt(mYear) == i) {
                item.setCheck(true);
            }
            mYearAdapter.addItem(item);
        }
        recyclerViewYear.scrollToPosition(mYearAdapter.getLastPosition()); //use to focus the item with index
        mYearAdapter.notifyDataSetChanged();
    }


    private void initMonthAdapter() {
        month = getResources().getStringArray(R.array.months);
        for (int i = month.length - 1; i >= 0; i--) {
            Item item = new Item(month[i]);
            if ((Calendar.getInstance().get(Calendar.MONTH)) == i) {
                mMonth = item.getTitle();
                item.setCheck(true);
            }
            mMonthAdapter.addItem(item);
        }
        recyclerViewMonth.scrollToPosition(mMonthAdapter.getLastPosition()); //use to focus the item with index
        mMonthAdapter.notifyDataSetChanged();
    }

    private void initDateAdapter() {
        for (int i = 31; i >= 1; i--) {
            Item item = new Item(i + "");
            if (Integer.parseInt(mDate) == i) {
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
                Item mItemOld = mMonthAdapter.getItem(mMonthAdapter.getLastPosition());
                Item mItemNew = mMonthAdapter.getItem(position);
                mItemNew.setCheck(true);
                mItemOld.setCheck(false);
                mMonthAdapter.replace(mMonthAdapter.getLastPosition(), mItemOld);
                mMonthAdapter.replace(position, mItemNew);
                mMonth = mItemNew.getTitle();
                break;
            }
            case R.id.rootViewDate: {

                Item mItemOld = mDayAdapter.getItem(mDayAdapter.getLastPosition());
                Item mItemNew = mDayAdapter.getItem(position);
                mItemNew.setCheck(true);
                mItemOld.setCheck(false);
                mDayAdapter.replace(mDayAdapter.getLastPosition(), mItemOld);
                mDayAdapter.replace(position, mItemNew);
                mDate = mItemNew.getTitle();
                break;
            }
            case R.id.rootViewYear: {
                Item mItemOld = mYearAdapter.getItem(mYearAdapter.getLastPosition());
                Item mItemNew = mYearAdapter.getItem(position);
                mItemNew.setCheck(true);
                mItemOld.setCheck(false);
                mYearAdapter.replace(mYearAdapter.getLastPosition(), mItemOld);
                mYearAdapter.replace(position, mItemNew);
                mYear = mItemNew.getTitle();
                break;
            }
        }
    }
}

