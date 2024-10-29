package com.example.bai1_20215491
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var edtMSSV: EditText
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var calendarView: CalendarView
    private lateinit var checkSports: CheckBox
    private lateinit var checkMovies: CheckBox
    private lateinit var checkMusic: CheckBox
    private lateinit var checkTerms: CheckBox
    private lateinit var btnShowCalendar: Button
    private lateinit var btnSubmit: Button
    private lateinit var txtBirthDate: TextView

    private lateinit var spinnerProvince: Spinner
    private lateinit var spinnerDistrict: Spinner
    private lateinit var spinnerWard: Spinner

    private lateinit var addressHelper: AddressHelper
    private var selectedBirthDate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtMSSV = findViewById(R.id.edtMSSV)
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        radioGroup = findViewById(R.id.radioGroup)
        calendarView = findViewById(R.id.calendarView)
        checkSports = findViewById(R.id.checkSports)
        checkMovies = findViewById(R.id.checkMovies)
        checkMusic = findViewById(R.id.checkMusic)
        checkTerms = findViewById(R.id.checkTerms)
        btnShowCalendar = findViewById(R.id.btnShowCalendar)
        btnSubmit = findViewById(R.id.btnSubmit)
        txtBirthDate = findViewById(R.id.txtBirthDate)

        spinnerProvince = findViewById(R.id.spinnerLocation)
        spinnerDistrict = findViewById(R.id.spinnerDistrict)
        spinnerWard = findViewById(R.id.spinnerWard)

        addressHelper = AddressHelper(resources)

        setupProvinces()

        spinnerProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedProvince = parent.getItemAtPosition(position).toString()
                setupDistricts(selectedProvince)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedDistrict = parent.getItemAtPosition(position).toString()
                setupWards(spinnerProvince.selectedItem.toString(), selectedDistrict)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        btnShowCalendar.setOnClickListener {
            // Ẩn hoặc hiện CalendarView khi nhấn nút
            if (calendarView.visibility == View.GONE) {
                calendarView.visibility = View.VISIBLE // Hiện CalendarView
                Log.d("TAG", "calendar show")
            } else {
                calendarView.visibility = View.GONE // Ẩn CalendarView
                Log.d("TAG", "calendar hide")
            }
        }

        // Lắng nghe sự kiện chọn ngày từ CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            selectedBirthDate = selectedDate.timeInMillis
            txtBirthDate.text = "Ngày sinh: ${dayOfMonth}/${month + 1}/$year" // Hiển thị ngày sinh
            calendarView.visibility = View.GONE // Ẩn CalendarView sau khi chọn
        }

        btnSubmit.setOnClickListener { validateInput() }
    }

    private fun setupProvinces() {
        val provinces = addressHelper.getProvinces()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProvince.adapter = adapter
    }

    private fun setupDistricts(province: String) {
        val districts = addressHelper.getDistricts(province)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDistrict.adapter = adapter
    }

    private fun setupWards(province: String, district: String) {
        val wards = addressHelper.getWards(province, district)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, wards)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerWard.adapter = adapter
    }

    private fun validateInput() {
        val mssv = edtMSSV.text.toString()
        val name = edtName.text.toString()
        val email = edtEmail.text.toString()
        val phone = edtPhone.text.toString()
        val gender = when (radioGroup.checkedRadioButtonId) {
            R.id.radioMale -> "Nam"
            R.id.radioFemale -> "Nữ"
            else -> ""
        }
        val termsAccepted = checkTerms.isChecked

        if (mssv.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() ||
            gender.isEmpty() || selectedBirthDate == 0L || !termsAccepted) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Thông tin đã được gửi!", Toast.LENGTH_SHORT).show()
        }
    }
}
