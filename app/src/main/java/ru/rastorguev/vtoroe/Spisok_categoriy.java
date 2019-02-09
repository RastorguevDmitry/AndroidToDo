package ru.rastorguev.vtoroe;



import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class Spisok_categoriy extends ListActivity implements
        LoaderCallbacks<Cursor> {

    private CatDB dbHelperCat;
     private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int ACTIVITY_WATCH = 2;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private String mCats;
    private SimpleCursorAdapter adapter;
    private static String cats2;
    private static String cats3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);

        setContentView(R.layout.spisok_categoriy);
      this.getListView().setDividerHeight(2);
      registerForContextMenu(getListView());

        dbHelperCat = new CatDB(this);



        /*
        mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.redColor));
         */



        Button back_in_main = (Button) findViewById(R.id.back_in_main);
        back_in_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_OK);
                Intent intent = new Intent(Spisok_categoriy.this, MainActivity.class);
                startActivityForResult(intent, ACTIVITY_WATCH);
            }
        });
        Button spisok_add_button = (Button) findViewById(R.id.spisok_add_button);
        spisok_add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_OK);
                Intent intent = new Intent(Spisok_categoriy.this, EditCats.class);
                  startActivityForResult(intent, ACTIVITY_CREATE);
                     }
        });



        fillData();

    }


  /*  private void saveState() {
        String category = cats2;
        String summary = mBodyText.getText().toString();
        String description = "";

        if (summary.length() == 0 ) {
            return;
        }
        long id = dbHelper.createNewItemSpisok(category, summary, description);
    //    if (mRowId == null) {

       //     if (id > 0) {                mRowId = id;            }
    //    }// else {            mDbHelper.updateTodo(mRowId, category, summary, description);        }
    }
*/


  /*  public void onClick(View view) {
        createNewTask();}*/



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // Первоначальный способ. Тоже работает
        // Cursor cursor = dbHelper.getAllTodos();
        // adapter.changeCursor(cursor);

        getLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Реакция на выбор меню
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
               createNewTask();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createNewTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                // получаем из пункта контекстного меню данные по пункту списка
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();
                // извлекаем id записи и удаляем соответствующую запись в БД
                dbHelperCat.deleteCat(info.id);
                // получаем новый курсор с данными
                getLoaderManager().getLoader(0).forceLoad();
                 fillData();

                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createNewTask() {
        Intent intent = new Intent(this, EditCats.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }


    private void fillData() {

        String[] from = new String[] { CatDB.COLUMN_CATEGORY, CatDB.COLUMN_PRIORITET, CatDB.COLUMN_COLOR };
        int[] to = new int[] { R.id.cats_name, R.id.prioritet , R.id.color };

        adapter = new SimpleCursorAdapter(this, R.layout.row_cats, null, from,
                to, 0);

      //  adapter.setViewBinder(new MyViewBinder());
        setListAdapter(adapter);


        // создаем лоадер для чтения данных
        getLoaderManager().initLoader(0, null, this);
        adapter.notifyDataSetInvalidated();
        adapter.notifyDataSetChanged();

    }


  /*  class MyViewBinder implements SimpleCursorAdapter.ViewBinder {

        int red = getResources().getColor(R.color.yellowColor);
        int orange = getResources().getColor(R.color.orang);
        int green = getResources().getColor(R.color.persik);

        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            int i=0;
            switch (view.getId()) {
                // LinearLayout
                case R.id.color:
                    //count.setText(String.valueOf(countcatof));
                    //  tvName.getText();
                    //i = ((String) data).toString();
                    i = ((Integer) cursor).intValue();
                    view.setBackgroundColor(i);
                    return true;

            }
            return false;
        }
    }*/



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, EditCats.class);
        intent.putExtra(CatDB.COLUMN_ID, id);
        // активность вернет результат если будет вызван с помощью этого метода
        startActivityForResult(intent, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            fillData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperCat.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // TODO Auto-generated method stub
        return new MyCursorLoader(this, dbHelperCat);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // TODO Auto-generated method stub
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // TODO Auto-generated method stub
        adapter.swapCursor(null);
    }

    static class MyCursorLoader extends CursorLoader {
        CatDB db;

        public MyCursorLoader(Context context, CatDB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {


           Cursor cursor = db.getAllCatsByOrder();

            return cursor;
        }
    }
}