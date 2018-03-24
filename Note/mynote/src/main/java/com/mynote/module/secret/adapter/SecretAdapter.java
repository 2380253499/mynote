package com.mynote.module.secret.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.baseclass.adapter.LoadMoreViewHolder;
import com.github.baseclass.utils.ActUtils;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.MyAdapter;
import com.mynote.event.GetDataEvent;
import com.mynote.module.secret.activity.AddSecretActivity;
import com.mynote.module.secret.bean.SecretBean;

/**
 * Created by Administrator on 2018/2/2.
 */

public class SecretAdapter extends MyAdapter<SecretBean> {
    private String searchInfo;
    private boolean isEdit;
    public SecretAdapter(Context mContext, int layoutId, int pageSize) {
        super(mContext, layoutId, pageSize);
    }

    public SecretAdapter(Context mContext, int layoutId, int pageSize, NestedScrollView nestedScrollView) {
        super(mContext, layoutId, pageSize, nestedScrollView);
    }

    @Override
    public void bindData(LoadMoreViewHolder holder, int position, SecretBean item) {
        int countLength = String.valueOf(getItemCount()-1).length();
        int number=position+1;
        StringBuffer stringBuffer=new StringBuffer();
        for (int i = 0; i < countLength-String.valueOf(number).length(); i++) {
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

        holder.setText(R.id.tv_data_id, stringBuffer.toString() + "" + number);

        TextView tv_account = holder.getTextView(R.id.tv_memo_content);
        TextView tv_source = holder.getTextView(R.id.tv_reminder);

        tv_source.setText(Html.fromHtml(dataSourceHTML==null?"":dataSourceHTML));
        tv_account.setText(Html.fromHtml(dataAccountHTML));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit()) {
                    getList().get(position).setCheck(!getList().get(position).isCheck());
                    cb_check.setChecked(getList().get(position).isCheck());
                } else {
                    SecretBean secretBean = getList().get(position);
                    Intent intent=new Intent();
                    intent.putExtra(IntentParam.tabIndex, GetDataEvent.secretIndex);
                    intent.putExtra(IntentParam.editSecretBean, secretBean);
//                    STActivityForResult(intent, AddSecretActivity.class,1000);
                    ActUtils.STActivityForResult((Activity)mContext,intent, AddSecretActivity.class,1000);
                }
            }
        });

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
