package su.rj.myapplication;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 * This test tests resources.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ResourceIntegrityInstrumentedTest {
    @Test
    public void checkDevel() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertFalse("devel_inMemoryDatabase is a developer setting",
                appContext.getResources().getBoolean(R.bool.devel_inMemoryDatabase) &&
                        ! appContext.getResources().getBoolean(R.bool.devel_root));
    }
}