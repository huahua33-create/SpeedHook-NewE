package com.example.speedhook;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        // 背景白色
        layout.setBackgroundColor(Color.WHITE);
        // 关键设置：让所有内容都在屏幕正中间显示
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(80, 80, 80, 80);

        TextView label = new TextView(this);
        label.setText("自定义速度（米/秒）");
        label.setTextSize(24);
        label.setTextColor(Color.BLACK);
        label.setGravity(Gravity.CENTER);
        label.setPadding(0, 0, 0, 40);

        EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setTextColor(Color.BLACK);
        input.setTextSize(22);
        input.setHint("在这里输入速度，如 5.0");  // 加个提示，不怕看不见
        input.setHintTextColor(Color.GRAY);
        
        // 强制输入框占满宽度，并且在下方留出间距
        LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        inputParams.setMargins(0, 0, 0, 60); // 下边距60像素
        
        float saved = getSharedPreferences("speed_config", 0).getFloat("fake_speed", 0f);
        input.setText(String.valueOf(saved));

        Button save = new Button(this);
        save.setText("保存");
        save.setTextColor(Color.BLACK);
        save.setTextSize(20);

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
        layout.addView(input, inputParams);
        layout.addView(save);

        setContentView(layout);
    }
}
