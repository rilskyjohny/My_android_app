package su.rj.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import su.rj.myapplication.databinding.FragmentMainBinding;

import java.util.ArrayList;

public class MainFragment extends Fragment
{
    SQLiteOpenHelper sqoh;
    ArrayList<Rq> rqs;
    ArrayList<Rq.SubRq> subRqs;
    protected final static String dbName = "cust";
    FragmentMainBinding fmb;
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        rqs = new ArrayList<>();
        subRqs = new ArrayList<>();
        if(context.getResources().getBoolean(R.bool.devel_inMemoryDatabase)){
            rqs.add(new Rq("Test",new ArrayList<>(),0));
            subRqs.add(new Rq.SubRq(rqs.get(0),0,0,0));
            rqs.get(0).getSubrqs().add(subRqs.get(0));
            MainActivity activity = (MainActivity)requireActivity();
            activity.showNotification("Warning:using in-memory database. ",2);
        } else {
            try{
                sqoh = new SQLiteOpenHelper(context,dbName,null,1) {
                    @Override
                    public void onCreate(SQLiteDatabase db) {
                        Log.d("Database","onCreate");
                        Log.d("Database","Create table zakazi. ");
                        db.execSQL("CREATE TABLE IF NOT EXISTS "+
                                "zakazi(subid INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                "id INTEGER,tovarid INTEGER,count INTEGER,creds TEXT)");
                        Log.d("Database","Create table tovari. ");
                        db.execSQL("CREATE TABLE IF NOT EXISTS tovari(tovarid INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                "name TEXT)");
                        db.execSQL("INSERT INTO tovari VALUES(?,?)", new Object[]{1, "Test"});
                        Log.d("Database","Inaert test values into table tovari. ");
                        db.execSQL("INSERT INTO zakazi VALUES(?,?,?,?,?)", new Object[]{1,1,1,1,"Test"});
                        Log.d("Database","Inaert test values into table zakazi. ");
                    }
                    @Override
                    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                        Log.d("Database","onUpgrade from "+oldVersion+" to "+newVersion);
                        Log.d("Database","Drop table zakazi. ");
                        db.execSQL("DROP TABLE IF EXISTS zakazi");
                        Log.d("Database","Drop table tovari. ");
                        db.execSQL("DROP TABLE IF EXISTS tovari");
                        this.onCreate(db);
                    }
                };
                SQLiteDatabase db = sqoh.getReadableDatabase();
                Cursor cur = db.rawQuery("SELECT * from zakazi",null);
                do {
                    String s = "Column names:[";
                    for(String i:cur.getColumnNames()){
                        s+=i;
                        s+=", ";
                    }
                    s+="]";
                    Log.d("Load rqs",s);
                    generateSubRq(cur);
                } while (cur.moveToNext());
                for (Rq.SubRq subRq : subRqs) {
                    Log.d("Load rqs","subRq:"+subRq.toString());
                }
                cur.close();
            } catch (Exception t){
                Log.e("Load rqs","Some error ocurred. ",t);
                ErrorFragment.scareUser(t,getContext(),requireActivity().getSupportFragmentManager());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fmb = FragmentMainBinding.inflate(inflater,container,false);
        return fmb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = fmb.fragmentMainRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new MyAdapter(rqs));
    }

    private static int getColumnInt(Cursor cur, String columnName) {
        int ci = cur.getColumnIndexOrThrow(columnName);
        if (ci == -1) {
            throw new RuntimeException();
        }
        return cur.getInt(ci);
    }
    private static String getColumnString(Cursor cur, String columnName) {
        int ci = cur.getColumnIndexOrThrow(columnName);
        if (ci == -1) {
            throw new RuntimeException();
        }
        return cur.getString(ci);
    }
}
