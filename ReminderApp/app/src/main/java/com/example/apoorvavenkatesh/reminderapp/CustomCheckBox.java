package com.example.apoorvavenkatesh.reminderapp;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.CheckBox;
import com.example.apoorvavenkatesh.reminderapp.R;
/**
 * Created by apoorvavenkatesh on 10/31/16.
 */
public class CustomCheckBox extends CheckBox{

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean t){
        if(t) {
            // checkbox_background is blue
           this.setBackgroundResource(R.drawable.checkbox_background);
            this.setTextColor(Color.BLACK);
        } else {
            this.setBackgroundColor(Color.TRANSPARENT);
            this.setTextColor(Color.BLACK);
        }
        super.setChecked(t);
    }
}
