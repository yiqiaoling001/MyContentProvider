package com.talentstech.mediaboard.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.talentstech.mediaboard.provider.ProviderMetaData.DownloadLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.ErrorLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.FTPDownloadFileLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.FileTransportLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.OperationLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.PlayLogData;
import com.talentstech.mediaboard.provider.ProviderMetaData.SystemLogData;

import java.util.HashMap;

public class AdvertisingProvider extends ContentProvider{
    private static final String TAG = "AdvertisingProvider";
    
    private static final UriMatcher mUriMatcher = new UriMatcher( UriMatcher.NO_MATCH );
    
    private static final int Play_Log = 1;
    private static final int Play_Log_Id = 2;
    
    private static final int File_Transport_Log = 101;
    private static final int File_Transport_Log_Id = 102;
    
    private static final int Operation_Log = 201;
    private static final int Operation_Log_Id = 202;
    
    private static final int System_Log = 301;
    private static final int System_Log_Id = 302;
    
    private static final int Download_Log = 401;
    private static final int Download_Log_Id = 402;
    
    private static final int FTPDownload_Log = 501;
    private static final int FTPDownload_Log_Id = 502;
    
    private static final int Error_Log = 601;
    private static final int Error_Log_Id = 602;
    
    static{
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + PlayLogData.TABLE_NAME, Play_Log );
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + PlayLogData.TABLE_NAME + "/#", Play_Log_Id );
        
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + FileTransportLogData.TABLE_NAME, File_Transport_Log );
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + FileTransportLogData.TABLE_NAME + "/#", File_Transport_Log_Id );
        
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + OperationLogData.TABLE_NAME, Operation_Log );
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + OperationLogData.TABLE_NAME + "/#", Operation_Log_Id );
        
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + SystemLogData.TABLE_NAME, System_Log );
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + SystemLogData.TABLE_NAME + "/#", System_Log_Id );
        
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + DownloadLogData.TABLE_NAME, Download_Log );
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + DownloadLogData.TABLE_NAME + "/#", Download_Log_Id );
        
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + FTPDownloadFileLogData.TABLE_NAME, FTPDownload_Log );
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + FTPDownloadFileLogData.TABLE_NAME, FTPDownload_Log_Id );
        
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + ErrorLogData.TABLE_NAME, Error_Log );
        mUriMatcher.addURI( ProviderMetaData.AUTHORITY, "/" + ErrorLogData.TABLE_NAME, Error_Log_Id );
     }
    
    private static HashMap<String,String> mPlayLogMap;
    private static HashMap<String,String> mFileTransportLogMap;
    private static HashMap<String,String> mOperationLogMap;
    private static HashMap<String,String> mSystemLogMap;
    private static HashMap<String,String> mDownloadLogMap;
    private static HashMap<String,String> mFTPDownloadMap;
    private static HashMap<String,String> mErrorMap;
    
    static{
        mPlayLogMap = new HashMap<String,String>();
        mPlayLogMap.put( PlayLogData._ID, PlayLogData._ID );
        mPlayLogMap.put( PlayLogData.COL_Date, PlayLogData.COL_Date );
        mPlayLogMap.put( PlayLogData.COL_Time, PlayLogData.COL_Time );
        mPlayLogMap.put( PlayLogData.COL_Layout, PlayLogData.COL_Layout );
        mPlayLogMap.put( PlayLogData.COL_ViewType, PlayLogData.COL_ViewType );
        mPlayLogMap.put( PlayLogData.COL_FileType, PlayLogData.COL_FileType );
        mPlayLogMap.put( PlayLogData.COL_Action, PlayLogData.COL_Action );
        mPlayLogMap.put( PlayLogData.COL_Target, PlayLogData.COL_Target );
        
        mFileTransportLogMap = new HashMap<String,String>();
        mFileTransportLogMap.put( FileTransportLogData._ID, FileTransportLogData._ID );
        mFileTransportLogMap.put( FileTransportLogData.COL_Date, FileTransportLogData.COL_Date );
        mFileTransportLogMap.put( FileTransportLogData.COL_Time, FileTransportLogData.COL_Time );
        mFileTransportLogMap.put( FileTransportLogData.COL_User, FileTransportLogData.COL_User );
        mFileTransportLogMap.put( FileTransportLogData.COL_Cmd, FileTransportLogData.COL_Cmd );
        mFileTransportLogMap.put( FileTransportLogData.COL_File, FileTransportLogData.COL_File );
        mFileTransportLogMap.put( FileTransportLogData.COL_Remark, FileTransportLogData.COL_Remark );
        
        mOperationLogMap = new HashMap<String,String>();
        mOperationLogMap.put( OperationLogData._ID, OperationLogData._ID );
        mOperationLogMap.put( OperationLogData.COL_Date, OperationLogData.COL_Date );
        mOperationLogMap.put( OperationLogData.COL_Time, OperationLogData.COL_Time );
        mOperationLogMap.put( OperationLogData.COL_User, OperationLogData.COL_User );
        mOperationLogMap.put( OperationLogData.COL_Cmd, OperationLogData.COL_Cmd );
        mOperationLogMap.put( OperationLogData.COL_Remark, OperationLogData.COL_Remark );
        
        mSystemLogMap = new HashMap<String,String>();
        mSystemLogMap.put( SystemLogData._ID, SystemLogData._ID );
        mSystemLogMap.put( SystemLogData.COL_Date, SystemLogData.COL_Date );
        mSystemLogMap.put( SystemLogData.COL_Time, SystemLogData.COL_Time );
        mSystemLogMap.put( SystemLogData.COL_Cmd, SystemLogData.COL_Cmd );
        mSystemLogMap.put( SystemLogData.COL_Remark, SystemLogData.COL_Remark );
        
        mDownloadLogMap = new HashMap<String,String>();
        mDownloadLogMap.put( DownloadLogData._ID, DownloadLogData._ID );
        mDownloadLogMap.put( DownloadLogData.COL_Date, DownloadLogData.COL_Date );
        mDownloadLogMap.put( DownloadLogData.COL_Time, DownloadLogData.COL_Time );
        mDownloadLogMap.put( DownloadLogData.COL_CmdHead, DownloadLogData.COL_CmdHead );
        mDownloadLogMap.put( DownloadLogData.COL_Instanceid, DownloadLogData.COL_Instanceid );
        
        mFTPDownloadMap = new HashMap<String,String>();
        mFTPDownloadMap.put( FTPDownloadFileLogData._ID, FTPDownloadFileLogData._ID );
        mFTPDownloadMap.put( FTPDownloadFileLogData.COL_Date, FTPDownloadFileLogData.COL_Date );
        mFTPDownloadMap.put( FTPDownloadFileLogData.COL_Time, FTPDownloadFileLogData.COL_Time );
        mFTPDownloadMap.put( FTPDownloadFileLogData.COL_AdNo, FTPDownloadFileLogData.COL_AdNo );
        mFTPDownloadMap.put( FTPDownloadFileLogData.COL_File, FTPDownloadFileLogData.COL_File );
        mFTPDownloadMap.put( FTPDownloadFileLogData.COL_PlanID, FTPDownloadFileLogData.COL_PlanID );
        
        mErrorMap = new HashMap<String,String>();
        mErrorMap.put( ErrorLogData._ID, ErrorLogData._ID );
        mErrorMap.put( ErrorLogData.COL_Date, ErrorLogData.COL_Date );
        mErrorMap.put( ErrorLogData.COL_Time, ErrorLogData.COL_Time );
        mErrorMap.put( ErrorLogData.COL_AdNo, ErrorLogData.COL_AdNo );
        mErrorMap.put( ErrorLogData.COL_ErrorId, ErrorLogData.COL_ErrorId );
        mErrorMap.put( ErrorLogData.COL_StartTime, ErrorLogData.COL_StartTime );
        mErrorMap.put( ErrorLogData.COL_EndTime, ErrorLogData.COL_EndTime );
        mErrorMap.put( ErrorLogData.COL_AckTime, ErrorLogData.COL_AckTime );
        mErrorMap.put( ErrorLogData.COL_Remark, ErrorLogData.COL_Remark );
    }
    
    private AdvertisingSQLiteOpenHelper mOpenHelper;
    
    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        //return false;
        //MLog.d( TAG, "onCreate" );
        mOpenHelper = new AdvertisingSQLiteOpenHelper( getContext(), ProviderMetaData.DATABASE_NAME, ProviderMetaData.DATABASE_VERSION );
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // TODO Auto-generated method stub
        //MLog.d( TAG, "query" );
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String tableName = uri.getPathSegments().get( 0 );
        String orderBy;
        if( tableName.equals( PlayLogData.TABLE_NAME ) ){
            MLog.d( TAG, "table : " + tableName );
            qb.setTables( PlayLogData.TABLE_NAME );
            qb.setProjectionMap( mPlayLogMap );
            switch( mUriMatcher.match( uri ) ){
                case Play_Log_Id:
                    qb.appendWhere( PlayLogData._ID + "=" + uri.getPathSegments().get( 1 ) );
                    break;
            }
            
            if( TextUtils.isEmpty( sortOrder ) ){
                orderBy = PlayLogData.DEFAULT_SORT_ORDER;
            }else{
                orderBy = sortOrder;
            }
        }else if( tableName.equals( FileTransportLogData.TABLE_NAME ) ){
            MLog.d( TAG, "table : " + tableName );
            qb.setTables( FileTransportLogData.TABLE_NAME );
            qb.setProjectionMap( mFileTransportLogMap );
            switch( mUriMatcher.match( uri ) ){
                case File_Transport_Log_Id:
                    qb.appendWhere( FileTransportLogData._ID + "=" + uri.getPathSegments().get( 1 ) );
                    break;
            }
            
            if( TextUtils.isEmpty( sortOrder ) ){
                orderBy = FileTransportLogData.DEFAULT_SORT_ORDER;
            }else{
                orderBy = sortOrder;
            }
            
        }else if( tableName.equals( OperationLogData.TABLE_NAME ) ){
           // MLog.d( TAG, "table : " + tableName );
            qb.setTables( OperationLogData.TABLE_NAME );
            qb.setProjectionMap( mOperationLogMap );
            switch( mUriMatcher.match( uri ) ){
                case Operation_Log_Id:
                    qb.appendWhere( OperationLogData._ID + "=" + uri.getPathSegments().get( 1 ) );
                    break;
            }
            if( TextUtils.isEmpty( sortOrder ) ){
                orderBy = OperationLogData.DEFAULT_SORT_ORDER;
            }else{
                orderBy = sortOrder;
            }
        }else if( tableName.equals( SystemLogData.TABLE_NAME ) ){
           // MLog.d( TAG, "table : " + tableName );
            qb.setTables( SystemLogData.TABLE_NAME );
            qb.setProjectionMap( mSystemLogMap );
            switch( mUriMatcher.match( uri ) ){
                case System_Log_Id:
                    qb.appendWhere( SystemLogData._ID + "=" + uri.getPathSegments().get( 1 ) );
                    break;
            }
            if( TextUtils.isEmpty( sortOrder ) ){
                orderBy = SystemLogData.DEFAULT_SORT_ORDER;
            }else{
                orderBy = sortOrder;
            }
        }else if( tableName.equals( DownloadLogData.TABLE_NAME ) ){
        	//MLog.d( TAG, "table : " + tableName );
        	qb.setTables( DownloadLogData.TABLE_NAME );
        	qb.setProjectionMap( mDownloadLogMap );
        	switch( mUriMatcher.match( uri ) ){
        		case Download_Log_Id:
        			qb.appendWhere( DownloadLogData._ID + "=" + uri.getPathSegments().get( 1 ) );
        			break;
        	}
        	if( TextUtils.isEmpty( sortOrder ) ){
        		orderBy = DownloadLogData.DEFAULT_SORT_ORDER;
        	}else{
        		orderBy = sortOrder;
        	}
        }else if( tableName.equals( FTPDownloadFileLogData.TABLE_NAME ) ){
        	//MLog.d( TAG, "table : " + tableName );
        	qb.setTables( FTPDownloadFileLogData.TABLE_NAME );
        	qb.setProjectionMap( mFTPDownloadMap );
        	switch( mUriMatcher.match( uri ) ){
        		case FTPDownload_Log_Id:
        			qb.appendWhere( FTPDownloadFileLogData._ID + "=" + uri.getPathSegments().get( 1 ) );
        			break;
        	}
        	if( TextUtils.isEmpty( sortOrder ) ){
        		orderBy = FTPDownloadFileLogData.DEFAULT_SORT_ORDER;
        	}else{
        		orderBy = sortOrder;
        	}
        	
        }else if( tableName.equals( ErrorLogData.TABLE_NAME ) ){
        	//MLog.d( TAG, "table : " + tableName );
        	qb.setTables( ErrorLogData.TABLE_NAME );
        	qb.setProjectionMap( mErrorMap );
        	switch( mUriMatcher.match( uri ) ){
        	    case Error_Log_Id:
        		    qb.appendWhere( ErrorLogData._ID + "=" + uri.getPathSegments().get( 1 ) );
        		    break;
        	}
        	if( TextUtils.isEmpty( sortOrder ) ){
        		
        		orderBy = ErrorLogData.DEFAULT_SORP_ORDER;
        		
        	}else{
        		
        		orderBy = sortOrder;
        	}
        }else{
            //MLog.d( TAG, "table : " + tableName + ",uri error." );
            return null;
        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, orderBy );
        cursor.setNotificationUri( getContext().getContentResolver(), uri );        
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        //return null;
        //MLog.d( TAG, "getType" );
        switch( mUriMatcher.match( uri ) ){
            case Play_Log:
                return PlayLogData.CONTENT_TYPE;
            case Play_Log_Id:
                return PlayLogData.CONTENT_TYPE_ITEM;
                
            case File_Transport_Log:
                return FileTransportLogData.CONTENT_TYPE;
            case File_Transport_Log_Id:
                return FileTransportLogData.CONTENT_TYPE_ITEM;
                
            case Operation_Log:
                return OperationLogData.CONTENT_TYPE;
            case Operation_Log_Id:
                return OperationLogData.CONTENT_TYPE_ITEM;
                
            case System_Log:
                return SystemLogData.CONTENT_TYPE;
            case System_Log_Id:
                return SystemLogData.CONTENT_TYPE_ITEM;
               
            case Download_Log:
            	return DownloadLogData.CONTENT_TYPE;
            case Download_Log_Id:
            	return DownloadLogData.CONTENT_TYPE_ITEM;
            	
            case FTPDownload_Log:
            	return FTPDownloadFileLogData.CONTENT_TYPE;            	
            case FTPDownload_Log_Id:
            	return FTPDownloadFileLogData.CONTENT_TYPE_ITEM;
            	
            case Error_Log:
            	return ErrorLogData.CONTENT_TYPE;
            case Error_Log_Id:
            	return ErrorLogData.CONTENT_TYPE_ITEM;
            	
            default:
                throw new IllegalArgumentException( "Unknown URI : " + uri );
        }
    }

    @Override
    public synchronized Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        //MLog.d( TAG, "insert" );
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();        
        String tableName = uri.getPathSegments().get( 0 );        
        Uri insertedUri;
        if( tableName.equals( PlayLogData.TABLE_NAME ) ){
            //MLog.d( TAG, "table : " + tableName );
            long rowId = db.insert( PlayLogData.TABLE_NAME, null,  values );
            if( rowId > 0 ){
                insertedUri = ContentUris.withAppendedId( PlayLogData.CONTENT_URI, rowId );
                getContext().getContentResolver().notifyChange( insertedUri, null );
                
                db.close();
                return insertedUri;
            }
        }else if( tableName.equals( FileTransportLogData.TABLE_NAME ) ){
            //MLog.d( TAG, "table : " + tableName );
            long rowId = db.insert(FileTransportLogData.TABLE_NAME, null, values );
            if( rowId > 0 ){
                insertedUri = ContentUris.withAppendedId( FileTransportLogData.CONTENT_URI, rowId );
                getContext().getContentResolver().notifyChange( insertedUri, null );
                db.close();
                return insertedUri;
            }
        }else if( tableName.equals( OperationLogData.TABLE_NAME ) ){
            //MLog.d( TAG, "table : " + tableName );
            long rowId = db.insert( OperationLogData.TABLE_NAME, null, values );
            if( rowId > 0 ){
                insertedUri = ContentUris.withAppendedId( OperationLogData.CONTENT_URI, rowId );
                getContext().getContentResolver().notifyChange( insertedUri, null );
                db.close();
                return insertedUri;
            }
            
        }else if( tableName.equals( SystemLogData.TABLE_NAME ) ){
            //MLog.d( TAG, "table : " + tableName );
            long rowId = db.insert( SystemLogData.TABLE_NAME, null, values );
            if( rowId > 0 ){
                insertedUri = ContentUris.withAppendedId( SystemLogData.CONTENT_URI, rowId );
                getContext().getContentResolver().notifyChange(insertedUri, null);
                db.close();
                return insertedUri;
            }
        }else if( tableName.equals( DownloadLogData.TABLE_NAME ) ){
        	//MLog.d( TAG, "table : " + tableName );
        	long rowId = db.insert( DownloadLogData.TABLE_NAME, null, values );
        	if( rowId > 0 ){
        		insertedUri = ContentUris.withAppendedId( DownloadLogData.CONTENT_URI, rowId );
        		getContext().getContentResolver().notifyChange( insertedUri, null );
        		db.close();
        		return insertedUri;
        	}
        	
        }else if( tableName.equals( FTPDownloadFileLogData.TABLE_NAME ) ){
        	//MLog.d( TAG, "table : " + tableName );
        	long rowId = db.insert( FTPDownloadFileLogData.TABLE_NAME, null, values );
        	if( rowId > 0 ){
        		insertedUri = ContentUris.withAppendedId( FTPDownloadFileLogData.CONTENT_URI, rowId );
        		getContext().getContentResolver().notifyChange( insertedUri, null );
        		db.close();
        		return insertedUri;
        	}
        }else if( tableName.equals( ErrorLogData.TABLE_NAME ) ){
        	//MLog.d( TAG, "table : " + tableName );
        	long rowId = db.insert( ErrorLogData.TABLE_NAME, null, values );
        	if( rowId > 0 ){
        		insertedUri = ContentUris.withAppendedId( ErrorLogData.CONTENT_URI, rowId );
        		getContext().getContentResolver().notifyChange( insertedUri, null );
        		db.close();
        		return insertedUri;
        	}
        }else{
            //MLog.d( TAG, "table : " + tableName + ",uri error." );
            db.close();
            return null;
        }        
        //MLog.d( TAG, "Failed to insert" );
        db.close();
        throw new SQLiteException( "Failed to insert row into " + uri );
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        //MLog.d( TAG, "delete" );
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String tableName = uri.getPathSegments().get( 0 );        
        int result = 0;
        
        if( tableName.equals( PlayLogData.TABLE_NAME ) ){
            
            result = db.delete( PlayLogData.TABLE_NAME, selection, selectionArgs );
            
        }else if( tableName.equals( FileTransportLogData.TABLE_NAME ) ){
            
            result = db.delete( FileTransportLogData.TABLE_NAME, selection, selectionArgs );
            
        }else if( tableName.equals( OperationLogData.TABLE_NAME ) ){
            
            result = db.delete(OperationLogData.TABLE_NAME, selection, selectionArgs);
            
        }else if( tableName.equals( SystemLogData.TABLE_NAME ) ){
            
            result  = db.delete(SystemLogData.TABLE_NAME, selection, selectionArgs);
            
        }else if( tableName.equals( DownloadLogData.TABLE_NAME ) ){
        	
        	result = db.delete( DownloadLogData.TABLE_NAME, selection, selectionArgs);
        	
        }else if( tableName.equals( FTPDownloadFileLogData.TABLE_NAME ) ){
        	
        	result = db.delete( FTPDownloadFileLogData.TABLE_NAME, selection, selectionArgs );
        	
        }else if( tableName.equals( ErrorLogData.TABLE_NAME ) ){
        	
        	result = db.delete( ErrorLogData.TABLE_NAME, selection, selectionArgs );
        	
        }else{
            //MLog.d( TAG, "table : " + tableName + ",uri error.");
            return 0;
            
        }
        if( result > 0 ){
            //MLog.d( TAG, "table : " + tableName + ", success delete" );
            getContext().getContentResolver().notifyChange(uri, null);
        }else{
           // MLog.d( TAG, "table : " + tableName + ",failed delete." );            
        }
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        //MLog.d( TAG, "update" );
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String tableName = uri.getPathSegments().get( 0 );
        int result = 0;
        try{
            if( tableName.equals( PlayLogData.TABLE_NAME ) ){
                
                result = db.update(PlayLogData.TABLE_NAME, values, selection, selectionArgs);
                
            }else if( tableName.equals( FileTransportLogData.TABLE_NAME ) ){
                
                result = db.update(FileTransportLogData.TABLE_NAME, values, selection, selectionArgs);
                
            }else if( tableName.equals( OperationLogData.TABLE_NAME ) ){
                
                result = db.update(OperationLogData.TABLE_NAME, values, selection, selectionArgs);
                
            }else if( tableName.equals( SystemLogData.TABLE_NAME ) ){
                
                result = db.update(SystemLogData.TABLE_NAME, values, selection, selectionArgs);
                
            }else if( tableName.equals( DownloadLogData.TABLE_NAME ) ){
            	
            	result = db.update( DownloadLogData.TABLE_NAME, values, selection, selectionArgs );
            	
            }else if( tableName.equals( FTPDownloadFileLogData.TABLE_NAME ) ){
            	
            	result = db.update( FTPDownloadFileLogData.TABLE_NAME, values, selection, selectionArgs);            
            	
            }else if( tableName.equals( ErrorLogData.TABLE_NAME ) ){
            	
            	result = db.update( ErrorLogData.TABLE_NAME, values, selection, selectionArgs );
            	
            }else{
                MLog.d( TAG, "table : " + tableName + ",uri error." );
                return 0;
            }
        }catch( SQLiteException sqlite ){
            //MLog.d( TAG, " update SQLiteException : " + sqlite.getMessage() );
        }
        if( result > 0 ){
            //MLog.d( TAG, "table : " + tableName + ",success update." );
            getContext().getContentResolver().notifyChange( uri, null );
        }else{
            //MLog.d( TAG, "table : " + tableName + ",failed update." );
        }
        return result;
    }
    
    
}
