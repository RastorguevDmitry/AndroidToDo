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
        import android.widget.AdapterView;
        import android.widget.Toast;
        import android.widget.LinearLayout;
        import java.util.ArrayList;
        import java.util.HashMap;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;
        import android.view.LayoutInflater;
        import android.view.ViewGroup;
        import android.view.View.OnClickListener;
        import android.widget.AdapterView.OnItemClickListener;
        import java.lang.Number;

public class MainActivity extends ListActivity implements     LoaderCallbacks<Cursor> {

    private ToDoDatabase dbHelper;
    private String[] categorys;
    private String[] categoryscolor;
    private CatDB dbHelperCat;
    private EditText mBodyText;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int ACTIVITY_WATCH = 2;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private SimpleCursorAdapter adapter;
    TextView tvName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
      this.getListView().setDividerHeight(2);
      registerForContextMenu(getListView());
        dbHelper = new ToDoDatabase(this);

        mBodyText = (EditText) findViewById(R.id.spisok_add_summary);
        fillData();
    }

    private void saveState(final String Cats) {
        String category = Cats;
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



   // public void onClick(View view) {
     //   createNewTask();}



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
                dbHelper.deleteTodo(info.id);
                // получаем новый курсор с данными
                getLoaderManager().getLoader(0).forceLoad();
                // fillData();

                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createNewTask() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }


    private void fillData() {




/*
     LinearLayout linLayout = (LinearLayout) findViewById(R.id.linearLayout1111);
        LayoutInflater ltInflater = getLayoutInflater();
        for (int i = 0; i < categorys.length; i++) {
            View item = ltInflater.inflate(R.layout.item_cats_main, linLayout, false);
            tvName = (TextView) item.findViewById(R.id.cats_name);
            tvName.setText(categorys[i]);
            tvName.setOnClickListener(new OnClickListener()
            {
              @Override
            public void onClick(View v) {
                  String summary = mBodyText.getText().toString();
                  String categoriy = tvName.getText().toString();
                  if (summary.length() == 0)
                  {
                   //   String categoriy = tvName.getText().toString();
                          Intent intent = new Intent(MainActivity.this, View_cats.class);
                          intent.putExtra("CATS", categoriy);
                          // активность вернет результат если будет вызван с помощью этого метода
                          startActivityForResult(intent, ACTIVITY_WATCH);
                  }
                  else {
                  //    if(position!=categorys.length) {
                          String description = "";
                          long id1 = dbHelper.createNewItemSpisok(categoriy, summary, description);
                          Intent intent = new Intent(MainActivity.this, MainActivity.class);
                          startActivityForResult(intent, ACTIVITY_EDIT);
                   //   }
                    /*  else {
                          Toast.makeText(MainActivity.this, "Выбирите кабегорию", Toast.LENGTH_LONG).show();
                      }
                  }
          //   Toast.makeText(MainActivity.this, "Выбирите кабегорию", Toast.LENGTH_LONG).show();
                                          }
            });
            int countcatof = dbHelper.countcats(categorys[i]);
            TextView tvPosition = (TextView) item.findViewById(R.id.count);
            tvPosition.setText(String.valueOf(countcatof));
          //  item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
        //    item.setBackgroundColor(colors[i % 2]);
            linLayout.addView(item);
        }

*/


        // находим список
        ListView list_category = (ListView) findViewById(R.id.list_category);
      //  registerForContextMenu(list_category);
        list_category.setItemsCanFocus(true);


        dbHelperCat = new CatDB(this);
        Cursor cursor = dbHelperCat.getAllCatsByOrder();


        if (cursor.getCount() != 0) {

            categorys= new String[cursor.getCount()];
            categoryscolor= new String[cursor.getCount()];
            int columnIndex = 1;
            int columnIndexcolor = 3;
            if (cursor.moveToFirst()) // если курсор не пустой
            {
                for (int i = 0; i < cursor.getCount(); i++) {
                    categorys[i] = cursor.getString(columnIndex);
                    categoryscolor[i] = cursor.getString(columnIndexcolor);
                    // заполняем
                    cursor.moveToNext();
                }
            }
            cursor.close();
            dbHelperCat.close();


            ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>(categorys.length);
            HashMap<String, Object> map;
            for (int i = 0; i < categorys.length; i++) {
                int countcatof = dbHelper.countcats(categorys[i]);
                map = new HashMap<String, Object>();
                map.put("CatName", categorys[i]);
                map.put("Countcatof", String.valueOf(countcatof));
                map.put("Color", categoryscolor[i]);

                data.add(map);
            }
            int countcatof = dbHelper.countcats("всевсевсе");
            map = new HashMap<String, Object>();
            map.put("CatName", "все записи");
            map.put("Countcatof", String.valueOf(countcatof));
            map.put("Color", "все записи");

            data.add(map);


// Массив имен атрибутов, из которых будут читаться данные
            String[] fromC = {"CatName", "Countcatof", "Color"};
// Массив идентификаторов компонентов, в которые будем вставлять данные
            int[] toC = {R.id.cats_name, R.id.count, R.id.cats_name_linlayout};
            SimpleAdapter adapterc = new SimpleAdapter(this, data, R.layout.item_cats_main, fromC, toC);
            // присваиваем адаптер списку
            adapterc.setViewBinder(new MyViewBinder());
            list_category.setAdapter(adapterc);


            list_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, final int position, long id) {
                    String summary = mBodyText.getText().toString();
                    if (summary.length() == 0) {
                        if (position != categorys.length) {
                            Intent intent = new Intent(MainActivity.this, View_cats.class);
                            intent.putExtra("CATS", categorys[position]);
                            // активность вернет результат если будет вызван с помощью этого метода
                            startActivityForResult(intent, ACTIVITY_WATCH);
                        } else {
                            Intent intent = new Intent(MainActivity.this, View_cats.class);
                            intent.putExtra("CATS", "все записи");
                            // активность вернет результат если будет вызван с помощью этого метода
                            startActivityForResult(intent, ACTIVITY_EDIT);
                        }
                    } else {
                        if (position != categorys.length) {
                            String description = "";
                            String status = "";
                            String priceInLenta= "";
                            String priceInAushan= "";
                            String priceInGigant= "";

                            long id1 = dbHelper.createNewItemSpisok(categorys[position], summary, description, status,
                                    priceInLenta,priceInAushan,priceInGigant);
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivityForResult(intent, ACTIVITY_EDIT);
                        } else {
                            Toast.makeText(MainActivity.this, "Выбирите кабегорию", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

           Button view_buy = (Button) findViewById(R.id.view_buy);
         /*   view_buy.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, View_buy.class);
                    //   intent.putExtra("CATS", "allallall");
                    // активность вернет результат если будет вызван с помощью этого метода
                    startActivityForResult(intent, ACTIVITY_EDIT);
                }
            });*/


            Button allnote = (Button) findViewById(R.id.allnote);
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
                }
            });


            String[] from = new String[]{ToDoDatabase.COLUMN_SUMMARY};
            int[] to = new int[]{R.id.label};

            adapter = new SimpleCursorAdapter(this, R.layout.list_row, null, from,
                    to, 0);
            setListAdapter(adapter);

            // создаем лоадер для чтения данных
            getLoaderManager().initLoader(0, null, this);
            adapter.notifyDataSetInvalidated();
            adapter.notifyDataSetChanged();


        }

        else {



            ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>(1);
            HashMap<String, Object> map;
            map = new HashMap<String, Object>();
            map.put("CatName", "Дабавьте категорию");
            map.put("Countcatof", "0");
            map.put("Color", "все записи");

            data.add(map);


// Массив имен атрибутов, из которых будут читаться данные
            String[] fromC = {"CatName"};
// Массив идентификаторов компонентов, в которые будем вставлять данные
            int[] toC = {R.id.cats_name};
            SimpleAdapter adapterc = new SimpleAdapter(this, data, R.layout.item_cats_main, fromC, toC);
            // присваиваем адаптер списку
            adapterc.setViewBinder(new MyViewBinder());
            list_category.setAdapter(adapterc);


            list_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, final int position, long id) {
                    Intent intent = new Intent(MainActivity.this, EditCats.class);
                    startActivityForResult(intent, ACTIVITY_CREATE);
                }
            });

            Button allnote = (Button) findViewById(R.id.allnote);
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
                }
            });


          /*  String[] from = new String[]{"qwerty"};
            int[] to = new int[]{R.id.label};

            adapter = new SimpleCursorAdapter(this, R.layout.list_row, null, from,
                    to, 0);
            setListAdapter(adapter);

            // создаем лоадер для чтения данных
            getLoaderManager().initLoader(0, null, this);
            adapter.notifyDataSetInvalidated();
            adapter.notifyDataSetChanged();
            */

            String[] from = new String[]{ToDoDatabase.COLUMN_SUMMARY};
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


    class MyViewBinder implements SimpleAdapter.ViewBinder {

      /*  int red = getResources().getColor(R.color.yellowColor);
        int orange = getResources().getColor(R.color.orang);
        int green = getResources().getColor(R.color.persik);*/

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            String i;

            switch (view.getId()) {
                // LinearLayout
                case R.id.cats_name_linlayout:
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


                /*    switch (i)
                {
                    case "yellowColor":
                        view.setBackgroundColor(getResources().getColor(R.color.yellowColor));
                    case "orang":
                        view.setBackgroundColor(getResources().getColor(R.color.orang));
                    case "persik":
                        view.setBackgroundColor(getResources().getColor(R.color.persik));
                    case "serovatorosoviy":
                        view.setBackgroundColor(getResources().getColor(R.color.serovatorosoviy));
                    case "fistashkoviy":
                        view.setBackgroundColor(getResources().getColor(R.color.fistashkoviy));
                    }*/


               /*     //count.setText(String.valueOf(countcatof));
                  //  tvName.getText();

                    i = "yellowColor";

                   // int color = dbHelperCat.color(i);
                    String q;
                    q="getResources().getColor(R.color."+i+")";
                 //   int iq = q.intValue();
                    int i2 = Integer.valueOf(q);

                    int iq = getResources().getColor(R.color.yellowColor);
                  //  int orange = getResources().getColor(R.color.Orange);
                    view.setBackgroundColor(i2);
                  //  getResources().getColor(R.color.Orange);

                   // view.setBackgroundColor(i);

                 /*   if ( i.equals("Общее")) view.setBackgroundColor(green);
                    else
                    if (i.equals("Ремонт")) view.setBackgroundColor(orange); else
                        view.setBackgroundColor(red);*/
                    return true;

            }
            return false;
        }
    }







    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
        intent.putExtra("CATS11", "0");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(ToDoDatabase.COLUMN_ID, id);
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
        dbHelper.close();
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
            Cursor cursor = db.getAllTodos();

            return cursor;
        }
    }
}