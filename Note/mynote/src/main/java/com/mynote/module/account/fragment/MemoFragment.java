package com.mynote.module.account.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.module.memo.bean.MemoBean;

import butterknife.BindView;

public class MemoFragment extends BaseFragment {
    @BindView(R.id.et_search_memo)
    EditText et_search_memo;
    @BindView(R.id.lv_memo_list)
    ListView lv_memo_list;
    private MemoBean memoBean;
    private boolean isCreateTime;
    public static MemoFragment newInstance() {
        Bundle args = new Bundle();
        MemoFragment fragment = new MemoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getContentView() {
        return R.layout.fragment_memo;
    }
    @Override
    protected void initView() {
    }
    @Override
    protected void initData() {
    }
    @Override
    protected void onViewClick(View v) {
        /*switch (v.getId()){
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
        }*/
    }


}
