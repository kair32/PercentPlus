package com.procentplus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.procentplus.R;
import com.procentplus.adapter.ObjectsAdapter;
import com.procentplus.adapter.SearchAdapter;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.ILogout;
import com.procentplus.retrofit.interfaces.IObjects;
import com.procentplus.retrofit.models.BonusData;
import com.procentplus.retrofit.models.CategoriesResponse;
import com.procentplus.retrofit.models.Objects;
import com.procentplus.retrofit.models.ObjectsRequest;
import com.procentplus.retrofit.models.Partner;
import com.procentplus.retrofit.models.PointOfSale;
import com.procentplus.retrofit.models.response_bubble.RestResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ObjectsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tv_object_category_name)
    TextView tv_category_name;
    @BindView(R.id.object_back_btn)
    ImageView object_back_btn;

    private IObjects iObjects;
    private Retrofit retrofit;

    private RecyclerView categoryRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<Partner> objectsList = new ArrayList<>();
    private List<PointOfSale> searchResultList = new ArrayList<>();

    private Integer category_id;

    private DividerItemDecoration itemDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objects);
        ButterKnife.bind(this);

        // init api
        retrofit = RetrofitClient.getInstance();

        // init recycler
        categoryRecyclerView = findViewById(R.id.objectsRecyclerView);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(this.getResources().getDrawable(R.drawable.divider));
        categoryRecyclerView.addItemDecoration(itemDecorator);

        ImageView logout_btn = findViewById(R.id.logout);
        logout_btn.setOnClickListener(view1 -> logout());

        if (getIntent().getExtras() != null) {
            // if activity is opened from search
            if (getIntent().getBundleExtra("searchList") != null) {
                Bundle bundle = getIntent().getBundleExtra("searchList");
                String search_by = getIntent().getStringExtra("search_by");
                tv_category_name.setText("Поиск по " + search_by);
                List<PointOfSale> searchList =
                        (ArrayList<PointOfSale>) bundle.getSerializable("search_result");

                searchResultList.addAll(searchList);
                setAdapterSearch();
            } else {
                // if activity is opened from categories
                String category_name_str = getIntent().getExtras().getString("category_name");
                category_id = getIntent().getExtras().getInt("category_id");
                tv_category_name.setText(category_name_str);

                getObjects();
                setAdapterObjects();
            }
        }

        object_back_btn.setOnClickListener(this);

    }

    private void logout() {
        ILogout iLogout = retrofit.create(ILogout.class);

        Call<CategoriesResponse> call = iLogout.logOut(MainActivity.prefConfig.readToken());

        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                int statusCode = response.code();
                Log.d("LOGGER Logout", "statusCode: " + statusCode);
                if (statusCode == 200 || statusCode == 204) {
                    MainActivity.prefConfig.writeLoginStatus(false);
                    Intent intent = new Intent(ObjectsActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                MainActivity.prefConfig.displayToast("Произошла ошибка при попытке выхода из профиля");
            }
        });
    }
    private void getObjects() {
        iObjects = retrofit.create(IObjects.class);

        Call<Objects> objectsCall = iObjects.getObjects(
                MainActivity.prefConfig.readToken(),
                new ObjectsRequest(category_id)
        );

        objectsCall.enqueue(new Callback<Objects>() {
            @Override
            public void onResponse(Call<Objects> call, Response<Objects> response) {
                int statusCode = response.code();
                if (response.body() != null && (response.body()).getErrorsCount() > 0) {
                    MainActivity.prefConfig.displayToast(response.body().getMsg());
                    return;
                }
                if (statusCode == 200) {
                    if (objectsList.size() > 0) objectsList.clear();

                    if (response.body() != null)
                        for (int i = 0; i < response.body().getActivityType().size(); i++)
                            objectsList.addAll(response.body().getActivityType().get(i).getPartners());

                    setAdapterObjects();
                }
            }

            @Override
            public void onFailure(Call<Objects> call, Throwable t) { MainActivity.prefConfig.displayToast("Произошла оибка попытайтесь снова."); }
        });
    }

    private void setAdapterSearch() {
        mAdapter = new SearchAdapter(searchResultList);
        categoryRecyclerView.setAdapter(mAdapter);
    }

    private void setAdapterObjects() {
        mAdapter = new ObjectsAdapter(objectsList);
        categoryRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.object_back_btn:
                onBackPressed();
        }
    }
}
