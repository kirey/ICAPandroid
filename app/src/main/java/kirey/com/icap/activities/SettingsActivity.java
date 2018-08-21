package kirey.com.icap.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import kirey.com.icap.R;
import kirey.com.icap.utils.ICAPApp;

/**
 * Created by kitanoskan on 15/11/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private RadioGroup radioGroupAlerLevel;
    private NumberPicker numberPicker;
    private Button setSizeButton;
    private int size;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

       /* numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                size = newVal;
            }
        });*/

        setSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        radioGroupAlerLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton selected = (RadioButton) findViewById(checkedId);
                int index = radioGroupAlerLevel.indexOfChild(selected);
                int level = 1;
                switch (index) {
                    case 0:
                        level = 1;
                        break;
                    case 1:
                        level = 2;
                        break;
                    case 2:
                        level = 3;
                        break;
                }
                ((ICAPApp)getApplicationContext()).setAlertLevel(level);
            }
        });




    }

    private void init() {

        setSizeButton = (Button) findViewById(R.id.setSizeButton);
        radioGroupAlerLevel = (RadioGroup) findViewById(R.id.radioGroupAlertLevel);
        int currentLevel = ((ICAPApp)getApplicationContext()).getAlertLevel();
        //size = ((KgriApp)getApplicationContext()).getMaxSizMessageStorage();

        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setValue(((ICAPApp)getApplicationContext()).getMaxSizMessageStorage());

        RadioButton current =  (RadioButton) radioGroupAlerLevel.getChildAt(currentLevel-1);
        current.setChecked(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((ICAPApp) getApplicationContext()).setMaxSizeMessageStorage(numberPicker.getValue());

            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               dialog.dismiss();
            }
        });
        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_body);

        dialog = builder.create();
    }
}
