package com.zr.note.ui.secret.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.secret.activity.contract.SecretContract;
import com.zr.note.ui.secret.activity.contract.imp.SecretImp;
import com.zr.note.ui.secret.constant.IntentParam;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/2/6.
 */
public class SecretActivity extends BaseActivity<SecretContract.View,SecretContract.Presenter> implements SecretContract.View {

    @BindView(R.id.lv_secret_list)
    ListView lv_secret_list;

    @BindView(R.id.fab_secret)
    FloatingActionButton fab_secret;
    public final int addDataRequestCode=100;

    @Override
    protected SecretImp initPresenter() {
        return new SecretImp(this);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_secret;
    }

    @Override
    protected void setToolbarStyle() {
        setTitle("数据列表");
    }

    @Override
    protected int setOptionsMenu() {
        return 0;
    }

    @Override
    protected void initView() {
        fab_secret.setOnClickListener(this);

        lv_secret_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.editBean(position);
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.selectData(lv_secret_list);
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.fab_secret:
                STActivityForResult(mIntent, AddSecretActivity.class, addDataRequestCode);
                break;
        }
    }

    @Override
    protected void menuOnClick(int itemId) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==addDataRequestCode&&resultCode==RESULT_OK){
            mPresenter.selectData(lv_secret_list);
        }
    }
    @Override
    public void editBean(List<MemoBean> memoBeans,int position) {
        Intent intent = getmIntent().putExtra(IntentParam.editData, memoBeans.get(position));
        STActivityForResult(intent, AddSecretActivity.class, addDataRequestCode);
    }
}
