package com.newnote.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.androidtools.ClickUtils;
import com.github.baseclass.BasePresenter;
import com.github.baseclass.fragment.IBaseFragment;
import com.github.baseclass.rx.RxBus;
import com.newnote.database.DBManager;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseFragment <P extends BasePresenter> extends IBaseFragment implements View.OnClickListener {
    protected P mPresenter;
    protected abstract P initPresenter();
    /************************************************************/
    protected int pageNum=2;
    protected int pageSize=DBManager.pageSize;

    private boolean isFirstLoadData=true;
    private boolean isPrepared;
    /************************************************/
    protected abstract int getContentView();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void onViewClick(View v);
    protected void initRxBus(){};
    protected Unbinder mUnBind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container, false);
        mUnBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mPresenter= initPresenter();
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<P> clazz = (Class<P>) pt.getActualTypeArguments()[0];
// 通过反射创建model的实例
        try {
            mPresenter= clazz.newInstance();
//            clazz.getConstructor()
        } catch (Exception e) {
//            mPresenter= initPresenter();
            e.printStackTrace();
        }
        if(mPresenter!=null){
            mPresenter.attach(this);
        }
        initView();
        initRxBus();
        isPrepared=true;
        setUserVisibleHint(true);
    }
    @Override
    public void onClick(View v) {
        if(!ClickUtils.isFastClick(v)){
            onViewClick(v);
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isFirstLoadData&&isPrepared&&getUserVisibleHint()&&isVisibleToUser){
            initData();
            isFirstLoadData=false;
        }
    }
    protected String getSStr(View view){
        if(view instanceof TextView){
            return ((TextView)view).getText().toString();
        } else if (view instanceof EditText) {
            return ((EditText)view).getText().toString();
        }else{
            return null;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.detach();
        }
        mUnBind.unbind();
        RxBus.getInstance().removeAllStickyEvents();
    }
    protected boolean isEmpty(List list){
        return list == null || list.size() == 0;
    }
    protected boolean notEmpty(List list){
        return !(list == null || list.size() == 0);
    }
}
