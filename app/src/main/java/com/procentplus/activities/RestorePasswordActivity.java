package com.procentplus.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.procentplus.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RestorePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.restore_back_btn)
    ImageView restore_back_btn;
    @BindView(R.id.register_btn_restore)
    TextView register_btn_restore;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);
        ButterKnife.bind(this);

        SpannableString content = new SpannableString("Если не можете войти в приложение, напишите на info@procentplus.com");
        content.setSpan(new UnderlineSpan(), 47, content.length(), 0);
        content.setSpan(new ForegroundColorSpan(Color.BLUE), 47, content.length(), 0);
        textView.setText(content);

        // underlining text views
        register_btn_restore.setPaintFlags(register_btn_restore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // init click listeners
        restore_back_btn.setOnClickListener(this);
        textView.setOnClickListener(this);
        register_btn_restore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.restore_back_btn:
                onBackPressed();
                break;
            case R.id.register_btn_restore:
                Intent intent = new Intent(RestorePasswordActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.textView:
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "Восстановление доступа"+ "&body=" + "Я забыл свой пароль. Пожалуйста напомните. \nМой номер:" + "&to=" + "info@procentplus.com");
                mailIntent.setData(data);
                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
                break;
        }
    }
}
