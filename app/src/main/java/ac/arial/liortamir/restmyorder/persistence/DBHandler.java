package ac.arial.liortamir.restmyorder.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ac.arial.liortamir.restmyorder.entity.Dish;
import ac.arial.liortamir.restmyorder.entity.OrderItem;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "restmyorders.db";
    private static final String TAG = "DBHandler";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table role(roleId integer primary key," +
                "roleName text not null)");
        db.execSQL("create table [user](userId integer primary key," +
                "fullName text not null," +
                "email text not null," +
                "password text not null," +
                "roleId integer not null," +
                "constraint unique_email unique(email)," +
                "foreign key(roleId) references role(roleId))");
        db.execSQL("create table dishType(dishTypeId integer primary key autoincrement, " +
                "dishTypeName text not null)");
        db.execSQL("create table dish(dishId integer primary key autoincrement, " +
                "dishName text not null, " +
                "dishPrice real not null, " +
                "dishTypeId integer, " +
                "foreign key(dishTypeId) references dishType(dishTypeId))");
        db.execSQL("create table diningTable(diningTableId integer primary key autoincrement," +
                "diningTableName text not null)");
        db.execSQL("create table [order](orderId integer primary key autoincrement, " +
                "orderDate text not null, " +
                "diningTableId integer, " +
                "isClosed integer not null check(isClosed == 0 || isClosed == 1) default 0, " +
                "foreign key (diningTableId) references diningTable(diningTableId))");
        db.execSQL("create table orderItem(orderItemId integer primary key autoincrement, " +
                "orderId integer not null, " +
                "waiterUserId integer not null, " +
                "chefUserId integer, " +
                "sitNum integer not null default 1, " +
                "dishId integer, " +
                "comments text, " +
                "isReady integer not null check(isReady == 0 || isReady == 1) default 0, " +
                "isServed integer not null check(isServed == 0 || isServed == 1) default 0, " +
                "foreign key(orderId) references [order](orderId), " +
                "foreign key(waiterUserId) references [user](userId), " +
                "foreign key(chefUserId) references [user](userId), " +
                "foreign key(dishId) references dish(dishId))");


        db.execSQL("insert into role values(null,\"Waiter\")");     //Order, history
        db.execSQL("insert into role values(null,\"Chef\")");       // kitchen screen
        db.execSQL("insert into role values(null,\"Manager\")");    // all screens besides DBA
        db.execSQL("insert into role values(null,\"Developer\")");  // all screens

        db.execSQL("insert into user values(null,\"Lior Tamir\",\"lior.tamir@gmail.com\",\"12345\",4)");
        db.execSQL("insert into user values(null,\"Developer1\",\"developer1@gmail.com\",\"12345\",4)");
        db.execSQL("insert into user values(null,\"Developer2\",\"developer2@gmail.com\",\"12345\",4)");
        db.execSQL("insert into user values(null,\"Waiter 1\",\"waiter1@gmail.com\",\"12345\",1)");
        db.execSQL("insert into user values(null,\"Waiter 2\",\"waiter2@gmail.com\",\"12345\",1)");
        db.execSQL("insert into user values(null,\"Waiter 3\",\"waiter3@gmail.com\",\"12345\",1)");
        db.execSQL("insert into user values(null,\"Waiter 4\",\"waiter4@gmail.com\",\"12345\",1)");
        db.execSQL("insert into user values(null,\"Waiter 5\",\"waiter5@gmail.com\",\"12345\",1)");
        db.execSQL("insert into user values(null,\"Chef 1\",\"chef1@gmail.com\",\"12345\",2)");
        db.execSQL("insert into user values(null,\"Chef 2\",\"chef2@gmail.com\",\"12345\",2)");
        db.execSQL("insert into user values(null,\"Chef 3\",\"chef3@gmail.com\",\"12345\",2)");
        db.execSQL("insert into user values(null,\"Manager 1\",\"manager1@gmail.com\",\"12345\",3)");
        db.execSQL("insert into user values(null,\"Manager 2\",\"manager2@gmail.com\",\"12345\",3)");

        // metadata
        db.execSQL("insert into diningtable values(null,\"Table 1\")");
        db.execSQL("insert into diningtable values(null,\"Table 2\")");
        db.execSQL("insert into diningtable values(null,\"Table 3\")");
        db.execSQL("insert into diningtable values(null,\"Table 4\")");
        db.execSQL("insert into diningtable values(null,\"Table 5\")");

        db.execSQL("insert into dishType values(null,\"Appetizer\")");
        db.execSQL("insert into dishType values(null,\"Main Course\")");
        db.execSQL("insert into dishType values(null,\"Side Dish\")");
        db.execSQL("insert into dishType values(null,\"Dessert\")");

        db.execSQL("insert into dish values(null,\"Soup\",23.5, 1)");
        db.execSQL("insert into dish values(null,\"Salad\",20.00, 1)");
        db.execSQL("insert into dish values(null,\"Beef\",45.5,2)");
        db.execSQL("insert into dish values(null,\"Salmon\",55.00,2)");
        db.execSQL("insert into dish values(null,\"Chicken\",48.00,2)");
        db.execSQL("insert into dish values(null,\"Rice\",7.00,3)");
        db.execSQL("insert into dish values(null,\"Potatos\",15.00,3)");
        db.execSQL("insert into dish values(null,\"Antipasto\",19.00,3)");
        db.execSQL("insert into dish values(null,\"Cheese Cake\",29.00,4)");
        db.execSQL("insert into dish values(null,\"Chocolate Cake\",27.00,4)");

        // Data
        db.execSQL("insert into [order] values(null,\"2018-07-01 19:50\",1,0)"); //YYYY-MM-DD HH:MM
        db.execSQL("insert into [order] values(null,\"2018-07-01 20:10\",2,0)");
        db.execSQL("insert into [order] values(null,\"2018-07-01 20:35\",3,1)");

        db.execSQL("insert into orderItem values(null,1,4,9,1,1,\"taste good\",1, 1)");
        db.execSQL("insert into orderItem values(null,1,4,null,1,3,\"Medium\",0, 0)");
        db.execSQL("insert into orderItem values(null,1,4,null,1,6,\"Medium\",0, 0)");
        db.execSQL("insert into orderItem values(null,1,4,null,1,9,\"Medium\",0, 0)");
        db.execSQL("insert into orderItem values(null,1,4,9,2,2,\"taste good\",1, 1)");
        db.execSQL("insert into orderItem values(null,1,5,10,2,4,\"Medium\",1, 1)");
        db.execSQL("insert into orderItem values(null,1,4,9,2,7,null,0, 0)");
        db.execSQL("insert into orderItem values(null,1,4,9,2,10,null,0, 0)");
        db.execSQL("insert into orderItem values(null,1,4,9,3,2,\"taste good\",1, 0)");
        db.execSQL("insert into orderItem values(null,1,5,9,3,5,\"Medium\",1, 0)");
        db.execSQL("insert into orderItem values(null,1,4,9,3,7,null,0, 0)");
        db.execSQL("insert into orderItem values(null,1,4,9,3,9,null,0, 0)");

        db.execSQL("insert into orderItem values(null,2,5,null,1,2,\"taste good\",0, 0)");
        db.execSQL("insert into orderItem values(null,2,5,null,1,4,\"Medium\",0, 0)");

        db.execSQL("insert into orderItem values(null,3,6,11,1,2,\"taste good\",1, 1)");
        db.execSQL("insert into orderItem values(null,3,6,11,1,4,\"Medium\",1, 1)");
        db.execSQL("insert into orderItem values(null,3,6,11,2,1,\"taste good\",1, 1)");
        db.execSQL("insert into orderItem values(null,3,6,11,2,5,\"Medium\",1, 1)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // ***** DATA INSERT ***** //

    public ContentValues getSingle(String sql, String[] params){
        SQLiteDatabase db = getReadableDatabase();
        String[] columnNames = null;
        if(db == null)
            return null;

        ContentValues rs = new ContentValues();
        Cursor cur = db.rawQuery(sql, params);

        columnNames = cur.getColumnNames();
        while(cur.moveToNext()){
            for(int col = 0; col < columnNames.length; col++){
                switch(cur.getType(col)){
                    case 1:
                        rs.put(columnNames[col], cur.getInt(col));
                        break;
                    case 2:
                        rs.put(columnNames[col], cur.getString(col));
                        break;
                    case 3:
                        rs.put(columnNames[col], cur.getString(col));
                        break;
                    case 4:
                        rs.put(columnNames[col], cur.getFloat(col));
                        break;
                    case 5: //bool, datetime
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        rs.put(columnNames[col], new String(""));
                        break;
                }
            }
        }

        cur.close();
        db.close();
        return rs;
    }

    public List<ContentValues> getMultiple(String sql, String[] params){
        SQLiteDatabase db = getReadableDatabase();
        List<ContentValues> cvList = new LinkedList<>();
        String[] columnNames = null;
        if(db == null)
            return null;

        Cursor cur = db.rawQuery(sql, params);

        columnNames = cur.getColumnNames();
        while(cur.moveToNext()){
            ContentValues contentValues = new ContentValues();
            for(int col = 0; col < columnNames.length; col++){
                switch(cur.getType(col)){
                    case 1:
                        contentValues.put(columnNames[col], cur.getInt(col));
                        break;
                    case 2:
                        contentValues.put(columnNames[col], cur.getString(col));
                        break;
                    case 3: // blob
                        contentValues.put(columnNames[col], cur.getString(col));
                        break;
                    case 4:
                        contentValues.put(columnNames[col], cur.getFloat(col));
                        break;
                    case 5: //bool, datetime
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        contentValues.put(columnNames[col], new String(""));
                        break;
                }
            }
            cvList.add(contentValues);
        }

        cur.close();
        db.close();
        return cvList;
    }

    public String update(String table, ContentValues cv, String whereClause, String[] whereArgs){
        SQLiteDatabase db = this.getWritableDatabase();
        String result = "OK";
        int updatedRows = 0;
        try {
            updatedRows = db.update(table, cv, whereClause, whereArgs);
        }catch(SQLException e){
            result = e.getLocalizedMessage();
            Log.d(TAG, "update failed");
            Log.d(TAG, "table: " + table);
            Log.d(TAG, "values:" + cv);
            Log.d(TAG, "predicates:" + whereClause);
            Log.d(TAG, "args:" + Arrays.toString(whereArgs));
            Log.d(TAG, "Error:" + e.getLocalizedMessage());
            e.printStackTrace();
        }
        db.close(); // Closing database connection
        if(updatedRows == 0)
            result = "NOK";
        return result;
    }
    // ***** DATA INSERT ***** //

    public long insert(String table, ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        long newId = db.insert(table, null, values);
        db.close(); // Closing database connection
        return newId;
    }

    public void delete(String table, String whereClause, String[] whereArgs){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, whereClause, whereArgs);
        db.close(); // Closing database connection
    }

}
