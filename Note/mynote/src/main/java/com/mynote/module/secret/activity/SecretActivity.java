package com.mynote.module.secret.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.BaseDividerListItem;
import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.rx.RxBus;
import com.github.baseclass.view.MyDialog;
import com.github.baseclass.view.MyPopupwindow;
import com.github.customview.MyEditText;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseActivity;
import com.mynote.event.GetDataEvent;
import com.mynote.event.OptionEvent;
import com.mynote.module.home.activity.AddDataActivity;
import com.mynote.module.secret.adapter.SecretAdapter;
import com.mynote.module.secret.bean.SecretBean;
import com.mynote.module.secret.dao.imp.SecretImp;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/2/6.
 */
public class SecretActivity extends BaseActivity<SecretImp> {
    String searchInfo;
    boolean isOrderByCreateTime;
    @BindView(R.id.fab_secret)
    FloatingActionButton fab_secret;
    @BindView(R.id.view_backgroud)
    View view_backgroud;
    @BindView(R.id.ll_home_operation)
    LinearLayout ll_home_operation;

    @BindView(R.id.ll_view)
    LinearLayout ll_view;
    @BindView(R.id.et_search_secret)
    MyEditText et_search_secret;

    @BindView(R.id.rv_secret)
    RecyclerView rv_secret;

    SecretAdapter adapter;
    private MyPopupwindow mPopupwindow;
    private SecretBean secretBean;
    private int dataCount;


    @Override
    protected int getContentView() {
        setAppTitle("数据列表");
        setAppRightImg(R.drawable.main_more);
        return R.layout.activity_secret;
    }

    @Override
    protected void initView() {
        setPopupwindow();
        rv_secret.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE){
                    ll_view.requestFocusFromTouch();
                    PhoneUtils.hiddenKeyBoard(mContext, et_search_secret);
                }
                return false;
            }
        });
        adapter=new SecretAdapter(mContext,R.layout.item_secret,pageSize);
        adapter.setOnLoadMoreListener(this);
        adapter.setClickListener(new LoadMoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SecretBean secretBean = adapter.getList().get(position);
                Intent intent=new Intent();
                intent.putExtra(IntentParam.tabIndex, GetDataEvent.secretIndex);
                intent.putExtra(IntentParam.editSecretBean, secretBean);
                STActivity(intent, AddDataActivity.class);
            }
        });
        adapter.setLongClickListener(new LoadMoreAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                secretBean =adapter.getList().get(position);
                secretBean.setAdapterIndex(position);
                mPopupwindow.showAsDropDown(view, PhoneUtils.getPhoneWidth(mContext) / 2 - PhoneUtils.dip2px(mContext, 50), -PhoneUtils.dip2px(mContext, 80));
            }
        });
        BaseDividerListItem dividerListItem=new BaseDividerListItem(mContext,2);
        rv_secret.addItemDecoration(dividerListItem);
        rv_secret.setLayoutManager(new LinearLayoutManager(mContext));
        rv_secret.setAdapter(adapter);



        et_search_secret.addTextChangedListener(new TextWatcher() {
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
        View menu = LayoutInflater.from(mContext).inflate(R.layout.popu_memo_menu, null);
        TextView tv_menu_copySecretContent= (TextView) menu.findViewById(R.id.tv_menu_copyMemoContent);
        TextView tv_menu_deleteSecret= (TextView) menu.findViewById(R.id.tv_menu_deleteMemo);
        tv_menu_copySecretContent.setOnClickListener(this);
        tv_menu_deleteSecret.setOnClickListener(this);
        mPopupwindow = new MyPopupwindow(mContext, menu);
        mPopupwindow.setBackground(R.color.transparent);
    }
    @Override
    protected void initData() {
        showProgress();
        getData(1,false);
    }
    @Override
    protected void getData(int page, boolean isLoad) {
        super.getData(page, isLoad);
        RXStart(pl_load,new IOCallBack<List<SecretBean>>() {
            @Override
            public void call(Subscriber<? super List<SecretBean>> subscriber) {
               /* for (int i = 0; i < 200; i++) {
                    SecretBean secretBean = new SecretBean();
                    secretBean.setDataContent(i+"secret"+new Random().nextInt(10)+20);
                    mDaoImp.addSecret(secretBean);
                }*/
                dataCount = mDaoImp.selectSecretCount();
                List<SecretBean> secretList = mDaoImp.selectSecret(page, searchInfo, isOrderByCreateTime);
                subscriber.onNext(secretList);
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(List<SecretBean> list) {
                if(isLoad){
                    pageNum++;
                    adapter.addList(list,true);
                }else{
                    RxBus.getInstance().post(new OptionEvent(OptionEvent.flag_get_data_count,GetDataEvent.secretIndex));
                    pageNum=2;
                    adapter.setList(list,true);
                }
                adapter.setSearchInfo(searchInfo);
            }
        });
    }
    private void promptForDelete(List<Integer> list) {
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

    @OnClick({R.id.fab_secret,R.id.app_right_iv})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.app_right_iv:
                showSeting();
                break;
            case R.id.fab_secret:
                Intent intent=new Intent();
                intent.putExtra(IntentParam.editSecretBean,secretBean);
                STActivityForResult(intent,AddSecretActivity.class,1000);
                break;
            case R.id.tv_menu_copyMemoContent:
                mPopupwindow.dismiss();
                if(!TextUtils.isEmpty(secretBean.getDataContent())){
                    PhoneUtils.copyText(mContext, secretBean.getDataContent());
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
                        deleteData(secretBean.get_id());
                    }
                });
                mDialog.create().show();
                break;
        }
    }
    private void showSeting() {
        View view = inflateView(R.layout.popu_options, null);
        mPopupwindow = new MyPopupwindow(this,view);
        view.findViewById(R.id.tv_orderBy_create).setOnClickListener(getSetListener(OptionEvent.flag_0));
        view.findViewById(R.id.tv_orderBy_update).setOnClickListener(getSetListener(OptionEvent.flag_1));
        view.findViewById(R.id.tv_batchDelete).setOnClickListener(getSetListener(OptionEvent.flag_prepare_delete));

        int xoff = PhoneUtils.getPhoneWidth(this) - PhoneUtils.dip2px(this, 125);
        mPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                view_backgroud.setVisibility(View.GONE);
            }
        });
        mPopupwindow.setBackground(R.color.transparent);
        mPopupwindow.showAsDropDown(toolbar, xoff,0);
        view_backgroud.setVisibility(View.VISIBLE);
    }
    @NonNull
    private MyOnClickListener getSetListener(int flag) {
        return new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                mPopupwindow.dismiss();
                if(flag==OptionEvent.flag_prepare_delete){
                    showOperation(true);
                }
            }
        };
    }
    public void showOperation(boolean isShow){
        ll_home_operation.setVisibility(isShow?View.VISIBLE:View.GONE);
        fab_secret.setVisibility(isShow?View.GONE:View.VISIBLE);
    }
    private void deleteData(List<Integer> list) {
        showLoading();
        RXStart(true,new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < list.size(); i++) {
                    mDaoImp.deleteSecret(list.get(i));
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
                mDaoImp.deleteSecret(id);
                dataCount = mDaoImp.selectSecretCount();
                subscriber.onNext("删除成功");
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(String s) {
                RxBus.getInstance().post(new OptionEvent(OptionEvent.flag_get_data_count,GetDataEvent.secretIndex));
                showMsg(s);
                adapter.getList().remove(secretBean.getAdapterIndex());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                showMsg("删除失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 1000:
                    getData(1,false);
                break;
            }
        }
    }
}
