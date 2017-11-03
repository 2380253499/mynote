package com.newnote.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.androidtools.ClickUtils;
import com.github.baseclass.IPresenter;
import com.github.baseclass.fragment.IBaseFragment;
import com.github.baseclass.rx.RxBus;
import com.newnote.database.DBManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseFragment <P extends IPresenter> extends IBaseFragment implements View.OnClickListener {
    protected P mPresenter;
    protected P initPresenter(){
        return null;
    };
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
    private void getPresenter(){
        Type genericSuperclass = getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType){
            //参数化类型
            ParameterizedType parameterizedType= (ParameterizedType) genericSuperclass;
            //返回表示此类型实际类型参数的 Type 对象的数组
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            try {
                mPresenter=((Class<P>)actualTypeArguments[0]).newInstance();
            } catch (java.lang.InstantiationException e) {
                mPresenter=null;
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                mPresenter=null;
                e.printStackTrace();
            }
        }else{
            mPresenter=null;
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter();
        if (initPresenter() != null) {
            mPresenter= initPresenter();
        }
        if(mPresenter!=null){
            mPresenter.setContext(mContext);
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
