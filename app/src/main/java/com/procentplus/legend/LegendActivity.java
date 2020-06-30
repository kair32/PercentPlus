package com.procentplus.legend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
                if (response.body() != null && (response.body()).getErrorsCount() > 0) {
                    MainActivity.prefConfig.displayToast(response.body().getMsg());
                    return;
                }
                if (response.body()!=null) {
                    if (response.body().getPartnerList()!=null) {
                        ArrayList<BonusData> bonusDataList = new ArrayList<>();
                        for (int i = 0; i < response.body().getPartnerList().size(); i++){
                            bonusDataList.addAll(response.body().getPartnerList().get(i).getBonus());
                        }
                        Collections.sort(bonusDataList, (o1, o2) -> {
                            int result = 0;
                            try {
                                if(Double.parseDouble(o1.getPercent()) ==  Double.parseDouble(o2.getPercent()) == true) result = 1;
                            }
                            catch (NumberFormatException ignored){}
                            return result;
                        });
                        binding.rv.setAdapter(new LegendAdapter(bonusDataList));
                    }
                } else onBackPressed();
            }

            @Override public void onFailure(Call<BonusList> call, Throwable t) { onBackPressed(); }
        });
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.legend_back_btn) {
            onBackPressed();
        }
    }
}
