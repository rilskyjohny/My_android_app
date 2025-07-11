package su.rj.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import su.rj.myapplication.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment
{
    List<Rq> rqs;
    List<Rq.SubRq> subRqs;
    FragmentMainBinding fmb;
    static public Rq.AppDatabase db = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        rqs = new ArrayList<>();
        subRqs = new ArrayList<>();
        if(context.getResources().getBoolean(R.bool.devel_inMemoryDatabase)){
            rqs.add(new Rq("Test",new ArrayList<>(),0));
            Rq.SubRq cur = new Rq.SubRq();
            cur.parentRqId= rqs.get(0).getId();
            cur.tovarid=0;
            cur.count=0;
            cur.id=0;
            subRqs.add(new Rq.SubRq());
            rqs.get(0).getSubrqs().add(subRqs.get(0));
            MainActivity activity = (MainActivity)requireActivity();
            activity.showNotification("Warning:using in-memory database. ",2);
        } else {
            if(db==null) {
                db = Room.databaseBuilder(context.getApplicationContext(),
                        Rq.AppDatabase.class, "cust").build();
            }
            new Thread(){
                public void run(){
                    try{
                        if(db==null) {
                            db = Room.databaseBuilder(context.getApplicationContext(),
                                    Rq.AppDatabase.class, "cust").build();
                        }
                        Rq.SubRqDAO subRqDAO = db.subRqDAO();
                        subRqs = subRqDAO.getAll();
                        for(Rq.SubRq cur:subRqs){
                            for(Rq curRq:rqs){
                                if(cur.parentRqId==curRq.getId()){
                                    curRq.getSubrqs().add(cur);
                                    curRq.setCreds(cur.creds);
                                }
                            }
                        }
                    } catch (Exception t){
                        Log.e("Load rqs","Some error ocurred. ",t);
                        MainFragment.this.requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ErrorFragment.scareUser(t,getContext(),requireActivity().getSupportFragmentManager());
                            }
                        });
                    }
                }
            }.start();
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
        fmb.fragmentMainRecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        fmb.fragmentMainRecyclerview.setAdapter(new MyAdapter(this));
        fmb.fragmentMainButtonClear.setOnClickListener(v -> {
            Log.i("MainFragment","Clear");
            int len=MainFragment.this.rqs.size();
            MainFragment.this.rqs.clear();
            MainFragment.this.subRqs.clear();
            Objects.requireNonNull(MainFragment.this.fmb.fragmentMainRecyclerview.getAdapter()).notifyItemRangeRemoved(0,len);
        });
        fmb.fragmentMainButtonAdd.setOnClickListener(v -> {
            Log.i("MainFragment","Add");
            int len=MainFragment.this.rqs.size();
            Objects.requireNonNull(MainFragment.this.fmb.fragmentMainRecyclerview.getAdapter()).notifyItemInserted(len);
        });
    }
}
