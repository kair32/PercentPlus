package com.procentplus.fragments.searchFragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.procentplus.R;
import com.procentplus.activities.MainActivity;
import com.procentplus.activities.ObjectsActivity;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.ISearch;
import com.procentplus.retrofit.models.PointOfSale;
import com.procentplus.retrofit.models.SearchRequest;
import com.procentplus.retrofit.models.SearchResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ByNameFragment extends Fragment {

    private ISearch iSearch;
    private Retrofit retrofit;

    private EditText et_object_name;
    private String object_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_by_name, null);

        // init api
        retrofit = RetrofitClient.getInstance();

        et_object_name = view.findViewById(R.id.et_object_name);
        Button search_by_code = view.findViewById(R.id.search_by_name);

        search_by_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object_name = et_object_name.getText().toString().trim().toLowerCase();
                if (!et_object_name.getText().toString().equals("")) {
                    getData();
                    // show dialog
                    MainActivity.dialogConfig.showDialog();
                } else
                    MainActivity.prefConfig.displayToast("Введите название для поиска");
            }
        });

        return view;
    }

    private void getData() {
        iSearch = retrofit.create(ISearch.class);

        List<SearchRequest.SearchParam> list = new ArrayList<>();
        list.add(new SearchRequest.SearchParam("company_name", object_name));

        Call<SearchResponse> searchResponseCall = iSearch.getSearchResult(
                MainActivity.prefConfig.readToken(),
                new SearchRequest(list)
        );

        searchResponseCall.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                int statusCode = response.code();
                Log.d("LOGGER Search", "statusCode: " + statusCode);

                List<PointOfSale> searchList = new ArrayList<>(response.body().getPointOfSales());

                if (statusCode == 200) {
                    // hide dialog
                    MainActivity.dialogConfig.dismissDialog();
                    Intent objectsIntent = new Intent(getContext(), ObjectsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("search_result",(Serializable) searchList);
                    objectsIntent.putExtra("searchList", bundle);
                    objectsIntent.putExtra("search_by", "названию: "+object_name);
                    startActivity(objectsIntent);
                } else {
                    Log.d("LOGGER Search", "onResponse: произошла ошибка при получении данных");
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // hide dialog
                MainActivity.dialogConfig.dismissDialog();
                MainActivity.prefConfig.displayToast("Ошибка при воспроизведении поиска");
            }
        });
    }
}
