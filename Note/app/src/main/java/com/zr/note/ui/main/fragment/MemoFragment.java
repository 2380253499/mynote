package com.zr.note.ui.main.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.main.activity.AddDataActivity;
import com.zr.note.ui.main.broadcast.AddMemoDataBro;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.main.fragment.contract.MemoCon;
import com.zr.note.ui.main.fragment.contract.imp.MemoImp;
import com.zr.note.ui.main.inter.AddDataInter;
import com.zr.note.view.MyPopupwindow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemoFragment extends BaseFragment<MemoCon.View,MemoCon.Presenter> implements MemoCon.View {
    @BindView(R.id.lv_memo_list)
    ListView lv_memo_list;
    private MemoBean memoBean;
    private AddMemoDataBro addDataBro;
    public static MemoFragment newInstance() {
        Bundle args = new Bundle();
        MemoFragment fragment = new MemoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addDataBro = new AddMemoDataBro(new AddDataInter.AddDataFinish() {
            @Override
            public void addDataFinish() {
                selectData(true);
            }
        });
        getActivity().registerReceiver(addDataBro, new IntentFilter(BroFilter.addData_memo));
    }
    @Override
    protected MemoImp initPresenter() {
        return new MemoImp(getActivity());
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_memo;
    }

    @Override
    protected void initView() {
        View menu = LayoutInflater.from(getActivity()).inflate(R.layout.popu_memo_menu, null);
        TextView tv_menu_copyMemoContent= (TextView) menu.findViewById(R.id.tv_menu_copyMemoContent);
        TextView tv_menu_deleteMemo= (TextView) menu.findViewById(R.id.tv_menu_deleteMemo);
        tv_menu_copyMemoContent.setOnClickListener(this);
        tv_menu_deleteMemo.setOnClickListener(this);
        mPopupwindow = new MyPopupwindow(getActivity(), menu);
        lv_memo_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                memoBean = mPresenter.copyMemo(position);
                mPopupwindow.showAsDropDown(view, PhoneUtils.getPhoneWidth(getActivity()) / 2 - PhoneUtils.dip2px(getActivity(), 55), -PhoneUtils.dip2px(getActivity(), 80));
                return true;
            }
        });
        lv_memo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemoBean memoBean = (MemoBean) parent.getItemAtPosition(position);
                mIntent.putExtra(IntentParam.tabIndex, 1);
                mIntent.putExtra(IntentParam.editMemoBean, memoBean);
                STActivity(mIntent, AddDataActivity.class);
            }
        });
    }

    @Override
    protected void initData() {
        selectData(true);
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_menu_copyMemoContent:
                mPopupwindow.dismiss();
                if(!TextUtils.isEmpty(memoBean.getDataContent())){
                    PhoneUtils.copyText(getActivity(),memoBean.getDataContent());
                    showToastS("复制成功");
                }
                break;
            case R.id.tv_menu_deleteMemo:
                mPopupwindow.dismiss();
                mPresenter.deleteMemoById(mDialog,memoBean.get_id());
            break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate AddMemoFragment fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void selectData(boolean isOrderByCreateTime) {
        mPresenter.selectData(lv_memo_list, true);
    }
    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(addDataBro);
        super.onDestroy();
    }
}
