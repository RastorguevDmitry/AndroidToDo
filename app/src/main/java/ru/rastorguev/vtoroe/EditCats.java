package ru.rastorguev.vtoroe;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditCats extends Activity {

    private EditText mCatname;
    private EditText mRang;
    private Long mRowId;
    private CatDB mDbHelper;
    private Spinner mColor;

    private static final int ACTIVITY_CREATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new CatDB(this);

        setContentView(R.layout.editcat);

        mColor = (Spinner) findViewById(R.id.color);
        mCatname = (EditText) findViewById(R.id.cat_edit_summary);
        mRang = (EditText) findViewById(R.id.cat_edit_rang);

        Button confirm_and_exitButton = (Button) findViewById(R.id.cat_edit_button);
        Button confirm_and_addButton = (Button) findViewById(R.id.cat_edit_and_add);

        mRowId = null;
        Bundle extras = getIntent().getExtras();
        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(CatDB.COLUMN_ID);
        if (extras != null) {
            mRowId = extras.getLong(CatDB.COLUMN_ID);
        }

        populateFields();

        confirm_and_exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                saveState();
                setResult(RESULT_OK);
                Intent intent = new Intent(EditCats.this, MainActivity.class);
                startActivityForResult(intent, ACTIVITY_CREATE);
            }
        });
        confirm_and_addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mCatname.getText().toString())) {
                    Toast.makeText(EditCats.this, "Данные не введены",
                            Toast.LENGTH_LONG).show();
                } else {
                    saveState();
                    setResult(RESULT_OK);
                    Intent intent = new Intent(EditCats.this, EditCats.class);
                    startActivityForResult(intent, ACTIVITY_CREATE);
                }
            }
        });





    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor todo = mDbHelper.getCat(mRowId);

            String color = todo.getString(todo
                    .getColumnIndexOrThrow(CatDB.COLUMN_COLOR));

            for (int i = 0; i < mColor.getCount(); i++) {

                String s = (String) mColor.getItemAtPosition(i);
                Log.e(null, s + " " + color);
                if (s.equalsIgnoreCase(color)) {
                    mColor.setSelection(i);
                }
            }

            mCatname.setText(todo.getString(todo
                    .getColumnIndexOrThrow(CatDB.COLUMN_CATEGORY)));
            mRang.setText(todo.getString(todo
                    .getColumnIndexOrThrow(CatDB.COLUMN_PRIORITET)));
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
        String color = (String) mColor.getSelectedItem();
        String catname = mCatname.getText().toString();
        String rang = mRang.getText().toString();
        String tip = "";

        if (catname.length() == 0 && rang.length() == 0) {
            return;
        }

        if (mRowId == null) {
            long id = mDbHelper.createNewCats(catname, rang, color, tip);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateTodo(mRowId, catname, rang, color, tip);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Spisok_categoriy.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }



}