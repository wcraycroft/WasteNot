package cs134.miracosta.wastenot.UI.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.OnItemClickListener;

public class ClaimsAdapter extends RecyclerView.Adapter<ClaimsAdapter.ItemHolder> {

    private Activity mActivity;
    private ArrayList<Donation> allClaims;
    private OnItemClickListener onItemClickListener;

    /**
     * ClaimsAdapter Constructor
     * @param mActivity the creator
     * @param allClaims the list of all items to add
     * @param onItemClickListener listener for when an item is clicked
     */
    public ClaimsAdapter(Activity mActivity,ArrayList<Donation> allClaims, OnItemClickListener onItemClickListener) {
        this.mActivity = mActivity;
        this.onItemClickListener = onItemClickListener;
        this.allClaims = allClaims;
    }

    /**
     * Responsible for inflating the view from the xml layout file
     * @return the created view
     */
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_claim, parent, false);
        return new ItemHolder(view);
    }

    /**
     * Responsible for show data for each item
     *
     * @param holder the view
     * @param position the position the view belongs
     */
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Donation donation = allClaims.get(position);
        if (donation != null) {
            holder.tvCompanyName.setText(donation.getFoodType().toString());
            holder.tvDropOffEndTime.setText(donation.getReadyTime());

            holder.parent.setTag(position);
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, pos);
                }
            });
        }
    }

    /**
     * @return the number of items in the adapter has
     */
    @Override
    public int getItemCount() {
        return allClaims.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView tvCompanyName, tvDropOffEndTime;
        ConstraintLayout parent;

        public ItemHolder(View itemView) {
            super(itemView);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvDropOffEndTime = itemView.findViewById(R.id.tvDropOffEndTime);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
