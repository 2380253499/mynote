package com.zr.note.ui.main.fragment;

import android.content.res.ColorStateList;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.main.fragment.contract.LeftMenuCon;
import com.zr.note.ui.main.fragment.contract.imp.LeftMenuImp;

import butterknife.BindView;

public class LeftMenuFragment extends BaseFragment<LeftMenuCon.View,LeftMenuCon.Presenter> implements LeftMenuCon.View {
    @BindView(R.id.nav_container)
    NavigationView nav_container;

    private TextView tv_leftmenu_qq;
    private ImageView civ_head;
    private View headerView;

    @Override
    protected LeftMenuImp initPresenter() {
        return new LeftMenuImp(getActivity());
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_left_menu;
    }
    @Override
    protected void initView() {
        headerView = nav_container.getHeaderView(0);
        civ_head= (ImageView) headerView.findViewById(R.id.civ_head);
        Glide.with(getActivity()).load(R.drawable.bird).into(civ_head);
        tv_leftmenu_qq= (TextView) headerView.findViewById(R.id.tv_leftmenu_qq);
        tv_leftmenu_qq.setOnClickListener(this);
        nav_container.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mPresenter.itemClick(item.getItemId());
                return false;
            }
        });
        nav_container.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorText)));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_leftmenu_qq:
                PhoneUtils.copyText(getActivity(),"271910854");
                showToastS("复制成功");
                break;
        }
    }
}
