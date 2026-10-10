package com.example.speedhook;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(64, 64, 64, 64);
        // 强制白色背景，防止深色模式看不见
        layout.setBackgroundColor(Color.WHITE); 

        TextView label = new TextView(this);
        label.setText("自定义速度（米/秒）");
        label.setTextSize(20);
        label.setTextColor(Color.BLACK);

        EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setTextColor(Color.BLACK);
        float saved = getSharedPreferences("speed_config", 0).getFloat("fake_speed", 0f);
        input.setText(String.valueOf(saved));

        Button save = new Button(this);
        save.setText("保存");
        save.setTextColor(Color.BLACK);
        save.setOnClickListener(v -> {
            try {
                float speed = Float.parseFloat(input.getText().toString());
                getSharedPreferences("speed_config", 0).edit().putFloat("fake_speed", speed).apply();
                ContentValues cv = new ContentValues();
                cv.put("fake_speed", speed);
                getContentResolver().insert(SpeedConfigProvider.CONTENT_URI, cv);
                Toast.makeText(this, "已保存: " + speed + " m/s", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "请输入有效数字", Toast.LENGTH_SHORT).show();
            }
        });

        layout.addView(label);
        layout.addView(input);
        layout.addView(save);
        setContentView(layout);
    }
}
