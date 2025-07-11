package su.rj.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import su.rj.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding amb;
    public final NotificationChannel channel;
    private static final String CHANNEL_ID = "Indev_notify";
    public MainActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID,"Channel name", NotificationManager.IMPORTANCE_LOW);
        }
        else {
            channel=null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        amb = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(amb.getRoot());
        boolean inDev = getApplicationContext().getResources().getBoolean(R.bool.devel_root);
        if(inDev){
            Toast.makeText(getApplicationContext(),R.string.devel_notify_root,Toast.LENGTH_LONG).show();
            showNotification("Developer mode on",0);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        amb.activityMainBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment targetFragment = null;
                if (item.getItemId() == R.id.bottom_navigation_menu_page2_settings) {
                    targetFragment=new SettingsFragment();
                } else if (item.getItemId() == R.id.bottom_navigation_menu_page1_orders){
                    targetFragment=new MainFragment();
                }
                if(Objects.nonNull(targetFragment)) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.activity_main_fragmentContainerView, targetFragment);
                    transaction.commit();
                }
                return true;
            }
        });
    }
    public void showNotification(String text,int id) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.settings)
                            .setContentTitle("Developer mode warning")
                            .setContentText(text);
            notificationManager.notify(id, builder.build());
        } else {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.settings)
                            .setContentTitle("Developer mode warning")
                            .setContentText(text)
                            .setVibrate(new long[]{1,5,8,4,2,8,555555,64000});
            notificationManager.notify(id, builder.build());
        }
    }
}