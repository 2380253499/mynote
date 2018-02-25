package com.mynote.module.account.fragment;

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
import com.mynote.module.account.adapter.AccountAdapter;
import com.mynote.module.account.bean.AccountBean;
import com.mynote.module.account.dao.imp.AccountImp;
import com.mynote.module.home.activity.AddDataActivity;
import com.mynote.event.OptionEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class AccountFragment extends BaseFragment<AccountImp> {
    @BindView(R.id.ll_view)
    LinearLayout ll_view;
    @BindView(R.id.et_search_account)
    MyEditText et_search_account;

    @BindView(R.id.rv_account)
    RecyclerView rv_account;

    AccountAdapter adapter;
    private MyPopupwindow mPopupwindow;
    private AccountBean accountBean;

    public static AccountFragment newInstance() {
        Bundle args = new Bundle();
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initView() {
        setPopupwindow();
        rv_account.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE){
                    ll_view.requestFocusFromTouch();
                    PhoneUtils.hiddenKeyBoard(mContext,et_search_account);
                }
                return false;
            }
        });
        adapter=new AccountAdapter(mContext,R.layout.item_account,pageSize);
        adapter.setOnLoadMoreListener(this);
        adapter.setClickListener(new LoadMoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AccountBean accountBean = adapter.getList().get(position);
                Intent intent=new Intent();
                intent.putExtra(IntentParam.tabIndex, GetDataEvent.accountIndex);
                intent.putExtra(IntentParam.editAccount, accountBean);
                STActivity(intent, AddDataActivity.class);
            }
        });
        adapter.setLongClickListener(new LoadMoreAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                accountBean =adapter.getList().get(position);
                accountBean.setAdapterIndex(position);
                mPopupwindow.showAsDropDown(view, PhoneUtils.getPhoneWidth(getActivity()) / 2 - PhoneUtils.dip2px(getActivity(),80), -PhoneUtils.dip2px(getActivity(), 80));
            }
        });
        rv_account.setNestedScrollingEnabled(false);
        BaseDividerListItem dividerListItem=new BaseDividerListItem(mContext,2);
        rv_account.addItemDecoration(dividerListItem);
        rv_account.setLayoutManager(new LinearLayoutManager(mContext));
        rv_account.setAdapter(adapter);



        et_search_account.addTextChangedListener(new TextWatcher() {
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
        View menu = LayoutInflater.from(getActivity()).inflate(R.layout.popu_account_menu, null);
        TextView tv_menu_copyAccount= (TextView) menu.findViewById(R.id.tv_menu_copyAccount);
        TextView tv_menu_copyPwd= (TextView) menu.findViewById(R.id.tv_menu_copyPwd);
        TextView tv_menu_deleteAccount= (TextView) menu.findViewById(R.id.tv_menu_deleteAccount);
        tv_menu_copyAccount.setOnClickListener(this);
        tv_menu_copyPwd.setOnClickListener(this);
        tv_menu_deleteAccount.setOnClickListener(this);
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
                if(event.index==GetDataEvent.accountIndex){
                    showLoading();
                    getData(1,false);
                }
            }
        });
        getRxBusEvent(OptionEvent.class, new MySubscriber<OptionEvent>() {
            @Override
            public void onMyNext(OptionEvent event) {
                if(event.index==GetDataEvent.accountIndex){
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
                            List<Integer>list=new ArrayList<>();
                            for (int i = 0; i < adapter.getList().size(); i++) {
                                AccountBean bean = adapter.getList().get(i);
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
        RXStart(pl_load,new IOCallBack<List<AccountBean>>() {
            @Override
            public void call(Subscriber<? super List<AccountBean>> subscriber) {
               /* for (int i = 0; i < 200; i++) {
                    AccountBean accountBean = new AccountBean();
                    accountBean.setDataAccount(i+"asfd"+new Random().nextInt(10)+20);
                    mDaoImp.addAccount(accountBean);
                }*/
                dataCount = mDaoImp.selectAccountCount();
                List<AccountBean> accountList = mDaoImp.selectAccount(page, searchInfo, isOrderByCreateTime);
                subscriber.onNext(accountList);
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(List<AccountBean> list) {
                if(isLoad){
                    pageNum++;
                    adapter.addList(list,true);
                }else{
                    RxBus.getInstance().post(new OptionEvent(OptionEvent.flag_get_data_count,GetDataEvent.accountIndex));
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
            case R.id.tv_menu_copyAccount:
                mPopupwindow.dismiss();
                if(!TextUtils.isEmpty(accountBean.getDataAccount())){
                    PhoneUtils.copyText(getActivity(), accountBean.getDataAccount());
                    showToastS("复制账号成功");
                }
            break;
            case R.id.tv_menu_copyPwd:
                mPopupwindow.dismiss();
                if(TextUtils.isEmpty(accountBean.getDataPassword())){
                    showToastS("密码为空无法复制");
                }else{
                    PhoneUtils.copyText(getActivity(), accountBean.getDataPassword());
                    showToastS("复制密码成功");
                }
            break;
            case R.id.tv_menu_deleteAccount:
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
                        deleteData(accountBean.get_id());
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
                    mDaoImp.deleteAccount(list.get(i));
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
                mDaoImp.deleteAccount(id);
                dataCount = mDaoImp.selectAccountCount();
                subscriber.onNext("删除成功");
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(String s) {
                RxBus.getInstance().post(new OptionEvent(OptionEvent.flag_get_data_count,GetDataEvent.accountIndex));
                showMsg(s);
                adapter.getList().remove(accountBean.getAdapterIndex());
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
