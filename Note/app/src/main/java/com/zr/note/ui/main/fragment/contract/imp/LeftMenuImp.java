package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;

import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.tools.ClickUtils;
import com.zr.note.ui.gesture.activity.GestureUpdateActivity;
import com.zr.note.ui.main.fragment.contract.LeftMenuCon;

/**
 * Created by Administrator on 2016/10/28.
 */
public class LeftMenuImp extends IPresenter<LeftMenuCon.View> implements LeftMenuCon.Presenter{
    public LeftMenuImp(Context context) {
        super(context);
    }

    @Override
    public void itemClick(int itemId) {
        if (!ClickUtils.isFastClickById(itemId)){
            switch (itemId){
                case R.id.update_pwd:
                    mView.STActivity(GestureUpdateActivity.class);
                    break;
            }
        }
    }
}
