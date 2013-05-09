package com.talentstech.mediaboard.provider;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProviderMetaData
{
    private static final String TAG = "ProviderMetaData";
    
    public static final String AUTHORITY = "ql.android.ads.contentprovider";
    
    public static final String DATABASE_NAME = "advertising.db";
    
    public static final int DATABASE_VERSION = 1;
    
    public static final String DATABASE_DIRECTORY = "database";        
    
    private static Uri getContentURI(String tableName)
    {
        //MLog.d(TAG, "getContentURI");
        return Uri.parse("content://" + AUTHORITY + "/" + tableName);
    }
    
    public static String[] getCurrentFormatDateTime()
    {
        //MLog.d(TAG, "getCurrentFormatDateTime");
        
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd__HH:mm:ss");
        String str = formater.format(date);
        String[] sTime = str.split("__");
        return sTime;
    }
    /* *
     * 播放日志
     */
    public static class PlayLogData implements BaseColumns
    {
        public static final String TABLE_NAME = "playlog";
        
        public static final Uri CONTENT_URI = getContentURI("playlog");
        
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.advertisingprovider.playlog";
        
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.advertisingprovider.playlog";
        
        public static final String COL_Date = "Date";// char(10)
        
        public static final String COL_Time = "Time";// char(8)
        
        public static final String COL_Layout = "Layout";
        
        public static final String COL_ViewType = "ViewTypw";
        
        public static final String COL_FileType = "FileType";// char(8)
        
        public static final String COL_Action = "Action";// char(64)
        
        public static final String COL_Target = "Target";// char(128)
        
        public static final String DEFAULT_SORT_ORDER = "_id";
        
      } 
    /* *
     * http上传下载日志
     */
    public static class FileTransportLogData implements BaseColumns
    {
        public static final String TABLE_NAME = "filetransportlog";
        
        public static final Uri CONTENT_URI = getContentURI("filetransportlog");
        
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.advertisingprovider.filetransportlog";
        
        public static final String CONTENT_TYPE_ITEM =
            "vnd.android.cursor.item/vnd.advertisingprovider.filetransportlog";
        
        public static final String COL_Date = "Date";// char(10)
        
        public static final String COL_Time = "Time";// char(8)
        
        public static final String COL_User = "User";// char(16)
        
        public static final String COL_Cmd = "Cmd";// char(64)
        
        public static final String COL_File = "File";// char(64)
        
        public static final String COL_Remark = "Remark";// char(128)
        
        public static final String DEFAULT_SORT_ORDER = "_id";
    }

    /* *
     * 操控广告机日志
     */
    public static class OperationLogData implements BaseColumns
    {
        public static final String TABLE_NAME = "operationlog";
        
        public static final Uri CONTENT_URI = getContentURI("operationlog");
        
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.advertisingprovider.operationlog";
        
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.advertisingprovider.operationlog";
        
        public static final String COL_Date = "Date";// char(10)
        
        public static final String COL_Time = "Time";// char(8)
        
        public static final String COL_User = "User";// char(16)
        
        public static final String COL_Cmd = "Cmd";// char(64)
        
        public static final String COL_Remark = "Remark";// char(128)
        
        public static final String DEFAULT_SORT_ORDER = "_id";
        
        
    }
    /* *
     * 广告机的操作系统日志
     */
    public static class SystemLogData implements BaseColumns
    {
        public static final String TABLE_NAME = "systemlog";
        
        public static final Uri CONTENT_URI = getContentURI("systemlog");
        
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.advertisingprovider.systemlog";
        
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.advertisingprovider.systemlog";
        
        public static final String COL_Date = "Date";// char(10)
        
        public static final String COL_Time = "Time";// char(8)
        
        public static final String COL_Cmd = "Cmd";// char(64)
        
        public static final String COL_Remark = "Remark";// char(128)
        
        public static final String DEFAULT_SORT_ORDER = "_id";
        
        
    }
    /* *
     * 当前正在下发的播放计划或是插播，同步播，一旦该计划的相关文件下载完毕会自动delete
     */
    public static class DownloadLogData implements BaseColumns
    {
        public static final String TABLE_NAME = "download";
        
        public static final Uri CONTENT_URI = getContentURI("download");
        
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.advertisingprovider.download";
        
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.advertisingprovider.download";
        
        public static final String COL_Date = "Date";
        
        public static final String COL_Time = "Time";
        
        public static final String COL_CmdHead = "CmdHead";
        
        public static final String COL_Instanceid = "Instanceid";
        
        public static final String DEFAULT_SORT_ORDER = "_id";
        
    }
    /* *
     * ftp上传下载文件日志
     */
    public static class FTPDownloadFileLogData implements BaseColumns{
    	
    	public static final String TABLE_NAME = "ftpdownload";
    	
    	public static final Uri CONTENT_URI = getContentURI( "ftpdownload" );
    	
    	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.advertisingprovider.ftpdownlaod";
    	
    	public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.advertisingprovider.ftpdownload";
    	
    	public static final String COL_Date = "Date";
    	
    	public static final String COL_Time = "Time";
    	
    	public static final String COL_AdNo = "AdNo";
    	
    	public static final String COL_File = "file";
    	
    	public static final String COL_PlanID = "PlanId";
    	
    	public static final String DEFAULT_SORT_ORDER = "_id";
    }
    /* *
     * 错误日志
     */
    public static class ErrorLogData implements BaseColumns{
    	
    	public static final String TABLE_NAME = "error";
    	
    	public static final Uri CONTENT_URI = getContentURI( "error" );
    	
    	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.advertisingprovider.error";
    	
    	public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.advertisingprovider.error";
    	
    	public static final String COL_Date = "Date";
    	
    	public static final String COL_Time = "Time";
    	
    	public static final String COL_AdNo = "AdNo";
    	
    	public static final String COL_ErrorId = "ErrorId";
    	
    	public static final String COL_StartTime = "StartTime";
    	
    	public static final String COL_EndTime = "EndTime";
    	
    	public static final String COL_AckTime = "AckTime";
    	
    	public static final String COL_Remark = "remark";
    	
    	public static final String DEFAULT_SORP_ORDER = "_id";
    }
}
