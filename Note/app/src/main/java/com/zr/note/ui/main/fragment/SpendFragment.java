package com.zr.note.ui.main.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.database.DBManager;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.main.activity.AddDataActivity;
import com.zr.note.ui.main.broadcast.AddSpendDataBro;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.entity.SpendBean;
import com.zr.note.ui.main.fragment.adapter.MySpendHolder;
import com.zr.note.ui.main.fragment.contract.SpendCon;
import com.zr.note.ui.main.fragment.contract.imp.SpendImp;
import com.zr.note.ui.main.inter.AddDataInter;
import com.zr.note.ui.main.inter.DateInter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpendFragment extends BaseFragment<SpendCon.View,SpendCon.Presenter> implements SpendCon.View, DateInter.dataManageInter {
    @BindView(R.id.ll_tree)
    LinearLayout ll_tree;
    @BindView(R.id.lv_spend_list)
    ListView lv_spend_list;
    private SpendBean spendBean;
    private AddSpendDataBro addDataBro;
    private boolean isCreateTime;

    public static SpendFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SpendFragment fragment = new SpendFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addDataBro = new AddSpendDataBro(new AddDataInter.AddDataFinish() {
            @Override
            public void addDataFinish() {
                selectData();
            }
        });
        getActivity().registerReceiver(addDataBro, new IntentFilter(BroFilter.addData_spend));
    }
    @Override
    protected SpendImp initPresenter() {
        return new SpendImp(getActivity());
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_spend;
    }

    @Override
    protected void initView() {
        lv_spend_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.deleteSpendById(mDialog,position);
                return true;
            }
        });
        lv_spend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpendBean spendBean = (SpendBean) parent.getItemAtPosition(position);
                mIntent.putExtra(IntentParam.tabIndex, 3);
                mIntent.putExtra(IntentParam.editSpendBean, spendBean);
                STActivity(mIntent, AddDataActivity.class);
            }
        });
    }

    @Override
    protected void initData() {
        selectData();
    }

    @Override
    protected void viewOnClick(View v) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate AddMemoFragment fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void selectData() {
        ll_tree.removeAllViews();
        MySpendHolder.IconTreeItem nodeItem;
        List<SpendBean> yearList = DBManager.getInstance(getActivity()).selectSpendByYear();
        TreeNode root = TreeNode.root();
        TreeNode[]yearArr=new TreeNode[yearList.size()];
        for (int i = 0; i < yearList.size(); i++) {
            SpendBean bean = yearList.get(i);
            int localYear = bean.getLocalYear();
//            TreeNode yearNode = new TreeNode(localYear);
            nodeItem = new MySpendHolder.IconTreeItem();
            nodeItem.date=localYear;
            nodeItem.totalSpend=bean.getTotalSpend();
            nodeItem.remark ="年";
//            TreeNode yearNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(getActivity()));
            TreeNode yearNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(getActivity()));
            yearArr[i]=yearNode;
            List<SpendBean> monthList = DBManager.getInstance(getActivity()).selectSpendByMonth(localYear);
            TreeNode[]monthArr=new TreeNode[monthList.size()];
            for (int j = 0; j < monthList.size(); j++) {
                SpendBean monthBean = monthList.get(j);
                int localMonth = monthBean.getLocalMonth();
//                TreeNode monthNode = new TreeNode(localMonth);
                nodeItem = new MySpendHolder.IconTreeItem();
                nodeItem.date=localMonth;
                nodeItem.totalSpend=monthBean.getTotalSpend();
                nodeItem.remark ="月";
                TreeNode monthNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(getActivity()));

                monthArr[j]=monthNode;
                List<SpendBean> dayList = DBManager.getInstance(getActivity()).selectSpendByDay(localYear, localMonth);

                    TreeNode[]dayArr=new TreeNode[dayList.size()];
                    for (int k = 0; k <dayList.size();k++) {
                        SpendBean dayBean = dayList.get(k);
                        int localDay = dayBean.getLocalDay();
//                        TreeNode dayNode = new TreeNode(localDay);
                        nodeItem = new MySpendHolder.IconTreeItem();
                        nodeItem.date=localDay;
                        nodeItem.totalSpend=dayBean.getTotalSpend();
                        nodeItem.remark ="日";
                        TreeNode dayNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(getActivity()));
                        dayArr[k]=dayNode;
                        List<SpendBean> hourList = DBManager.getInstance(getActivity()).selectSpendByOneDay(localYear, localMonth, localDay);
                        TreeNode[]hourArr=new TreeNode[hourList.size()];
                        for (int l = 0; l <hourList.size(); l++) {
                            SpendBean bean1 = hourList.get(l);
//                            TreeNode hourNode = new TreeNode(bean1.getCreatTime()+"=="+bean1.getLiveSpend());
                            nodeItem = new MySpendHolder.IconTreeItem();
                            nodeItem.dateFormat=bean1.getCreatTime();
                            nodeItem.totalSpend=bean1.getLiveSpend();
                            nodeItem.isLast=true;
                            TreeNode hourNode = new TreeNode(nodeItem).setViewHolder(new MySpendHolder(getActivity()));
                            hourArr[l]=hourNode;
                        }
                        dayArr[k].addChildren(hourArr);
                    }
                    monthArr[j].addChildren(dayArr);
            }
            yearArr[i].addChildren(monthArr);
        }
        root.addChildren(yearArr);
        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(MySpendHolder.class);
        ll_tree.addView(tView.getView());
    }

    @Override
    public void afterSelectData(List list) {

    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(addDataBro);
        super.onDestroy();
    }

    @Override
    public void orderByCreateTime(boolean isCreateTime) {
        if(this.isCreateTime!=isCreateTime){
            this.isCreateTime = isCreateTime;
            selectData();
        }

    }
}
