package com.attila.wakerupper.Factories;


import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.attila.wakerupper.R;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.Timer;
import java.util.TimerTask;

public class AnimationFactory {
    private static Timer _timer;
    private static TimerTask _timerTask;
    private static final Handler _handler = new Handler();

    public static void onResume(final View element) {
        _timer = new Timer();

        _timerTask = new TimerTask() {
            @Override
            public void run() {
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewAnimator
                                .animate(element)
                                .wave()
                                .duration(3000)
                                .start();
                    }
                });
            }
        };

        _timer.schedule(_timerTask, 3000, 2500);
    }

    public static void onPause() {
        _timer.cancel();
        _timerTask.cancel();

        _timer = null;
        _timerTask = null;
    }

    public static void turnOnUIEffects( final Button bTurnOnOff, final View element, int... duration ) {
        int _duration = duration.length > 0 ? duration[0] : 1000;

        bTurnOnOff.setEnabled(false);
        bTurnOnOff.setClickable(false);
        bTurnOnOff.setText(R.string.disable_button);

        ViewAnimator.animate(element)
                .rotation(-90)
                .fadeIn()
                .translationY(1000, 0).descelerate()
                .duration(_duration)
                .thenAnimate(element)
                .rotation(360)
                .duration(_duration)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        bTurnOnOff.setEnabled(true);
                        bTurnOnOff.setClickable(true);
                    }
                })
                .start();
    }

    public static void turnOffUIEffects( final Button bTurnOnOff, final View element ) {
        bTurnOnOff.setEnabled(false);
        bTurnOnOff.setClickable(false);
        bTurnOnOff.setText(R.string.enable_button);

        ViewAnimator.animate(element)
                .rotation(90)
                .fadeOut()
                .translationY(0, 1000).descelerate()
                .duration(500)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        bTurnOnOff.setEnabled(true);
                        bTurnOnOff.setClickable(true);
                    }
                })
                .start();
    }
}
