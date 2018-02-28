package com.mynote.module.secret.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.baseclass.rx.MySubscriber;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.event.GetDataEvent;
import com.mynote.module.secret.dao.imp.SecretImp;

public class SecretFragment extends BaseFragment<SecretImp> {



    public static SecretFragment newInstance() {
        Bundle args = new Bundle();
        SecretFragment fragment = new SecretFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getContentView() {
        return R.layout.fragment_secret;
    }
    @Override
    protected void initView() {

    }


    @Override
    protected void initData() {

    }
    @Override
    protected void initRxBus() {
        super.initRxBus();
        getRxBusEvent(GetDataEvent.class, new MySubscriber<GetDataEvent>() {
            @Override
            public void onMyNext(GetDataEvent event) {
                if(event.index==GetDataEvent.secretIndex){
                    showLoading();
                    getData(1,false);
                }
            }
        });
        /*getRxBusEvent(OptionEvent.class, new MySubscriber<OptionEvent>() {
            @Override
            public void onMyNext(OptionEvent event) {
                if(event.index==GetDataEvent.secretIndex){
                    //0创建时间排序
                    //1修改时间排序
                    //2批量删除
                    switch (event.flag){
                        case OptionEvent.flag_0:
                            isOrderByCreateTime=true;
                            showLoading();
                            getData(1,false);
                            break;
                        case OptionEvent.flag_1:
                            isOrderByCreateTime=false;
                            showLoading();
                            getData(1,false);
                            break;
                        case OptionEvent.flag_prepare_delete:
                            adapter.setEdit(true);
                            adapter.notifyDataSetChanged();
                            break;
                        case OptionEvent.flag_cancel_delete:
                            adapter.setEdit(false);
                            adapter.notifyDataSetChanged();
                            break;
                        case OptionEvent.flag_start_delete:
                            if(isEmpty(adapter.getList())){
                                showMsg("暂无数据可删除");
                                return;
                            }
                            boolean flag=false;
                            List<Integer> list=new ArrayList<>();
                            for (int i = 0; i < adapter.getList().size(); i++) {
                                SecretBean bean = adapter.getList().get(i);
                                if(bean.isCheck()){
                                    flag=true;
                                    list.add(bean.get_id());
                                }
                            }
                            if(flag==false){
                                showMsg("请选择数据");
                                return;
                            }
                            promptForDelete(list);
                            break;
                    }

                }
            }
        });*/
    }


    @Override
    protected void onViewClick(View v) {

    }



}
