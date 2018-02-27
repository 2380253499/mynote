package com.mynote.module.memo.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.baseclass.BaseDividerListItem;
import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.rx.MySubscriber;
import com.github.baseclass.rx.RxBus;
import com.github.baseclass.view.MyDialog;
import com.github.baseclass.view.MyPopupwindow;
import com.github.customview.MyEditText;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.event.GetDataEvent;
import com.mynote.module.home.activity.AddDataActivity;
import com.mynote.event.OptionEvent;
import com.mynote.module.memo.adapter.MemoAdapter;
import com.mynote.module.memo.bean.MemoBean;
import com.mynote.module.memo.dao.imp.MemoImp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class MemoFragment extends BaseFragment<MemoImp> {
    @BindView(R.id.ll_view)
    LinearLayout ll_view;
    @BindView(R.id.et_search_memo)
    MyEditText et_search_memo;

    @BindView(R.id.rv_memo)
    RecyclerView rv_memo;

    MemoAdapter adapter;
    private MyPopupwindow mPopupwindow;
    private MemoBean memoBean;

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
        setPopupwindow();
        rv_memo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE){
                    ll_view.requestFocusFromTouch();
                    PhoneUtils.hiddenKeyBoard(mContext, et_search_memo);
                }
                return false;
            }
        });
        adapter=new MemoAdapter(mContext,R.layout.item_memo,pageSize);
        adapter.setOnLoadMoreListener(this);
        adapter.setClickListener(new LoadMoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MemoBean memoBean = adapter.getList().get(position);
                Intent intent=new Intent();
                intent.putExtra(IntentParam.tabIndex, GetDataEvent.memoIndex);
                intent.putExtra(IntentParam.editMemoBean, memoBean);
                STActivity(intent, AddDataActivity.class);
            }
        });
        adapter.setLongClickListener(new LoadMoreAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                memoBean =adapter.getList().get(position);
                memoBean.setAdapterIndex(position);
                mPopupwindow.showAsDropDown(view, PhoneUtils.getPhoneWidth(getActivity()) / 2 - PhoneUtils.dip2px(getActivity(), 50), -PhoneUtils.dip2px(getActivity(), 80));
            }
        });
        BaseDividerListItem dividerListItem=new BaseDividerListItem(mContext,2);
        rv_memo.addItemDecoration(dividerListItem);
        rv_memo.setLayoutManager(new LinearLayoutManager(mContext));
        rv_memo.setAdapter(adapter);



        et_search_memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                searchInfo=s.toString().replace(" ","");
                getData(1,false);
            }
        });
    }
    private void setPopupwindow() {
        View menu = LayoutInflater.from(getActivity()).inflate(R.layout.popu_memo_menu, null);
        TextView tv_menu_copyMemoContent= (TextView) menu.findViewById(R.id.tv_menu_copyMemoContent);
        TextView tv_menu_deleteMemo= (TextView) menu.findViewById(R.id.tv_menu_deleteMemo);
        tv_menu_copyMemoContent.setOnClickListener(this);
        tv_menu_deleteMemo.setOnClickListener(this);
        mPopupwindow = new MyPopupwindow(getActivity(), menu);
        mPopupwindow.setBackground(R.color.transparent);
    }

    @Override
    protected void initData() {
        showProgress();
        getData(1,false);
    }
    @Override
    protected void initRxBus() {
        super.initRxBus();
        getRxBusEvent(GetDataEvent.class, new MySubscriber<GetDataEvent>() {
            @Override
            public void onMyNext(GetDataEvent event) {
                if(event.index==GetDataEvent.memoIndex){
                    showLoading();
                    getData(1,false);
                }
            }
        });
        getRxBusEvent(OptionEvent.class, new MySubscriber<OptionEvent>() {
            @Override
            public void onMyNext(OptionEvent event) {
                if(event.index==GetDataEvent.memoIndex){
                    //0创建时间排序
                    //1修改时间排序
                    //2批量删除
                    switch (event.flag){
                        case OptionEvent.flag_0:
                            isOrderByCreateTime=true;
                            showLoading();
                            getData(1,false);
                            break;
                        case OptionEvent.flag_1:
                            isOrderByCreateTime=false;
                            showLoading();
                            getData(1,false);
                            break;
                        case OptionEvent.flag_prepare_delete:
                            adapter.setEdit(true);
                            adapter.notifyDataSetChanged();
                            break;
                        case OptionEvent.flag_cancel_delete:
                            adapter.setEdit(false);
                            adapter.notifyDataSetChanged();
                            break;
                        case OptionEvent.flag_start_delete:
                            if(isEmpty(adapter.getList())){
                                showMsg("暂无数据可删除");
                                return;
                            }
                            boolean flag=false;
                            List<Integer> list=new ArrayList<>();
                            for (int i = 0; i < adapter.getList().size(); i++) {
                                MemoBean bean = adapter.getList().get(i);
                                if(bean.isCheck()){
                                    flag=true;
                                    list.add(bean.get_id());
                                }
                            }
                            if(flag==false){
                                showMsg("请选择数据");
                                return;
                            }
                            promptForDelete(list);
                            break;
                    }

                }
            }
        });
    }
    private void promptForDelete(List<Integer>list) {
        MyDialog.Builder mDialog=new MyDialog.Builder(mContext);
        mDialog.setMessage("确认删除所选数据吗?");
        mDialog.setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteData(list);
            }
        });
        mDialog.create().show();
    }
    @Override
    protected void getData(int page, boolean isLoad) {
        super.getData(page, isLoad);
        RXStart(pl_load,new IOCallBack<List<MemoBean>>() {
            @Override
            public void call(Subscriber<? super List<MemoBean>> subscriber) {
               /* for (int i = 0; i < 200; i++) {
                    SecretBean memoBean = new SecretBean();
                    memoBean.setDataContent(i+"memo"+new Random().nextInt(10)+20);
                    mDaoImp.addMemo(memoBean);
                }*/
                dataCount = mDaoImp.selectMemoCount();
                List<MemoBean> memoList = mDaoImp.selectMemo(page, searchInfo, isOrderByCreateTime);
                subscriber.onNext(memoList);
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(List<MemoBean> list) {
                if(isLoad){
                    pageNum++;
                    adapter.addList(list,true);
                }else{
                    RxBus.getInstance().post(new OptionEvent(OptionEvent.flag_get_data_count,GetDataEvent.memoIndex));
                    pageNum=2;
                    adapter.setList(list,true);
                }
                adapter.setSearchInfo(searchInfo);
            }
        });
    }
    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_menu_copyMemoContent:
                mPopupwindow.dismiss();
                if(!TextUtils.isEmpty(memoBean.getDataContent())){
                    PhoneUtils.copyText(getActivity(), memoBean.getDataContent());
                    showToastS("复制内容成功");
                }
                break;
            case R.id.tv_menu_deleteMemo:
                mPopupwindow.dismiss();
                mDialog=new MyDialog.Builder(mContext);
                mDialog.setMessage(mContext.getString(R.string.delete_data));
                mDialog.setNegativeButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteData(memoBean.get_id());
                    }
                });
                mDialog.create().show();
                break;
        }
    }

    private void deleteData(List<Integer> list) {
        showLoading();
        RXStart(true,new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < list.size(); i++) {
                    mDaoImp.deleteMemo(list.get(i));
                }
                subscriber.onNext("删除成功");
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(String s) {
                showMsg(s);
                getData(1,false);
            }
            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                showMsg("删除失败");
                getData(1,false);
            }
        });
    }
    private void deleteData(int id) {
        showLoading();
        RXStart(new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                mDaoImp.deleteMemo(id);
                dataCount = mDaoImp.selectMemoCount();
                subscriber.onNext("删除成功");
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(String s) {
                RxBus.getInstance().post(new OptionEvent(OptionEvent.flag_get_data_count,GetDataEvent.memoIndex));
                showMsg(s);
                adapter.getList().remove(memoBean.getAdapterIndex());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                showMsg("删除失败");
            }
        });
    }

}
