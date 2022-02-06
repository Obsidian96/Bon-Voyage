package paris.obsidian.bonvoyage.days

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import paris.obsidian.bonvoyage.R
import paris.obsidian.bonvoyage.days.Day


class DayAdapter(private val onClick: (Day) -> Unit,
                 private val onRemoveClick: (Day) -> Unit,
                 private val onAddClick: (Day) -> Unit)
    : ListAdapter<Day, DayAdapter.DayViewHolder>(DayDiffCallback) {

    class DayViewHolder(view: View,
                        val onClick: (Day) -> Unit,
                        val onRemoveClick: (Day) -> Unit,
                        val onAddClick: (Day) -> Unit) : RecyclerView.ViewHolder(view) {
        private var currentDay: Day? = null

        private val dayNumber: TextView
        private val backgroundImage: ImageView
        private val plusDay: ImageView
        private val plusTrip: ImageView
        private val plusButtonContainer: LinearLayout
        private val deleteDay: ImageButton

        init {
            // Define click listener for the ViewHolder's View.
            dayNumber = view.findViewById(R.id.dayNumber)
            backgroundImage = view.findViewById(R.id.backgroundImage)
            plusDay = view.findViewById(R.id.plusDay)
            plusTrip = view.findViewById(R.id.plusTrip)
            plusButtonContainer = view.findViewById(R.id.plusButtonContainer)
            deleteDay = view.findViewById(R.id.delete_day)

            view.setOnClickListener {
                currentDay?.let {
                    onClick(it)
                }
            }

            deleteDay.setOnClickListener {
                currentDay?.let {
                        it1 -> onRemoveClick(it1)
                }
            }

            plusDay.setOnClickListener {
                currentDay?.type = "day"
                currentDay?.let {
                        it2 -> onAddClick(it2)
                }
            }

            plusTrip.setOnClickListener {
                currentDay?.type = "trip"
                currentDay?.let {
                        it3 -> onAddClick(it3)
                }
            }
        }

        /* Bind Day name and image. */
        fun bind(day: Day) {
            if (day.id == 0) {
                plusButtonContainer.alpha = 1F
                dayNumber.alpha = 0F
                backgroundImage.alpha = 0F
                deleteDay.alpha = 0F
            }
            else {
                if (day.type == "trip") {
                    backgroundImage.setBackgroundColor(backgroundImage.context.resources.getColor(R.color.purple_200))
                }
                else if (day.type == "day") {
                    backgroundImage.setBackgroundColor(backgroundImage.context.resources.getColor(R.color.teal_200))
                }
                plusButtonContainer.alpha = 0F
                dayNumber.alpha = 1F
                backgroundImage.alpha = 1F
                deleteDay.alpha = 1F
            }

            currentDay = day
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_day, parent, false)

        val width =  Resources.getSystem().displayMetrics.widthPixels
        view.layoutParams.width = (width * 0.9).toInt()
        view.layoutParams.height = view.layoutParams.height
        return DayViewHolder(view, onClick, onRemoveClick, onAddClick)
    }

    // Gets current Day and uses it to bind view.
    override fun onBindViewHolder(viewHolder: DayViewHolder, position: Int) {
        val trip = getItem(position)
        viewHolder.bind(trip)
    }
}

object DayDiffCallback : DiffUtil.ItemCallback<Day>() {
    override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem.id == newItem.id
    }
}
