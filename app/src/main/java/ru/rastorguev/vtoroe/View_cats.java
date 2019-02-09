package ru.rastorguev.vtoroe;



import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;


public class View_cats extends ListActivity implements LoaderCallbacks<Cursor> {

    private ToDoDatabase dbHelper;
    private CatDB dbHelperCat;
    private EditText mBodyText;
    private Spinner mSpinerCats;
    private Spinner mSpinerShops;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int ACTIVITY_WATCH = 2;
    private static final int ACTIVITY_FROM_PRICE = 5;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private String mCats;
    private SimpleCursorAdapter adapter;
    private static String cats2;
    private static String cats3;
    int spiner_selected;
    int spinerOfShopSelected;
    private String[] items;
    private String[] items_color;
    private String[] items_status;
    private Long[] items_id;
    private String[] category_description;

    private String[] priceInLenta;
    private String[] priceInAushan;
    private String[] priceInGigant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);

        setContentView(R.layout.view_cats);
      this.getListView().setDividerHeight(2);
   //   registerForContextMenu(getListView());


        mCats = getIntent().getExtras().getString("CATS");
        cats2=mCats;
        cats3=mCats;


        /*
        mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.redColor));
         */

        //Spiner
        mSpinerCats = (Spinner) findViewById(R.id.spinner);
        List<String> List_of_cat;
        ArrayAdapter<String> myAdapter1;
        List_of_cat = new ArrayList<String>();
        dbHelperCat = new CatDB(this);
        Cursor cursor = dbHelperCat.getAllCatsByOrder();
        int columnIndex = 1;
        List_of_cat.add("все записи");
        if (cursor.moveToFirst()) // если курсор не пустой
        { for (int i = 0; i < cursor.getCount(); i++)
            {  List_of_cat.add(cursor.getString(columnIndex));
                if (cats3.equals(cursor.getString(columnIndex))) {spiner_selected=i+1;}
                cursor.moveToNext();            }       }
        cursor.close();

        dbHelperCat.close();

        myAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, List_of_cat);
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinerCats.setAdapter(myAdapter1);

        mSpinerCats.setSelection(spiner_selected);
        mSpinerCats.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int ItemSelected, long selectedId) {

                    if (!cats3.equals(mSpinerCats.getSelectedItem().toString())) {

                        Intent intent = new Intent(View_cats.this, View_cats.class);
                        intent.putExtra("CATS", mSpinerCats.getSelectedItem().toString());
                        // активность вернет результат если будет вызван с помощью этого метода
                        startActivityForResult(intent, ACTIVITY_WATCH);
                        //  Toast.makeText(View_cats.this, mSpinerCats.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mSpinerShops = (Spinner) findViewById(R.id.spinnerOfShop);
        List<String> List_of_shop;
        ArrayAdapter<String> myAdapterShop;
        List_of_shop = new ArrayList<String>();
        spinerOfShopSelected=0;
        List_of_shop.add("лента");
        List_of_shop.add("ашан");
        List_of_shop.add("гигант");
        myAdapterShop = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, List_of_shop);
        myAdapterShop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinerShops.setAdapter(myAdapterShop);
        mSpinerShops.setSelection(spinerOfShopSelected);

        mSpinerShops.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int ItemSelected, long selectedId) {

                spinerOfShopSelected=ItemSelected;
                fillData();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



       /* Button regim_pokupok = (Button) findViewById(R.id.regim_pokupok);
        regim_pokupok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                        Intent intent = new Intent(View_cats.this, View_buy.class);
                          intent.putExtra("CATSF", cats2);
                        startActivityForResult(intent, ACTIVITY_CREATE);
                    }
        });*/


        fillData();

    }


    private void saveState() {
        String category = cats2;
        String summary = mBodyText.getText().toString();
        String description = "";
        String status = "";
        String priceInLenta= "";
        String priceInAushan= "";
        String priceInGigant= "";

        if (summary.length() == 0 ) {
            return;
        }
        long id = dbHelper.createNewItemSpisok(category, summary, description, status,
                priceInLenta,priceInAushan,priceInGigant);
    //    if (mRowId == null) {

       //     if (id > 0) {                mRowId = id;            }
    //    }// else {            mDbHelper.updateTodo(mRowId, category, summary, description);        }
    }







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

            case R.id.View_wotchPrice:
                Intent intent = new Intent(View_cats.this, View_wotchPrice.class);
                intent.putExtra("CATSF", cats2);

                startActivityForResult(intent, ACTIVITY_FROM_PRICE);
                break;
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
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                // извлекаем id записи и удаляем соответствующую запись в БД
                dbHelper.deleteTodo(items_id[info.position]);
                // получаем новый курсор с данными
              //  getLoaderManager().getLoader(0).forceLoad();
                 fillData();

                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createNewTask() {
        Intent intent = new Intent(this, EditActivity.class);

     //   intent.putExtra("CATS_F", getIntent().getExtras().getString("CATS"));
        startActivityForResult(intent, ACTIVITY_CREATE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           // если пришло ОК
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTIVITY_FROM_PRICE:
                    InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    fillData();
                    break;

            }
            // если вернулось не ОК
        } else {
            Toast.makeText(this, "ничего не введено", Toast.LENGTH_SHORT).show();
        }
    }






    private void fillData() {



        mBodyText = (EditText) findViewById(R.id.spisok_add_summary);
        if (!cats2.equals("все записи"))
        {  mBodyText.setHint("новая запись");}
        // {  mBodyText.setHint("запись в " + mCats);}

        else {
            mBodyText.setHint("выберите категорию");
            mBodyText.setFocusable(false);
        }



        Button spisok_add_button = (Button) findViewById(R.id.spisok_add_button);
        spisok_add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!cats2.equals("все записи")) {
                    String summary = mBodyText.getText().toString();
                    if(summary.length() == 0)
                    {
                        Intent intent = new Intent(View_cats.this, EditActivity.class);
                        //    intent.putExtra("CATS11", "0");
                        startActivityForResult(intent, ACTIVITY_EDIT);

                        //  createNewTask();
                    }
                    else
                    {saveState();

                   setResult(RESULT_OK);
                 //   Intent intent = new Intent(View_cats.this, View_cats.class);
                  //  intent.putExtra("CATS", cats2);
                  //  startActivityForResult(intent, ACTIVITY_CREATE);
                        fillData();

                        mBodyText.setText(null);

                    }
                }
                else Toast.makeText(View_cats.this, "выберите категорию", Toast.LENGTH_SHORT).show();
            }
        });



        // находим список
        ListView list_category = (ListView) findViewById(R.id.list_category);
        registerForContextMenu(list_category);
        list_category.setItemsCanFocus(true);

        dbHelper = new ToDoDatabase(this);
        Cursor cursor;
                //= dbHelper.getTodoCats(cats2);


        String color;
        if (cats2.equals("все записи"))
        {  cursor = dbHelper.getAllTodos();
            color="";}

        else { cursor = dbHelper.getTodoCats(cats2);

            dbHelperCat = new CatDB(this);
            color = dbHelperCat.color(cats2);
        }



        if (cursor.getCount() != 0) {

            items_id = new Long[cursor.getCount()];
            items = new String[cursor.getCount()];
            category_description = new String[cursor.getCount()];
            items_status = new String[cursor.getCount()];
            items_color = new String[cursor.getCount()];

            priceInLenta= new String[cursor.getCount()];
            priceInAushan= new String[cursor.getCount()];
            priceInGigant= new String[cursor.getCount()];


            if (cursor.moveToFirst()) // если курсор не пустой
            {
                for (int i = cursor.getCount()-1; i >= 0 ; i--) {
                    items_id[i] = cursor.getLong(0);
                    items[i] = cursor.getString(2);
                    category_description[i] = cursor.getString(3);
                    items_status[i] = cursor.getString(4);
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

            for (int i = 0; i < items.length ; i++) {
                map = new HashMap<String, Object>();
                map.put("Items", items[i]);

                switch (spinerOfShopSelected) {
                    case 0:
                        map.put("Items_status", priceInLenta[i]);
                        break;
                    case 1:
                        map.put("Items_status", priceInAushan[i]);
                        break;
                    case 2:
                        map.put("Items_status", priceInGigant[i]);
                        break;
                }
             //   map.put("Items_status", items_id[i]);
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
            adapterc.notifyDataSetChanged();
            list_category.setAdapter(adapterc);

            registerForContextMenu(list_category);

            list_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, final int position, long id) {


               /*     String items_status_for_chang;
                    if (!items_status[position].equals("1")) items_status_for_chang = "1";
                    else items_status_for_chang = "0";
                    if (items_status[position].equals(null)) items_status_for_chang = "1";
                    Toast.makeText(View_cats.this, items_status_for_chang, Toast.LENGTH_SHORT).show();
                    //   dbHelper.changStatus(items[position],items_status_for_chang);
                    dbHelper.updateTodo(items_id[position], cats2, items[position],
                            category_description[position], items_status_for_chang,
                            priceInLenta[position], priceInAushan[position], priceInGigant[position]
                    );

                    Intent intent = new Intent(View_cats.this, View_cats.class);
                    intent.putExtra("CATS", cats2);
                    startActivityForResult(intent, ACTIVITY_WATCH);*/

                    Intent intent = new Intent(View_cats.this, AddPrice.class);
                    intent.putExtra("ITEM_POSITION", items_id[position]);
                    intent.putExtra("SHOP", spinerOfShopSelected);

                    startActivityForResult(intent, ACTIVITY_FROM_PRICE);

                }
            });


            String[] from = new String[]{ToDoDatabase.COLUMN_SUMMARY};
            int[] to = new int[]{R.id.label};
            adapter = new SimpleCursorAdapter(this, R.layout.list_row, null, from, to, 0);
            setListAdapter(adapter);
            // создаем лоадер для чтения данных
            getLoaderManager().initLoader(0, null, this);
            adapter.notifyDataSetInvalidated();
            adapter.notifyDataSetChanged();

        }


        else {

            ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map;
            dbHelperCat = new CatDB(this);
            color = dbHelperCat.color(cats2);
                map = new HashMap<String, Object>();

                map.put("Items", "Дабавьте элемент списка");
                map.put("Items_status", "");
                //   map.put("Items_status", items_id[i]);
                map.put("Items_color", color);

                data.add(map);

     /*       ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>(1);
            HashMap<String, Object> map;
            map = new HashMap<String, Object>();
            map.put("CatName", "Дабавьте категорию");
            map.put("Countcatof", "0");
            map.put("Color", "все записи");*/


            // Массив имен атрибутов, из которых будут читаться данные
            String[] fromC = {"Items", "Items_status", "Items_color"};
// Массив идентификаторов компонентов, в которые будем вставлять данные
            int[] toC = {R.id.item_name, R.id.price, R.id.item_of_buy_linlayout};
            SimpleAdapter adapterc = new SimpleAdapter(this, data, R.layout.view_buy_row, fromC, toC);
            // присваиваем адаптер списку
            adapterc.setViewBinder(new MyViewBinder());
            adapterc.notifyDataSetChanged();
            list_category.setAdapter(adapterc);


//////
      /*     list_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, final int position, long id) {
                  //  Intent intent = new Intent(MainActivity.this, EditCats.class);
                  //  startActivityForResult(intent, ACTIVITY_CREATE);
                }
            });

            Button allnote = (Button) findViewById(R.id.allnote);
            allnote.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                //    Intent intent = new Intent(MainActivity.this, Spisok_categoriy.class);
                    //   intent.putExtra("CATS", "allallall");
                    // активность вернет результат если будет вызван с помощью этого метода
                 //   startActivityForResult(intent, ACTIVITY_EDIT);

                }
            });*/


            String[] from = new String[]{ToDoDatabase.COLUMN_SUMMARY};
            int[] to = new int[]{R.id.label};
            adapter = new SimpleCursorAdapter(this, R.layout.list_row, null, from, to, 0);
            setListAdapter(adapter);
            // создаем лоадер для чтения данных
            getLoaderManager().initLoader(0, null, this);
            adapter.notifyDataSetInvalidated();
            adapter.notifyDataSetChanged();

        }




    }




  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            fillData();
        }
    }*/

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


             //   case R.id.price:
               //     i = ((String) data).toString();
                    // int i2 = Integer.valueOf(i);
                    //    mText = (TextView) findViewById(R.id.item_name);
                    //setTextColor


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

       //     Cursor cursor = db.getAllTodos();

            return cursor;
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }

}



