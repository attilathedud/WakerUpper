package com.attila.wakerupper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.attila.wakerupper.Factories.AnimationFactory;
import com.attila.wakerupper.Factories.NotificationFactory;
import com.attila.wakerupper.Factories.ReceiverFactory;
import com.attila.wakerupper.Factories.SharedPreferencesFactory;
import com.attila.wakerupper.Logging.DebugLogger;
import com.attila.wakerupper.R;
import com.attila.wakerupper.Services.ListenSMSService;
import com.serchinastico.coolswitch.CoolSwitch;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

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

        Integer textAmount = SharedPreferencesFactory.readInt(this, getString(R.string.text_amount_service_key));
        if( textAmount == -1 ) {
            SharedPreferencesFactory.writeInt(this, getString(R.string.text_amount_service_key), 3);
            textAmount = 3;
        }

        etTextsToReceive.setText(textAmount.toString());

        AnimationFactory.onResume(ivSubmarine);

        if( ReceiverFactory.isHandlerAttached(this) ) {
            AnimationFactory.turnOnUIEffects(bTurnOnOff, ivSubmarine, 0);
        }

        //android doesn't save & restore togglebuttons for some reason, so we need to do it manually
        boolean csTextWatchChecked = SharedPreferencesFactory.readBoolean(this, getString(R.string.text_watch_enabled_service_key));
        csTextWatch.setChecked(csTextWatchChecked);
        if( !csCallWatch.isChecked() ) {
            llDisabledViewTextWatch.setBackgroundResource(R.drawable.sky_to_orange_gradient);
        }

        boolean csCallWatchChecked = SharedPreferencesFactory.readBoolean(this, getString(R.string.call_watch_enabled_service_key));
        csCallWatch.setChecked(csCallWatchChecked);
        if( csCallWatch.isChecked() ) {
            llEnabledViewTextWatch.setBackgroundResource(R.drawable.yellow_to_sky_gradient);
            llDisabledViewTextWatch.setBackgroundResource(R.color.colorLightSky);
        }
        else {
            llEnabledViewTextWatch.setBackgroundResource(R.drawable.yellow_to_orange_gradient);
            llDisabledViewTextWatch.setBackgroundResource(R.drawable.sky_to_orange_gradient);
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

        if( bTurnOnOff.getText().equals(getString(R.string.disable_button)) ) {
            disableHandler();
            enableHandler();
        }

        SharedPreferencesFactory.writeBoolean(this,
                getString(R.string.call_watch_enabled_service_key), csCallWatch.isChecked());
    }

    @OnClick(R.id.csTextWatch)
    public void textWatchClicked() {
        if( !csCallWatch.isChecked() ) {
            llDisabledViewTextWatch.setBackgroundResource(R.drawable.sky_to_orange_gradient);
        }

        if( bTurnOnOff.getText().equals(getString(R.string.disable_button)) ) {
            disableHandler();
            enableHandler();
        }

        SharedPreferencesFactory.writeBoolean(this,
                getString(R.string.text_watch_enabled_service_key), csTextWatch.isChecked());
    }

    @OnClick(R.id.bTurnOnOff)
    public void bTurnOnOffClicked() {
        if( bTurnOnOff.getText().equals(getString(R.string.enable_button))) {
            //since coolswitch inverts the logic, only enable if one feature is selected
            if( csTextWatch.isChecked() && csCallWatch.isChecked() )
                return;

            AnimationFactory.turnOnUIEffects(bTurnOnOff, ivSubmarine);
            enableHandler();

            if( etTextsToReceive.length() == 0 ) {
                etTextsToReceive.setText("3");
            }
        }
        else {
            AnimationFactory.turnOffUIEffects(bTurnOnOff, ivSubmarine);
            disableHandler();
        }
    }

    @OnClick(R.id.llTextSwitch)
    public void textIconClicked() {
        csTextWatch.performClick();
        textWatchClicked();
    }

    @OnClick(R.id.llCallSwitch)
    public void callIconClicked() {
        csCallWatch.performClick();
        callWatchClicked();
    }

    @OnTextChanged(R.id.etTextsToReceive)
    public void onTextChanged(CharSequence text) {
        int textsToReceive = _safeGetTextAmount();
        SharedPreferencesFactory.writeInt(this, getString(R.string.text_amount_service_key), textsToReceive);
    }

    private int _safeGetTextAmount() {
        int textsToReceive = -1;

        if( etTextsToReceive != null && etTextsToReceive.getText().length() != 0) {
            textsToReceive = Integer.valueOf(etTextsToReceive.getText().toString());
        }

        if( textsToReceive == -1 ) {
            textsToReceive = 3;
        }

        return textsToReceive;
    }

    private void enableHandler() {
        //only enable if one feature is selected
        if( csCallWatch.isChecked() && csTextWatch.isChecked() )
            return;

        //coolswitch toggles seem to reverse on and off from a UI perspective
        ReceiverFactory.bindHandler(this, !csTextWatch.isChecked(), !csCallWatch.isChecked());
        NotificationFactory.enableNotification(this, MainActivity.class,
                !csTextWatch.isChecked(), !csCallWatch.isChecked());

        Intent intent = new Intent(MainActivity.this, ListenSMSService.class);
        startService(intent);
    }

    private void disableHandler() {
        ReceiverFactory.unbindHandler(this);
        NotificationFactory.disableNotification(this);

        Intent intent = new Intent(MainActivity.this, ListenSMSService.class);
        stopService(intent);
    }

}
