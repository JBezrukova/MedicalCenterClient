package ru.spbstu.icc.kspt.bezrukova

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.start_acitivity.*
import okhttp3.RequestBody
import ru.spbstu.icc.kspt.bezrukova.api.RestApi
import ru.spbstu.icc.kspt.bezrukova.model.Admin
import ru.spbstu.icc.kspt.bezrukova.model.Doctor
import ru.spbstu.icc.kspt.bezrukova.model.Role
import ru.spbstu.icc.kspt.bezrukova.model.User
import ru.spbstu.icc.kspt.bezrukova.utils.Constants
import ru.spbstu.icc.kspt.bezrukova.utils.RequestsBody

class StartActivity : AppCompatActivity() {

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_acitivity)

        loginButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (login.isNotBlank() && password.isNotBlank()) {
                val requestBody = RequestsBody.getLoginPassword(login, password)

                when (radioGroup.checkedRadioButtonId) {
                    userRadioButton.id -> userRequestLogin(requestBody)

                    adminRadioButton.id -> adminRequestLogin(requestBody)

                    doctorRadioButton.id -> doctorRequestLogin(requestBody)
                }
            }
        }
    }

    private fun userRequestLogin(requestBody: RequestBody) {
        disposable.add(
            RestApi.get().requestUserLogin(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userLoginSuccess(it) }, { loginError() })
        )
    }

    private fun adminRequestLogin(requestBody: RequestBody) {
        disposable.add(
            RestApi.get().requestAdminLogin(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userAdminSuccess(it) }, { loginError() })
        )
    }

    private fun doctorRequestLogin(requestBody: RequestBody) {
        disposable.add(
            RestApi.get().requestDoctorLogin(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userDoctorSuccess(it) }, { loginError() })
        )
    }

    private fun userLoginSuccess(user: User) {
        startActivity(getIntent(Role.USER).apply {
            putExtra(Constants.USER, user)
        })
        finish()
    }

    private fun userAdminSuccess(admin: Admin) {
        startActivity(getIntent(Role.ADMIN).apply {
            putExtra(Constants.ADMIN, admin)
        })
        finish()
    }

    private fun userDoctorSuccess(doctor: Doctor) {
        startActivity(getIntent(Role.DOCTOR).apply {
            putExtra(Constants.DOCTOR, doctor)
        })
        finish()
    }

    private fun getIntent(role: Role) = Intent(this, MainActivity::class.java).apply {
        putExtra(Constants.ROLE, role)
    }


    private fun loginError() {
        Toast.makeText(
            applicationContext,
            "Некорректный логин/пароль, введите еще раз!",
            Toast.LENGTH_LONG
        ).show()
    }


    override fun onStop() {
        disposable.dispose()
        super.onStop()
    }
}