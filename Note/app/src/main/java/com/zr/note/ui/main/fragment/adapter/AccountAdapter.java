package com.zr.note.ui.main.fragment.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;

import com.hwangjr.rxbus.RxBus;
import com.zr.note.R;
import com.zr.note.adapter.CommonAdapter;
import com.zr.note.adapter.ViewHolder;
import com.zr.note.ui.constant.RxTag;
import com.zr.note.ui.main.entity.AccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */
public class AccountAdapter extends CommonAdapter<AccountBean> {
    private SparseBooleanArray checkState;
    private boolean startCheck;
    private List<Integer> data_id;
    public AccountAdapter(Context context, List<AccountBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        checkState=new SparseBooleanArray();
        data_id=new ArrayList<Integer>();
    }

    public List<Integer> getData_id() {
        return data_id;
    }

    @Override
    public void convert(final ViewHolder helper,final AccountBean item) {
        int countLength = String.valueOf(getCount()).length();
        int position=helper.getPosition()+1;
        StringBuffer stringBuffer=new StringBuffer();
        for (int i = 0; i < countLength-String.valueOf(position).length(); i++) {
            stringBuffer.append("0");
        }
        final CheckBox cb_check = helper.getView(R.id.cb_check);
        helper.setText(R.id.tv_data_id, stringBuffer.toString()+""+position)
                .setText(R.id.tv_source, item.getDataSource())
                .setText(R.id.tv_account, item.getDataAccount());
        cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = cb_check.isChecked();
                checkState.put(helper.getPosition(), isChecked);
                if (isChecked) {
                    if (!data_id.contains(item.get_id() )) {
                        data_id.add(item.get_id() );
                    }
                    if (data_id.size() == getCount()) {//选择的item数量和数据集数量相等时选中全选按钮
                        RxBus.get().post(RxTag.dataSelectAll, 0);
                    }
                } else {
                    if (data_id.contains(item.get_id())) {
                        int indexOf = data_id.indexOf(item.get_id());
                        data_id.remove(indexOf);
                    }
                    RxBus.get().post(RxTag.dataNoSelectAll, 0);
                }
                Log.i("====", "===="+data_id.size());
            }
        });
        if(startCheck){
            cb_check.setVisibility(View.VISIBLE);
        }else{
            cb_check.setVisibility(View.GONE);
        }
        cb_check.setChecked(checkState.get(helper.getPosition()));
    }
    public void setCheck(){
        setCheck(true);
    }
    public void setCheck(boolean check){
        startCheck=check;
    }

    public void checkAll(final boolean isOrderByCreateTime) {
        for (int i = 0; i < mDatas.size(); i++) {
            checkState.put(i,true);
            if(!data_id.contains(mDatas.get(i).get_id())){
                data_id.add(mDatas.get(i).get_id());
            }
        }
    }
    public void cancelCheckAll(boolean isOrderByCreateTime) {
        data_id.clear();
        checkState.clear();
    }
}
