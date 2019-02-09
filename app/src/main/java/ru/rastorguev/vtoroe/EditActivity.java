package ru.rastorguev.vtoroe;



        import android.content.Intent;
        import android.os.Bundle;
        import android.app.Activity;
        import android.database.Cursor;
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

public class EditActivity extends Activity {

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private ToDoDatabase mDbHelper;
    private CatDB dbHelperCat;
    private Spinner mCategory;
    private String mCats;
    private static String cats2;
    int spiner_selected;

    private static final int ACTIVITY_CREATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new ToDoDatabase(this);

        setContentView(R.layout.edit);

        mCategory = (Spinner) findViewById(R.id.category);
        List<String> List_of_cat;
        ArrayAdapter<String> myAdapter1;
        List_of_cat = new ArrayList<String>();
        dbHelperCat = new CatDB(this);
        Cursor cursor = dbHelperCat.getAllCatsByOrder();
        int columnIndex = 1;
     //   List_of_cat.add("все записи");


      //  mCats = getIntent().getExtras().getString("CATS11");
    //    cats2=mCats;
        spiner_selected=1;


        if (cursor.moveToFirst()) // если курсор не пустой
        { for (int i = 0; i < cursor.getCount(); i++)
        {  List_of_cat.add(cursor.getString(columnIndex));
         //   if (cats2.equals(cursor.getString(columnIndex))) {spiner_selected=i;}
            cursor.moveToNext();      }      }
        cursor.close();
        dbHelperCat.close();
        myAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, List_of_cat);
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategory.setAdapter(myAdapter1);
        mCategory.setSelection(spiner_selected);




        mTitleText = (EditText) findViewById(R.id.todo_edit_summary);
        mBodyText = (EditText) findViewById(R.id.todo_edit_description);

        Button confirm_and_exitButton = (Button) findViewById(R.id.todo_edit_button);
        Button confirm_and_addButton = (Button) findViewById(R.id.todo_edit_and_add);

        mRowId = null;
        Bundle extras = getIntent().getExtras();
        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(ToDoDatabase.COLUMN_ID);
        if (extras != null) {
            mRowId = extras.getLong(ToDoDatabase.COLUMN_ID);
        }

        populateFields();

        confirm_and_exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                saveState();
                setResult(RESULT_OK);
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivityForResult(intent, ACTIVITY_CREATE);
            }
        });
        confirm_and_addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mTitleText.getText().toString())) {
                    Toast.makeText(EditActivity.this, "Данные не введены",
                            Toast.LENGTH_LONG).show();
                } else {
                    saveState();
                    setResult(RESULT_OK);
                    Intent intent = new Intent(EditActivity.this, EditActivity.class);
                    startActivityForResult(intent, ACTIVITY_CREATE);
                }
            }
        });





    }

    private void populateFields() {
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
    }

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
        populateFields();
    }

    private void saveState() {
        String category = (String) mCategory.getSelectedItem();
        String summary = mTitleText.getText().toString();
        String description = mBodyText.getText().toString();
        String status = "0";

        if (description.length() == 0 && summary.length() == 0) {
            return;
        }
        if (mRowId == null) {
            long id=0;// = mDbHelper.createNewTodo(category, summary, description, status);
            if (id > 0) {
                mRowId = id;
            }
        } else {
         //   mDbHelper.updateTodo(mRowId, category, summary, description, status);
        }
    }
}