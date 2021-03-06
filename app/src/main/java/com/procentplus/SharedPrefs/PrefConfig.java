package com.procentplus.SharedPrefs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.procentplus.R;
import com.procentplus.activities.AuthActivity;
import com.procentplus.activities.MainActivity;

public class PrefConfig {

    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(
                "AccountData", Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status), status);
        editor.commit();
    }

    public boolean readLoginStatus() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status), false);
    }

    public void writePhone(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_username), name);
        editor.commit();
    }

    public void writeId(int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.pref_id), id);
        editor.commit();
    }

    public void writeToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_token), "Bearer "+token);
        editor.commit();
    }

    public void writePassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_password), password);
        editor.commit();
    }

    public String readToken() {
        return sharedPreferences.getString(context.getString(R.string.pref_token), "");
    }

    public int readId() {
        return sharedPreferences.getInt(context.getString(R.string.pref_id), -1);
    }

    public String readPhone() {
        return sharedPreferences.getString(context.getString(R.string.pref_username), "");
    }

    public String readPassword() {
        return sharedPreferences.getString(context.getString(R.string.pref_password), "");
    }


    public void startLoginActivity(Activity activity, boolean withFinish){
        Intent auth_intent = new Intent(activity, AuthActivity.class);
        activity.startActivity(auth_intent);
        if (withFinish) activity.finish();
    }

    public void displayToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
