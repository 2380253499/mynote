package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.base.customview.MyEditText;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.entity.JokeBean;
import com.zr.note.ui.main.fragment.contract.AddJokeCon;
import com.zr.note.ui.main.fragment.contract.imp.AddJokeImp;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddJokeFragment extends BaseFragment<AddJokeCon.View,AddJokeCon.Presenter> implements AddDataInter,AddJokeCon.View {

    @BindView(R.id.et_joke_remark)
    MyEditText et_joke_remark;
    @BindView(R.id.et_joke_content)
    MyEditText et_joke_content;
    @BindView(R.id.tv_joke_clear)
    TextView tv_joke_clear;
    @BindView(R.id.tv_joke_copy)
    TextView tv_joke_copy;
    @BindView(R.id.tv_joke_lengthprompt)
    TextView tv_joke_lengthprompt;

    private boolean isEdit;
    private JokeBean jokeBean;


    private boolean isPrePareSelectData;

    public static AddJokeFragment newInstance(JokeBean bean) {
        Bundle args = new Bundle();
        if (args!=null){
            args.putSerializable(IntentParam.editJokeBean,bean);
        }
        AddJokeFragment fragment = new AddJokeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static AddJokeFragment newInstance() {
        return newInstance(null);
    }
    @Override
    protected AddJokeImp initPresenter() {
        return new AddJokeImp(getActivity());
    }
    
    @Override
    protected int setContentView() {
        return R.layout.fragment_add_joke;
    }

    @Override
    protected void initView() {
        et_joke_content.requestFocus();
        tv_joke_clear.setOnClickListener(this);
        tv_joke_copy.setOnClickListener(this);

        et_joke_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                tv_joke_lengthprompt.setText("("+length+"/2000)");
            }
        });
    }

    @Override
    protected void initData() {
        jokeBean = (JokeBean) getArguments().getSerializable(IntentParam.editJokeBean);
        if(jokeBean !=null){
            isEdit=true;
            et_joke_remark.setText(jokeBean.getDataRemark());
            et_joke_content.setText(jokeBean.getDataContent());
        }
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_joke_clear:
                et_joke_content.setText("");
            break;
            case R.id.tv_joke_copy:
                String jokeContent = et_joke_content.getText().toString().trim();
                if (TextUtils.isEmpty(jokeContent)) {
                    showToastS("段子内容不能为空");
                } else {
                    showToastS("复制成功");
                }
            break;
        }
    }

    @Override
    public boolean saveData() {
        String jokeContent = et_joke_content.getText().toString().trim();
        if (TextUtils.isEmpty(jokeContent)) {
            showToastS("段子内容不能为空");
        } else {
            String jokeRemark = et_joke_remark.getText().toString().trim();
            JokeBean bean=new JokeBean();
            bean.setDataRemark(jokeRemark);
            bean.setDataContent(jokeContent);
            bean.set_id(isEdit ? jokeBean.get_id() : -1);
            boolean b = mPresenter.addJoke(bean);
            if(b){
                isPrePareSelectData=true;
            }
            return b;
        }
        return false;
    }

    @Override
    public void clearData() {
        et_joke_remark.setText(null);
        et_joke_content.setText(null);

        et_joke_content.requestFocus();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(isPrePareSelectData){
            mIntent.setAction(BroFilter.addData_joke);
            mIntent.putExtra(BroFilter.isAddData, true);
            mIntent.putExtra(BroFilter.isAddData_index,BroFilter.index_2);
            getActivity().sendBroadcast(mIntent);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate AddMemoFragment fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
