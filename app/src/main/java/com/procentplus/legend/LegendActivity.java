package com.procentplus.legend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.procentplus.R;
import com.procentplus.activities.MainActivity;
import com.procentplus.databinding.ActivityLegendBinding;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.GetBonusList;
import com.procentplus.retrofit.models.BonusData;
import com.procentplus.retrofit.models.BonusList;
import com.procentplus.retrofit.models.BonusRequest;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LegendActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLegendBinding binding;
    private GetBonusList bonusList;
    private Retrofit retrofit;
    private int object_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_legend);

        object_id = getIntent().getIntExtra("partner_id", -1);
        retrofit = RetrofitClient.getInstance();
        response();
        ImageView onBackPressed = findViewById(R.id.legend_back_btn);
        onBackPressed.setOnClickListener(this);

    }

    private void response(){
        if (object_id==-1)  onBackPressed();
        bonusList = retrofit.create(GetBonusList.class);

        Call<BonusList> bonusListResponseCall = bonusList.getBonusList( MainActivity.prefConfig.readToken(),
                new BonusRequest(object_id));

        bonusListResponseCall.enqueue(new Callback<BonusList>() {
            @Override
            public void onResponse(Call<BonusList> call, Response<BonusList> response) {
                int statusCode = response.code();
                Log.d("LOGGER Category", "statusCode: " + statusCode);
                if (response.body()!=null) {
                    if (response.body().getBonus()!=null) {
                        List<BonusData> bonuses = response.body().getBonus();
                        Collections.sort(bonuses, (contact, another) -> contact.getPercent().compareToIgnoreCase(another.getPercent()));
                        binding.rv.setAdapter(new LegendAdapter(bonuses));
                    }
                } else onBackPressed();
            }

            @Override
            public void onFailure(Call<BonusList> call, Throwable t) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.legend_back_btn) {
            onBackPressed();
        }
    }
}
