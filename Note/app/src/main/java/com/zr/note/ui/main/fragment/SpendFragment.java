package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.ui.main.entity.SpendBean;
import com.zr.note.ui.main.fragment.contract.SpendCon;
import com.zr.note.ui.main.fragment.contract.imp.SpendImp;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpendFragment extends BaseFragment<SpendCon.View,SpendCon.Presenter> implements AddDataInter,SpendCon.View {
    @BindView(R.id.lv_spend_list)
    ListView lv_spend_list;
    private SpendBean spendBean;
    public static SpendFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SpendFragment fragment = new SpendFragment();
        fragment.setArguments(args);
        return fragment;
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
        lv_spend_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.deleteSpendById(mDialog,position);
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        selectData();
    }

    @Override
    protected void viewOnClick(View v) {

    }

    @Override
    public boolean saveData() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate AddMemoFragment fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void selectData() {
        mPresenter.selectData(lv_spend_list,true);
    }
}
