package ru.spbstu.icc.kspt.bezrukova


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_activity.*
import ru.spbstu.icc.kspt.bezrukova.model.Admin
import ru.spbstu.icc.kspt.bezrukova.model.Doctor
import ru.spbstu.icc.kspt.bezrukova.model.Role
import ru.spbstu.icc.kspt.bezrukova.model.User
import ru.spbstu.icc.kspt.bezrukova.ui.fragments.admin.AdminRequestsFragment
import ru.spbstu.icc.kspt.bezrukova.ui.fragments.doctor.DoctorRecordsFragment
import ru.spbstu.icc.kspt.bezrukova.ui.fragments.doctor.DoctorRequestsFragment
import ru.spbstu.icc.kspt.bezrukova.ui.fragments.user.UserCardFragment
import ru.spbstu.icc.kspt.bezrukova.ui.fragments.user.UserRecordsFragment
import ru.spbstu.icc.kspt.bezrukova.ui.fragments.user.UserRequestsFragment
import ru.spbstu.icc.kspt.bezrukova.utils.Constants

class MainActivity : AppCompatActivity() {

    lateinit var role: Role
    var user: User? = null
    var admin: Admin? = null
    var doctor: Doctor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        role = intent.getSerializableExtra(Constants.ROLE) as? Role ?: Role.USER

        when (role) {
            Role.USER -> {
                user = intent.getParcelableExtra(Constants.USER)
                user_nav_view.visibility = View.VISIBLE
                doctor_nav_view.visibility = View.GONE
                user_nav_view.setOnNavigationItemSelectedListener(userNavigationItemSelectedListener)
                user_nav_view.selectedItemId = R.id.navigation_user_requests
            }

            Role.ADMIN -> {
                admin = intent.getParcelableExtra(Constants.ADMIN)
                user_nav_view.visibility = View.GONE
                doctor_nav_view.visibility = View.GONE
                changeFragment(AdminRequestsFragment.newInstance(admin))
            }

            Role.DOCTOR -> {
                doctor = intent.getParcelableExtra(Constants.DOCTOR)
                user_nav_view.visibility = View.GONE
                doctor_nav_view.visibility = View.VISIBLE
                doctor_nav_view.setOnNavigationItemSelectedListener(
                    doctorNavigationItemSelectedListener
                )
                doctor_nav_view.selectedItemId = R.id.navigation_doctor_requests
            }
        }

    }

    private fun changeFragment(fragment: Fragment) {
//        val currentFragment =
//            supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)
//        if (currentFragment != null && currentFragment.isVisible) {
//            return
//        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
//            if (supportFragmentManager.backStackEntryCount < 2) {
//                addToBackStack(null)
//            }

    }

    private val userNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_user_requests -> {
                    changeFragment(UserRequestsFragment.newInstance(user))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_user_records -> {
                    changeFragment(UserRecordsFragment.newInstance(user))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_user_card -> {
                    changeFragment(UserCardFragment.newInstance(user))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private val doctorNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_doctor_requests -> {
                    changeFragment(DoctorRequestsFragment.newInstance(doctor))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_doctor_records -> {
                    changeFragment(DoctorRecordsFragment.newInstance(doctor))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onBackPressed() {
//        if (supportFragmentManager.backStackEntryCount > 1) {
//            supportFragmentManager.popBackStack()
//        }
//        else {
        super.onBackPressed()
//            finish()
//        }
    }
}
