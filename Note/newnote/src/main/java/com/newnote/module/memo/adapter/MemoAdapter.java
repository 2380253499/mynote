package com.newnote.module.memo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.github.androidtools.StringUtils;
import com.github.baseclass.adapter.ListLoadAdapter;
import com.github.baseclass.adapter.ViewHolder;
import com.newnote.R;
import com.newnote.module.memo.entity.MemoBean;

/**
 * Created by Administrator on 2017/7/7.
 */

public class MemoAdapter extends ListLoadAdapter<MemoBean> {
    private SparseBooleanArray checkState;
    private boolean startCheck;
    private String searchInfo;
    public MemoAdapter(Context context, ListView listView,int itemLayoutId,  int pageSize) {
        super(context, listView, itemLayoutId, pageSize);
    }
    @Override
    public void convert(final ViewHolder helper,final MemoBean item) {

        TextView tv_reminder = helper.getTextView(R.id.tv_reminder);

        String dataContentHTML=item.getDataContent();
        String dataRemarkHTML=item.getDataRemark();
        if(searchInfo!=null){//关键字搜索变色
            dataContentHTML = getSearchColorString(dataContentHTML);
            dataRemarkHTML = getSearchColorString(dataRemarkHTML);
        }
        helper.setText(R.id.tv_data_id, StringUtils.getStringLength(getCount(), helper.getPosition()) + "" + (helper.getPosition() + 1))
                .setText(R.id.tv_memo_content, Html.fromHtml(dataContentHTML));

        if (dataRemarkHTML== null || dataRemarkHTML.trim().length() == 0) {
            tv_reminder.setVisibility(View.GONE);
        } else {
            tv_reminder.setVisibility(View.VISIBLE);
            tv_reminder.setText(Html.fromHtml(dataRemarkHTML));
        }
        final CheckBox cb_check = helper.getView(R.id.cb_memo_check);
        cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = cb_check.isChecked();
                checkState.put(helper.getPosition(), isChecked);
                /*if (isChecked) {
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
                }*/
            }
        });
        if(startCheck){
            cb_check.setVisibility(View.VISIBLE);
        }else{
            cb_check.setVisibility(View.GONE);
        }
        cb_check.setChecked(checkState.get(helper.getPosition()));
    }

    @NonNull
    private String getSearchColorString(String dataContentHTML) {
        StringBuffer dataContent=new StringBuffer(dataContentHTML);
        int indexOf = dataContentHTML.toLowerCase().indexOf(searchInfo.toLowerCase());
        if(indexOf>=0){
            String sameInfo= "<font color='#18B4ED'>"+dataContent.subSequence(indexOf, indexOf + searchInfo.length())+"</font>";
            dataContent=dataContent.replace(indexOf,indexOf+searchInfo.length(),sameInfo);
        }
        dataContentHTML=  dataContent.toString();
        return dataContentHTML;
    }

    public void setCheck(){
        setCheck(true);
    }
    public void setCheck(boolean check){
        startCheck=check;
        checkState.clear();
    }

    public void setSearchInfo(String searchInfo) {
        this.searchInfo=searchInfo;
    }
}
