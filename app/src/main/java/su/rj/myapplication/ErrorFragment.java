package su.rj.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Arrays;

import su.rj.myapplication.databinding.FragmentErrorBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ErrorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ErrorFragment extends Fragment {
    static String exception_name;
    static String exception_stacktrace;
    FragmentErrorBinding feb;
    public ErrorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Name of error.
     * @param param2 Error e:e.toString().
     * @return A new instance of fragment ErrorFragment.
     */
    public static ErrorFragment newInstance(String param1, String param2) {
        if(param1!=null&&param2!=null) {
            ErrorFragment.exception_name = param1;
            ErrorFragment.exception_stacktrace = param2;
        }
        ErrorFragment fragment = new ErrorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        feb = FragmentErrorBinding.inflate(inflater, container, false);
        return feb.getRoot();
    }

    public void sync(View view){
        view.setAlpha(0.5F);
        if(exception_name!=null){
            feb.textViewErrorName.setText(exception_name);
        }
        if(exception_stacktrace!=null){
            feb.textViewErrorToStr.setText(exception_stacktrace);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sync(view);
    }

    public static void scareUser(Exception t, Context context, FragmentManager fm){
        Toast.makeText(context, "Some error occured.", Toast.LENGTH_SHORT).show();
        ErrorFragment.exception_name = t.getClass().getName();
        ErrorFragment.exception_stacktrace = Arrays.toString(t.getStackTrace());
        FragmentTransaction fragmentTransition = fm.beginTransaction();
        fragmentTransition.setReorderingAllowed(true);
        fragmentTransition.replace(R.id.fragmentContainerView,ErrorFragment.newInstance(null,null));
        fragmentTransition.addToBackStack(null);
        fragmentTransition.commit();
    }
}