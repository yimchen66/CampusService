package com.example.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.example.R;
import com.example.adapter.HistoryItemAdapter;
import com.example.adapter.ItemAdapter;
import com.example.model.sqlliteentity.HistoryTrack;
import com.example.utils.ChangeTabColor;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ActivityPersonHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Context context;
    private HistoryItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_history);
        context = this;
        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#8EC5FB"),true);
        //推荐订单
        recyclerView = findViewById(R.id.recycler_view_history);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        List<com.example.model.domain.HistoryTrack> listForAdapter = new ArrayList<>();
        List<HistoryTrack> all = DataSupport.findAll(HistoryTrack.class);
        for (HistoryTrack historyTrack : all) {
            String json_history = historyTrack.getHistoryTrack();
            com.example.model.domain.HistoryTrack track = JSON.parseObject(json_history, com.example.model.domain.HistoryTrack.class);
            listForAdapter.add(track);
        }

        itemAdapter = new HistoryItemAdapter(listForAdapter,context,20);
        recyclerView.setAdapter(itemAdapter);
    }
}