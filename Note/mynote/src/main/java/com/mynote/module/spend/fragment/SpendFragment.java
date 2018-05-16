package com.mynote.module.spend.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;

import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.base.EventCallback;
import com.mynote.base.IOCallBack;
import com.mynote.event.GetDataEvent;
import com.mynote.module.home.activity.AddDataActivity;
import com.mynote.module.spend.adapter.MySpendHolder;
import com.mynote.module.spend.bean.SpendBean;
import com.mynote.module.spend.dao.imp.SpendImp;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.FlowableEmitter;

public class SpendFragment extends BaseFragment<SpendImp> {
    @BindView(R.id.ll_tree)
    LinearLayout ll_tree;

    private AndroidTreeView tView;
    private SparseBooleanArray itemIsClicked = new SparseBooleanArray();

    public static SpendFragment newInstance() {

        Bundle args = new Bundle();

        SpendFragment fragment = new SpendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_spend;
    }

    @Override
    protected void initView() {
        showProgress();
        getData(1,false);
    }
    @Override
    protected void initRxBus() {
        super.initRxBus();
        getEvent(GetDataEvent.class, new EventCallback<GetDataEvent>() {
            @Override
            public void accept(GetDataEvent event) {
                if(event.index==GetDataEvent.spendIndex){
                    showLoading();
                    getData(1,false);
                }
            }
        });
    }

    @Override
    protected void getData(int page, boolean isLoad) {
        super.getData(page, isLoad);
        itemIsClicked.clear();
        ll_tree.removeAllViews();//再次查询时清除旧视图
        RXStart(pl_load,new IOCallBack<TreeNode[]>() {
            @Override
            public void call(FlowableEmitter<TreeNode[]> subscriber) {
                MySpendHolder.IconTreeItem nodeItem;
                //根据年份分组获取年份以及每年的消费总数
                List<SpendBean> yearList = mDaoImp.selectSpendGroupByYear();
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
                subscriber.onNext(yearArr);
                subscriber.onComplete();
            }
            @Override
            public void onMyNext(TreeNode[] yearArr) {
                TreeNode root = TreeNode.root();
                root.addChildren(yearArr);
                tView = new AndroidTreeView(mContext, root);
                tView.setDefaultAnimation(true);
                tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
                tView.setDefaultViewHolder(MySpendHolder.class);
                tView.setDefaultNodeClickListener(nodeClickListener);
                tView.setDefaultNodeLongClickListener(nodeLongClickListener);

                ll_tree.addView(tView.getView());
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onViewClick(View v) {

    }

    /**
     * 将年月日拼接成一个int做为key
     */
    private String YMD;
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(final TreeNode node, Object value) {
            final MySpendHolder.IconTreeItem item = (MySpendHolder.IconTreeItem) value;

            if (item.isLast) {//如果是最后一级菜单，点击进入编辑页面
                Intent intent=new Intent();
                intent.putExtra(IntentParam.tabIndex, GetDataEvent.spendIndex);
                intent.putExtra(IntentParam.editSpendBean, item.spendBean);
                STActivity(intent, AddDataActivity.class);
            } else {
                final int tag = item.dateTag;
                final int date = item.date;
                switch (tag) {
                    case 0://年
                        if (!itemIsClicked.get(item.spendBean.getLocalYear())) {
                            showLoading();
                            RXStart(new IOCallBack<TreeNode>() {
                                @Override
                                public void call(FlowableEmitter<TreeNode> subscriber) {
                                    List<SpendBean> monthList = mDaoImp.selectSpendGroupByMonth(date);
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
                                        if(monthList.size()>12){
                                            SystemClock.sleep(30);//数据过多treeview无法加载出来
                                        }
                                        subscriber.onNext(monthNode);
                                    }
                                    itemIsClicked.put(item.spendBean.getLocalYear(), true);
                                    subscriber.onComplete();
                                }
                                @Override
                                public void onMyNext(TreeNode treeNode) {
                                    node.getViewHolder().getTreeView().addNode(node, treeNode);
                                }
                            });
                        }

                        break;
                    case 1://月
                        YMD = item.spendBean.getLocalYear() + "" + item.spendBean.getLocalMonth();
                        if (!itemIsClicked.get(Integer.parseInt(YMD))) {
                            showLoading();
                            RXStart(new IOCallBack<TreeNode>() {
                                @Override
                                public void call(FlowableEmitter<TreeNode> subscriber) {
                                    List<SpendBean> dayList = mDaoImp.selectSpendGroupByDay(item.spendBean.getLocalYear(), item.spendBean.getLocalMonth());
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

                                        if(dayList.size()>12){
                                            SystemClock.sleep(30);//数据过多treeview无法加载出来
                                        }
                                        subscriber.onNext(dayNode);

                                    }
                                    itemIsClicked.put(Integer.parseInt(YMD), true);
                                    subscriber.onComplete();
                                }

                                @Override
                                public void onMyNext(TreeNode dayNode) {
                                    node.getViewHolder().getTreeView().addNode(node, dayNode);
                                }
                            });
                        }
                        break;
                    case 2://日
                        YMD = item.spendBean.getLocalYear() + "" + item.spendBean.getLocalMonth() + "" + item.spendBean.getLocalDay();
                        if (!itemIsClicked.get(Integer.parseInt(YMD))) {
                            showLoading();
                            RXStart(new IOCallBack<TreeNode>() {
                                @Override
                                public void call(FlowableEmitter<TreeNode> subscriber) {
                                    List<SpendBean> hourList = mDaoImp.selectSpendByOneDay(item.spendBean.getLocalYear(), item.spendBean.getLocalMonth(), item.spendBean.getLocalDay());
                                    for (int l = 0; l < hourList.size(); l++) {
                                        SpendBean sBean = hourList.get(l);
                                        MySpendHolder.IconTreeItem nodeItem = new MySpendHolder.IconTreeItem();
                                        nodeItem.spendBean = sBean;
                                        nodeItem.dateFormat = new Date(sBean.getCreateTime());
                                        nodeItem.totalSpend = sBean.getLiveSpend();
                                        nodeItem.dateTag = 3;
                                        nodeItem.isLast = true;
                                        nodeItem.spendBean = sBean;
                                        TreeNode hourNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(mContext));
                                        if(hourList.size()>12){
                                            SystemClock.sleep(80);//数据过多treeview无法加载出来
                                        }
//
                                        subscriber.onNext(hourNode);
                                    }
                                    itemIsClicked.put(Integer.parseInt(YMD), true);
                                    subscriber.onComplete();
                                }

                                @Override
                                public void onMyNext(TreeNode hourNode) {
                                    node.getViewHolder().getTreeView().addNode(node, hourNode);
                                }
                            });
                        }
                        break;
                }

            }
        }
    };
    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            return true;
        }
    };
}
