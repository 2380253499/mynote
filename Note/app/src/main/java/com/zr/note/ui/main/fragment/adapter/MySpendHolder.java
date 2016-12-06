package com.zr.note.ui.main.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.zr.note.R;
import com.zr.note.database.DBManager;
import com.zr.note.inter.MyOnClickListener;
import com.zr.note.tools.DateUtils;
import com.zr.note.tools.MyDialog;
import com.zr.note.tools.MyUtils;
import com.zr.note.tools.StringUtils;
import com.zr.note.ui.main.entity.SpendBean;
import com.zr.note.view.Loading;

import java.math.BigDecimal;
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
    public View createNodeView(final TreeNode node,final IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_tree_node, null, false);
        TextView tv_spend_year = (TextView) view.findViewById(R.id.tv_spend_year);
        TextView tv_spend_remark = (TextView) view.findViewById(R.id.tv_spend_remark);
        TextView tv_total_spend = (TextView) view.findViewById(R.id.tv_total_spend);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        ImageView iv_icon_delete = (ImageView) view.findViewById(R.id.iv_icon_delete);
        if(value.isLast){
            iv_icon_delete.setVisibility(View.VISIBLE);
            iv_icon_delete.setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    deleteItem(node, value);
                }
            });
            iv_icon.setVisibility(View.GONE);
            tv_spend_remark.setText(value.spendBean.getDataRemark());
        }else{
            tv_spend_remark.setText(null);
            iv_icon_delete.setVisibility(View.GONE);
            iv_icon.setVisibility(View.VISIBLE);
            iv_icon.setImageResource(R.drawable.arrow_right);
        }
        if(value.dateFormat!=null){
            tv_spend_year.setText(DateUtils.dateToString(value.dateFormat, "HH:mm"));
        }else{
            tv_spend_year.setText((value.date<10?"0"+value.date:value.date)+value.remark);
        }
        String divide = StringUtils.roundForBigDecimal(value.totalSpend)+"";
        tv_total_spend.setText("￥" +divide+ "元");
        return view;
    }

    private void deleteItem(final TreeNode node,final IconTreeItem item) {
        MyDialog.Builder builder=new MyDialog.Builder(context);
        builder.setMessage("是否删除时间点为"+DateUtils.dateToString(item.dateFormat,"HH:mm")+"的消费记录?");
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
                Loading.show(context);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final boolean isSuccess = DBManager.getInstance(context).deleteSpend(item.spendBean.get_id());
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Loading.dismissLoading();
                                if (isSuccess) {
                                    TreeNode.BaseNodeViewHolder dayViewHolder = node.getParent().getViewHolder();
                                    TreeNode.BaseNodeViewHolder monthViewHolder = node.getParent().getParent().getViewHolder();
                                    TreeNode.BaseNodeViewHolder yearVHolder = node.getParent().getParent().getParent().getViewHolder();
                                    TextView dayTotalView =(TextView) dayViewHolder.getView().findViewById(R.id.tv_total_spend);
                                    TextView monthTotalView =(TextView) monthViewHolder.getView().findViewById(R.id.tv_total_spend);
                                    TextView yearTotalView =(TextView) yearVHolder.getView().findViewById(R.id.tv_total_spend);
                                    double dayTotal = Double.parseDouble(dayTotalView.getText().toString().replace("￥","").replace("元",""));
                                    double monthTotal = Double.parseDouble(monthTotalView.getText().toString().replace("￥", "").replace("元", ""));
                                    double yearTotal = Double.parseDouble(yearTotalView.getText().toString().replace("￥", "").replace("元", ""));

                                    dayTotalView.setText("￥"+new BigDecimal((dayTotal - item.totalSpend)).setScale(1, BigDecimal.ROUND_HALF_UP)+"元");
                                    monthTotalView.setText("￥"+new BigDecimal((monthTotal - item.totalSpend)).setScale(1,BigDecimal.ROUND_HALF_UP)+"元");
                                    yearTotalView.setText("￥"+new BigDecimal((yearTotal - item.totalSpend)).setScale(1,BigDecimal.ROUND_HALF_UP)+"元");

                                    MyUtils.showToast(context, "删除成功");
                                    getTreeView().removeNode(node);
                                } else {
                                    MyUtils.showToast(context,"删除失败");
                                }
                            }
                        });
                    }
                }).start();
            }
        });
        builder.create().show();
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
