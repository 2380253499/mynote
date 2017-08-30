package com.newnote.module.memo.contract;

import com.github.baseclass.BasePresenter;
import com.github.baseclass.BaseView;
import com.newnote.module.memo.entity.MemoBean;

import java.util.List;

/**
 * Created by administartor on 2017/8/29.
 */

public interface MemoCon {
    interface View extends BaseView{
        void getMemoList(int page, List<MemoBean> item);
    }
    interface Presenter extends BasePresenter<View>{
        void getMemoList(int page,String search,boolean orderByCreateTime);
        void getMemoList(int page,String search,boolean orderByCreateTime,boolean noLoading);
    }
}
