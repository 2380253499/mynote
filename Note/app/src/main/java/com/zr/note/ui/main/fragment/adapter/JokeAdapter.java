package com.zr.note.ui.main.fragment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.zr.note.R;
import com.zr.note.adapter.CommonAdapter;
import com.zr.note.adapter.ViewHolder;
import com.zr.note.tools.StringUtils;
import com.zr.note.ui.constant.RxTag;
import com.zr.note.ui.main.entity.JokeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */
public class JokeAdapter extends CommonAdapter<JokeBean> {
    private SparseBooleanArray checkState;
    private boolean startCheck;
    private List<Integer> data_id;
    private String searchInfo;

    public JokeAdapter(Context context, List<JokeBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        checkState=new SparseBooleanArray();
        data_id=new ArrayList<Integer>();
    }

    public List<Integer> getData_id() {
        return data_id;
    }

    @Override
    public void convert(final ViewHolder helper,final JokeBean item) {
        String dataContentHTML=item.getDataContent();
        String dataRemarkHTML=item.getDataRemark();
        if(searchInfo!=null){//关键字搜索变色
            dataContentHTML= getSearchColorString(item.getDataContent());
            dataRemarkHTML=  getSearchColorString(item.getDataRemark());
        }
        helper.setText(R.id.tv_data_id, StringUtils.getStringLength(getCount(), helper.getPosition()) + "" + (helper.getPosition() + 1))
                .setText(R.id.tv_joke_content, Html.fromHtml(dataContentHTML));
        TextView tv_reminder = helper.getTextView(R.id.tv_joke_remark);
        if (item.getDataRemark() == null || item.getDataRemark().trim().length() == 0) {
            tv_reminder.setVisibility(View.GONE);
        } else {
            tv_reminder.setVisibility(View.VISIBLE);
            tv_reminder.setText(Html.fromHtml(dataRemarkHTML));
        }

        final CheckBox cb_check = helper.getView(R.id.cb_joke_check);
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
        data_id.clear();
        checkState.clear();
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
    @Override
    public void setData(List<JokeBean> mDatas) {
        super.setData(mDatas);
        cancelCheckAll(true);
    }

    public void setSearchInfo(String info) {
        searchInfo=info;
    }
    @NonNull
    private String getSearchColorString(String dataContentHTML) {
        StringBuffer dataContent=new StringBuffer(dataContentHTML);
        int indexOf = dataContentHTML.toLowerCase().indexOf(searchInfo.toLowerCase());
        if(indexOf>=0){
            String sameInfo= "<font color='#18B4ED'>"+dataContent.subSequence(indexOf, indexOf + searchInfo.length())+"</font>";
            dataContent=dataContent.replace(indexOf, indexOf + searchInfo.length(), sameInfo);

        }
        dataContentHTML=  dataContent.toString();
        return dataContentHTML;
    }
}
