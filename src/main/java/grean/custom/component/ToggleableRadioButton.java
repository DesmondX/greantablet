package grean.custom.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ToggleableRadioButton extends RadioButton {
    public ToggleableRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void toggle() {
        if(isChecked()) {
            if(getParent() instanceof RadioGroup) {
                ((RadioGroup)getParent()).clearCheck();
            }
        } else {
            setChecked(true);
        }
    }
}
