package paris.obsidian.bonvoyage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.DatePickerDialog
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*


class TripAddingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        val cal = Calendar.getInstance()

        val editBeginDate: TextView = findViewById(R.id.editBeginDate)
        val editEndDate: TextView = findViewById(R.id.editEndDate)
        val spinner: Spinner = findViewById(R.id.countrySpinner)
        val imagePreview: ImageView = findViewById(R.id.imagePreview)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.country_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        editBeginDate.text = getDateInstance().toString()
        editEndDate.text = getDateInstance().toString()// SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            editBeginDate.text = sdf.format(cal.time)
        }

        editBeginDate.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        editEndDate.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        Glide.with(this).load(R.drawable.original)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(5, 2)))
            .into(imagePreview)
    }
}