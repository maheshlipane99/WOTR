package com.stack.wotr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.stack.wotr.R;
import com.stack.wotr.model.Data;

import androidx.appcompat.app.AppCompatActivity;

public class DataActivity extends AppCompatActivity {

    Data mData;
    private TextView textData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        setTitle("Collected Data");

        mData= getIntent().getParcelableExtra("data");
        textData=findViewById(R.id.textData);

        StringBuilder mB=new StringBuilder();
        mB.append("पेरनी दिनांक/नियोजित पेरनी दिनांक : "+mData.getDate()+" "+mData.getMonth()+" "+mData.getYear()+"\n");
        mB.append("अपेक्षित पिकवाडीचा कलावधी : "+mData.getPeriod()+"\n");
        mB.append("क्षेत्र : "+mData.getArea());
        textData.setText(mB);

    }
}
