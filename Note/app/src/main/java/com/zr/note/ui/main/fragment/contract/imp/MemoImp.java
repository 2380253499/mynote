package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListView;

import com.hwangjr.rxbus.RxBus;
import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.tools.LogUtils;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.constant.RxTag;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.main.fragment.adapter.MemoAdapter;
import com.zr.note.ui.main.fragment.contract.MemoCon;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public class MemoImp extends IPresenter<MemoCon.View> implements MemoCon.Presenter{
    private List<MemoBean>memoBeanList;
    private MemoAdapter memoAdapter;
    private boolean isOrderByCreateTime;
    public MemoImp(Context context) {
        super(context);
    }

    @Override
    public List<MemoBean> selectData(ListView lv_memo_list, boolean isOrderByCreateTime) {
        this.isOrderByCreateTime=isOrderByCreateTime;
        memoBeanList= DBManager.getInstance(mContext).selectMemo(isOrderByCreateTime);
        memoAdapter = new MemoAdapter(mContext, memoBeanList, R.layout.item_memo);
        lv_memo_list.setAdapter(memoAdapter);
        return memoBeanList;
    }

    @Override
    public MemoBean copyMemo(int position) {
        return memoBeanList.get(position);
    }

    @Override
    public void deleteMemoById(MyDialog.Builder mDialog,final int id) {
        mDialog=new MyDialog.Builder(mContext);
        mDialog.setMessage(getStr(R.string.delete_data));
        mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean flag = DBManager.getInstance(mContext).deleteMemo(id);
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

    @Deprecated
    public void deleteMemoById(MyDialog.Builder mDialog,final String[] id) {
        mDialog=new MyDialog.Builder(mContext);
        mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean flag = DBManager.getInstance(mContext).deleteAccount(id);
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
    public boolean dataBatchCheckNotEmpty() {
        if(memoAdapter.getCount()>0){
            memoAdapter.setCheck();
            memoAdapter.notifyDataSetChanged();
            return true;
        }else{
            mView.showMsg("暂无数据");
            return false;
        }
    }

    @Override
    public void endDataBatchSelect() {
        memoAdapter.setCheck(false);
        memoAdapter.notifyDataSetChanged();
    }

    @Override
    public void checkAll(boolean isOrderByCreateTime) {
        memoAdapter.checkAll(isOrderByCreateTime);
        memoAdapter.notifyDataSetChanged();
    }

    @Override
    public void cancelCheckAll(boolean isOrderByCreateTime) {
        memoAdapter.cancelCheckAll(isOrderByCreateTime);
        memoAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteAll() {
        if(memoAdapter.getData_id()!=null&&memoAdapter.getData_id().size()>0){
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
                            List<Integer> data_id = memoAdapter.getData_id();
                            final boolean isDeleteAll=data_id.size()==memoAdapter.getCount();
                            for (int i = 0; i < data_id.size(); i++) {
                                LogUtils.Log("====" + data_id.get(i) + "============");
//                                DBManager.getInstance(mContext).deleteMemo(data_id.get(i));
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mView.hideLoading();
                                    mView.showMsg("删除成功");
                                    memoBeanList = DBManager.getInstance(mContext).selectMemo(isOrderByCreateTime);
                                    memoAdapter.setData(memoBeanList);
                                    memoAdapter.notifyDataSetChanged();
                                    RxBus.get().post(RxTag.dataDeleteAllSuccess,isDeleteAll);
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
}
