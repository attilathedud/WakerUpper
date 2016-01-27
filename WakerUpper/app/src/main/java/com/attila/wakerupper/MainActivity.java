package com.attila.wakerupper;

import android.os.Bundle;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DebugLogger.init();
        ButterKnife.bind(this);

        //todo: move to onresume
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
        else {
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

}
