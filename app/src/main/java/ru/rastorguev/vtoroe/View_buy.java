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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.TextView;


public class View_buy extends ListActivity implements LoaderCallbacks<Cursor> {

    private ToDoDatabase dbHelper;
    private CatDB dbHelperCat;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int ACTIVITY_WATCH = 2;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private String mCats;
    private Long mRowId;
    private SimpleCursorAdapter adapter;
    private static String cats2;
    private static String cats3;
    int spiner_selected;
    private String[] items;
    private String[] items_color;
    private String[] items_status;
    private Long[] items_id;
    private String[] category_description;
    private String[] priceInLenta;
    private String[] priceInAushan;
    private String[] priceInGigant;

    private TextView mText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);

        setContentView(R.layout.view_buy);
      this.getListView().setDividerHeight(2);
      registerForContextMenu(getListView());

     //  dbHelper = new ToDoDatabase(this);
       mCats = getIntent().getExtras().getString("CATSF");
        cats2=mCats;
        cats3=mCats;

        fillData();

    }


  /*  public void onClick(View view) {
        createNewTask();}

*/



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
           //     createNewTask();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
           //     createNewTask();
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
                dbHelper.deleteTodo(info.id);
                // получаем новый курсор с данными
                getLoaderManager().getLoader(0).forceLoad();
                // fillData();

                return true;
        }
        return super.onContextItemSelected(item);
    }

  /*  private void createNewTask() {
        Intent intent = new Intent(this, EditActivity.class);

     //   intent.putExtra("CATS_F", getIntent().getExtras().getString("CATS"));
        startActivityForResult(intent, ACTIVITY_CREATE);
    }*/


    private void fillData() {

        // находим список
        ListView list_category = (ListView) findViewById(R.id.list_category);
        list_category.setItemsCanFocus(true);

        dbHelper = new ToDoDatabase(this);
        Cursor cursor = dbHelper.getTodoCats(cats2);

        if (cursor.getCount() != 0) {

            items_id= new Long [cursor.getCount()];
            items= new String[cursor.getCount()];
            category_description= new String[cursor.getCount()];
            items_status= new String[cursor.getCount()];
            priceInLenta= new String[cursor.getCount()];
            priceInAushan= new String[cursor.getCount()];
            priceInGigant= new String[cursor.getCount()];


            items_color= new String[cursor.getCount()];

            int column_items = 2;
            int column_items_status = 4;
            if (cursor.moveToFirst()) // если курсор не пустой
            {
                for (int i = 0; i < cursor.getCount(); i++) {
                    items_id[i]=cursor.getLong(0);
                    items[i] = cursor.getString(column_items);
                    category_description[i]=cursor.getString(3);
                    items_status[i] = cursor.getString(column_items_status);
                    priceInLenta[i]=cursor.getString(5);
                    priceInAushan[i]=cursor.getString(6);
                    priceInGigant[i]=cursor.getString(7);


                    // заполняем
                    cursor.moveToNext();
                }
            }
            cursor.close();
            dbHelper.close();


            ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>(items.length);
            HashMap<String, Object> map;
            dbHelperCat = new CatDB(this);
           String color=dbHelperCat.color(cats2);
            for (int i = 0; i < items.length; i++) {
                map = new HashMap<String, Object>();
                map.put("Items", items[i]);
                map.put("Items_status", items_status[i]);
                map.put("Items_color", color);

                data.add(map);
            }

// Массив имен атрибутов, из которых будут читаться данные
            String[] fromC = {"Items", "Items_status", "Items_color"};
// Массив идентификаторов компонентов, в которые будем вставлять данные
            int[] toC = {R.id.item_name, R.id.price, R.id.item_of_buy_linlayout};
            SimpleAdapter adapterc = new SimpleAdapter(this, data, R.layout.view_buy_row, fromC, toC);
            // присваиваем адаптер списку
            adapterc.setViewBinder(new MyViewBinder());
            list_category.setAdapter(adapterc);


            list_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, final int position, long id) {


                    String items_status_for_chang;
                    if(!items_status[position].equals("1")) items_status_for_chang="1";
                    else items_status_for_chang="0";
                    if(items_status[position].equals(null)) items_status_for_chang="1";
                    Toast.makeText(View_buy.this, items_status_for_chang , Toast.LENGTH_LONG).show();
                 //   dbHelper.changStatus(items[position],items_status_for_chang);
                    dbHelper.updateTodo(items_id[position], cats2, items[position],
                            category_description[position], items_status_for_chang,
                            priceInLenta[position], priceInAushan[position], priceInGigant[position]
                                        );



                    Intent intent = new Intent(View_buy.this, View_buy.class);
                    intent.putExtra("CATSF", cats2);
                    startActivityForResult(intent, ACTIVITY_WATCH);

                }
            });




        /*    Button allnote = (Button) findViewById(R.id.allnote);
            allnote.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, Spisok_categoriy.class);
                    //   intent.putExtra("CATS", "allallall");
                    // активность вернет результат если будет вызван с помощью этого метода
                    startActivityForResult(intent, ACTIVITY_EDIT);


             /*   Intent intent = new Intent(MainActivity.this, View_cats.class);
                intent.putExtra("CATS", "allallall");
                // активность вернет результат если будет вызван с помощью этого метода
                startActivityForResult(intent, ACTIVITY_EDIT);*/
      //          }
      //      });


            String[] from = new String[]{ToDoDatabase.COLUMN_ID};
            int[] to = new int[]{R.id.label};

            adapter = new SimpleCursorAdapter(this, R.layout.list_row, null, from,
                    to, 0);
            setListAdapter(adapter);

            // создаем лоадер для чтения данных
            getLoaderManager().initLoader(0, null, this);
            adapter.notifyDataSetInvalidated();
            adapter.notifyDataSetChanged();


        }

   }



   /* @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(ToDoDatabase.COLUMN_ID, id);
        // активность вернет результат если будет вызван с помощью этого метода
        startActivityForResult(intent, ACTIVITY_EDIT);
    }*/

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
        dbHelper.close();
    }



    class MyViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            String i;

            switch (view.getId()) {
                // LinearLayout
                case R.id.item_of_buy_linlayout:
                    i = ((String) data).toString();
                    // int i2 = Integer.valueOf(i);

                    if (i.equalsIgnoreCase("yellowColor")) {
                        view.setBackgroundColor(getResources().getColor(R.color.yellowColor));
                    } else if (i.equalsIgnoreCase("orang")){
                        view.setBackgroundColor(getResources().getColor(R.color.orang));
                    } else if (i.equalsIgnoreCase("persik")) {
                        view.setBackgroundColor(getResources().getColor(R.color.persik));
                    }else if (i.equalsIgnoreCase("serovatorosoviy")) {
                        view.setBackgroundColor(getResources().getColor(R.color.serovatorosoviy));
                    }else if (i.equalsIgnoreCase("fistashkoviy")) {
                        view.setBackgroundColor(getResources().getColor(R.color.fistashkoviy));
                    }


                case R.id.price:
                    i = ((String) data).toString();
                    // int i2 = Integer.valueOf(i);
                    mText = (TextView) findViewById(R.id.item_name);


            /*        if (!i.equalsIgnoreCase("1")) {
                        view.setBackgroundColor(getResources().getColor(R.color.yellowColor));
                        mText.setText(i);
                    }*/


                //    CharSequence styledText = Html.fromHtml("<s>" + YOUR_TEXT + "</s>");
                //    holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    return true;

            }
            return false;
        }
    }











    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // TODO Auto-generated method stub
        return new MyCursorLoader(this, dbHelper);
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
        ToDoDatabase db;

        public MyCursorLoader(Context context, ToDoDatabase db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor;
            if (cats3.equals("все записи"))
            {  cursor = db.getAllTodos();}

         else { cursor = db.getTodoCats(cats2);}

          //  Cursor cursor = db.getAllTodos();

            return cursor;
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }

}