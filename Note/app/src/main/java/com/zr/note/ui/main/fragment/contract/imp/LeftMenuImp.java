package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;

import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.tools.ClickUtils;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.gesture.activity.GestureUpdateActivity;
import com.zr.note.ui.main.fragment.contract.LeftMenuCon;
import com.zr.utils.FileUtils;

import java.io.File;

/**
 * Created by Administrator on 2016/10/28.
 */
public class LeftMenuImp extends IPresenter<LeftMenuCon.View> implements LeftMenuCon.Presenter{
    public LeftMenuImp(Context context) {
        super(context);
    }

    @Override
    public void itemClick(int itemId) {
        if (!ClickUtils.isFastClickById(itemId)){
            switch (itemId){
                case R.id.update_pwd:
                    mView.STActivity(GestureUpdateActivity.class);
                    break;
                case R.id.update_importd:
                    File file = new File("/data/data/" + mContext.getPackageName() + "/databases");
                    File oldFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/mynote/"+ DBManager.getNewInstance(mContext).getDBName()+".temp");
                    File dbfile = new File("/data/data/" + mContext.getPackageName() + "/databases/"+ DBManager.getNewInstance(mContext).getDBName());
                    if(!oldFile.exists()){
                        MyDialog.Builder dialog=new MyDialog.Builder(mContext);
                        dialog.setMessage("文件不存在,请把需要复制的文件放在手机根部目录(非SD卡)下的mynote文件夹下,文件名为" + DBManager.getNewInstance(mContext).getDBName());
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
                        return;
                    }
                    if (!dbfile.exists()) {
                        file.mkdirs();
                        FileUtils.copyFile(oldFile.getPath(), dbfile.getPath());
                    }else{

                    }
                    break;
                case R.id.update_export:
                    String absolutePath1 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File exportFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/mynote");
                    File exportFileName = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/mynote/"+ DBManager.getNewInstance(mContext).getDBName()+".temp");
                    File dbFile = new File("/data/data/" + mContext.getPackageName() + "/databases/"+ DBManager.getNewInstance(mContext).getDBName());
                    if (dbFile.exists()) {
                        if(!exportFileName.exists()){
                            exportFile.mkdirs();
                        }
                        FileUtils.copyFile(dbFile.getPath(), exportFileName.getPath());
                    }else{
                        mView.showMsg("暂无数据文件");
                    }

                    break;
            }
        }
    }
}
