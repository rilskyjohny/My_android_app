package su.rj.myapplication;

import android.content.Context;
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
import su.rj.myapplication.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.Objects;

public class MainFragment extends Fragment
{
    SQLiteOpenHelper sqoh;
    ArrayList<Rq> rqs;
    ArrayList<Rq.SubRq> subRqs;
    protected final static String dbName = "cust";
    FragmentMainBinding fmb;

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
        fmb.fragmentMainRecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        fmb.fragmentMainRecyclerview.setAdapter(new MyAdapter(this));
        fmb.fragmentMainButtonClear.setOnClickListener(v -> {
            Log.i("MainFragment","Clear");
            int len=MainFragment.this.rqs.size();
            MainFragment.this.rqs=new ArrayList<>();
            MainFragment.this.subRqs=new ArrayList<>();
            if(!requireContext().getResources().getBoolean(R.bool.devel_inMemoryDatabase)) {
                MainFragment.this.sqoh.getWritableDatabase().execSQL("DELETE FROM zakazi ");
            }
            Objects.requireNonNull(MainFragment.this.fmb.fragmentMainRecyclerview.getAdapter()).notifyItemRangeRemoved(0,len);
        });
        fmb.fragmentMainButtonAdd.setOnClickListener(v -> {
            Log.i("MainFragment","Add");
            int len=MainFragment.this.rqs.size();
            Objects.requireNonNull(MainFragment.this.fmb.fragmentMainRecyclerview.getAdapter()).notifyItemInserted(len);
        });
    }
}
