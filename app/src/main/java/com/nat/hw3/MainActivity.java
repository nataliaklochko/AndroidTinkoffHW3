package com.nat.hw3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup.LayoutParams;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    CustomView customView;
    CustomView customViewSecond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customView = (CustomView) findViewById(R.id.custom_view);
        customViewSecond = (CustomView) findViewById(R.id.custom_view_second);

        ArrayList<String> names = new ArrayList<String>() {
            {
                add("Михаил");
                add("Дмитрий");
                add("Нур");
                add("Наталья");
                add("Анастасия");
                add("Тимофей");
            }
        };

        ArrayList<String> namesSecond = new ArrayList<String>() {
            {
                add("Артур");
                add("Даниил");
                add("Алексей");
                add("Павел");
                add("Алексей");
                add("Арсений");
            }
        };

        for (String name: names) {
            final Chip nameChip = createChip(name);
            nameChip.setChipBackgroundColor(getColorStateList(R.color.green));
            customView.addView(nameChip);
            setListener(nameChip);
        }

        for (String name: namesSecond) {
            final Chip nameChip = createChip(name);
            nameChip.setChipBackgroundColor(getColorStateList(R.color.yellow));
            customViewSecond.addView(nameChip);
            setListener(nameChip);

        }

    }

    private void setListener(final Chip chip) {
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip.getParent() == customView) {
                    customView.removeView(chip);
                    customViewSecond.addView(chip);
                } else {
                    customViewSecond.removeView(chip);
                    customView.addView(chip);
                }
            }
        });
    }


    private Chip createChip(String text) {
        Chip nameChip = new Chip(this);
        nameChip.setText(text);
        nameChip.setCloseIconVisible(true);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        nameChip.setLayoutParams(lp);
        return nameChip;
    }

}
