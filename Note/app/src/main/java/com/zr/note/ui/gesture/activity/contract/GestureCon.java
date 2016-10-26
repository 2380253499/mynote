package com.zr.note.ui.gesture.activity.contract;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface GestureCon {
   interface View extends BaseView{

   }

   interface Presenter extends BasePresenter<View>{
   }

}
