package by.pda.demoapp.android.view.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import by.pda.demoapp.android.R;
import by.pda.demoapp.android.databinding.ItemColorBinding;
import by.pda.demoapp.android.interfaces.OnItemClickListener;
import by.pda.demoapp.android.model.ColorModel;
import by.pda.demoapp.android.utils.Constants;
import by.pda.demoapp.android.utils.SingletonClass;

import java.util.List;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ViewHolder> {
	Activity mAct;
	List<ColorModel> list;
	OnItemClickListener clickListener;
	SingletonClass ST;

	int selectedPos = 0;

	public ColorsAdapter(Activity mAct, List<ColorModel> list, OnItemClickListener clickListener) {
		this.mAct = mAct;
		this.list = list;
		ST = SingletonClass.getInstance();
		this.clickListener = clickListener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		ItemColorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
			R.layout.item_color, parent, false);
		return new ViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
		ColorModel model = list.get(position);
		holder.binding.colorIV.setImageResource(model.getColorImg());
		holder.binding.colorIV.setContentDescription(holder.getContentDescription(model));

		if (selectedPos == position) {
			holder.binding.aroundIV.setImageResource(holder.getAroundIV(model));
			holder.binding.aroundIV.setVisibility(View.VISIBLE);
		} else {
			holder.binding.aroundIV.setVisibility(View.INVISIBLE);
		}

		holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedPos = position;
				notifyDataSetChanged();
				clickListener.OnClick(position, -1);
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		ItemColorBinding binding;

		public ViewHolder(@NonNull ItemColorBinding itemView) {
			super(itemView.getRoot());
			this.binding = itemView;
		}

		private String getContentDescription(ColorModel model) {
			switch (model.getColorValue()) {

				case Constants.BLACK:
					return "Black color";

				case Constants.GREEN:
					return "Green color";

				case Constants.GRAY:
					return "Gray color";

				case Constants.BLUE:
					return "Blue color";

				default:
					return "Unknown color";
			}
		}

		private int getAroundIV(ColorModel model) {
			switch (model.getColorValue()) {
				case Constants.BLACK:
					return R.drawable.ic_black_circle_side;

				case Constants.GREEN:
					return R.drawable.ic_green_circle_side;

				case Constants.GRAY:
					return R.drawable.ic_gray_circle_side;

				case Constants.BLUE:
					return R.drawable.ic_blue_circle_side;

				default:
					return 0;
			}
		}
	}
}
