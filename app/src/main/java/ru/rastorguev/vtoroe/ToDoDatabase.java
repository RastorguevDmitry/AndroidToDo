package ru.rastorguev.vtoroe;



import android.os.Bundle;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;



        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;
        import android.content.Intent;
import android.widget.Toast;

public class ToDoDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo_app.db";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_TABLE = "todos";

    // поля таблицы
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_PRICE_IN_LENTA = "price_in_lenta";
    public static final String COLUMN_PRICE_IN_AUSHAN = "price_in_aushan";
    public static final String COLUMN_PRICE_IN_GIGANT = "price_in_gigant";


    // запрос на создание базы данных
    private static final String DATABASE_CREATE = "create table "
            + DATABASE_TABLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CATEGORY
            + " text not null, " + COLUMN_SUMMARY + " text not null,"
            + COLUMN_DESCRIPTION + " text not null,"
            + COLUMN_STATUS + " text not null,"
            + COLUMN_PRICE_IN_LENTA + " text not null,"
            + COLUMN_PRICE_IN_AUSHAN + " text not null,"
            + COLUMN_PRICE_IN_GIGANT + " text not null" + ");";

    public ToDoDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w(ToDoDatabase.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS todos");
        onCreate(db);
    }

    /**
     * Создаёт новый элемент списка дел. Если создан успешно - возвращается
     * номер строки rowId, иначе -1
     */
    public long createNewTodo(String category, String summary, String description, String status,
                                   String priceInLenta, String priceInAushan, String priceInGigant ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(category, summary,
                description, status, priceInLenta, priceInAushan, priceInGigant);

        long row = db.insert(DATABASE_TABLE, null, initialValues);
        db.close();

        return row;
    }


    public long createNewItemSpisok (String category, String summary,
                              String description, String status,
                              String priceInLenta, String priceInAushan, String priceInGigant ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(category, summary,
                description, status, priceInLenta, priceInAushan, priceInGigant);

        long row = db.insert(DATABASE_TABLE, null, initialValues);
        db.close();

        return row;
    }




    /**
     * Удаляет элемент списка
     */
    public void deleteTodo(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, COLUMN_ID + "=" + rowId, null);
        db.close();
    }

    /**
     * Возвращает курсор со всеми элементами списка дел
     *
     * @return курсор с результатами всех записей
     */
    public Cursor getAllTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE, new String[] { COLUMN_ID,
                        COLUMN_CATEGORY, COLUMN_SUMMARY, COLUMN_DESCRIPTION, COLUMN_STATUS,
                        COLUMN_PRICE_IN_LENTA, COLUMN_PRICE_IN_AUSHAN, COLUMN_PRICE_IN_GIGANT}, null,
                null, null, null, null);
    }

    /**
     * Возвращает курсор с указанной записи
     */
    public Cursor getTodo(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE,
                new String[] { COLUMN_ID, COLUMN_CATEGORY, COLUMN_SUMMARY,
                        COLUMN_DESCRIPTION, COLUMN_STATUS,
                        COLUMN_PRICE_IN_LENTA, COLUMN_PRICE_IN_AUSHAN, COLUMN_PRICE_IN_GIGANT}, COLUMN_ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int countcats (String Cats) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!Cats.equals("всевсевсе"))
        {
            String query = "SELECT * FROM todos WHERE category LIKE ?";
            Cursor mCursor = db.rawQuery(query , new String[] {Cats} );
            int count= mCursor.getCount();
            return count;
          }
        else
        {
            String query = "SELECT * FROM todos";
            Cursor mCursor = db.rawQuery(query , null);
            int count= mCursor.getCount();
            return count;
        }


    }





    public Cursor getTodoCats(String Cats) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM todos WHERE category LIKE ? ORDER BY status";
        Cursor mCursor = db.rawQuery(query , new String[] {Cats} );
        return mCursor;

    }



    /**
     * Обновляет список
     */
    public boolean updateTodo(long rowId, String category, String summary,
                              String description, String status,
                              String priceInLenta, String priceInAushan, String priceInGigant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValues(category, summary,
                description, status, priceInLenta, priceInAushan, priceInGigant);

        return db.update(DATABASE_TABLE, updateValues, COLUMN_ID + "=" + rowId,
                null) > 0;
    }







    //смена статуса



  /*  public boolean changStatus(String summary, String items_status_for_chang) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updateStatus = updateStatus(items_status_for_chang);
        return db.update(DATABASE_TABLE, updateStatus, COLUMN_SUMMARY + "=" + summary,
                null) > 0;
    }

    private ContentValues updateStatus(String status) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        return values;
    }*/



    /*
     * Создаёт пару ключ-значение и записывает в базу
     */
    private ContentValues createContentValues(String category, String summary,
                                              String description, String status,
                                              String priceInLenta, String priceInAushan, String priceInGigant) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_SUMMARY, summary);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_PRICE_IN_LENTA, priceInLenta);
        values.put(COLUMN_PRICE_IN_AUSHAN, priceInAushan);
        values.put(COLUMN_PRICE_IN_GIGANT, priceInGigant);
        return values;
    }


    public boolean updateTodoPriceLenta(long rowId, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValuesPriceLenta(price);

        return db.update(DATABASE_TABLE, updateValues, COLUMN_ID + "=" + rowId,
                null) > 0;
    }
    private ContentValues createContentValuesPriceLenta(String price) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRICE_IN_LENTA, price);
        return values;
    }


    public boolean updateTodoPriceAushan(long rowId, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValuesPriceAushan(price);

        return db.update(DATABASE_TABLE, updateValues, COLUMN_ID + "=" + rowId,
                null) > 0;
    }
    private ContentValues createContentValuesPriceAushan(String price) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRICE_IN_AUSHAN, price);
        return values;
    }


    public boolean updateTodoPriceGigant(long rowId, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValuesPriceGigant(price);

        return db.update(DATABASE_TABLE, updateValues, COLUMN_ID + "=" + rowId,
                null) > 0;
    }
    private ContentValues createContentValuesPriceGigant(String price) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRICE_IN_GIGANT, price);
        return values;
    }

}