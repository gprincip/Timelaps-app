package com.example.android.camera2basic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton buttonOk;
    private RadioGroup radioGroupShootingInterval;
    private RadioGroup radioGroupVideoDuration;
    public final String SHOOTING_INTERVAL = "SHOOTING_INTERVAL";
    public final String VIDEO_DURATION = "VIDEO_DURATION";
    public EditText customIntervalEditText;
    public EditText customDurationEditText;
    boolean intervalSet = false;
    boolean durationSet = false;

    public class rbClickListenerInterval implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            customIntervalEditText.setText("");
        }
    }

    public class rbClickListenerDuration implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            customDurationEditText.setText("");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        buttonOk = findViewById(R.id.settings_activity_buttonOk);
        radioGroupShootingInterval = findViewById(R.id.settings_activity_radio_group_framerate);
        radioGroupVideoDuration = findViewById(R.id.settings_activity_radio_group_videoDuration);
        customIntervalEditText = findViewById(R.id.settings_activity_editText_custom_interval);
        customDurationEditText = findViewById(R.id.settings_activity_editText_custom_duration);

        RadioButton b1sec = findViewById(R.id.settings_activity_radio_button_1sec);
        RadioButton b1_5sec = findViewById(R.id.settings_activity_radio_button_1_5sec);
        RadioButton b2sec = findViewById(R.id.settings_activity_radio_button_2sec);
        RadioButton b3sec = findViewById(R.id.settings_activity_radio_button_3sec);
        RadioButton b5sec = findViewById(R.id.settings_activity_radio_button_5sec);

        RadioButton b10sec = findViewById(R.id.settings_activity_radio_button_10sec);
        RadioButton b30sec = findViewById(R.id.settings_activity_radio_button_30sec);
        RadioButton b1min = findViewById(R.id.settings_activity_radio_button_1min);
        RadioButton b2min = findViewById(R.id.settings_activity_radio_button_2min);
        RadioButton bdisabled = findViewById(R.id.settings_activity_radio_button_disabled);

        rbClickListenerInterval cli = new rbClickListenerInterval();
        rbClickListenerDuration cld = new rbClickListenerDuration();

        b1sec.setOnClickListener(cli);
        b1_5sec.setOnClickListener(cli);
        b2sec.setOnClickListener(cli);
        b3sec.setOnClickListener(cli);
        b5sec.setOnClickListener(cli);

        b10sec.setOnClickListener(cld);
        b30sec.setOnClickListener(cld);
        b1min.setOnClickListener(cld);
        b2min.setOnClickListener(cld);
        bdisabled.setOnClickListener(cld);

        customIntervalEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupShootingInterval.clearCheck();
            }
        });

        customDurationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupVideoDuration.clearCheck();
            }
        });

        if (CameraActivity.shootingInterval != -1) {

            switch (CameraActivity.shootingInterval) {
                case 1000:
                    radioGroupShootingInterval.check(R.id.settings_activity_radio_button_1sec);
                    intervalSet = true;
                    break;

                case 1500:
                    radioGroupShootingInterval.check(R.id.settings_activity_radio_button_1_5sec);
                    intervalSet = true;
                    break;

                case 2000:
                    radioGroupShootingInterval.check(R.id.settings_activity_radio_button_2sec);
                    intervalSet = true;
                    break;

                case 3000:
                    radioGroupShootingInterval.check(R.id.settings_activity_radio_button_3sec);
                    intervalSet = true;
                    break;

                case 5000:
                    radioGroupShootingInterval.check(R.id.settings_activity_radio_button_5sec);
                    intervalSet = true;
                    break;

                default:
                    customIntervalEditText.setText(String.valueOf((double)CameraActivity.shootingInterval / 1000));
                    radioGroupShootingInterval.clearCheck();
                    intervalSet = true;
            }

        } else {
            radioGroupShootingInterval.check(R.id.settings_activity_radio_button_1_5sec);
            intervalSet = true;
        }

        if (CameraActivity.videoDuration != -1) {

            switch (CameraActivity.videoDuration) {
                case 1000 * 10:
                    radioGroupVideoDuration.check(R.id.settings_activity_radio_button_10sec);
                    durationSet = true;
                    break;

                case 1000 * 30:
                    radioGroupVideoDuration.check(R.id.settings_activity_radio_button_30sec);
                    durationSet = true;
                    break;

                case 1000 * 60:
                    radioGroupVideoDuration.check(R.id.settings_activity_radio_button_1min);
                    durationSet = true;
                    break;

                case 1000 * 120:
                    radioGroupVideoDuration.check(R.id.settings_activity_radio_button_2min);
                    durationSet = true;
                    break;

                default:
                    customDurationEditText.setText(String.valueOf((double)CameraActivity.videoDuration / 1000));
                    radioGroupVideoDuration.clearCheck();
                    durationSet = true;
            }

        } else {
            radioGroupVideoDuration.check(R.id.settings_activity_radio_button_disabled);
            durationSet = true;
        }

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);

                int selectedShootingInterval = -1;
                int selectedVideoDuration = -1;

                switch (radioGroupShootingInterval.getCheckedRadioButtonId()) {
                    case R.id.settings_activity_radio_button_1sec:
                        selectedShootingInterval = 1000;
                        intervalSet = true;
                        break;

                    case R.id.settings_activity_radio_button_1_5sec:
                        selectedShootingInterval = 1500;
                        intervalSet = true;
                        break;

                    case R.id.settings_activity_radio_button_2sec:
                        selectedShootingInterval = 2000;
                        intervalSet = true;
                        break;

                    case R.id.settings_activity_radio_button_3sec:
                        selectedShootingInterval = 3000;
                        intervalSet = true;
                        break;

                    case R.id.settings_activity_radio_button_5sec:
                        selectedShootingInterval = 5000;
                        intervalSet = true;
                        break;
                }

                switch (radioGroupVideoDuration.getCheckedRadioButtonId()) {
                    case R.id.settings_activity_radio_button_10sec:
                        selectedVideoDuration = 1000 * 10;
                        durationSet = true;
                        break;

                    case R.id.settings_activity_radio_button_30sec:
                        selectedVideoDuration = 1000 * 30;
                        durationSet = true;
                        break;

                    case R.id.settings_activity_radio_button_1min:
                        selectedVideoDuration = 1000 * 60;
                        durationSet = true;
                        break;

                    case R.id.settings_activity_radio_button_2min:
                        selectedVideoDuration = 1000 * 120;
                        durationSet = true;
                        break;

                    case R.id.settings_activity_radio_button_disabled:
                        selectedVideoDuration = -1;
                        durationSet = true;
                        break;
                }

                String interval = customIntervalEditText.getText().toString();

                switch (interval) {
                    case "1":
                        radioGroupShootingInterval.check(R.id.settings_activity_radio_button_1sec);
                        selectedShootingInterval = 1000;
                        intervalSet = true;
                        break;

                    case "1.5":
                        radioGroupShootingInterval.check(R.id.settings_activity_radio_button_1_5sec);
                        selectedShootingInterval = 1500;
                        intervalSet = true;
                        break;

                    case "2":
                        radioGroupShootingInterval.check(R.id.settings_activity_radio_button_2sec);
                        selectedShootingInterval = 2000;
                        intervalSet = true;
                        break;

                    case "3":
                        radioGroupShootingInterval.check(R.id.settings_activity_radio_button_3sec);
                        selectedShootingInterval = 3000;
                        intervalSet = true;
                        break;

                    case "5":
                        radioGroupShootingInterval.check(R.id.settings_activity_radio_button_5sec);
                        selectedShootingInterval = 5000;
                        intervalSet = true;
                        break;

                    default:
                        if (interval.compareTo("") != 0) {
                            double d;
                            try {
                                d = Double.parseDouble(interval);
                                selectedShootingInterval = (int) (d * 1000);
                                radioGroupShootingInterval.clearCheck();
                                intervalSet = true;
                            } catch (NumberFormatException e) {
                                Toast.makeText(getApplicationContext(), "Entered value is not a number", Toast.LENGTH_SHORT).show();
                                intervalSet = false;
                            }
                        }
                }

                String duration = customDurationEditText.getText().toString();

                switch (duration) {
                    case "10":
                        radioGroupVideoDuration.check(R.id.settings_activity_radio_button_10sec);
                        selectedVideoDuration = 1000 * 10;
                        durationSet = true;
                        break;

                    case "30":
                        radioGroupVideoDuration.check(R.id.settings_activity_radio_button_30sec);
                        selectedVideoDuration = 1000 * 30;
                        durationSet = true;
                        break;

                    case "60":
                        radioGroupVideoDuration.check(R.id.settings_activity_radio_button_1min);
                        selectedVideoDuration = 1000 * 60;
                        durationSet = true;
                        break;

                    case "120":
                        radioGroupVideoDuration.check(R.id.settings_activity_radio_button_2min);
                        selectedVideoDuration = 1000 * 120;
                        durationSet = true;
                        break;

                    default:
                        if (duration.compareTo("") != 0) {
                            double d;
                            try {
                                d = Double.parseDouble(duration);
                                selectedVideoDuration = (int) (d * 1000);
                                radioGroupShootingInterval.clearCheck();
                                durationSet = true;
                            } catch (NumberFormatException e) {
                                Toast.makeText(getApplicationContext(), "Entered value is not a number", Toast.LENGTH_SHORT).show();
                                durationSet = false;
                            }
                        }
                }

                CameraActivity.shootingInterval = selectedShootingInterval;
                CameraActivity.videoDuration = selectedVideoDuration;


                if (durationSet && intervalSet) startActivity(intent);
            }
        });

    }

}
