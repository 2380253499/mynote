package com.mynote.module.memo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.baseclass.adapter.LoadMoreViewHolder;
import com.mynote.R;
import com.mynote.base.MyAdapter;
import com.mynote.module.memo.bean.MemoBean;

/**
 * Created by Administrator on 2018/2/2.
 */

public class MemoAdapter extends MyAdapter<MemoBean> {
    private String searchInfo;
    private boolean isEdit;
    public MemoAdapter(Context mContext, int layoutId, int pageSize) {
        super(mContext, layoutId, pageSize);
    }

    public MemoAdapter(Context mContext, int layoutId, int pageSize, NestedScrollView nestedScrollView) {
        super(mContext, layoutId, pageSize, nestedScrollView);
    }

    @Override
    public void bindData(LoadMoreViewHolder holder, int pos, MemoBean item) {
        int countLength = String.valueOf(getItemCount()-1).length();
        int position=pos+1;
        StringBuffer stringBuffer=new StringBuffer();
        for (int i = 0; i < countLength-String.valueOf(position).length(); i++) {
            stringBuffer.append("0");
        }
        String dataAccountHTML=item.getDataContent();
        String dataSourceHTML=item.getDataRemark();
        if(searchInfo!=null){//关键字搜索变色
            dataAccountHTML= getSearchColorString(item.getDataContent());
            dataSourceHTML=  getSearchColorString(item.getDataRemark());
        }

        final CheckBox cb_check = (CheckBox) holder.getView(R.id.cb_check);
        if(isEdit){
            cb_check.setVisibility(View.VISIBLE);
            cb_check.setChecked(item.isCheck());
            cb_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG+"===","==="+cb_check.isChecked());
                    item.setCheck(cb_check.isChecked());
                }
            });
        }else{
            cb_check.setVisibility(View.GONE);
        }

        holder.setText(R.id.tv_data_id, stringBuffer.toString() + "" + position);

        TextView tv_account = holder.getTextView(R.id.tv_memo_content);
        TextView tv_source = holder.getTextView(R.id.tv_reminder);

        tv_source.setText(Html.fromHtml(dataSourceHTML==null?"":dataSourceHTML));
        tv_account.setText(Html.fromHtml(dataAccountHTML));

    }
    public void setSearchInfo(String info) {
        searchInfo=info;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isEdit() {
        return isEdit;
    }

    @NonNull
    private String getSearchColorString(String dataContentHTML) {
        if(TextUtils.isEmpty(dataContentHTML)){
            return dataContentHTML;
        }
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
