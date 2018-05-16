package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.tools.ClickUtils;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.gesture.activity.GestureUpdateActivity;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.entity.AccountBean;
import com.zr.note.ui.main.entity.JokeBean;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.main.entity.SpendBean;
import com.zr.note.ui.main.fragment.contract.LeftMenuCon;
import com.github.rxjava.rxbus.MySubscriber;
import com.github.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/28.
 */
public class LeftMenuImp extends IPresenter<LeftMenuCon.View> implements LeftMenuCon.Presenter {
    private final String rootFileName="amynote";
    public LeftMenuImp(Context context) {
        super(context);
    }

    @Override
    public void itemClick(int itemId) {
        if (!ClickUtils.isFastClickById(itemId)) {
            switch (itemId) {
                case R.id.update_pwd:
                    mView.STActivity(GestureUpdateActivity.class);
                    break;
                case R.id.update_import:
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
                    exportDialog.setMessage("是否导出数据->手机根目录"+rootFileName+"文件夹下?");
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
    }

    private void exportData() {
        mView.showLoading();
        Subscription subscribe = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                File exportFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+rootFileName);
                File exportFileName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+rootFileName+"/" + DBManager.getNewInstance(mContext).getDBName() + ".temp");
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
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new EventCallback<String>() {
                    @Override
                    public void onMyNext(String obj) {
                        mView.hideLoading();
                        mView.showMsg("暂无数据文件");
                    }

                    @Override
                    public void onResult(boolean isCompleted, Throwable e) {
                        mView.hideLoading();
                        if (isCompleted) {
                            mView.showMsg("导出成功");
                        } else {
                            mView.showMsg("导出失败");
                        }
                    }
                });
        addSubscription(subscribe);
    }

    private void importData() {
        mView.showLoading();
        Subscription subscribe1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                File file = new File("/data/data/" + mContext.getPackageName() + "/databases");
                File backupFileForTemp = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+rootFileName+"/" + DBManager.getNewInstance(mContext).getDBName() + ".temp");
                File backupFileForDB = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+rootFileName+"/" + DBManager.getNewInstance(mContext).getDBName() + ".db");
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
                        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<Integer> dataSizeList = new ArrayList<Integer>();
                        List<AccountBean> accountList = DBManager.getNewInstance(mContext).selectAccount(null, true, database);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<MemoBean> memoList = DBManager.getNewInstance(mContext).selectMemo(null, true, database);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<JokeBean> jokeList = DBManager.getNewInstance(mContext).selectJoke(null, true, database);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<SpendBean> spendList = DBManager.getNewInstance(mContext).selectSpend(true, database);
                        database = SQLiteDatabase.openOrCreateDatabase(backupFileForDB, null);
                        List<MemoBean> secretList = DBManager.getNewInstance(mContext).selectSecret(database);
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
                            if (i < secretList.size()) {
                                DBManager.getNewInstance(mContext).addSecret(secretList.get(i));
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
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new EventCallback<String>() {
                    @Override
                    public void onMyNext(String obj) {
                        mView.hideLoading();
                        MyDialog.Builder dialog = new MyDialog.Builder(mContext);
                        dialog.setMessage("文件不存在,请把需要复制的文件放在手机根部目录(非SD卡)下的"+rootFileName+"文件夹下,文件名为" + DBManager.getNewInstance(mContext).getDBName() + ".temp");
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
                    public void onResult(boolean isCompleted, Throwable e) {
                        mView.hideLoading();
                        if (isCompleted) {
                            mView.showMsg("导入成功");
                            sendSelectDataBroadcast(BroFilter.addData_account,BroFilter.index_0);
                            sendSelectDataBroadcast(BroFilter.addData_memo,BroFilter.index_1);
                            sendSelectDataBroadcast(BroFilter.addData_joke,BroFilter.index_2);
                            sendSelectDataBroadcast(BroFilter.addData_spend,BroFilter.index_3);
                        } else {
                            mView.showMsg("导入失败");
                        }
                    }
                });
        addSubscription(subscribe1);
    }

    private void sendSelectDataBroadcast(String action,int index) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(BroFilter.isAddData, true);
        intent.putExtra(BroFilter.isAddData_index, index);
        mContext.sendBroadcast(intent);
    }
}
