package com.fast.note.ui.main.fragment.contract.imp;

import android.content.Context;

import com.fast.note.base.IPresenter;
import com.fast.note.database.DBManager;
import com.fast.note.ui.main.entity.JokeBean;
import com.fast.note.ui.main.fragment.contract.AddJokeCon;

/**
 * Created by Administrator on 2016/10/24.
 */
public class AddJokeImp extends IPresenter<AddJokeCon.View> implements AddJokeCon.Presenter {
    public AddJokeImp(Context context) {
        super(context);
    }
    @Override
    public boolean addJoke(JokeBean bean) {
        if(DBManager.getNewInstance(mContext).addOrEditJoke(bean)>0){
            mView.showMsg("保存成功");
            return true;
        }else{
            mView.showMsg("保存失败");
            return false;
        }
    }
}
