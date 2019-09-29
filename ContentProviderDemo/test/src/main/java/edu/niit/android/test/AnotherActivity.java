package edu.niit.android.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AnotherActivity extends AppCompatActivity {
    private static final String PROVIDER_NAME = "edu.niit.android.sqlite.provider";
    public static final String URL = "content://" + PROVIDER_NAME + "/student";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String CLASSMATE = "classmate";
    public static final String AGE = "age";

    private EditText etName;
    private EditText etClassmate;
    private EditText etAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
        etName = findViewById(R.id.et_name);
        etClassmate = findViewById(R.id.et_classmate);
        etAge = findViewById(R.id.et_age);
    }

    public void onClickAddName(View view) {
        // Add a new student record
        ContentValues values = new ContentValues();

        values.put(NAME, etName.getText().toString());
        values.put(CLASSMATE, etClassmate.getText().toString());
        values.put(AGE, etAge.getText().toString());

        Uri uri = getContentResolver().insert(CONTENT_URI, values);
        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
    }

    public void onClickRetrieveStudent(View view) {
        Uri students = Uri.parse(URL);
        Cursor c = getContentResolver().query(students, null, null, null, "name");

        if (c != null && c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(_ID)) +
                                ", " + c.getString(c.getColumnIndex(NAME)) +
                                ", " + c.getString(c.getColumnIndex(CLASSMATE)) +
                                ", " + c.getInt(c.getColumnIndex(AGE)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
            c.close();
        }
    }
}
