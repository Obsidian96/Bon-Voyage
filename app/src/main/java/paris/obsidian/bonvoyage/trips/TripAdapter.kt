package paris.obsidian.bonvoyage.trips

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import paris.obsidian.bonvoyage.R

private const val TAG = "Trip Adapter"

class TripAdapter(private val onClick: (Trip) -> Unit, private val onRemoveClick: (Trip) -> Unit) :
    ListAdapter<Trip, TripAdapter.TripViewHolder>(TripDiffCallback) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class TripViewHolder(view: View, val onClick: (Trip) -> Unit, val onRemoveClick: (Trip) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private var currentTrip: Trip? = null

        val countryName: TextView
        val backgroundImage: ImageView
        val plusImage: ImageView
        val deleteTrip: ImageButton

        init {
            // Define click listener for the ViewHolder's View.
            countryName = view.findViewById(R.id.countryName)
            backgroundImage = view.findViewById(R.id.backgroundImage)
            plusImage = view.findViewById(R.id.plusImage)
            deleteTrip = view.findViewById(R.id.delete_trip)

            view.setOnClickListener {
                currentTrip?.let {
                    onClick(it)
                }
            }

            deleteTrip.setOnClickListener {
                currentTrip?.let {
                        it1 -> onRemoveClick(it1)
                }
            }
        }

        /* Bind Trip name and image. */
        fun bind(trip: Trip) {

            if (trip.id == 0) {
                plusImage.alpha = 1F
                countryName.alpha = 0F
                backgroundImage.alpha = 0F
                deleteTrip.alpha = 0F
            }
            else {
                plusImage.alpha = 0F
                countryName.alpha = 1F
                backgroundImage.alpha = 1F
                deleteTrip.alpha = 1F
            }

            Glide.with(itemView.context).load(trip.image)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(5, 2)))
                .into(backgroundImage)

            currentTrip = trip
            countryName.text = trip.name

            Log.v(TAG, "Adapter finished loading");
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_country, parent, false)

        val width =  Resources.getSystem().displayMetrics.widthPixels
        view.layoutParams.width = (width * 0.5).toInt()
        view.layoutParams.height = view.layoutParams.width
        return TripViewHolder(view, onClick, onRemoveClick)
    }

    // Gets current Trip and uses it to bind view.
    override fun onBindViewHolder(viewHolder: TripViewHolder, position: Int) {
        val trip = getItem(position)
        viewHolder.bind(trip)
    }

}

object TripDiffCallback : DiffUtil.ItemCallback<Trip>() {
    override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem.id == newItem.id
    }
}
