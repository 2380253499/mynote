package com.mynote.module.home.fragment;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.androidtools.AES;
import com.github.androidtools.ClickUtils;
import com.github.androidtools.DateUtils;
import com.github.androidtools.PhoneUtils;
import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.view.MyDialog;
import com.github.utils.FileUtils;
import com.mynote.Constant;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.database.DBManager;
import com.mynote.module.account.bean.AccountBean;
import com.mynote.module.account.dao.imp.AccountImp;
import com.mynote.module.gesture.activity.GestureUpdateActivity;
import com.mynote.module.joke.bean.JokeBean;
import com.mynote.module.joke.dao.imp.JokeImp;
import com.mynote.module.memo.bean.MemoBean;
import com.mynote.module.memo.dao.imp.MemoImp;
import com.mynote.module.secret.activity.SecretActivity;
import com.mynote.module.secret.bean.SecretBean;
import com.mynote.module.secret.dao.imp.SecretImp;
import com.mynote.module.spend.bean.SpendBean;
import com.mynote.module.spend.dao.imp.SpendImp;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class LeftMenuFragment extends BaseFragment {
    @BindView(R.id.nav_container)
    NavigationView nav_container;

    View v_click_view;
    TextView tv_super_pwd;

    private TextView tv_leftmenu_qq;
    private ImageView civ_head;
    private View headerView;
    private int clickNum;
    private int secretNum;
    @Override
    protected int getContentView() {
        return R.layout.fragment_left_menu;
    }
    @Override
    protected void initView() {
        headerView = nav_container.getHeaderView(0);
        civ_head= (ImageView) headerView.findViewById(R.id.civ_head);
        civ_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNum++;
            }
        });
        tv_super_pwd= (TextView) headerView.findViewById(R.id.tv_super_pwd);
        tv_super_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(secretNum==2){
                    tv_super_pwd.setVisibility(View.GONE);

                    STActivity(SecretActivity.class);
                }else if(secretNum<2){
                    secretNum++;
                }
            }
        });
        v_click_view=headerView.findViewById(R.id.v_click_view);
        v_click_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickNum>=5){
                    clickNum=0;
                    tv_super_pwd.setText(getSuperPWD());
                    tv_super_pwd.setVisibility(View.VISIBLE);
                }else{
                    secretNum=0;
                    tv_super_pwd.setVisibility(View.GONE);
                }
            }
        });
        Glide.with(getActivity()).load(R.drawable.bird).into(civ_head);
        tv_leftmenu_qq= (TextView) headerView.findViewById(R.id.tv_leftmenu_qq);
        tv_leftmenu_qq.setOnClickListener(this);
        nav_container.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (!ClickUtils.isFastClickById(item.getItemId())) {
                    switch (item.getItemId()) {
                        case R.id.update_pwd:
                            STActivity(GestureUpdateActivity.class);
                            break;
                        case R.id.update_import:
//                            STActivity(ImportDataActivity.class);
                            MyDialog.Builder dialog=new MyDialog.Builder(mContext);
                            dialog.setMessage("是否导入数据?");
                            dialog.setNegativeButton(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setPositiveButton(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    importData();
                                }
                            });
                            dialog.create().show();
                            break;
                        case R.id.update_export:
                            MyDialog.Builder exportDialog=new MyDialog.Builder(mContext);
                            exportDialog.setMessage("是否导出数据->手机根目录"+ Constant.rootFileName+"文件夹下?");
                            exportDialog.setNegativeButton(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            exportDialog.setPositiveButton(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    exportData();
                                }
                            });
                            exportDialog.create().show();
                            break;
                    }
                }
                return false;
            }
        });
        nav_container.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorText)));
    }

    @Override
    protected void initData() {

    }
    private void importData() {
        showLoading();
        RXStart(new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                File file = new File("/data/data/" + mContext.getPackageName() + "/databases");
                File backupFileForTemp = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+Constant.rootFileName+"/" + DBManager.getNewInstance(mContext).getDBName() + ".temp");
                File backupFileForDB = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+Constant.rootFileName+"/" + DBManager.getNewInstance(mContext).getDBName() + ".db");
                File appDBFile = new File("/data/data/" + mContext.getPackageName() + "/databases/" + DBManager.getNewInstance(mContext).getDBName());
                try {
                    if (!backupFileForTemp.exists()) {
                        subscriber.onNext(null);
                        return;
                    }
                    if (!appDBFile.exists()) {
                        file.mkdirs();
                        FileUtils.copyFile(backupFileForTemp.getPath(), appDBFile.getPath());
                    } else {
                        backupFileForTemp.renameTo(backupFileForDB);
                        List<Integer> dataSizeList = new ArrayList<Integer>();

                        AccountImp accountImp=new AccountImp();
                        accountImp.setContext(mContext);
                        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<AccountBean> accountList = accountImp.selectAccount(0,null, true, database);

                        MemoImp memoImp=new MemoImp();
                        memoImp.setContext(mContext);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<MemoBean> memoList = memoImp.selectMemo(0,null, true, database);

                        JokeImp jokeImp=new JokeImp();
                        jokeImp.setContext(mContext);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<JokeBean> jokeList = jokeImp.selectJoke(0,null, true, database);

                        SpendImp spendImp=new SpendImp();
                        spendImp.setContext(mContext);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<SpendBean> spendList = spendImp.selectSpend(0,true, database);

                        SecretImp secretImp=new SecretImp();
                        secretImp.setContext(mContext);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<SecretBean> secretList = secretImp.selectSecret(0,null, true,database);

                        dataSizeList.add(accountList.size());
                        dataSizeList.add(memoList.size());
                        dataSizeList.add(jokeList.size());
                        dataSizeList.add(spendList.size());
                        dataSizeList.add(secretList.size());
                        Collections.sort(dataSizeList, new Comparator<Integer>() {
                            @Override
                            public int compare(Integer lhs, Integer rhs) {
                                return rhs - lhs;
                            }
                        });
                        //等价于i <= dataSizeList.get(0)-1
                        //i <= memoList.size()-1
                        for (int i = 0; i < dataSizeList.get(0); i++) {
                            if (i < accountList.size()) {
                                AccountBean bean = accountList.get(i);
                                if(accountImp.selectTableCount(DBManager.T_Account_Note,bean.getUid())==0){
                                    accountImp.addAccount(bean);
                                }
                            }
                            if (i < memoList.size()) {
                                MemoBean memoBean = memoList.get(i);
                                if(memoImp.selectTableCount(DBManager.T_Memo_Note,memoBean.getUid())==0){
                                    memoImp.addMemo(memoBean);
                                }
                            }
                            if (i < jokeList.size()) {
                                JokeBean jokeBean = jokeList.get(i);
                                if(jokeImp.selectTableCount(DBManager.T_Joke_Note,jokeBean.getUid())==0){
                                    jokeImp.addJoke(jokeBean);
                                }
                            }
                            if (i < spendList.size()) {
                                SpendBean spendBean = spendList.get(i);
                                if(spendImp.selectTableCount(DBManager.T_Spend_Note,spendBean.getUid())==0){
                                    spendImp.addSpend(spendBean);
                                }
                            }
                            if (i < secretList.size()) {
                                SecretBean secretBean = secretList.get(i);
                                if(secretImp.selectTableCount(DBManager.T_Secret_Note,secretBean.getUid())==0){
                                    secretImp.addSecret(secretBean);
                                }
                            }
                        }
                        if (backupFileForDB.exists()) {
                            backupFileForDB.renameTo(backupFileForTemp);
                        }
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    if (backupFileForDB.exists()) {
                        backupFileForDB.renameTo(backupFileForTemp);
                    }
                    subscriber.onError(e);
                }
            }
            @Override
            public void onMyNext(String s) {
                MyDialog.Builder dialog = new MyDialog.Builder(mContext);
                dialog.setMessage("文件不存在,请把需要复制的文件放在手机根部目录(非SD卡)下的"+ Constant.rootFileName+"文件夹下,文件名为" + DBManager.getNewInstance(mContext).getDBName() + ".temp");
                dialog.setNegativeButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.create().show();
            }
            @Override
            public void onMyCompleted() {
                super.onMyCompleted();
                showMsg("导入成功");
                /*sendSelectDataBroadcast(BroFilter.addData_account,BroFilter.index_0);
                    sendSelectDataBroadcast(BroFilter.addData_memo,BroFilter.index_1);
                    sendSelectDataBroadcast(BroFilter.addData_joke,BroFilter.index_2);
                    sendSelectDataBroadcast(BroFilter.addData_spend,BroFilter.index_3);*/
            }
            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                showMsg("导入失败");
            }
        });

    }

    private void exportData() {
        showLoading();
        RXStart(new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                File exportFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+Constant.rootFileName);
                File exportFileName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+Constant.rootFileName+"/" + DBManager.getNewInstance(mContext).getDBName() + ".temp");
                File dbFile = new File("/data/data/" + mContext.getPackageName() + "/databases/" + DBManager.getNewInstance(mContext).getDBName());
                if (dbFile.exists()) {
                    if (!exportFileName.exists()) {
                        exportFile.mkdirs();
                    }
                    FileUtils.copyFile(dbFile.getPath(), exportFileName.getPath());
                } else {
                    subscriber.onNext(null);
                }
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(String s) {
                showMsg("暂无数据文件");
            }
            @Override
            public void onMyCompleted() {
                super.onMyCompleted();
                showMsg("导出成功");
            }
            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                showMsg("导出失败");
            }
        });

    }
    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.tv_leftmenu_qq:
                PhoneUtils.copyText(getActivity(), "271910854");
                showToastS("复制成功");
                break;
        }
    }

    public String getSuperPWD() {
        String time = DateUtils.dateToString(new Date(), "yyyyMMdd");
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String strHour=hour+"";
        if(hour<10){
            strHour="0"+hour;
        }
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        String strMinute="00";
        if(minute>55){
            strMinute="55";
        }else if(minute>50){
            strMinute="50";
        }else if(minute>45){
            strMinute="45";
        }else if(minute>40){
            strMinute="40";
        }else if(minute>35){
            strMinute="35";
        }else if(minute>30){
            strMinute="30";
        }else if(minute>25){
            strMinute="25";
        }else if(minute>20){
            strMinute="20";
        }else if(minute>15){
            strMinute="15";
        }else if(minute>10){
            strMinute="10";
        }else if(minute>5){
            strMinute="05";
        }else if(minute>=0){
            strMinute="00";
        }
        strMinute=time+""+strHour+""+strMinute+"note";
        String encode = AES.encode(strMinute);
        return encode.substring(0,10);
    }
}
