package com.talentstech.mediaboard.provider;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.talentstech.mediaboard.provider.ProviderMetaData.DownloadLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.ErrorLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.FTPDownloadFileLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.FileTransportLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.OperationLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.PlayLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.SystemLogData;

public class AdvertisingSQLiteOpenHelper extends SQLiteOpenHelper
{
    private static final String TAG = "AdvertisingSQLiteOpenHelper";
    
    private SQLiteDatabase mDatabase = null;
    
    private int mDatabaseVersion;
    
    private boolean mIsInitializing = false;
    
    public AdvertisingSQLiteOpenHelper(Context context, String name, int version)
    {
        super(context, name, null, version);
        
        mDatabaseVersion = version;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        //MLog.d(TAG, "onCreate");
        db.execSQL("create table " + PlayLogData.TABLE_NAME + "(" + PlayLogData._ID + " integer primary key,"
            + PlayLogData.COL_Date + " char(10)," + PlayLogData.COL_Time + " char(8)," + PlayLogData.COL_Layout 
            + " char(64)," + PlayLogData.COL_ViewType + " char(8)," + PlayLogData.COL_FileType
            + " char(8)," + PlayLogData.COL_Action + " char(64)," + PlayLogData.COL_Target + " char(128));");
        db.execSQL("create table " + FileTransportLogData.TABLE_NAME + "(" + FileTransportLogData._ID
            + " integer primary key," + FileTransportLogData.COL_Date + " char(10)," + FileTransportLogData.COL_Time
            + " char(8)," + FileTransportLogData.COL_User + " char(16)," + FileTransportLogData.COL_Cmd + " char(64),"
            + FileTransportLogData.COL_File + " char(64)," + FileTransportLogData.COL_Remark + " char(128));");
        db.execSQL("create table " + OperationLogData.TABLE_NAME + "(" + OperationLogData._ID + " integer primary key,"
            + OperationLogData.COL_Date + " char(10)," + OperationLogData.COL_Time + " char(8),"
            + OperationLogData.COL_User + " char(16)," + OperationLogData.COL_Cmd + " char(64),"
            + OperationLogData.COL_Remark + " char(128));");
        db.execSQL("create table " + SystemLogData.TABLE_NAME + "(" + SystemLogData._ID + " integer primary key,"
            + SystemLogData.COL_Date + " char(10)," + SystemLogData.COL_Time + " char(8)," + SystemLogData.COL_Cmd
            + " char(64)," + SystemLogData.COL_Remark + " char(128));");
        db.execSQL("create table " + DownloadLogData.TABLE_NAME + "(" + DownloadLogData._ID + " integer primary key,"
            + DownloadLogData.COL_Date + " char(10)," + DownloadLogData.COL_Time + " char(8),"
            + DownloadLogData.COL_CmdHead + " integer," + DownloadLogData.COL_Instanceid + " char(64));");
        db.execSQL( "create table " + FTPDownloadFileLogData.TABLE_NAME + "(" + FTPDownloadFileLogData._ID + " integer primary key," 
        	+ FTPDownloadFileLogData.COL_Date + " char(10)," + FTPDownloadFileLogData.COL_Time + " char(8)," 
        	+ FTPDownloadFileLogData.COL_AdNo + " char(8)," + FTPDownloadFileLogData.COL_File + " char(40)," 
        	+ FTPDownloadFileLogData.COL_PlanID + " char(32));" );
        db.execSQL( "create table " + ErrorLogData.TABLE_NAME + "(" + ErrorLogData._ID + " integer primary key," 
        		+ ErrorLogData.COL_Date + " char(10)," + ErrorLogData.COL_Time + " char(8)," 
        		+ ErrorLogData.COL_AdNo + " char(8)," + ErrorLogData.COL_ErrorId + " integer," 
        		+ ErrorLogData.COL_StartTime + " char(8)," + ErrorLogData.COL_EndTime + " char(8)," 
        		+ ErrorLogData.COL_AckTime + " integer," + ErrorLogData.COL_Remark + " char(127));");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub
        //MLog.d(TAG, "onUpgrader");
        
    }
    
    @Override
    public synchronized void close()
    {
        // TODO Auto-generated method stub
        // super.close();
        //MLog.d(TAG, "close");
        
        if (mIsInitializing)
        {
            MLog.d(TAG, "closed during initialization");
            throw new IllegalStateException("Closed during initialization");
        }
        
        if (mDatabase != null && mDatabase.isOpen())
        {
            mDatabase.close();
            mDatabase = null;
        }
    }
    
    @Override
    public synchronized SQLiteDatabase getReadableDatabase()
    {
        //MLog.d(TAG, "getReadableDatabase");
        
        if (mDatabase != null)
        {
            if (!mDatabase.isOpen())
            {
                mDatabase = null;
            }
            else
            {
                return mDatabase;
            }
        }
        
        if (mIsInitializing)
        {
           // MLog.d(TAG, "getReadableDatabase called recursively");
            throw new IllegalStateException("getReadableDatabase called recursively");
        }
        
        try
        {
            return getWritableDatabase();
        }
        catch (SQLiteException sqlite)
        {
            //MLog.d(TAG, "getReadableDatabase ; SQLiteException : " + sqlite.getMessage());
        }
        
        SQLiteDatabase db = null;
        try
        {
            mIsInitializing = true;
            String strFile =
                Environment.getExternalStorageDirectory() + "/" + ProviderMetaData.DATABASE_DIRECTORY + "/"
                    + ProviderMetaData.DATABASE_NAME;
            db = SQLiteDatabase.openDatabase(strFile, null, SQLiteDatabase.OPEN_READONLY);
            
            if (db.getVersion() != mDatabaseVersion)
            {
                //MLog.d(TAG, "can not upgrade read-only database from version " + db.getVersion() + " to "
                    //+ mDatabaseVersion + ": " + strFile);
                throw new SQLiteException("Can not upgrade read-only database from version " + db.getVersion() + " to "
                    + mDatabaseVersion + ": " + strFile);
            }
            onOpen(db);
            mDatabase = db;
            //MLog.d(TAG, "getReadableDatabase end");
            return mDatabase;
        }
        finally
        {
            mIsInitializing = false;
            if (db != null && db != mDatabase)
                db.close();
        }
        
    }
    
    @Override
    public synchronized SQLiteDatabase getWritableDatabase()
    {
        //MLog.d(TAG, "getWritableDatabase");
        
        if (mDatabase != null)
        {
            if (!mDatabase.isOpen())
            {
                mDatabase = null;
            }
            else if (!mDatabase.isReadOnly())
            {
                return mDatabase;
            }
        }
        
        if (mIsInitializing)
        {
            //MLog.d(TAG, "getWritableDatabase called recursively");
            throw new IllegalStateException("getWritableDatabase called recursively");
        }
        
        boolean success = false;
        SQLiteDatabase db = null;
        try
        {
            mIsInitializing = true;
            
            String strPath = Environment.getExternalStorageDirectory() + "/" + ProviderMetaData.DATABASE_DIRECTORY;
            String strFile = strPath + "/" + ProviderMetaData.DATABASE_NAME;
            
            File filePath = new File(strPath);
            if (!filePath.exists())
            {
                filePath.mkdir();
            }
            
            File fileFile = new File(strFile);
            if (!fileFile.exists())
            {
                try
                {
                    fileFile.createNewFile();
                }
                catch (IOException io)
                {
                    //MLog.d(TAG, "IOException ; " + io.getMessage());
                    return null;
                }
            }
            
            db = SQLiteDatabase.openOrCreateDatabase(strFile, null);
            
            int version = db.getVersion();
            if (version != mDatabaseVersion)
            {
                db.beginTransaction();
                try
                {
                    if (version == 0)
                    {
                        //MLog.d(TAG, "version zero");
                        onCreate(db);
                    }
                    else
                    {
                        if (version > mDatabaseVersion)
                        {
                            onDowngrade(db, version, mDatabaseVersion);
                        }
                        else
                        {
                            onUpgrade(db, version, mDatabaseVersion);
                        }
                    }
                    db.setVersion(mDatabaseVersion);
                    db.setTransactionSuccessful();
                }
                finally
                {
                    db.endTransaction();
                }
            }
            onOpen(db);
            success = true;
            //MLog.d(TAG, "getWritableDatabase end");
            return db;
        }
        finally
        {
            mIsInitializing = false;
            if (success)
            {
                if (mDatabase != null)
                {
                    try
                    {
                        mDatabase.close();
                    }
                    catch (Exception right)
                    {
                        //MLog.d(TAG, "Exception : " + right.getMessage());
                    }
                    mDatabase = db;
                }
            }
            else
            {
                if (db != null)
                {
                    db.close();
                }
            }
            
        }
    }
    
}
