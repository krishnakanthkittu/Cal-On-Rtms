package cal_on.usbterminal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mitch on 2016-05-13.
 */
public class Customerlistdb extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Custdatabase.db";
    public static final String TABLE_NAME = "Custdetails";
    public static final String COL1 = "Eid";
    public static final String COL2 = "Custname";
    public static final String COL3 = "Custarea";
    public static final String COL4= "Custnum";
    public static final String COL5= "Custid";



    public Customerlistdb(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "create table if not exists Custdetails(Eid Integer primary key autoincrement," +
                "Custid varchar(20),Custname varchar(20),Custarea varchar2(20),Custnum varchar2(20) )";
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String Custid, String Custname, String Custarea, String Custnum  ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, Custname);
        contentValues.put(COL3, Custarea);
        contentValues.put(COL4, Custnum);
        contentValues.put(COL5,Custid);


        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
    public Cursor getdata(String Custname,SQLiteDatabase sqLiteDatabase){
        String[] add={COL3,COL4};
String select=COL2+" LIKE ?";
        String[] selected={Custname};
        Cursor cursor=sqLiteDatabase.query(TABLE_NAME,add,select,selected,null,null,null);
        return  cursor;
    }
    public boolean updateData(String id, String Custname, String Custarea, String Custnum  ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, id);
        contentValues.put(COL2, Custname);
        contentValues.put(COL3, Custarea);
        contentValues.put(COL4, Custnum);

        db.update(TABLE_NAME,contentValues,"Eid=?",new String[]{id});
        return true;
    }
}