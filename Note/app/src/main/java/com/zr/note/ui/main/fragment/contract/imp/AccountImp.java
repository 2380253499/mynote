package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.widget.ListView;

import com.zr.note.R;
import com.zr.note.adapter.CommonAdapter;
import com.zr.note.adapter.ViewHolder;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.ui.main.entity.AccountBean;
import com.zr.note.ui.main.fragment.contract.AccountCon;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public class AccountImp extends IPresenter<AccountCon.View> implements AccountCon.Presenter{
    public AccountImp(Context context) {
        super(context);
    }

    @Override
    public List<AccountBean> selectData(ListView lv_account_list) {
        List<AccountBean> list = DBManager.getInstance(mContext).selectAccount();
        CommonAdapter<AccountBean> commonAdapter = new CommonAdapter<AccountBean>(mContext, list, R.layout.item_account) {
            @Override
            public void convert(ViewHolder helper, AccountBean item) {
                int countLength = String.valueOf(getCount()).length();
                int position=helper.getPosition()+1;
                StringBuffer stringBuffer=new StringBuffer();
                for (int i = 0; i < countLength-String.valueOf(position).length(); i++) {
                    stringBuffer.append("0");
                }

                helper.setText(R.id.tv_data_id, stringBuffer.toString()+""+position)
                        .setText(R.id.tv_source, item.getDataSource())
                        .setText(R.id.tv_account, item.getDataAccount());
            }
        };
        lv_account_list.setAdapter(commonAdapter);
        return list;
    }
}
