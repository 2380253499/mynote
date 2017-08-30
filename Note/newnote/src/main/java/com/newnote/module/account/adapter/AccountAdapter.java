package com.newnote.module.account.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.widget.CheckBox;
import android.widget.ListView;

import com.github.baseclass.adapter.ListLoadAdapter;
import com.github.baseclass.adapter.ViewHolder;
import com.newnote.R;
import com.newnote.module.account.entity.AccountBean;

/**
 * Created by Administrator on 2017/7/7.
 */

public class AccountAdapter extends ListLoadAdapter<AccountBean> {
    public AccountAdapter(Context context, ListView listView,int itemLayoutId,  int pageSize) {
        super(context, listView, itemLayoutId, pageSize);
    }
    private String searchInfo;
    @Override
    public void convert(final ViewHolder helper,final AccountBean item) {
        int countLength = String.valueOf(getCount()).length();
        int position=helper.getPosition()+1;
        StringBuffer stringBuffer=new StringBuffer();
        for (int i = 0; i < countLength-String.valueOf(position).length(); i++) {
            stringBuffer.append("0");
        }
        String dataAccountHTML=item.getDataAccount();
        String dataSourceHTML=item.getDataSource();
        if(searchInfo!=null){//关键字搜索变色
            dataAccountHTML= getSearchColorString(item.getDataAccount());
            dataSourceHTML=  getSearchColorString(item.getDataSource());
        }


        final CheckBox cb_check = helper.getView(R.id.cb_check);
        helper.setText(R.id.tv_data_id, stringBuffer.toString() + "" + position)
                .setText(R.id.tv_source, Html.fromHtml(dataSourceHTML))
                .setText(R.id.tv_account,Html.fromHtml(dataAccountHTML));

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
