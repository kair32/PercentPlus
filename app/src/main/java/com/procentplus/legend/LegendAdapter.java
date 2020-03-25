package com.procentplus.legend;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procentplus.databinding.ItemLegendBinding;
import com.procentplus.retrofit.models.Bonus;

import java.util.List;

public class LegendAdapter extends RecyclerView.Adapter<LegendAdapter.LegendViewHolder> {
    public class LegendViewHolder extends RecyclerView.ViewHolder {
        ItemLegendBinding binding;
        LegendViewHolder(@NonNull ItemLegendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Bonus.BonusData bonus) {
            binding.setItem(bonus);
            String text = bonus.getPercent().substring(0,bonus.getPercent().length()-2) + "%";
            binding.tvPercent.setText(text);
        }
    }

    private List<Bonus.BonusData> listBonus;
    LegendAdapter(List<Bonus.BonusData> listBonus){ this.listBonus = listBonus; }

    @NonNull
    @Override
    public LegendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemLegendBinding binding = ItemLegendBinding.inflate(inflater, viewGroup, false);
        return new LegendViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LegendViewHolder legendViewHolder, int i) {
        legendViewHolder.bind(listBonus.get(i));
    }

    @Override
    public int getItemCount() { return listBonus.size(); }

}
