package com.saksham.trkr;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class AddData extends AppCompatActivity {
    DBHelper helper;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        new InsertTask().execute();
        tv = (TextView)findViewById(R.id.wait_text_view);

    }

    private class InsertTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            helper = new DBHelper(AddData.this);
            helper.open();
            helper.deleteData();
            helper.insert("my_dict.txt");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Cursor c = helper.getData();
            Toast.makeText(AddData.this, "" + c.getCount(), Toast.LENGTH_LONG).show();
            tv.setText("Data sucessfully added\n row_count = " + c.getCount() );
        }
    }
}
