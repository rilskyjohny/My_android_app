package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MainFragment extends Fragment
{
    SQLiteOpenHelper sqoh;
    ArrayList<Rq> rqs;
    protected final static String dbName = "cust";
    private Rq.SubRq generateSubRq(Cursor cur){

    }
    public MainFragment() {
        super(R.layout.fragment_main);
        try{
            sqoh = new SQLiteOpenHelper(this.getContext(),dbName,null,0) {
                @Override
                public void onCreate(SQLiteDatabase db) {
                    db.execSQL("CREATE TABLE IF NOT EXISTS "+
                            "zakazi(subid INTEGER PRIMARY KEY AUTOINCREMENT,"+
                            "id INTEGER,tovarid INTEGER,count INTEGER,creds TEXT)");
                    db.execSQL("CREATE TABLE IF NOT EXISTS tovari(tovarid INTEGER PRIMARY KEY AUTOINCREMENT,"+
                            "name TEXT)");
                    db.execSQL("INSERT INTO tovari VALUES(?,?)", new Object[]{1, "Test"});
                    db.execSQL("INSERT INTO zakazi VALUES(?,?,?,?,?)", new Object[]{1,1,1,1,"Test"});
                }
                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    db.execSQL("DROP TABLE IF EXISTS zakazi");
                    db.execSQL("DROP TABLE IF EXISTS tovari");
                    this.onCreate(db);
                }
            };
            SQLiteDatabase db = sqoh.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT * from zakazi",null);
            LinkedList<Rq.SubRq> subRqs = new LinkedList<>();
            do {
                getColumnInt(cur, "subid");
            } while (cur.moveToNext());
            cur.close();
            throw new Exception("Hi");
        } catch (Throwable t){
            Toast.makeText(this.getContext(), "Some error occured.", Toast.LENGTH_SHORT).show();
            ErrorFragment.exception_name = t.getClass().getName();
            ErrorFragment.exception_stacktrace = Arrays.toString(t.getStackTrace());
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransition = fm.beginTransaction();
            fragmentTransition.setReorderingAllowed(true);
            fragmentTransition.replace(R.id.fragmentContainerView,ErrorFragment.newInstance(null,null));
            fragmentTransition.addToBackStack(null);
            fragmentTransition.commit();
        }
    }

    private static int getColumnInt(Cursor cur, String columnName) {
        int ci = cur.getColumnIndex(columnName);
        if (ci == -1) {
            throw new RuntimeException();
        }
        return cur.getInt(ci);
    }
}
