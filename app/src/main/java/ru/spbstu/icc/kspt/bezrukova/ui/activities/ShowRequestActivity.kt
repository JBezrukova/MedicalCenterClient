package ru.spbstu.icc.kspt.bezrukova.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.show_request_activity.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.api.RestApi
import ru.spbstu.icc.kspt.bezrukova.model.Request
import ru.spbstu.icc.kspt.bezrukova.utils.Constants
import ru.spbstu.icc.kspt.bezrukova.utils.RequestsBody

class ShowRequestActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_request_activity)

        val request = intent.getParcelableExtra<Request>(Constants.REQUEST) ?: return
        val isDoctor = intent.getBooleanExtra(Constants.IS_APPROVE, false)
        val isAdmin = intent.getBooleanExtra(Constants.IS_ADMIN, false)

        updateRequest(request, isDoctor)

        if (isDoctor) {
            initButton(request, isAdmin)
        }
    }

    private fun updateRequest(request: Request, isDoctor: Boolean) {
        request_id.text = getString(R.string.request_id, request.id.toString())
        request_date.text = getString(R.string.request_date, request.date)
        request_time.text = getString(R.string.request_time, request.time)
        request_doctor_approved.text =
            getString(R.string.request_doctor_approved, request.approvedByDoctor.toString())
        request_admin_approved.text =
            getString(R.string.request_admin_approved, request.approvedByAdmin.toString())
        request_doctor_reason.text = getString(R.string.request_doctor_reason, request.reason)
        if (isDoctor) {
            request_doctor_name.text = getString(R.string.request_user_name, request.user.userName)
            request_doctor_spec.text =
                getString(R.string.request_user_birthDay, request.user.birthDay)
            request_doctor_phone.text = getString(R.string.request_user_phone, request.user.phone)
        } else {
            request_doctor_name.text = getString(R.string.request_doctor_name, request.doctor.name)
            request_doctor_spec.text =
                getString(R.string.request_doctor_spec, request.doctor.specialization.name)
            request_doctor_phone.text =
                getString(R.string.request_doctor_phone, request.doctor.phone)
        }
    }

    private fun initButton(request: Request, isAdmin: Boolean) {
        val requestBody = if (isAdmin) {
            RequestsBody.updateRequest(
                request.id,
                request.date,
                request.time,
                request.user.userId,
                request.doctor.doctorId,
                request.reason,
                true,
                request.approvedByDoctor
            )
        } else {
            RequestsBody.updateRequest(
                request.id,
                request.date,
                request.time,
                request.user.userId,
                request.doctor.doctorId,
                request.reason,
                request.approvedByAdmin,
                true
            )
        }


        request_button.visibility = View.VISIBLE
        request_button.setOnClickListener {
            disposable.add(
                RestApi.get().updateRequest(requestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(
                            applicationContext,
                            "Подтверждение сохранено",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }, {
                        Log.e("MYTAG", it.toString())
                    })
            )
        }
    }

    override fun onStop() {
        disposable.dispose()
        super.onStop()
    }

}
