package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;
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
import com.zr.rxjava.RxUtils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SpendImp extends IPresenter<SpendCon.View> implements SpendCon.Presenter {
    private List<SpendBean> spendBeanList;
    private AndroidTreeView tView;
    private SparseBooleanArray itemIsClicked = new SparseBooleanArray();

    public SpendImp(Context context) {
        super(context);
    }

    @Override
    public void selectData(final ListView lv_spend_list, final boolean isOrderByCreateTime) {
        mView.showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MySpendHolder.IconTreeItem nodeItem;
                //根据年份分组获取年份以及每年的消费总数
                List<SpendBean> yearList = DBManager.getNewInstance(mContext).selectSpendGroupByYear();
                final TreeNode[] yearArr = new TreeNode[yearList.size()];
                for (int i = 0; i < yearList.size(); i++) {
                    SpendBean bean = yearList.get(i);
                    int localYear = bean.getLocalYear();

                    nodeItem = new MySpendHolder.IconTreeItem();
                    nodeItem.date = localYear;
                    nodeItem.totalSpend = bean.getTotalSpend();
                    nodeItem.spendBean = bean;
                    nodeItem.remark = "年";
                    TreeNode yearNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(mContext));

                    yearArr[i] = yearNode;
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

    /**
     * 将年月日拼接成一个int做为key
     */
    private String YMD;
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(final TreeNode node, Object value) {
            final MySpendHolder.IconTreeItem item = (MySpendHolder.IconTreeItem) value;

            if (item.isLast) {
                mView.startToEditSpend(item);
            } else {
                final int tag = item.dateTag;
                final int date = item.date;
                switch (tag) {
                    case 0://年
                        if (!itemIsClicked.get(item.spendBean.getLocalYear())) {
                            Subscription subscribe = Observable.create(new Observable.OnSubscribe<TreeNode>() {
                                @Override
                                public void call(Subscriber<? super TreeNode> subscriber) {
                                    List<SpendBean> monthList = DBManager.getNewInstance(mContext).selectSpendGroupByMonth(date);
                                    for (int j = 0; j < monthList.size(); j++) {
                                        SpendBean monthBean = monthList.get(j);
                                        int localMonth = monthBean.getLocalMonth();
                                        MySpendHolder.IconTreeItem nodeItem = new MySpendHolder.IconTreeItem();
                                        nodeItem.spendBean = monthBean;
                                        nodeItem.dateTag = 1;
                                        nodeItem.date = localMonth;
                                        nodeItem.totalSpend = monthBean.getTotalSpend();
                                        nodeItem.remark = "月";
                                        TreeNode monthNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(mContext));
                                        subscriber.onNext(monthNode);
                                    }
                                    itemIsClicked.put(item.spendBean.getLocalYear(), true);
                                    subscriber.onCompleted();

                                }
                            })
                                    .asObservable()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new RxUtils.MySubscriber<TreeNode>() {
                                        @Override
                                        public void onMyNext(TreeNode treeNode) {
                                            node.getViewHolder().getTreeView().addNode(node, treeNode);
                                        }

                                        @Override
                                        public void onResult(int tag) {
                                            mView.hideLoading();
                                        }
                                    });
                            mCSubscription.add(subscribe);
                            mView.showLoading();
                        }

                        break;
                    case 1://月
                        YMD = item.spendBean.getLocalYear() + "" + item.spendBean.getLocalMonth();
                        if (!itemIsClicked.get(Integer.parseInt(YMD))) {
                            Subscription subscribe = Observable.create(new Observable.OnSubscribe<TreeNode>() {
                                @Override
                                public void call(Subscriber<? super TreeNode> subscriber) {
                                    List<SpendBean> dayList = DBManager.getNewInstance(mContext).selectSpendGroupByDay(item.spendBean.getLocalYear(), item.spendBean.getLocalMonth());
                                    for (int k = 0; k < dayList.size(); k++) {
                                        SpendBean dayBean = dayList.get(k);
                                        int localDay = dayBean.getLocalDay();
                                        MySpendHolder.IconTreeItem nodeItem = new MySpendHolder.IconTreeItem();
                                        nodeItem.spendBean = dayBean;
                                        nodeItem.date = localDay;
                                        nodeItem.dateTag = 2;
                                        nodeItem.totalSpend = dayBean.getTotalSpend();
                                        nodeItem.remark = "日";
                                        TreeNode dayNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(mContext));
                                        subscriber.onNext(dayNode);

                                    }
                                    itemIsClicked.put(Integer.parseInt(YMD), true);
                                    subscriber.onCompleted();
                                }
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .asObservable()
                                    .subscribe(new RxUtils.MySubscriber<TreeNode>() {
                                        @Override
                                        public void onMyNext(TreeNode dayNode) {
                                            node.getViewHolder().getTreeView().addNode(node, dayNode);
                                        }
                                        @Override
                                        public void onResult(int tag) {
                                            mView.hideLoading();
                                        }
                                    });
                            mCSubscription.add(subscribe);
                            mView.showLoading();
                        }

                        break;
                    case 2://日
                        YMD = item.spendBean.getLocalYear() + "" + item.spendBean.getLocalMonth() + "" + item.spendBean.getLocalDay();
                        if (!itemIsClicked.get(Integer.parseInt(YMD))) {
                            Subscription subscribe = Observable.create(new Observable.OnSubscribe<TreeNode>() {
                                @Override
                                public void call(Subscriber<? super TreeNode> subscriber) {
                                    List<SpendBean> hourList = DBManager.getNewInstance(mContext).selectSpendByOneDay(item.spendBean.getLocalYear(), item.spendBean.getLocalMonth(), item.spendBean.getLocalDay());
                                    for (int l = 0; l < hourList.size(); l++) {
                                        SpendBean sBean = hourList.get(l);
                                        MySpendHolder.IconTreeItem nodeItem = new MySpendHolder.IconTreeItem();
                                        nodeItem.spendBean = sBean;
                                        nodeItem.dateFormat = sBean.getCreatTime();
                                        nodeItem.totalSpend = sBean.getLiveSpend();
                                        nodeItem.dateTag = 3;
                                        nodeItem.isLast = true;
                                        nodeItem.spendBean = sBean;
                                        TreeNode hourNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(mContext));
                                        subscriber.onNext(hourNode);
                                    }
                                    itemIsClicked.put(Integer.parseInt(YMD), true);
                                    subscriber.onCompleted();
                                }
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .asObservable()
                                    .subscribe(new RxUtils.MySubscriber<TreeNode>() {
                                        @Override
                                        public void onMyNext(TreeNode hourNode) {
                                            node.getViewHolder().getTreeView().addNode(node, hourNode);
                                        }
                                        @Override
                                        public void onResult(int tag) {
                                            mView.hideLoading();
                                        }
                                    });
                            mCSubscription.add(subscribe);
                            mView.showLoading();
                        }
                        break;
                }

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
    public void deleteSpendById(MyDialog.Builder mDialog, final int position) {
        mDialog = new MyDialog.Builder(mContext);
        mDialog.setMessage(getStr(R.string.delete_data));
        mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean flag = DBManager.getInstance(mContext).deleteSpend(spendBeanList.get(position).get_id());
                if (flag) {
                    mView.showMsg("删除成功");
                    mView.selectData();
                } else {
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

    @Override
    public void resetItemClickSparse() {
        itemIsClicked.clear();
    }
}
