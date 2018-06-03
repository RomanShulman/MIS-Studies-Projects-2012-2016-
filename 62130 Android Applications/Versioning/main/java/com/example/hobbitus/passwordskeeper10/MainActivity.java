package com.example.hobbitus.passwordskeeper10;

        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.AdapterView.OnItemSelectedListener;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonAdd;
    Button buttonSearch;
    Button buttonHelp;
    ListView listView;
    ArrayAdapter<String> tempAdapter;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonHelp = (Button) findViewById(R.id.buttonHelp);
        listView = (ListView) findViewById(R.id.listView);

        buttonAdd.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonHelp.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] projecti = new String[]{dbHelper.KEY_ID, dbHelper.KEY_NAME};
//        Cursor cursor = database.query(dbHelper.TABLE_PASSWORDS, projecti, null, null, null, null, null);
        Cursor cursor = database.rawQuery("select " + dbHelper.KEY_NAME + " as _id, " + dbHelper.KEY_NAME + " from " + dbHelper.TABLE_PASSWORDS + " where " + dbHelper.KEY_STATUS + "='Active'", null);
//        Cursor cursor = database.rawQuery("select * from " + dbHelper.TABLE_PASSWORDS + " where " + dbHelper.KEY_STATUS + "='Active'", null);

/******************************SIMPLECURSORADAPTER DOESN'T WORK...NO REASON WHY****************************/
/*        String[] fromDBNames = new String[] {"_id", dbHelper.KEY_NAME};
        int[] too = new int[]{R.id.textViewName, R.id.textViewName};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.my_list_view, cursor,fromDBNames, too, 0);
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.my_list_view, cursor,new String[]{dbHelper.KEY_ID, dbHelper.KEY_NAME}, new int[]{android.R.id.text1}, 0);*/

        tempAdapter = new ArrayAdapter<String>(this, R.layout.my_list_view);
//        ListAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.my_list_view,new String[] {dbHelper.KEY_NAME});//not needed anymore

        /*************************** CRUTCH instead of proper working SimpleCursorAdapter **************************/
//         cursor = database.rawQuery("select * from " + dbHelper.TABLE_PASSWORDS + "", null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            /*int addressIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS);
            int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
            int commentsIndex = cursor.getColumnIndex(DBHelper.KEY_COMMENTS);
            int statusIndex = cursor.getColumnIndex(DBHelper.KEY_STATUS);*/
            do {
                tempAdapter.add(cursor.getString(nameIndex));
                //Log.d("mLog", "name = " + cursor.getString(nameIndex));
                              /*  ", address = " + cursor.getString(addressIndex) +
                                ", password = " + cursor.getString(passwordIndex) +
                                ", comments = " + cursor.getString(commentsIndex) +
                        ", status = " + cursor.getString(statusIndex));*/
            } while (cursor.moveToNext());
        } else
           //Log.d("mLog","0 rows");

        /**********************************************************************************************************/
        listView.setAdapter(tempAdapter);
        listView.refreshDrawableState();
//        listView.setAdapter(listAdapter);
//        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);




/*        if (adapter.getCount() >0) {

                Log.d("mLog", "name = " + (adapter.getItem(1)).toString() +
                                ", address = " + adapter.getItem(2) +
                                ", password = " + adapter.getItem(3) +
                                ", comments = " + adapter.getItem(4) +
                        ", status = " + adapter.getItem(5) +
                        ", id = " + adapter.getItem(0));

        } else
            Log.d("mLog","0 rows");*/


        if (cursor.getCount() == 0)//if database is empty - no EDITing, SEARCHing or DELETing.
        {
            buttonSearch.setEnabled(false);
            buttonSearch.setBackgroundColor(Color.DKGRAY);
//            buttonEdit.setEnabled(false);
//            buttonEdit.setBackgroundColor(Color.DKGRAY);
//            buttonDelete.setEnabled(false);
//            buttonDelete.setBackgroundColor(Color.DKGRAY);
        }
        //cursor.close();

 /*        cursor = database.rawQuery("select * from " + dbHelper.TABLE_PASSWORDS + "", null);
        if (cursor.moveToFirst()) {
            int addressIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
            int commentsIndex = cursor.getColumnIndex(DBHelper.KEY_COMMENTS);
            int statusIndex = cursor.getColumnIndex(DBHelper.KEY_STATUS);
            do {
                Log.d("mLog", "name = " + cursor.getString(nameIndex) +
                                ", address = " + cursor.getString(addressIndex) +
                                ", password = " + cursor.getString(passwordIndex) +
                                ", comments = " + cursor.getString(commentsIndex) +
                        ", status = " + cursor.getString(statusIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");*/

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra(dbHelper.KEY_NAME, listView.getItemAtPosition((int) (id)).toString());
                startActivity(intent);
                //check
//                Log.d("mLog", "itemClick: position = " + position + ", id = " + id + "name: " + listView.getItemAtPosition((int)(id)).toString());
            }

        });
        listView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                Log.d("mLog", "itemSelect: position = " + position + ", id = "
//                        + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Log.d("mLog", "itemSelect: Nothing");
            }
        });

//        cursor.close();
/*        // Create a message handling object as an anonymous class.
        private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Do something in response to the click
            }
        };
                listView.setOnItemClickListener(mMessageClickedHandler);*/
    }

    @Override
//    Implementation of ClickListeners of the buttons with SWITCH-CASE.
    public void onClick(View v) {
        Intent intent = new Intent(this, DetailsActivity.class);
        //Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);

        switch (v.getId()) {
            case R.id.buttonAdd://ADD new record
                startActivityForResult(intent, 1);
                break;
/*            case R.id.buttonEdit://EDIT existing record
                //intent.putExtra(listView.getSelectedItem().toString(),(int)(listView.getSelectedItemId()));
                intent.putExtra("name", listView.getSelectedItem().toString());
                startActivity(intent);*/
//                break;
            case R.id.buttonSearch://Search through existing records
                startActivity(intent);
                break;
/*            case R.id.buttonDelete://DELETE existing record
                startActivity(intent);*/
//                break;
            case R.id.buttonHelp://HELP - TBD.
//                startActivity(intent);
                break;
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        tempAdapter.clear();
        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] projecti = new String[]{dbHelper.KEY_ID, dbHelper.KEY_NAME};
//        Cursor cursor = database.query(dbHelper.TABLE_PASSWORDS, projecti, null, null, null, null, null);
        Cursor cursor = database.rawQuery("select " + dbHelper.KEY_NAME + " as _id, " + dbHelper.KEY_NAME + " from " + dbHelper.TABLE_PASSWORDS + " where " + dbHelper.KEY_STATUS + "='Active'", null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            /*int addressIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS);
            int usernameIndex = cursor.getColumnIndex(DBHelper.KEY_USERNAME);
            int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
            int commentsIndex = cursor.getColumnIndex(DBHelper.KEY_COMMENTS);
            int statusIndex = cursor.getColumnIndex(DBHelper.KEY_STATUS);*/
            do {
                tempAdapter.add(cursor.getString(nameIndex));
//                Log.d("mLog", "name = " + cursor.getString(nameIndex));// +
                                /*", address = " + cursor.getString(addressIndex) +
                                ", username = " + cursor.getString(usernameIndex) +
                                ", password = " + cursor.getString(passwordIndex) +
                                ", comments = " + cursor.getString(commentsIndex) +
                        ", status = " + cursor.getString(statusIndex));*/
            } while (cursor.moveToNext());
        } else ;
//            Log.d("mLog","0 rows");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            tempAdapter.notifyDataSetChanged();
//            listView.deferNotifyDataSetChanged();
            return;
        }

    }
}
