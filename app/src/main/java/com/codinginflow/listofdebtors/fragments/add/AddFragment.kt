package com.codinginflow.listofdebtors.fragments.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codinginflow.listofdebtors.R
import com.codinginflow.listofdebtors.model.User
import com.codinginflow.listofdebtors.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.text.DateFormat
import java.util.*


class AddFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

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
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.btn_add.setOnClickListener {
            insertDataToDatabase()
        }
        return view
    }

    private fun insertDataToDatabase() {
        val name = et_firstName.text.toString()
        val amount = et_amount.text.toString().toInt()
        val note = et_note.text.toString()
        val timestamp = date

        if (inputCheck(name, amount, note, timestamp)) {
            // Create User object
            val user = User(
                0,
                name,
                amount.toDouble(),
                note,
                timestamp
            )
            // Add Data to Database
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "Должник успешно добавлен!", Toast.LENGTH_LONG).show()
            // Navigate back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun inputCheck(name: String, amount: Int, note: String, timestamp: Long): Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(amount.toString()) && TextUtils.isEmpty(
            note
        ) && TextUtils.isEmpty(
            timestamp.toString()
        ))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickDate()
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
        et_timestamp.setOnClickListener {
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

        et_timestamp.text = "$dateCurrent"
    }
}