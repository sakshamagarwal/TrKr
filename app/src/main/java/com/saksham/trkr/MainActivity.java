package com.saksham.trkr;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTV);
        final int[] to = new int[] {android.R.id.text1};
        final String[] from = new String[] {DBHelper.COL_DATA};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
                from,
                to,
                0);

        textView.setAdapter(adapter);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                dbHelper.open();
                Cursor c = dbHelper.getData(constraint.toString());
                int a = c.getCount();
                return c;
            }
        });

        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex(DBHelper.COL_DATA));
            }
        });

        Button b = (Button)findViewById(R.id.add_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddData.class));
            }
        });
    }
}
