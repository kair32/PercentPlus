package com.procentplus.adapter;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procentplus.activities.BonusActivity;
import com.procentplus.activities.MainActivity;
import com.procentplus.R;
import com.procentplus.retrofit.models.Objects;
import com.procentplus.retrofit.models.Partner;

import java.util.ArrayList;
import java.util.List;

public class ObjectsAdapter extends RecyclerView.Adapter<ObjectsAdapter.ViewHolder>{

    private List<Partner> objectList = new ArrayList<>();

    public ObjectsAdapter(List<Partner> objectList) {
        this.objectList = objectList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.object_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Partner object = objectList.get(position);
        holder.objectName.setText(object.getCompanyName());

        holder.objectsItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BonusActivity.class);
                intent.putExtra("object_name", object.getName());
                intent.putExtra("object_id", object.getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView objectName;
        private ConstraintLayout objectsItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            objectsItemLayout = itemView.findViewById(R.id.objectItemLayout);
            objectName = itemView.findViewById(R.id.objectName);
        }
    }
}
