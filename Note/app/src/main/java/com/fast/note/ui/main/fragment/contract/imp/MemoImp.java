package com.fast.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListView;

import com.hwangjr.rxbus.RxBus;
import com.fast.note.R;
import com.fast.note.base.IPresenter;
import com.fast.note.database.DBManager;
import com.fast.note.tools.MyDialog;
import com.fast.note.ui.constant.RxTag;
import com.fast.note.ui.main.entity.MemoBean;
import com.fast.note.ui.main.fragment.adapter.MemoAdapter;
import com.fast.note.ui.main.fragment.contract.MemoCon;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public class MemoImp extends IPresenter<MemoCon.View> implements MemoCon.Presenter{
    private List<MemoBean>memoBeanList;
    private MemoAdapter memoAdapter;
    private boolean isOrderByCreateTime;
    private String searchInfo;

    public MemoImp(Context context) {
        super(context);
    }

    @Override
    public void selectData(final ListView lv_memo_list,final  boolean isOrderByCreateTime) {
        this.isOrderByCreateTime=isOrderByCreateTime;
        mView.showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                memoBeanList= DBManager.getInstance(mContext).selectMemo(searchInfo,isOrderByCreateTime);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideLoading();
                        if(memoAdapter==null){
                            memoAdapter = new MemoAdapter(mContext, memoBeanList, R.layout.item_memo);
                            lv_memo_list.setAdapter(memoAdapter);
                        }else{
                            memoAdapter.setData(memoBeanList);
                            memoAdapter.notifyDataSetChanged();
                        }
                        mView.afterSelectData(memoBeanList);
                    }
                });

            }
        }).start();

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
                            int memoCount = DBManager.getInstance(mContext).selectTableCount(DBManager.T_Memo_Note);
                            List<Integer> data_id = memoAdapter.getData_id();
                            final boolean isDeleteAll=data_id.size()==memoCount;
                            for (int i = 0; i < data_id.size(); i++) {
//                                LogUtils.Log("====" + data_id.get(i) + "============");
                                DBManager.getInstance(mContext).deleteMemo(data_id.get(i));
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mView.hideLoading();
                                    mView.showMsg("删除成功");
                                    memoBeanList = DBManager.getInstance(mContext).selectMemo(searchInfo,isOrderByCreateTime);
                                    memoAdapter.setData(memoBeanList);
                                    if(isDeleteAll){
                                        memoAdapter.setCheck(false);//删除全部后，隐藏选择按钮，防止添加数据后继续显示checkbox
                                    }
                                    memoAdapter.notifyDataSetChanged();
                                    RxBus.get().post(RxTag.dataDeleteAllSuccess, isDeleteAll);
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
    public void searchMemo(String info) {
        searchInfo = info;
        RxBus.get().post(RxTag.dataNoSelectAll, 0);
        memoBeanList= DBManager.getInstance(mContext).selectMemo(searchInfo, isOrderByCreateTime);
        memoAdapter.setSearchInfo(searchInfo);
        memoAdapter.setData(memoBeanList);
    }
}
