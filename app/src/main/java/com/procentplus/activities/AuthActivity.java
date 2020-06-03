package com.procentplus.activities;

import android.content.Intent;
import android.graphics.Paint;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.procentplus.ProgressDialog.DialogConfig;
import com.procentplus.R;
import com.procentplus.databinding.ActivityAuthBinding;
import com.procentplus.databinding.ActivityCalculateBinding;
import com.procentplus.retrofit.interfaces.IAuthorization;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.models.SignRequest;
import com.procentplus.retrofit.models.AuthResponse;
import com.procentplus.retrofit.models.MobileUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    public static class AuthValue{
        public ObservableField<String> phone = new ObservableField<>("");
        public ObservableField<String> password = new ObservableField<>("");
        public ObservableField<String> confirmPassword = new ObservableField<>("");
    }
    private DialogConfig progressDialog;

    AuthValue value = new AuthValue();
    ActivityAuthBinding binding;
    private IAuthorization iAuthorization;
    private Retrofit retrofit;

    private String phone;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        binding.setValue(value);
        ButterKnife.bind(this);

        // init api
        retrofit = RetrofitClient.getInstance();

        // init dialog
        progressDialog = new DialogConfig(this, "Идет загрузка");

        // underlining the textviews
        binding.forgotPassword.setPaintFlags(binding.forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.registerBtnAuth.setPaintFlags(binding.registerBtnAuth.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // init click listeners
        binding.forgotPassword.setOnClickListener(this);
        binding.signInAuth.setOnClickListener(this);
        binding.registerBtnAuth.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.forgot_password:
                intent = new Intent(AuthActivity.this, RestorePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_in_auth:
                phone = value.phone.get();
                password = value.password.get();
                if (phone.length() > 16 && !password.isEmpty()) {
                    getAuthResponse();
                    // show dialog
                    progressDialog.showDialog();

                } else MainActivity.prefConfig.displayToast("Заполните все поля!");

                break;
            case R.id.register_btn_auth:
                intent = new Intent(AuthActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

        }

    }

    private void getAuthResponse() {
        IAuthorization iAuthorization = retrofit.create(IAuthorization.class);

        Call<AuthResponse> authResponseCall = iAuthorization.getAccountData(
                new SignRequest(
                        new MobileUser(phone, password))
        );

        authResponseCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                int statusCode = response.code();
                Log.d("LOGGER Auth", "statusCode: " + statusCode);
                progressDialog.dismissDialog();
                if (statusCode == 200) {
                    if (response.body()==null) return;
                    if (response.body().getErrorsCount() !=null && response.body().getErrorsCount() > 0) {
                        showError(response.body().getMsg());
                        return;
                    }
                    MainActivity.prefConfig.writeLoginStatus(true);
                    MainActivity.prefConfig.writePhone(phone);
                    MainActivity.prefConfig.writePassword(password);
                    MainActivity.prefConfig.writeToken(response.body().getAccessToken());
                    MainActivity.prefConfig.writeId(response.body().getUser().getId());
                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    intent.putExtra("tab_id", 0);
                    startActivity(intent);
                    finish();
                } else {
                    // hide dialog
                    try { showError( new JSONObject(response.errorBody().string()).getString("error_message"));}
                    catch (JSONException | IOException e) { showError("Email или пароль были введены неверно!");  }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // hide dialog
                progressDialog.dismissDialog();
                showError("Произошла ошибка при попытке авторизации, попытайтесь снова.");
            }
        });
    }
    private void showError(String text){
        MainActivity.prefConfig.displayToast(text);
    }
}
