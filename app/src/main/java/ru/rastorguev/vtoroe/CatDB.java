package ru.rastorguev.vtoroe;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CatDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "catdb.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "cats";

    // поля таблицы
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PRIORITET = "prioritet";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_TIP = "tip";

    // запрос на создание базы данных
    private static final String DATABASE_CREATE = "create table "
            + DATABASE_TABLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_CATEGORY + " text not null, "
            + COLUMN_PRIORITET + " text not null,"
            + COLUMN_COLOR + " text not null,"
            + COLUMN_TIP + " text not null" + ");";






    public CatDB(Context context) {
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
        Log.w(CatDB.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS todos");
        onCreate(db);
    }

    /**
     * Создаёт новый элемент списка дел. Если создан успешно - возвращается
     * номер строки rowId, иначе -1
     */
    public long createNewCats(String category, String priority, String color, String tip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(category, priority,
                color, tip);

        long row = db.insert(DATABASE_TABLE, null, initialValues);
        db.close();

        return row;
    }


    public long createNewItemSpisok_of_cat (String category, String priority, String color, String tip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(category, priority,
                color, tip);

        long row = db.insert(DATABASE_TABLE, null, initialValues);
        db.close();

        return row;
    }


    /**
     * Обновляет список
     */
    public boolean updateTodo(long rowId, String category, String priority, String color, String tip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValues(category, priority,
                color, tip);

        return db.update(DATABASE_TABLE, updateValues, COLUMN_ID + "=" + rowId,
                null) > 0;
    }

    /**
     * Удаляет элемент списка
     */
    public void deleteCat(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, COLUMN_ID + "=" + rowId, null);
        db.close();
    }

    /**
     * Возвращает курсор со всеми элементами списка дел
     *
     * @return курсор с результатами всех записей
     */
    public Cursor getAllCats() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE, new String[] { COLUMN_ID,
                        COLUMN_CATEGORY, COLUMN_PRIORITET, COLUMN_COLOR, COLUMN_TIP }, null,
                null, null, null, null);
    }

    public Cursor getAllCatsByOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM cats ORDER BY prioritet";
        Cursor mCursor = db.rawQuery(query , null);
                return mCursor;
        }


  /*  public Cursor getAllCats() {
        SQLiteDatabase db = this.getWritableDatabase();
        //    String query = "SELECT category FROM cats ORDER BY prioritet";
        String query = "SELECT * FROM cats";
        Cursor mCursor = db.rawQuery(query , null );
        mCursor.moveToFirst();
        String[] catname;
        for (int i = 0; mCursor.isAfterLast() == false; i++)
        {
            catname[i] = mCursor.getString(mCursor.getColumnIndex(CatDB.COLUMN_CATEGORY));
            mCursor.moveToNext();
        }

        mCursor.close();
        return catname;

    }*/


    /**
     * Возвращает курсор с указанной записи
     */
    public Cursor getCat(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE,
                new String[] { COLUMN_ID,
                        COLUMN_CATEGORY, COLUMN_PRIORITET, COLUMN_COLOR, COLUMN_TIP }, COLUMN_ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

   /* public Cursor getCatByName (String Cats) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM cats WHERE category LIKE ?";
        Cursor mCursor = db.rawQuery(query , new String[] {Cats} );
        return mCursor;

    }*/



        public String color (String Cats) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
            String color;
            String query = "SELECT * FROM cats WHERE category LIKE ?";
            Cursor mCursor = db.rawQuery(query , new String[] {Cats} );
            mCursor.moveToFirst(); // если курсор не пустой

                color  = mCursor.getString(3);

            mCursor.close();


            return color;

    }

  /*  public int countcats (String Cats) throws SQLException {
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
    }*/



/*

    public Cursor getTodoCats(String Cats) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM todos WHERE category LIKE ?";
        Cursor mCursor = db.rawQuery(query , new String[] {Cats} );
        return mCursor;

    }*/




    /*
     * Создаёт пару ключ-значение и записывает в базу
     */
    private ContentValues createContentValues(String category, String priority, String color, String tip) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_PRIORITET, priority);
        values.put(COLUMN_COLOR, color);
        values.put(COLUMN_TIP, tip);
            return values;
    }
}