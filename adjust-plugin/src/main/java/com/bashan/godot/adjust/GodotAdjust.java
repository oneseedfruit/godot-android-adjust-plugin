package com.bashan.godot.adjust;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.LogLevel;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.UsedByGodot;

public class GodotAdjust extends GodotPlugin {

    private static final String TAG = "godot-adjust";

    private final Activity activity;

    public GodotAdjust(Godot godot) {
        super(godot);
        activity = godot.getActivity();
    }

    @UsedByGodot
    public void init(final String appToken, boolean isProduction) {
        Log.i(TAG, "Started initializing GodotAdjust Singleton with App Token: " + appToken);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String environment = isProduction ?
                        AdjustConfig.ENVIRONMENT_PRODUCTION :
                        AdjustConfig.ENVIRONMENT_SANDBOX;

                AdjustConfig config = new AdjustConfig(activity.getApplicationContext(), appToken, environment);
                config.setLogLevel(LogLevel.VERBOSE);

                Adjust.onCreate(config);

                activity.getApplication().registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());

                Log.i(TAG, "Finished initializing GodotAdjust Singleton");
            }
        });
    }

    private static final class AdjustLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }

    @NonNull
    @Override
    public String getPluginName() {
        return "GodotAdjust";
    }
}
