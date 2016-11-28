package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListView;

import com.hwangjr.rxbus.RxBus;
import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.constant.RxTag;
import com.zr.note.ui.main.entity.JokeBean;
import com.zr.note.ui.main.fragment.adapter.JokeAdapter;
import com.zr.note.ui.main.fragment.contract.JokeCon;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class JokeImp extends IPresenter<JokeCon.View> implements JokeCon.Presenter{
    private List<JokeBean>jokeBeanList;
    private JokeAdapter jokeAdapter;
    private boolean isOrderByCreateTime;
    private String searchInfo;
    public JokeImp(Context context) {
        super(context);
    }
    @Override
    public void selectData(final ListView lv_joke_list,final  boolean isOrderByCreateTime) {
        this.isOrderByCreateTime=isOrderByCreateTime;
        mView.showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                jokeBeanList= DBManager.getInstance(mContext).selectJoke(searchInfo,isOrderByCreateTime);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideLoading();
                        if(jokeAdapter==null){
                            jokeAdapter=new JokeAdapter(mContext, jokeBeanList, R.layout.item_joke);
                            lv_joke_list.setAdapter(jokeAdapter);
                        }else{
                            jokeAdapter.setData(jokeBeanList);
                            jokeAdapter.notifyDataSetChanged();
                        }
                        mView.afterSelectData(jokeBeanList);
                    }
                });
            }
        }).start();

    }

    @Override
    public JokeBean copyJoke(int position) {
        return jokeBeanList.get(position);
    }

    @Override
    public void deleteJokeById(MyDialog.Builder mDialog,final int id) {
        mDialog=new MyDialog.Builder(mContext);
        mDialog.setMessage(getStr(R.string.delete_data));
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

    @Override
    public boolean dataBatchCheckNotEmpty() {
        if(jokeAdapter.getCount()>0){
            jokeAdapter.setCheck();
            jokeAdapter.notifyDataSetChanged();
            return true;
        }else{
            mView.showMsg("暂无数据");
            return false;
        }
    }

    @Override
    public void endDataBatchSelect() {
        jokeAdapter.setCheck(false);
        jokeAdapter.notifyDataSetChanged();
    }

    @Override
    public void checkAll(boolean isOrderByCreateTime) {
        jokeAdapter.checkAll(isOrderByCreateTime);
        jokeAdapter.notifyDataSetChanged();
    }

    @Override
    public void cancelCheckAll(boolean isOrderByCreateTime) {

        jokeAdapter.cancelCheckAll(isOrderByCreateTime);
        jokeAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteAll() {
        if(jokeAdapter.getData_id()!=null&&jokeAdapter.getData_id().size()>0){
            MyDialog.Builder builder=new MyDialog.Builder(mContext);
            builder.setMessage("确定删除吗?");
            builder.setNegativeButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mView.showLoading();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int jokeCount = DBManager.getInstance(mContext).selectTableCount(DBManager.T_Joke_Note);
                            List<Integer> data_id = jokeAdapter.getData_id();
                            final boolean isDeleteAll=data_id.size()==jokeCount;
                            for (int i = 0; i < data_id.size(); i++) {
//                                LogUtils.Log("====" + data_id.get(i) + "============");
                                DBManager.getInstance(mContext).deleteJoke(data_id.get(i));
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mView.hideLoading();
                                    mView.showMsg("删除成功");
                                    jokeBeanList = DBManager.getInstance(mContext).selectJoke(searchInfo,isOrderByCreateTime);
                                    jokeAdapter.setData(jokeBeanList);
                                    if(isDeleteAll){
                                        jokeAdapter.setCheck(false);
                                    }
                                    jokeAdapter.notifyDataSetChanged();
                                    RxBus.get().post(RxTag.dataDeleteAllSuccess,isDeleteAll);
                                    mView.hiddenSearch(isDeleteAll);
                                }
                            });
                        }
                    }).start();
                }
            });
            builder.create().show();
        }else{
            mView.showMsg("请选择需要删除的数据");
        }
    }

    @Override
    public void searchJoke(String info) {
        searchInfo=info;
        RxBus.get().post(RxTag.dataNoSelectAll,RxTag.accountDataIndex);
        jokeBeanList= DBManager.getInstance(mContext).selectJoke(searchInfo, isOrderByCreateTime);
        jokeAdapter.setSearchInfo(searchInfo);
        jokeAdapter.setData(jokeBeanList);
    }
}
