package com.newnote.module.home.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.base.adapter.LoadMoreAdapter;
import com.base.adapter.LoadMoreViewHolder;
import com.newnote.R;
import com.newnote.base.BaseActivity;
import com.newnote.base.BasePresenter;
import com.newnote.module.home.inter.OnAgainInter;
import com.newnote.view.ProgressLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/4.
 */

public class TestActivity extends BaseActivity implements OnAgainInter{
    @BindView(R.id.pl_load)
    ProgressLayout pl_load;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int[] getContentView() {
        return new int[]{R.layout.act_test_view,R.string.title_note};
    }

    @Override
    protected void initView() {
        pl_load.showProgress();
        pl_load.setInter(this);

        rv_list.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void initData() {
        pl_load.postDelayed(new Runnable() {
            @Override
            public void run() {
                pl_load.showErrorText();
            }
        },2000);
    }

    @Override
    protected int setOptionsMenu() {
        return 0;
    }

    @Override
    protected void menuOnClick(int itemId) {

    }
    public void setData(){
        List<String> list=new ArrayList<>();
        for (int i = 0; i < 15; i++) {

            list.add("第"+i+1+"个");
        }
        LoadMoreAdapter<String> loadMoreAdapter = new LoadMoreAdapter<String>(this, 10) {
            @Override
            public int getItemLayoutId(int i) {
                return R.layout.item_view;
            }

            @Override
            public void bindData(LoadMoreViewHolder loadMoreViewHolder, int i, String bean) {
                loadMoreViewHolder.setText(R.id.item_text, bean);
            }
        };
        loadMoreAdapter.setList(list);
        rv_list.setAdapter(loadMoreAdapter);
    }
    @Override
    public void again() {
        pl_load.showProgress();
        pl_load.postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
                pl_load.showContent();
            }
        },2000);
    }
}
