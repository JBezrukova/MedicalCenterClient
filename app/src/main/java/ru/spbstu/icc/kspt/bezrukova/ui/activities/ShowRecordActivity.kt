package ru.spbstu.icc.kspt.bezrukova.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.show_record_activity.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.api.RestApi
import ru.spbstu.icc.kspt.bezrukova.model.Record
import ru.spbstu.icc.kspt.bezrukova.utils.Constants
import ru.spbstu.icc.kspt.bezrukova.utils.RequestsBody

class ShowRecordActivity : AppCompatActivity() {

    val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_record_activity)

        val record = intent.getParcelableExtra<Record>(Constants.RECORD) ?: return
        val notShowDeleteButton = intent.getBooleanExtra(Constants.NOT_DELETE, false)

        updateRecord(record, isDoctor = notShowDeleteButton)

        if (!notShowDeleteButton) {
            showDeleteButton(record)
        }

    }

    private fun showDeleteButton(record: Record) {
        record_remove_button.visibility = View.VISIBLE
        record_remove_button.setOnClickListener {
            disposable.add(
                RestApi.get().removeRecord(RequestsBody.getId(record.id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(
                            applicationContext,
                            "Заявка на удаление принята",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }, {
                        Log.e("MYTAG", it.toString())
                    })
            )
        }
    }

    private fun updateRecord(record: Record, isDoctor: Boolean) {
        record_id.text = getString(R.string.request_id, record.id.toString())
        record_date.text = getString(R.string.request_date, record.date)
        record_time.text = getString(R.string.request_time, record.time)
        if (isDoctor) {
            record_doctor_name.text = getString(R.string.request_user_name, record.user.userName)
            record_doctor_spec.text =
                getString(R.string.request_user_birthDay, record.user.birthDay)
            record_doctor_phone.text = getString(R.string.request_user_phone, record.user.phone)
        } else {
            record_doctor_name.text = getString(R.string.request_doctor_name, record.doctor.name)
            record_doctor_spec.text =
                getString(R.string.request_doctor_spec, record.doctor.specialization.name)
            record_doctor_phone.text = getString(R.string.request_doctor_phone, record.doctor.phone)
        }
    }

    override fun onStop() {
        disposable.dispose()
        super.onStop()
    }
}
