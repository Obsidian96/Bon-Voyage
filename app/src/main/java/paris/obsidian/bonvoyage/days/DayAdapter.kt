package paris.obsidian.bonvoyage.days

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import paris.obsidian.bonvoyage.R
import paris.obsidian.bonvoyage.trips.Trip
import java.text.SimpleDateFormat
import java.util.*

import android.view.inputmethod.EditorInfo

import android.widget.TextView

private const val TAG = "DayAdapter"

class DayAdapter(
    private val trip: Trip,
    private val onTextEdited: (Day) -> Unit,
    private val onClick: (Day) -> Unit,
    private val onRemoveClick: (Day) -> Unit,
    private val onAddClick: (Day) -> Unit)
    : ListAdapter<Day, DayAdapter.DayViewHolder>(DayDiffCallback) {

    class DayViewHolder(view: View,
                        val trip: Trip,
                        val onTextEdited: (Day) -> Unit,
                        val onClick: (Day) -> Unit,
                        val onRemoveClick: (Day) -> Unit,
                        val onAddClick: (Day) -> Unit) : RecyclerView.ViewHolder(view) {
        private var currentDay: Day? = null

        private val plusButtonContainer: LinearLayout
        private val contentContainer: LinearLayout
        private val dayNumber: TextView
        private val backgroundImage: ImageView
        private val plusDay: ImageView
        private val plusTrip: ImageView
        private val deleteDay: ImageButton
        private val dayDescription: EditText
        private val hotelLoc: Button

        init {
            plusButtonContainer = view.findViewById(R.id.plusButtonContainer)
            contentContainer = view.findViewById(R.id.contentContainer)
            dayNumber = view.findViewById(R.id.dayNumber)
            backgroundImage = view.findViewById(R.id.backgroundImage)
            plusDay = view.findViewById(R.id.plusDay)
            plusTrip = view.findViewById(R.id.plusTrip)
            deleteDay = view.findViewById(R.id.delete_day)
            dayDescription = view.findViewById(R.id.dayDescription)
            hotelLoc = view.findViewById(R.id.hotelLoc)

            // Define click listeners for the ViewHolder's View.
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

            dayDescription.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {
                    currentDay?.let {
                        onTextEdited(it)
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {}
            })
        }

        /* Bind Day name and image. */
        fun bind(day: Day) {
            val ctx = backgroundImage.context

            if (day.id == 0L || day.type == "default") {
                plusButtonContainer.visibility = View.VISIBLE
                contentContainer.visibility = View.GONE
                dayNumber.alpha = 0F
                backgroundImage.alpha = 0F
                deleteDay.alpha = 0F
            }
            else {
                plusButtonContainer.visibility = View.GONE
                contentContainer.visibility = View.VISIBLE
                dayNumber.alpha = 1F
                backgroundImage.alpha = 1F
                deleteDay.alpha = 1F

                if (day.type == "trip") {
                    backgroundImage.setBackgroundColor(ctx.resources.getColor(R.color.purple_200))
                }
                else if (day.type == "day") {
                    backgroundImage.setBackgroundColor(ctx.resources.getColor(R.color.teal_200))
                }

                if (day.type == "day") {
                    val dateFormat = "dd.MM.yyyy"
                    val sdf = SimpleDateFormat(dateFormat, Locale.FRANCE)
                    val date = sdf.parse(trip.dateBegin)

                    val nextDay : Date

                    if (date != null) {
                        val dateFormat2 = "E dd/MM"
                        val sdf2 = SimpleDateFormat(dateFormat2, Locale.getDefault())
                        nextDay = Date(date.time + ((1000 * 60 * 60 * 24) * (day.dayNumber - 1)))
                        dayNumber.text = ctx.getString(R.string.day_number, day.dayNumber, sdf2.format(nextDay))
                    }
                    else {
                        dayNumber.text = ctx.getString(R.string.day_number_short, day.dayNumber)
                    }
                    hotelLoc.visibility = View.VISIBLE
                    dayDescription.hint = ctx.getString(R.string.add_day_description)
                }
                else {
                    dayNumber.text = ctx.getString(R.string.day_travel)
                    hotelLoc.visibility = View.GONE
                    dayDescription.hint = ctx.getString(R.string.add_transport_description)
                }

                dayDescription.setText(day.content)

                //Listen to changes on text
                dayDescription.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        day.content = v.text.toString()
                        true
                    }
                    false
                }

                dayDescription.addTextChangedListener(object: TextWatcher {

                    var delay : Long = 500 // 0.5
                    var timer = Timer()

                    override fun afterTextChanged(p0: Editable?) {

                        day.content = p0.toString()
                        timer = Timer()
                        timer.schedule(object : TimerTask() {
                            override fun run() {

                            }
                        }, delay)
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        timer.cancel() //3
                        timer.purge()
                    }
                })
                //hotelLoc.doNothingForNow()
            }

            currentDay = day
            Log.v(TAG, "Adapter finished loading");
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
        return DayViewHolder(view, trip, onTextEdited, onClick, onRemoveClick, onAddClick)
    }

    // Gets current Day and uses it to bind view.
    override fun onBindViewHolder(viewHolder: DayViewHolder, position: Int) {
        val day = getItem(position)
        viewHolder.bind(day)
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
