package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.main.entity.SpendBean;
import com.zr.note.ui.main.fragment.adapter.MySpendHolder;
import com.zr.note.ui.main.fragment.contract.SpendCon;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SpendImp extends IPresenter<SpendCon.View> implements SpendCon.Presenter {
    private List<SpendBean>spendBeanList;
    private AndroidTreeView tView;

    public SpendImp(Context context) {
        super(context);
    }

    @Override
    public void selectData(final ListView lv_spend_list,final boolean isOrderByCreateTime) {
        mView.showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MySpendHolder.IconTreeItem nodeItem;
                //根据年份分组获取年份以及每年的消费总数
                List<SpendBean> yearList = DBManager.getInstance(mContext).selectSpendByYear();
                final TreeNode[]yearArr=new TreeNode[yearList.size()];
                for (int i = 0; i < yearList.size(); i++) {
                    SpendBean bean = yearList.get(i);
                    int localYear = bean.getLocalYear();

                    nodeItem = new MySpendHolder.IconTreeItem();
                    nodeItem.date=localYear;
                    nodeItem.totalSpend=bean.getTotalSpend();
                    nodeItem.remark ="年";
                    TreeNode yearNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(mContext));

                    yearArr[i]=yearNode;
                    //根据月份分组获取月份以及每月的消费总数
                    List<SpendBean> monthList = DBManager.getInstance(mContext).selectSpendByMonth(localYear);
                    TreeNode[]monthArr=new TreeNode[monthList.size()];
                    for (int j = 0; j < monthList.size(); j++) {
                        SpendBean monthBean = monthList.get(j);
                        int localMonth = monthBean.getLocalMonth();

                        nodeItem = new MySpendHolder.IconTreeItem();
                        nodeItem.date=localMonth;
                        nodeItem.totalSpend=monthBean.getTotalSpend();
                        nodeItem.remark ="月";
                        TreeNode monthNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(mContext));

                        monthArr[j]=monthNode;
                        //根据天数分组获取日期以及每天的消费总数
                        List<SpendBean> dayList = DBManager.getInstance(mContext).selectSpendByDay(localYear, localMonth);

                        TreeNode[]dayArr=new TreeNode[dayList.size()];
                        for (int k = 0; k <dayList.size();k++) {
                            SpendBean dayBean = dayList.get(k);
                            int localDay = dayBean.getLocalDay();

                            nodeItem = new MySpendHolder.IconTreeItem();
                            nodeItem.date=localDay;
                            nodeItem.totalSpend=dayBean.getTotalSpend();
                            nodeItem.remark ="日";
                            TreeNode dayNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(mContext));

                            dayArr[k]=dayNode;
                            List<SpendBean> hourList = DBManager.getInstance(mContext).selectSpendByOneDay(localYear, localMonth, localDay);
                            TreeNode[]hourArr=new TreeNode[hourList.size()];
                            for (int l = 0; l <hourList.size(); l++) {
                                SpendBean sBean = hourList.get(l);
                                nodeItem = new MySpendHolder.IconTreeItem();
                                nodeItem.dateFormat=sBean.getCreatTime();
                                nodeItem.totalSpend=sBean.getLiveSpend();
                                nodeItem.isLast=true;
                                nodeItem.spendBean=sBean;
                                TreeNode hourNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(mContext));
                                hourArr[l]=hourNode;
                            }
                            dayArr[k].addChildren(hourArr);
                        }
                        monthArr[j].addChildren(dayArr);
                    }
                    yearArr[i].addChildren(monthArr);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideLoading();

                        TreeNode root = TreeNode.root();
                        root.addChildren(yearArr);
                        tView = new AndroidTreeView(mContext, root);
                        tView.setDefaultAnimation(true);
                        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
                        tView.setDefaultViewHolder(MySpendHolder.class);
                        tView.setDefaultNodeClickListener(nodeClickListener);
                        tView.setDefaultNodeLongClickListener(nodeLongClickListener);

                        mView.afterSelectData(yearArr, tView);
                    }
                });
            }
        }).start();
    }
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {

            MySpendHolder.IconTreeItem item = (MySpendHolder.IconTreeItem) value;
            if(item.isLast){
                mView.startToEditSpend(item);
            }
        }
    };
    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
//            final MySpendHolder.IconTreeItem item = (MySpendHolder.IconTreeItem) value;

            return true;
        }
    };
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
