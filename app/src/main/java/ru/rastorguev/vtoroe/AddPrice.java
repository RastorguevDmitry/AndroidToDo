package ru.rastorguev.vtoroe;

//package ru.alexanderklimov.todo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.view.inputmethod.InputMethodManager;

import android.text.InputType;

public class AddPrice extends Activity {

    private EditText mAddPrice;
    private EditText mBodyText;
    private Long mRowId;
    private ToDoDatabase mDbHelper;
    private CatDB dbHelperCat;
    private Spinner mSpinerShops;
    private String mCats;
    private static String cats2;
    int spiner_selected;

    private static final int ACTIVITY_FROM_PRICE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new ToDoDatabase(this);

        setContentView(R.layout.add_price);


       mRowId = getIntent().getExtras().getLong("ITEM_POSITION");
        spiner_selected = getIntent().getExtras().getInt("SHOP");

        mCats = getIntent().getExtras().getString("CATS");



        mSpinerShops = (Spinner) findViewById(R.id.spinner);
        List<String> List_of_cat;
        ArrayAdapter<String> myAdapter1;
        List_of_cat = new ArrayList<String>();
      //  spiner_selected=1;
        List_of_cat.add("лента");
        List_of_cat.add("ашан");
        List_of_cat.add("гигант");
        myAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, List_of_cat);
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinerShops.setAdapter(myAdapter1);
        mSpinerShops.setSelection(spiner_selected);

        mSpinerShops.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int ItemSelected, long selectedId) {

                spiner_selected=ItemSelected;

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        mAddPrice = (EditText) findViewById(R.id.add_price);
   //     mAddPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);



        Button add_price_button = (Button) findViewById(R.id.add_price_button);
        add_price_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                saveState();
                setResult(RESULT_OK);
                Intent intent = new Intent(AddPrice.this, View_cats.class);
                intent.putExtra("CATS", mCats);
                setResult(RESULT_OK, intent);
                finish();
             //   startActivityForResult(intent, ACTIVITY_FROM_PRICE);
            }
        });

   /*     mRowId = null;
        Bundle extras = getIntent().getExtras();
        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(ToDoDatabase.COLUMN_ID);
        if (extras != null) {
            mRowId = extras.getLong(ToDoDatabase.COLUMN_ID);
        }

        populateFields();*/




    }

  /*  private void populateFields() {
        if (mRowId != null) {
            Cursor todo = mDbHelper.getTodo(mRowId);

            String category = todo.getString(todo
                    .getColumnIndexOrThrow(ToDoDatabase.COLUMN_CATEGORY));

           for (int i = 0; i < mCategory.getCount(); i++) {

                String s = (String) mCategory.getItemAtPosition(i);
                Log.e(null, s + " " + category);
                if (s.equalsIgnoreCase(category)) {
                    mCategory.setSelection(i);
                }
            }


            mTitleText.setText(todo.getString(todo
                    .getColumnIndexOrThrow(ToDoDatabase.COLUMN_SUMMARY)));
            mBodyText.setText(todo.getString(todo
                    .getColumnIndexOrThrow(ToDoDatabase.COLUMN_DESCRIPTION)));
            todo.close();
        }
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // saveState();
        // outState.putSerializable(ToDoDatabase.COLUMN_ID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //populateFields();
    }

    private void saveState() {
     //   String category = (String) mSpinerShops.getSelectedItem();
        mRowId = getIntent().getExtras().getLong("ITEM_POSITION");
        String price = mAddPrice.getText().toString();

        if (price.length() == 0 )   return;

        switch (spiner_selected) {
            case 0:
                mDbHelper.updateTodoPriceLenta(mRowId, price);
                break;
            case 1:
                mDbHelper.updateTodoPriceAushan(mRowId, price);
                break;
            case 2:
                mDbHelper.updateTodoPriceGigant(mRowId, price);
                break;
        }

    }
}