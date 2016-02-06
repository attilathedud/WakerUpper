package com.attila.wakerupper.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.attila.wakerupper.Factories.AnimationFactory;
import com.attila.wakerupper.Factories.NotificationFactory;
import com.attila.wakerupper.Factories.ReceiverFactory;
import com.attila.wakerupper.Logging.DebugLogger;
import com.attila.wakerupper.R;
import com.serchinastico.coolswitch.CoolSwitch;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.csCallWatch) CoolSwitch csCallWatch;
    @Bind(R.id.csTextWatch) CoolSwitch csTextWatch;

    @Bind(R.id.enabled_view_text_watch) LinearLayout llEnabledViewTextWatch;
    @Bind(R.id.disabled_view_text_watch) LinearLayout llDisabledViewTextWatch;

    @Bind(R.id.bTurnOnOff) Button bTurnOnOff;

    @Bind(R.id.ivSubmarine) ImageView ivSubmarine;

    @Bind(R.id.etTextsToReceive) EditText etTextsToReceive;

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

        AnimationFactory.onResume(ivSubmarine);

        if( ReceiverFactory.isHandlerAttached(this) ) {
            AnimationFactory.turnOnUIEffects(bTurnOnOff, ivSubmarine, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        AnimationFactory.onPause();
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
            AnimationFactory.turnOnUIEffects(bTurnOnOff, ivSubmarine);

            int textsToReceive = 0;

            if( etTextsToReceive != null && etTextsToReceive.getText().length() != 0) {
                textsToReceive = Integer.valueOf(etTextsToReceive.getText().toString());
            }

            ReceiverFactory.bindHandler(this, textsToReceive);
            NotificationFactory.enableNotification(this, MainActivity.class,
                    !csTextWatch.isChecked(), !csCallWatch.isChecked());
        }
        else {
            AnimationFactory.turnOffUIEffects(bTurnOnOff, ivSubmarine);
            ReceiverFactory.unbindHandler(this);
            NotificationFactory.disableNotification(this);
        }
    }

}
