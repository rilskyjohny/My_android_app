package su.rj.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainFragment extends Fragment
{
    SQLiteOpenHelper sqoh;
    ArrayList<Rq> rqs;
    ArrayList<Rq.SubRq> subRqs;
    protected final static String dbName = "cust";

    private Rq.SubRq generateSubRq(@NonNull Cursor cur){
        int subid = getColumnInt(cur, "subid");
        for(Rq.SubRq subRq:subRqs){
            if(subRq.getId()==subid){
                throw new RuntimeException();
            }
        }
        Rq rq = getRq(getColumnInt(cur,"id"), cur);
        Rq.SubRq subRq = new Rq.SubRq(rq,
                subid, 0, 0);
        rq.getSubrqs().add(subRq);
        return subRq;
    }
    private Rq getRq(int id,Cursor cur) {
        for(Rq rq:rqs)
            if (id == rq.getId()) return rq;
        Rq rq = new Rq(getColumnString(cur,"creds"),
                new ArrayList<>(),id);
        rqs.add(rq);
        return rq;
    }
    public MainFragment() {
        super(R.layout.fragment_main);
        rqs = new ArrayList<>();
        subRqs = new ArrayList<>();
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
            do {
                generateSubRq(cur);
            } while (cur.moveToNext());
            for (Rq.SubRq subRq : subRqs) {
                Log.d("Hi",subRq.toString());
            }
            cur.close();
            throw new Exception("Hi");
        } catch (Exception t){
            ErrorFragment.scareUser(t,getContext(),requireActivity().getSupportFragmentManager());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new MyAdapter(rqs));
    }

    private static int getColumnInt(Cursor cur, String columnName) {
        int ci = cur.getColumnIndex(columnName);
        if (ci == -1) {
            throw new RuntimeException();
        }
        return cur.getInt(ci);
    }
    private static String getColumnString(Cursor cur, String columnName) {
        int ci = cur.getColumnIndex(columnName);
        if (ci == -1) {
            throw new RuntimeException();
        }
        return cur.getString(ci);
    }
}
