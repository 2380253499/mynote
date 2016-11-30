package com.zr.note.ui.main.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.main.activity.AddDataActivity;
import com.zr.note.ui.main.broadcast.AddSpendDataBro;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.fragment.adapter.MySpendHolder;
import com.zr.note.ui.main.fragment.contract.SpendCon;
import com.zr.note.ui.main.fragment.contract.imp.SpendImp;
import com.zr.note.ui.main.inter.AddDataInter;
import com.zr.note.ui.main.inter.DateInter;

import butterknife.BindView;

public class SpendFragment extends BaseFragment<SpendCon.View,SpendCon.Presenter> implements SpendCon.View, DateInter.dataManageInter {
    @BindView(R.id.ll_tree)
    LinearLayout ll_tree;
    private AddSpendDataBro addDataBro;
    private boolean isCreateTime;

    public static SpendFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SpendFragment fragment = new SpendFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addDataBro = new AddSpendDataBro(new AddDataInter.AddDataFinish() {
            @Override
            public void addDataFinish() {
                selectData();
            }
        });
        getActivity().registerReceiver(addDataBro, new IntentFilter(BroFilter.addData_spend));
    }
    @Override
    protected SpendImp initPresenter() {
        return new SpendImp(getActivity());
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_spend;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        selectData();
    }

    @Override
    protected void viewOnClick(View v) {

    }


    @Override
    public void selectData() {
        ll_tree.removeAllViews();//再次查询时清除旧视图
        mPresenter.selectData(null, true);
    }
    @Override
    public void afterSelectData(TreeNode[]yearArr,AndroidTreeView atView) {
        ll_tree.addView(atView.getView());
    }

    @Override
    public void startToEditSpend(MySpendHolder.IconTreeItem item) {
        mIntent.putExtra(IntentParam.tabIndex, 3);
        mIntent.putExtra(IntentParam.editSpendBean, item.spendBean);
        STActivity(mIntent, AddDataActivity.class);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(addDataBro);
        super.onDestroy();
    }

    @Override
    public void orderByCreateTime(boolean isCreateTime) {
        if(this.isCreateTime!=isCreateTime){
            this.isCreateTime = isCreateTime;
            selectData();
        }

    }
}
