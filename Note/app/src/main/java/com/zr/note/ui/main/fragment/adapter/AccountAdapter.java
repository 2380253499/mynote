package com.zr.note.ui.main.fragment.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;

import com.zr.note.R;
import com.zr.note.adapter.CommonAdapter;
import com.zr.note.adapter.ViewHolder;
import com.zr.note.ui.main.entity.AccountBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */
public class AccountAdapter extends CommonAdapter<AccountBean> {
    private SparseArray<String>dataId;
    private boolean isCheck;
    public AccountAdapter(Context context, List<AccountBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        dataId=new SparseArray<String>();
    }
    @Override
    public void convert(ViewHolder helper, AccountBean item) {
        int countLength = String.valueOf(getCount()).length();
        int position=helper.getPosition()+1;
        StringBuffer stringBuffer=new StringBuffer();
        for (int i = 0; i < countLength-String.valueOf(position).length(); i++) {
            stringBuffer.append("0");
        }
        CheckBox cb_check = helper.getView(R.id.cb_check);
        helper.setText(R.id.tv_data_id, stringBuffer.toString()+""+position)
                .setText(R.id.tv_source, item.getDataSource())
                .setText(R.id.tv_account, item.getDataAccount());
        if(isCheck){
            cb_check.setVisibility(View.VISIBLE);
        }else{
            cb_check.setVisibility(View.GONE);
        }
    }
    public void setCheck(){
        isCheck=true;
    }
}
