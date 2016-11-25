package com.zr.note.ui.main.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.constant.RxTag;
import com.zr.note.ui.main.activity.AddDataActivity;
import com.zr.note.ui.main.broadcast.AddJokeDataBro;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.entity.JokeBean;
import com.zr.note.ui.main.fragment.contract.JokeCon;
import com.zr.note.ui.main.fragment.contract.imp.JokeImp;
import com.zr.note.ui.main.inter.AddDataInter;
import com.zr.note.ui.main.inter.DateInter;
import com.zr.note.view.MyPopupwindow;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JokeFragment extends BaseFragment<JokeCon.View,JokeCon.Presenter> implements JokeCon.View, DateInter.dataManageInter {
    @BindView(R.id.et_search_joke)
    EditText et_search_joke;
    @BindView(R.id.lv_joke_list)
    ListView lv_joke_list;
    private JokeBean jokeBean;
    private AddJokeDataBro addDataBro;
    private boolean isCreateTime=true;

    public static JokeFragment newInstance() {
        Bundle args = new Bundle();
        JokeFragment fragment = new JokeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addDataBro = new AddJokeDataBro(new AddDataInter.AddDataFinish() {
            @Override
            public void addDataFinish() {
                selectData();
            }
        });
        getActivity().registerReceiver(addDataBro, new IntentFilter(BroFilter.addData_joke));
    }
    @Override
    protected JokeImp initPresenter() {
        return new JokeImp(getActivity());
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_joke;
    }

    @Override
    protected void initView() {
        View menu = LayoutInflater.from(getActivity()).inflate(R.layout.popu_joke_menu, null);
        TextView tv_menu_copyJokeContent= (TextView) menu.findViewById(R.id.tv_menu_copyJokeContent);
        TextView tv_menu_deleteJoke= (TextView) menu.findViewById(R.id.tv_menu_deleteJoke);
        tv_menu_copyJokeContent.setOnClickListener(this);
        tv_menu_deleteJoke.setOnClickListener(this);
        mPopupwindow = new MyPopupwindow(getActivity(), menu);
        lv_joke_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                jokeBean = mPresenter.copyJoke(position);
                mPopupwindow.showAsDropDown(view, PhoneUtils.getPhoneWidth(getActivity()) / 2 - PhoneUtils.dip2px(getActivity(), 55), -PhoneUtils.dip2px(getActivity(), 80));
                return true;
            }
        });
        lv_joke_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JokeBean jokeBean = (JokeBean) parent.getItemAtPosition(position);
                mIntent.putExtra(IntentParam.tabIndex, 2);
                mIntent.putExtra(IntentParam.editJokeBean, jokeBean);
                STActivity(mIntent, AddDataActivity.class);
            }
        });
        et_search_joke.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.searchJoke(s.toString().replace(" ", ""));
                mPresenter.cancelCheckAll(isCreateTime);
            }
        });
    }

    @Override
    protected void initData() {
        selectData();
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_menu_copyJokeContent:
                mPopupwindow.dismiss();
                String jokeContent = jokeBean.getDataContent();
                if (!TextUtils.isEmpty(jokeContent)) {
                    PhoneUtils.copyText(getActivity(),jokeContent);
                    showToastS("复制成功");
                }
                break;
            case R.id.tv_menu_deleteJoke:
                mPopupwindow.dismiss();
                mPresenter.deleteJokeById(mDialog,jokeBean.get_id());
            break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate AddMemoFragment fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void selectData() {
        List<JokeBean> list = mPresenter.selectData(lv_joke_list, isCreateTime);
        if(list!=null&&list.size()>0){
            et_search_joke.setVisibility(View.VISIBLE);
        }else{
            et_search_joke.setVisibility(View.GONE);
        }
    }
    @Override
    public void hiddenSearch(boolean isDeleteAll) {
        if(isDeleteAll){
            et_search_joke.setVisibility(View.GONE);
        }
    }
    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(addDataBro);
        super.onDestroy();
    }

    @Override
    public void orderByCreateTime(boolean isCreateTime) {
        if(this.isCreateTime!=isCreateTime){
            this.isCreateTime = isCreateTime;
            selectData();
        }
    }
    //开始批量选择
    @Subscribe(tags = @Tag(RxTag.dataBatchSelect_joke))
    public void dataBatchSelect(Integer index){
        boolean notEmpty = mPresenter.dataBatchCheckNotEmpty();
        RxBus.get().post(RxTag.notEmpty, notEmpty);
    }
    //取消批量选择
    @Subscribe(tags = @Tag(RxTag.endDataBatchSelect_joke))
    public void endDataBatchSelect(Integer index){
        mPresenter.endDataBatchSelect();
    }
    //true全选  false取消全选
    @Subscribe(tags = @Tag(RxTag.dataCheckAll_joke))
    public void dataCheckAll_joke(Boolean isCheckAll){
        if(isCheckAll){
            mPresenter.checkAll(isCreateTime);
        }else{
            mPresenter.cancelCheckAll(isCreateTime);
        }
    }
    //开始删除
    @Subscribe(tags = @Tag(RxTag.deleteAll_joke))
    public void deleteAll_2(Integer index){
        mPresenter.deleteAll();
    }

}
