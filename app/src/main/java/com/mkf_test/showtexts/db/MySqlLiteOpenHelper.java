package com.yuqiang.uchon.lib.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yuqiang.uchon.lib.greendao.bean.table.AbnormalDao;
import com.yuqiang.uchon.lib.greendao.bean.table.AbnormalInterruptDao;
import com.yuqiang.uchon.lib.greendao.bean.table.ClipActionDao;
import com.yuqiang.uchon.lib.greendao.bean.table.ClipActionSnapshotDao;
import com.yuqiang.uchon.lib.greendao.bean.table.ClipDao;
import com.yuqiang.uchon.lib.greendao.bean.table.DaoMaster;
import com.yuqiang.uchon.lib.greendao.bean.table.ExportDao;
import com.yuqiang.uchon.lib.greendao.bean.table.FileExport;
import com.yuqiang.uchon.lib.greendao.bean.table.FileExportDao;
import com.yuqiang.uchon.lib.greendao.bean.table.FilesShareDao;
import com.yuqiang.uchon.lib.greendao.bean.table.HighlightsGenerateDao;
import com.yuqiang.uchon.lib.greendao.bean.table.ImportFileDao;
import com.yuqiang.uchon.lib.greendao.bean.table.LocalFileDao;
import com.yuqiang.uchon.lib.greendao.bean.table.MemberDao;
import com.yuqiang.uchon.lib.greendao.bean.table.SectionShootDao;
import com.yuqiang.uchon.lib.greendao.bean.table.ShootDao;
import com.yuqiang.uchon.lib.greendao.bean.table.ShootPointDao;
import com.yuqiang.uchon.lib.greendao.bean.table.ShortVideoCreateDao;
import com.yuqiang.uchon.lib.greendao.bean.table.ShortVideoDao;
import com.yuqiang.uchon.lib.greendao.bean.table.TMakePointDao;
import com.yuqiang.uchon.lib.greendao.bean.table.TSectionVideoDeleteDao;
import com.yuqiang.uchon.lib.greendao.bean.table.TVideoMakePointDao;
import com.yuqiang.uchon.lib.greendao.bean.table.VideoFileDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by 马凯风 on 2018/1/23.
 */

public class MySqlLiteOpenHelper extends DaoMaster.DevOpenHelper {
    public MySqlLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MySqlLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        DaoMaster.createAllTables(db, true);
        MigrationHelper.getInstance().migrate(db, AbnormalDao.class, ClipDao.class, ClipActionDao.class
                , ClipActionSnapshotDao.class, FilesShareDao.class, HighlightsGenerateDao.class, ImportFileDao.class
                , LocalFileDao.class, MemberDao.class, SectionShootDao.class, ShootDao.class, ShootPointDao.class
                , ShortVideoDao.class, ShortVideoCreateDao.class, VideoFileDao.class, AbnormalInterruptDao.class, ExportDao.class
                , FileExportDao.class, TVideoMakePointDao.class, TMakePointDao.class, TSectionVideoDeleteDao.class);
    }

}
