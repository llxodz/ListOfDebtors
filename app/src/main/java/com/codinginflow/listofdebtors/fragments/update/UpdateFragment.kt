package com.codinginflow.listofdebtors.fragments.update

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codinginflow.listofdebtors.R
import com.codinginflow.listofdebtors.model.User
import com.codinginflow.listofdebtors.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import java.text.DateFormat
import java.util.*

class UpdateFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mUserViewModel: UserViewModel
    private var date: Long = 0
    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private var savedHour = 0
    private var savedMinute = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.btn_update.setOnClickListener {
            updateItem()
        }

        val date = DateFormat.getDateInstance().format(args.currentUser.timestamp)

        view.et_firstName_update.setText(args.currentUser.name)
        view.et_amount_update.setText(args.currentUser.amount.toString())
        view.et_note_update.setText(args.currentUser.note)
        view.et_timestamp_update.text = date

        // Add menu
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_amount_update.transformationMethod = null
        pickDate()
    }

    private fun updateItem() {
        val name = et_firstName_update.text.toString()
        val amount = et_amount_update.text.toString().toDouble()
        val note = et_note_update.text.toString()
        val timestamp = if (date == 0.toLong()) {
            val dateNow = System.currentTimeMillis()
            dateNow
        } else {
            date
        }

        if (et_firstName_update.text.toString().equals("") || et_amount_update.text.toString()
                .equals("") || et_note_update.text.toString().equals("")
        ) {
            Toast.makeText(
                requireContext(),
                "Пожалуйста, введите информацию корректно",
                Toast.LENGTH_LONG
            ).show()
        } else {
            // Update User object
            val updatedUser = User(args.currentUser.id, name, amount.toDouble(), note, timestamp)
            // Add Data to Database
            mUserViewModel.updateUser(updatedUser)
            Toast.makeText(
                requireContext(),
                "Информация о пидорасе успешно обновлена!",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Да") { _, _ ->
            mUserViewModel.deleteUser(args.currentUser)
            Toast.makeText(
                requireContext(),
                "Успешно удален пидорас: ${args.currentUser.name}",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Нет") { _, _ ->

        }
        builder.setTitle("Удалить ${args.currentUser.name}?")
        builder.setMessage("Вы уверены, что хотите удалить пидораса ${args.currentUser.name}?")
        builder.create().show()
    }


    private fun getTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        et_timestamp_update.setOnClickListener {
            getTimeCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getTimeCalendar()

        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute
        val calendar = Calendar.getInstance()
        calendar[savedYear, savedMonth, savedDay, savedHour, savedMinute] = 0
        val startTime = calendar.timeInMillis
        date = startTime
        val dateCurrent = DateFormat.getDateTimeInstance().format(startTime)

        et_timestamp_update.text = "$dateCurrent"
    }
}
