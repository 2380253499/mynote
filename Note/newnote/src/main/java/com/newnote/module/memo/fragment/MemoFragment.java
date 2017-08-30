package com.newnote.module.memo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.github.baseclass.adapter.ListLoadAdapter;
import com.newnote.R;
import com.newnote.base.BaseFragment;
import com.newnote.module.memo.adapter.MemoAdapter;
import com.newnote.module.memo.contract.MemoCon;
import com.newnote.module.memo.contract.imp.MemoImp;
import com.newnote.module.memo.entity.MemoBean;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/4.
 */

public class MemoFragment extends BaseFragment<MemoCon.Presenter> implements MemoCon.View,ListLoadAdapter.OnLoadMoreListener{
    @BindView(R.id.et_search_memo)
    EditText et_search_memo;
    @BindView(R.id.lv_memo_list)
    ListView lv_memo_list;
    private MemoBean memoBean;
//    private AddMemoDataBro addDataBro;
    private boolean orderByCreateTime;
    private MemoAdapter memoAdapter;
    private String searchInfo;

    public static MemoFragment newInstance() {
        Bundle args = new Bundle();
        MemoFragment fragment = new MemoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected MemoImp initPresenter() {
        return new MemoImp(mContext);
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
        memoAdapter =new MemoAdapter(getActivity(),lv_memo_list,R.layout.item_account,pageSize);
        mPresenter.getMemoList(1,searchInfo,orderByCreateTime);
    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    public void loadMore() {
        mPresenter.getMemoList(pageNum,searchInfo,orderByCreateTime);
    }

    @Override
    public void getMemoList(int page, List<MemoBean> item) {
        memoAdapter.setSearchInfo(searchInfo);//搜索关键字变色作用
        if(page==1){
            this.pageNum=2;
            memoAdapter.setList(item);
            lv_memo_list.setAdapter(memoAdapter);
        }else{
            this.pageNum++;
            memoAdapter.addList(item,true);
        }
    }
}
