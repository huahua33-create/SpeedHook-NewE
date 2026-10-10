package com.example.speedhook;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import java.util.Locale;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.WHITE);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(80, 80, 80, 80);

        TextView label = new TextView(this);
        // 改为时速单位
        label.setText("自定义时速 (km/h)");
        label.setTextSize(24);
        label.setTextColor(Color.BLACK);
        label.setGravity(Gravity.CENTER);
        label.setPadding(0, 0, 0, 40);

        EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setTextColor(Color.BLACK);
        input.setTextSize(22);
        input.setHint("例如：18.0 (等于5m/s)");
        input.setHintTextColor(Color.GRAY);
        
        LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        inputParams.setMargins(0, 0, 0, 60);
        
        // 读取时：把存储的米/秒乘以 3.6 变回时速，显示在界面上
        float savedMs = getSharedPreferences("speed_config", 0).getFloat("fake_speed", 0f);
        float savedKmh = savedMs * 3.6f;
        // 格式化保留一位小数，避免显示一长串数字
        input.setText(String.format(Locale.getDefault(), "%.1f", savedKmh));

        Button save = new Button(this);
        save.setText("保存");
        save.setTextColor(Color.BLACK);
        save.setTextSize(20);

        save.setOnClickListener(v -> {
            try {
                // 获取用户输入的时速
                float inputKmh = Float.parseFloat(input.getText().toString());
                // 核心换算：时速除以 3.6 = 米/秒
                float speedMs = inputKmh / 3.6f;
                
                getSharedPreferences("speed_config", 0).edit().putFloat("fake_speed", speedMs).apply();
                ContentValues cv = new ContentValues();
                cv.put("fake_speed", speedMs);
                getContentResolver().insert(SpeedConfigProvider.CONTENT_URI, cv);
                
                Toast.makeText(this, "已保存: " + inputKmh + " km/h", Toast.LENGTH_SHORT).show();
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
