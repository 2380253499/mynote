package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.adapter.CommonAdapter;
import com.zr.note.adapter.ViewHolder;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.tools.MyDialog;
import com.zr.note.tools.StringUtils;
import com.zr.note.ui.main.entity.JokeBean;
import com.zr.note.ui.main.fragment.contract.JokeCon;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class JokeImp extends IPresenter<JokeCon.View> implements JokeCon.Presenter{
    private List<JokeBean>jokeBeanList;
    public JokeImp(Context context) {
        super(context);
    }
    @Override
    public List<JokeBean> selectData(ListView lv_joke_list, boolean isOrderByCreateTime) {
        jokeBeanList= DBManager.getInstance(mContext).selectJoke(isOrderByCreateTime);
        CommonAdapter<JokeBean> commonAdapter = new CommonAdapter<JokeBean>(mContext, jokeBeanList, R.layout.item_joke) {
            @Override
            public void convert(ViewHolder helper, JokeBean item) {
                helper.setText(R.id.tv_data_id, StringUtils.getStringLength(getCount(), helper.getPosition()) + "" + (helper.getPosition() + 1))
                        .setText(R.id.tv_joke_content, item.getDataContent());

                TextView tv_reminder = helper.getTextView(R.id.tv_joke_remark);
                if (item.getDataRemark() == null || item.getDataRemark().trim().length() == 0) {
                    tv_reminder.setVisibility(View.GONE);
                } else {
                    tv_reminder.setVisibility(View.VISIBLE);
                    tv_reminder.setText(item.getDataRemark());
                }
            }
        };
        lv_joke_list.setAdapter(commonAdapter);
        return jokeBeanList;
    }

    @Override
    public JokeBean copyJoke(int position) {
        return jokeBeanList.get(position);
    }

    @Override
    public void deleteJokeById(MyDialog.Builder mDialog,final int id) {
        mDialog=new MyDialog.Builder(mContext);
        mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean flag = DBManager.getInstance(mContext).deleteJoke(id);
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
    public void deleteJokeById(MyDialog.Builder mDialog, String[] id) {

    }
}
