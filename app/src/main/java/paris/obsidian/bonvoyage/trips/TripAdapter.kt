package paris.obsidian.bonvoyage.trips

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import paris.obsidian.bonvoyage.R

class TripAdapter(private val onClick: (Trip) -> Unit) :
    ListAdapter<Trip, TripAdapter.TripViewHolder>(FlowerDiffCallback) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class TripViewHolder(view: View, val onClick: (Trip) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private var currentTrip: Trip? = null

        val countryName: TextView
        val backgroundImage: ImageView
        val plusImage: ImageView
        val goToButton: Button

        init {
            // Define click listener for the ViewHolder's View.
            countryName = view.findViewById(R.id.countryName)
            backgroundImage = view.findViewById(R.id.backgroundImage)
            plusImage = view.findViewById(R.id.plusImage)
            goToButton = view.findViewById(R.id.goToButton)

            view.setOnClickListener {
                currentTrip?.let {
                    onClick(it)
                }
            }

        }

        /* Bind flower name and image. */
        fun bind(trip: Trip) {
            currentTrip = trip
            countryName.text = trip.name
            backgroundImage.setImageResource(trip.image)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_country, parent, false)
        return TripViewHolder(view, onClick)
    }

    // Gets current Trip and uses it to bind view.
    override fun onBindViewHolder(viewHolder: TripViewHolder, position: Int) {

        val trip = getItem(position)
        viewHolder.bind(trip)
    }

}

    object FlowerDiffCallback : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem.id == newItem.id
        }
    }
