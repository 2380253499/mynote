package com.mynote.module.account.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.baseclass.BaseDividerListItem;
import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.rx.MySubscriber;
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
import com.mynote.module.home.event.OptionEvent;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class AccountFragment extends BaseFragment<AccountImp> {
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
        adapter=new AccountAdapter(mContext,R.layout.item_account,pageSize,nsv);
        adapter.setOnLoadMoreListener(this);
        adapter.setClickListener(new LoadMoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AccountBean accountBean = adapter.getList().get(position);
                Intent intent=new Intent();
                intent.putExtra(IntentParam.tabIndex, 0);
                intent.putExtra(IntentParam.editAccount, accountBean);
                STActivity(intent, AddDataActivity.class);
            }
        });
        adapter.setLongClickListener(new LoadMoreAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                accountBean =adapter.getList().get(position);
                mPopupwindow.showAsDropDown(view, PhoneUtils.getPhoneWidth(getActivity()) / 2 - PhoneUtils.dip2px(getActivity(), 90), -PhoneUtils.dip2px(getActivity(), 80));
            }
        });
        rv_account.setNestedScrollingEnabled(false);
        BaseDividerListItem dividerListItem=new BaseDividerListItem(mContext,2);
        rv_account.addItemDecoration(dividerListItem);
        rv_account.setLayoutManager(new LinearLayoutManager(mContext));
        rv_account.setAdapter(adapter);
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
                if(event.index==0){
                    //0创建时间排序
                    //1修改时间排序
                    //2批量删除
                    switch (event.flag){
                        case 0:
                            isOrderByCreateTime=true;
                            showLoading();
                            getData(1,false);
                        break;
                        case 1:
                            isOrderByCreateTime=false;
                            showLoading();
                            getData(1,false);
                        break;
                        case 2:
                            adapter.setEdit(true);
                            adapter.notifyDataSetChanged();
                        break;
                    }

                }
            }
        });
    }

    @Override
    protected void getData(int page, boolean isLoad) {
        super.getData(page, isLoad);
        RXStart(pl_load,new IOCallBack<List<AccountBean>>() {
            @Override
            public void call(Subscriber<? super List<AccountBean>> subscriber) {
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
                    pageNum=2;
                    adapter.setList(list,true);
                }
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

    private void deleteData(int id) {
        showLoading();
        RXStart(true,new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                boolean b = mDaoImp.deleteAccount(id);
                subscriber.onNext(b?"删除成功":"删除失败");
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(String s) {
                showMsg(s);
                getData(1,false);
            }
        });
    }


}
