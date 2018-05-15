package com.mynote.module.leftmenu.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.baseclass.adapter.MyLoadMoreAdapter;
import com.github.baseclass.adapter.MyRecyclerViewHolder;
import com.mynote.R;
import com.mynote.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/8.
 */

public class BirthdayActivity extends BaseActivity {
    @BindView(R.id.rv_birthday)
    RecyclerView rv_birthday;

    MyLoadMoreAdapter adapter;

    @Override
    protected int getContentView() {
        setAppTitle("生日列表");
        setAppRightTitle("添加");
        return R.layout.act_birthday;
    }

    @Override
    protected void initView() {
        adapter=new MyLoadMoreAdapter<String>(mContext,R.layout.item_birthday,pageSize) {
            @Override
            public void bindData(MyRecyclerViewHolder holder, int position, String bean) {

            }
        };
        adapter.setOnLoadMoreListener(this);
        rv_birthday.setAdapter(adapter);


    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.app_right_tv})
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.app_right_tv:
                STActivity(AddBirthdayActivity.class);
            break;
        }
    }
}
