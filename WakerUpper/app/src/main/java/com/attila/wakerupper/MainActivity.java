package com.attila.wakerupper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.serchinastico.coolswitch.CoolSwitch;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.csCallWatch) CoolSwitch csCallWatch;

    @Bind(R.id.enabled_view_text_watch) LinearLayout llEnabledViewTextWatch;
    @Bind(R.id.disabled_view_text_watch) LinearLayout llDisabledViewTextWatch;

    @Bind(R.id.bTurnOnOff) Button bTurnOnOff;

    @Bind(R.id.ivSubmarine) ImageView ivSubmarine;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DebugLogger.init();
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewAnimator
                                .animate(ivSubmarine)
                                .wave()
                                .duration(3000)
                                .start();
                    }
                });
            }
        };

        timer.schedule(timerTask, 5000, 5000);

        if( ReceiverFactory.isHandlerAttached(this) ) {
            turnOnUIEffects();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        timer.cancel();
        timerTask.cancel();
    }

    @OnClick(R.id.csCallWatch)
    public void callWatchClicked() {
        if( csCallWatch.isChecked() ) {
            llEnabledViewTextWatch.setBackgroundResource(R.drawable.yellow_to_sky_gradient);
            llDisabledViewTextWatch.setBackgroundResource(R.color.colorLightSky);
        }
        else {
            llEnabledViewTextWatch.setBackgroundResource(R.drawable.yellow_to_orange_gradient);
            llDisabledViewTextWatch.setBackgroundResource(R.drawable.sky_to_orange_gradient);
        }
    }

    @OnClick(R.id.csTextWatch)
    public void textWatchClicked() {
        if( !csCallWatch.isChecked() ) {
            llDisabledViewTextWatch.setBackgroundResource(R.drawable.sky_to_orange_gradient);
        }
    }

    @OnClick(R.id.bTurnOnOff)
    public void bTurnOnOffClicked() {
        if( bTurnOnOff.getText().equals(getString(R.string.enable_button))) {
            turnOnUIEffects();
            ReceiverFactory.bindHandler(this);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Monitoring")
                            .setContentText("Checking for texts and phone calls")
                            .setOngoing(true);

            Intent resultIntent = new Intent(this, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(mId, mBuilder.build());
        }
        else {
            turnOffUIEffects();
            ReceiverFactory.unbindHandler(this);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(mId);
        }
    }

    private void turnOnUIEffects() {
        bTurnOnOff.setEnabled(false);
        bTurnOnOff.setClickable(false);

        ViewAnimator.animate(ivSubmarine)
                .rotation(-90)
                .fadeIn()
                .translationY(1000, 0).descelerate()
                .duration(1000)
                .thenAnimate(ivSubmarine)
                .rotation(360)
                .duration(1000)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        bTurnOnOff.setText(R.string.disable_button);
                        bTurnOnOff.setEnabled(true);
                        bTurnOnOff.setClickable(true);
                    }
                })
                .start();
    }

    private void turnOffUIEffects() {
        bTurnOnOff.setEnabled(false);
        bTurnOnOff.setClickable(false);

        ViewAnimator.animate(ivSubmarine)
                .rotation(90)
                .fadeOut()
                .translationY(0, 1000).descelerate()
                .duration(500)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        bTurnOnOff.setText(R.string.enable_button);
                        bTurnOnOff.setEnabled(true);
                        bTurnOnOff.setClickable(true);
                    }
                })
                .start();
    }

}
