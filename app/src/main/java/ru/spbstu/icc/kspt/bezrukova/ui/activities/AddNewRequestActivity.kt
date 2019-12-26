package ru.spbstu.icc.kspt.bezrukova.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_new_request_activity.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.api.RestApi
import ru.spbstu.icc.kspt.bezrukova.model.Doctor
import ru.spbstu.icc.kspt.bezrukova.model.DoctorCategory
import ru.spbstu.icc.kspt.bezrukova.model.User
import ru.spbstu.icc.kspt.bezrukova.utils.Constants
import ru.spbstu.icc.kspt.bezrukova.utils.RequestsBody
import java.text.SimpleDateFormat
import java.util.*

class AddNewRequestActivity : AppCompatActivity() {

    val disposable = CompositeDisposable()
    val disposable2 = CompositeDisposable()
    val disposable3 = CompositeDisposable()
    val disposable4 = CompositeDisposable()

    var listOfSpecialization: List<DoctorCategory> = emptyList()
    var listOfDoctors: List<Doctor> = emptyList()
    var listOfTimes: List<String> = emptyList()

    var currentDate = ""
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_request_activity)

        user = intent.getParcelableExtra(Constants.USER) ?: return

        val sdf = SimpleDateFormat("dd-M-yyyy", Locale.US)
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_YEAR, 1)
        currentDate = sdf.format(date.time)

        disposable.add(
            RestApi.get().getAllSpecialisations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listOfSpecialization = it
                    val arrayAdapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        listOfSpecialization
                    )
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    spinnerSpec.adapter = arrayAdapter
                }, {
                    Log.e("MYTAG", it.toString())
                })
        )


        spinnerSpec.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val currentSpecialization = listOfSpecialization[position]

                disposable2.add(
                    RestApi.get().getDoctorsWithSpecialisation(
                        RequestsBody.getId(currentSpecialization.categoryId)
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            spinnerListDoctors.visibility = View.VISIBLE
                            add_new_request_text_spisok_doctorov.visibility = View.VISIBLE
                            listOfDoctors = it
                            val arrayAdapter = ArrayAdapter(
                                parent?.context!!,
                                android.R.layout.simple_spinner_item,
                                listOfDoctors
                            )
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                            spinnerListDoctors.adapter = arrayAdapter
                        }, {
                            Log.e("MYTAG", it.toString())

                        })
                )
            }

        }

        spinnerListDoctors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val currentDoctor = listOfDoctors[position]

                disposable3.add(
                    RestApi.get().getFreeTimeForDoctor(
                        RequestsBody.getFreeTimeForDoctor(currentDoctor.doctorId, currentDate)
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            listOfTimes = it
                            val arrayAdapter = ArrayAdapter(
                                parent?.context!!,
                                android.R.layout.simple_spinner_item,
                                listOfTimes
                            )
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                            spinnerListTimes.adapter = arrayAdapter
                        }, {
                            Log.e("MYTAG", it.toString())

                        })
                )
            }
        }

        add_new_request_button.setOnClickListener {
            disposable3.add(
                RestApi.get().createRequest(
                    RequestsBody.createRequest(
                        currentDate,
                        spinnerListTimes.selectedItem as String,
                        user.userId,
                        (spinnerListDoctors.selectedItem as Doctor).doctorId
                    )
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(
                            applicationContext,
                            "Заявка успешно отправлена",
                            Toast.LENGTH_LONG
                        ).show()
                    }, {
                        Log.e("MYTAG", it.toString())
                    })
            )
        }

    }

    override fun onStop() {
        disposable.dispose()
        disposable2.dispose()
        disposable3.dispose()
        disposable4.dispose()
        super.onStop()
    }
}
