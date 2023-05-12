import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

class BookDialog(private val title: String, private val type: Boolean) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { // Use the Builder class for convenient dialog construction
        var builder = AlertDialog.Builder(it)
            builder.setTitle(title)
            if (!type){
                builder.setItems(arrayOf("Read", "Story Info", "Share Story", "Remove from Library","Remove from Offline"),
                    DialogInterface.OnClickListener { dialog, which ->
                        Log.d("nlhdung", "Selected item index $which") }) // Create the AlertDialog object and return it
            }
            else {
                builder.setItems(arrayOf("Read", "Story Info", "Share Story", "Remove from Library"),
                    DialogInterface.OnClickListener { dialog, which ->
                        Log.d("nlhdung", "Selected item index $which") }) // Create the AlertDialog object and return it
            }

        builder.create() } ?: throw IllegalStateException("Activity cannot be null") }
}
