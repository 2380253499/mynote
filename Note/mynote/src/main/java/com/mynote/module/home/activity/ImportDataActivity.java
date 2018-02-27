package com.mynote.module.home.activity;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.view.View;

import com.github.baseclass.adapter.BaseRecyclerAdapter;
import com.github.baseclass.adapter.RecyclerViewHolder;
import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.view.MyDialog;
import com.github.customview.MyCheckBox;
import com.github.utils.FileUtils;
import com.library.base.view.MyRecyclerView;
import com.mynote.Constant;
import com.mynote.R;
import com.mynote.base.BaseActivity;
import com.mynote.database.DBManager;
import com.mynote.module.account.bean.AccountBean;
import com.mynote.module.account.dao.imp.AccountImp;
import com.mynote.module.home.bean.TableBean;
import com.mynote.module.joke.bean.JokeBean;
import com.mynote.module.joke.dao.imp.JokeImp;
import com.mynote.module.memo.bean.MemoBean;
import com.mynote.module.memo.dao.imp.MemoImp;
import com.mynote.module.secret.dao.imp.SecretImp;
import com.mynote.module.spend.bean.SpendBean;
import com.mynote.module.spend.dao.imp.SpendImp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/2/26.
 */

public class ImportDataActivity extends BaseActivity {
    @BindView(R.id.rv_table)
    MyRecyclerView rv_table;
    private String[] tableName = new String[]{
            DBManager.T_Account_Note,
            DBManager.T_Memo_Note,
            DBManager.T_Joke_Note,
            DBManager.T_Spend_Note,
            DBManager.T_Secret_Note,
    };
    BaseRecyclerAdapter adapter;

    @Override
    protected int getContentView() {
        setAppTitle("导入数据");
        return R.layout.act_import_data;
    }

    @Override
    protected void initView() {
        List<TableBean> list = new ArrayList<>();
        for (int i = 0; i < tableName.length; i++) {
            if (i != tableName.length - 1) {
                list.add(new TableBean(tableName[i], true));
            } else {
                list.add(new TableBean(tableName[i], false));
            }
        }
        adapter = new BaseRecyclerAdapter<TableBean>(mContext, R.layout.item_import_table) {
            @Override
            public void bindData(RecyclerViewHolder holder, int position, TableBean bean) {
                MyCheckBox rb_table = (MyCheckBox) holder.getView(R.id.rb_table);
                rb_table.setText(bean.getTableName());
                rb_table.setChecked(bean.isCheck());
                rb_table.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bean.setCheck(rb_table.isChecked());
                    }
                });
            }
        };
        adapter.setList(list);
        rv_table.setAdapter(adapter);

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.bt_table_import})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.bt_table_import:
                boolean flag=true;
                for (int i = 0; i < adapter.getList().size(); i++) {
                    TableBean bean = (TableBean) adapter.getList().get(i);
                    if(bean.isCheck()){
                        flag=false;
                        break;
                    }
                }
                if(flag){
                    showMsg("请选择需要导入的数据");
                    return;
                }
                startImportData();
                break;
        }
    }

    private void startImportData() {
        MyDialog.Builder dialog = new MyDialog.Builder(mContext);
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
    }

    private void importData() {
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

                        JokeImp JokeImp=new JokeImp();
                        JokeImp.setContext(mContext);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<JokeBean> jokeList = JokeImp.selectJoke(0,null, true, database);

                        SpendImp spendImp=new SpendImp();
                        spendImp.setContext(mContext);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<SpendBean> spendList = spendImp.selectSpend(0,true, database);

                        SecretImp secretImp=new SecretImp();
                        secretImp.setContext(mContext);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
//                        List<SecretBean> secretList = secretImp.selectSecret(database);

                        dataSizeList.add(accountList.size());
                        dataSizeList.add(memoList.size());
                        dataSizeList.add(jokeList.size());
                        dataSizeList.add(spendList.size());
//                        dataSizeList.add(secretList.size());
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
                                DBManager.getNewInstance(mContext).addAccount(accountList.get(i));
                            }
                            if (i < memoList.size()) {
                                DBManager.getNewInstance(mContext).addMemo(memoList.get(i));
                            }
                            if (i < jokeList.size()) {
                                DBManager.getNewInstance(mContext).addJoke(jokeList.get(i));
                            }
                            if (i < spendList.size()) {
                                DBManager.getNewInstance(mContext).addSpend(spendList.get(i));
                            }
                            /*if (i < secretList.size()) {
                                DBManager.getNewInstance(mContext).addSecret(secretList.get(i));
                            }*/
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
}
