package com.procentplus.activities;

import android.content.Intent;
import android.graphics.Paint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.procentplus.ProgressDialog.DialogConfig;
import com.procentplus.R;
import com.procentplus.databinding.ActivityRegisterBinding;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.IRegistration;
import com.procentplus.retrofit.models.MobileUser;
import com.procentplus.retrofit.models.RegistrationResponse;
import com.procentplus.retrofit.models.response_bubble.ResponseCallback;
import com.procentplus.retrofit.models.response_bubble.RestResponse;
import com.procentplus.retrofit.models.response_bubble.RetrofitCallback;
import com.procentplus.retrofit.models.SignRequest;

import retrofit2.Call;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    AuthActivity.AuthValue value = new AuthActivity.AuthValue();
    ActivityRegisterBinding binding;

    private Retrofit retrofit;

    private String phone;
    private String password;
    private DialogConfig progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setValue(value);

        // init api
        retrofit = RetrofitClient.getInstance();

        // init dialog
        progressDialog = new DialogConfig(this, "Идет загрузка");

        // underlining text views
        binding.authBtnRegister.setPaintFlags(binding.authBtnRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // init click listeners
        binding.nextRegister.setOnClickListener(this);
        binding.authBtnRegister.setOnClickListener(this);
        binding.registerBackBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_register:
                phone = value.phone.get();
                password = value.password.get();
                String confirm_password = value.confirmPassword.get();
                if (!phone.isEmpty() && !password.isEmpty() && !confirm_password.isEmpty()) {
                    if (password.length() >= 6) {
                        if (password.equals(confirm_password)) {
                            registerUser();

                            // show dialog
                            progressDialog.showDialog();

                        } else MainActivity.prefConfig.displayToast("Убедитесь в соответствии паролей.");
                    } else MainActivity.prefConfig.displayToast("Пароль не может быть меньше 6 символов.");

                } else MainActivity.prefConfig.displayToast("Заполните все поля!");
                break;
            case R.id.auth_btn_register:
                Intent intent = new Intent(RegisterActivity.this, AuthActivity.class);
                startActivity(intent);
                break;
            case R.id.register_back_btn:
                onBackPressed();
                break;
        }
    }

    private void registerUser() {
        IRegistration iRegistration = retrofit.create(IRegistration.class);

        Call<RestResponse<RegistrationResponse>> registrationResponseCall = iRegistration.registrationData(
                new SignRequest(new MobileUser(phone, password))
        );
        registrationResponseCall.enqueue(new RetrofitCallback<>(new ResponseCallback<RegistrationResponse>() {
            @Override
            public void onSuccessfulRequest(RegistrationResponse response) {
                MainActivity.dialogConfig.dismissDialog();
                MainActivity.prefConfig.writePhone(phone);
                MainActivity.prefConfig.writePassword(password);
                MainActivity.prefConfig.displayToast("Вы успешно зарегистрированы. Авторизуйтесь");
                Intent intent = new Intent(RegisterActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onErrorRequest(@Nullable String message, boolean isUnauthorized) {
                progressDialog.dismissDialog();
                MainActivity.prefConfig.displayToast(message);
            }
        }));

    }
}
