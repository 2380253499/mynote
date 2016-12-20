package com.fast.note.ui.main.fragment.contract;

import com.fast.note.base.BasePresenter;
import com.fast.note.base.BaseView;
import com.fast.note.ui.main.entity.MemoBean;

/**
 * Created by Administrator on 2016/10/19.
 */
public interface AddMemoCon {
    interface View extends BaseView{

    }
    interface Presenter extends BasePresenter<View>{
        boolean addMemo(MemoBean bean);
    }
}
