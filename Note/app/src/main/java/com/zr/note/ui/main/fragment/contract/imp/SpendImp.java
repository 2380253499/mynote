package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListView;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.adapter.CommonAdapter;
import com.zr.note.adapter.ViewHolder;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.tools.DateUtils;
import com.zr.note.tools.MyDialog;
import com.zr.note.tools.StringUtils;
import com.zr.note.ui.main.entity.SpendBean;
import com.zr.note.ui.main.fragment.contract.SpendCon;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SpendImp extends IPresenter<SpendCon.View> implements SpendCon.Presenter {
    private List<SpendBean>spendBeanList;
    public SpendImp(Context context) {
        super(context);
    }

    @Override
    public void selectData(final ListView lv_spend_list,final boolean isOrderByCreateTime) {
        mView.showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                spendBeanList= DBManager.getInstance(mContext).selectSpend(isOrderByCreateTime);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideLoading();
                        CommonAdapter<SpendBean> commonAdapter = new CommonAdapter<SpendBean>(mContext, spendBeanList, R.layout.item_spend) {
                            @Override
                            public void convert(ViewHolder helper, SpendBean item) {
                                double spend = item.getLiveSpend();
                                String liveSpend;
                                if (spend > 10000) {
                                    liveSpend = spend / 10000 + "万元";
                                } else {
                                    liveSpend = spend + "元";
                                }
                                helper.setText(R.id.tv_data_id, StringUtils.getStringLength(getCount(), helper.getPosition()) + "" + (helper.getPosition() + 1))
                                        .setText(R.id.tv_spend, "消费:" + liveSpend);
                                TextView tv_reminder = helper.getTextView(R.id.tv_spend_date);
                                tv_reminder.setText(DateUtils.dateToString(item.getCreatTime(), "yyyy-MM-dd HH:mm"));
                            }
                        };
                        lv_spend_list.setAdapter(commonAdapter);
                        mView.afterSelectData(spendBeanList);
                    }
                });

            }
        }).start();

    }

    @Override
    public SpendBean copySpend(int position) {
        return spendBeanList.get(position);
    }

    @Override
    public void deleteSpendById(MyDialog.Builder mDialog,final int position) {
        mDialog=new MyDialog.Builder(mContext);
        mDialog.setMessage(getStr(R.string.delete_data));
        mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean flag = DBManager.getInstance(mContext).deleteSpend(spendBeanList.get(position).get_id());
                if(flag){
                    mView.showMsg("删除成功");
                    mView.selectData();
                }else{
                    mView.showMsg("删除失败");
                }
                dialog.dismiss();
            }
        });
        mDialog.setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.create().show();
    }

    @Override
    public void deleteSpendById(MyDialog.Builder mDialog, String[] id) {

    }


}
