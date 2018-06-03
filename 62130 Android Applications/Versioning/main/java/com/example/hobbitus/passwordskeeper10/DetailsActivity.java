package com.example.hobbitus.passwordskeeper10;

        import android.content.ContentValues;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.text.Editable;
        import android.text.InputType;
        import android.text.TextWatcher;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.TextView;
        import android.widget.ToggleButton;

public class DetailsActivity extends AppCompatActivity {

    Button buttonSave;
    Button buttonCancel;
    Button buttonErase;
    ToggleButton buttonShowPass;
    TextView textName;
    TextView textAddress;
    TextView textUsername;
    TextView textPassword;
    TextView textComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonErase = (Button) findViewById(R.id.buttonErase);
        buttonShowPass = (ToggleButton) findViewById(R.id.buttonShowP);

        textName = (TextView) findViewById(R.id.textViewName);
        textName.setEnabled(true);
        textAddress = (TextView) findViewById(R.id.textViewAddress);

        textUsername = (TextView) findViewById(R.id.textViewUsername);

        textPassword = (TextView) findViewById(R.id.textViewPassword);
        textComments = (TextView) findViewById(R.id.textViewComments);

        final DBHelper dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        //getting intentExtra to understand if it will be new record (ADD) or existing (EDIT)
        Intent intent = getIntent();
        String nameFromMain = intent.getStringExtra(dbHelper.KEY_NAME);
        //Log.d("mLog", "got from intent : " + nameFromMain);


        final Cursor cursor = database.rawQuery("select * from " + dbHelper.TABLE_PASSWORDS + " where " + dbHelper.KEY_STATUS + "='Active' and " + dbHelper.KEY_NAME + "='" + nameFromMain + "'", null);
//
//        String[] fromDBNames = new String[] {"_id", dbHelper.KEY_NAME};
//        int[] too = new int[]{R.id.textViewAddress, R.id.textViewName};

//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.my_list_view, cursor,fromDBNames, too, 0);

        final ContentValues contentValues = new ContentValues();
        //Log.d("mLog", "how many rows in cursor : " + cursor.getCount());
        if (cursor.getCount() == 1)//EDIT
        {
            cursor.moveToFirst();
            textName.setText(cursor.getString(cursor.getColumnIndex(dbHelper.KEY_NAME)));
            textName.setEnabled(false);//Name is our Primary Key, we don't allow to change it.
            textAddress.setText(cursor.getString(cursor.getColumnIndex(dbHelper.KEY_ADDRESS)));
            textUsername.setText(cursor.getString(cursor.getColumnIndex(dbHelper.KEY_USERNAME)));
            textPassword.setText(cursor.getString(cursor.getColumnIndex(dbHelper.KEY_PASSWORD)));
            textComments.setText(cursor.getString(cursor.getColumnIndex(dbHelper.KEY_COMMENTS)));
        } else //ADD
        {
            textName.setText("");
            textAddress.setText("");
            textUsername.setText("");
            textPassword.setText("");
            textComments.setText("");

        }
/*--------------- Text Change Listeners. if TextView changes - background become yellow, and changed string entered into ContentValue------*/
        textAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textAddress.setBackgroundColor(Color.parseColor("#FFF8F00E"));
                contentValues.put(dbHelper.KEY_ADDRESS, textAddress.getText().toString());

            }
        });
        textUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textUsername.setBackgroundColor(Color.parseColor("#FFF8F00E"));
                contentValues.put(dbHelper.KEY_USERNAME, textUsername.getText().toString());


            }
        });
        textPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textPassword.setBackgroundColor(Color.parseColor("#FFF8F00E"));
                contentValues.put(dbHelper.KEY_PASSWORD, textPassword.getText().toString());

            }
        });
        textComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textComments.setBackgroundColor(Color.parseColor("#FFF8F00E"));
                contentValues.put(dbHelper.KEY_COMMENTS, textComments.getText().toString());

            }
        });
/*----------------------------------------------------------------------------------------------------------------------*/

        //Saves the changes ( new or existing record)
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((cursor.getCount()) == 1)//EDIT
                {
//                    contentValues.put(dbHelper.KEY_NAME, textName.getText().toString());//No need in Name, it doesn't change, it's Primal Key
                    contentValues.put(dbHelper.KEY_STATUS, "Active");

                    int updCount = database.update(dbHelper.TABLE_PASSWORDS, contentValues, dbHelper.KEY_NAME + "= ?", new String[]{textName.getText().toString()});
//                    Log.d("mLog", "updated rows count = " + updCount);
                } else //ADD
                {
                    contentValues.clear();
                    contentValues.put(dbHelper.KEY_NAME, textName.getText().toString());
                    contentValues.put(dbHelper.KEY_ADDRESS, textAddress.getText().toString());
                    contentValues.put(dbHelper.KEY_USERNAME, textUsername.getText().toString());
                    contentValues.put(dbHelper.KEY_PASSWORD, textPassword.getText().toString());
                    contentValues.put(dbHelper.KEY_COMMENTS, textComments.getText().toString());
                    contentValues.put(dbHelper.KEY_STATUS, "Active");

                    long instCount = database.insert(dbHelper.TABLE_PASSWORDS, null, contentValues);
//                    Log.d("mLog", "added rows count = " + instCount);
                }
                Intent intent2 = new Intent();
                setResult(RESULT_OK, intent2);
                finish();
            }
        });
        //Discards the changes in editing
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent();
                setResult(RESULT_CANCELED, intent3);
                finish();
            }
        });
        //DELETE existing record
        buttonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int delCount = database.delete(dbHelper.TABLE_PASSWORDS, dbHelper.KEY_NAME + "= '" + textName.getText().toString() + "'", null);

//                Log.d("mLog", "deleted rows count = " + delCount);
                Intent intent3 = new Intent();
                setResult(RESULT_OK, intent3);
                finish();
            }
        });
        //Shows or Hides password.
        buttonShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    textPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // The toggle is disabled
                    textPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

    }
}

