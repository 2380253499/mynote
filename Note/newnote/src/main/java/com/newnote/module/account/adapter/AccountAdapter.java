package com.newnote.module.account.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.widget.CheckBox;

import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.adapter.LoadMoreViewHolder;
import com.newnote.R;
import com.newnote.module.account.entity.AccountBean;

/**
 * Created by Administrator on 2017/7/7.
 */

public class AccountAdapter extends LoadMoreAdapter<AccountBean> {

    private String searchInfo;

    public AccountAdapter(Context mContext, int layoutId, int pageSize, NestedScrollView nestedScrollView) {
        super(mContext, layoutId, pageSize, nestedScrollView);
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

    @Override
    public void bindData(LoadMoreViewHolder holder, int position, AccountBean item) {
        int countLength = String.valueOf(getItemCount()).length();
        int index=position+1;
        StringBuffer stringBuffer=new StringBuffer();
        for (int i = 0; i < countLength-String.valueOf(index).length(); i++) {
            stringBuffer.append("0");
        }
        String dataAccountHTML=item.getDataAccount();
        String dataSourceHTML=item.getDataSource();
        if(searchInfo!=null){//关键字搜索变色
            dataAccountHTML= getSearchColorString(item.getDataAccount());
            dataSourceHTML=  getSearchColorString(item.getDataSource());
        }
        final CheckBox cb_check = (CheckBox) holder.getView(R.id.cb_check);
        holder.setText(R.id.tv_data_id, stringBuffer.toString() + "" + index)
                .setText(R.id.tv_source, Html.fromHtml(dataSourceHTML)+"")
                .setText(R.id.tv_account,Html.fromHtml(dataAccountHTML)+"");
    }
}
