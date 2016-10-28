package com.zr.note.ui.main.fragment;

import android.content.res.ColorStateList;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.ui.main.fragment.contract.LeftMenuCon;
import com.zr.note.ui.main.fragment.contract.imp.LeftMenuImp;

import butterknife.BindView;

public class LeftMenuFragment extends BaseFragment<LeftMenuCon.View,LeftMenuCon.Presenter> implements LeftMenuCon.View {
    @BindView(R.id.nav_container)
    NavigationView nav_container;
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

    }
}
