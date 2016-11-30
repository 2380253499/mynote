package com.zr.note.ui.main.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.zr.note.R;
import com.zr.note.tools.DateUtils;
import com.zr.note.ui.main.entity.SpendBean;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/29.
 */
public class MySpendHolder extends TreeNode.BaseNodeViewHolder<MySpendHolder.IconTreeItem> {

    private ImageView iv_icon;

    public MySpendHolder(Context context) {
        super(context);
    }
    @Override
    public View createNodeView(TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_tree_node, null, false);
        TextView tv_spend_year = (TextView) view.findViewById(R.id.tv_spend_year);
        TextView tv_total_spend = (TextView) view.findViewById(R.id.tv_total_spend);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        iv_icon.setImageResource(R.drawable.arrow_right);
        if(value.isLast){
            iv_icon.setVisibility(View.INVISIBLE);
        }else{
            iv_icon.setVisibility(View.VISIBLE);
        }
        if(value.dateFormat!=null){
            tv_spend_year.setText(DateUtils.dateToString(value.dateFormat, "HH:mm"));
        }else{
            tv_spend_year.setText((value.date<10?"0"+value.date:value.date)+value.remark);
        }
        tv_total_spend.setText("￥"+value.totalSpend+"元");
        return view;
    }

    @Override
    public void toggle(boolean active) {
        super.toggle(active);
        if(active){
            iv_icon.setImageResource(R.drawable.arrow_down);
        }else{
            iv_icon.setImageResource(R.drawable.arrow_right);
        }
    }

    public static class IconTreeItem {
        public int icon;
        public int date;
        public Date dateFormat;
        public String text;
        public double totalSpend;
        public boolean isLast;
        public String remark;
        public SpendBean spendBean;
    }
}
