package ru.spbstu.icc.kspt.bezrukova.ui.fragments.admin


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.doctor_requests.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.api.RestApi
import ru.spbstu.icc.kspt.bezrukova.model.Admin
import ru.spbstu.icc.kspt.bezrukova.model.Request
import ru.spbstu.icc.kspt.bezrukova.ui.activities.ShowRequestActivity
import ru.spbstu.icc.kspt.bezrukova.ui.adapters.doctor.DoctorRequestsAdapter
import ru.spbstu.icc.kspt.bezrukova.utils.Constants
import ru.spbstu.icc.kspt.bezrukova.utils.RequestsBody

private const val ARG_PARAM1 = "param1"

class AdminRequestsFragment : Fragment() {

    private var admin: Admin? = null

    private var disposable = CompositeDisposable()

    private var adapter = DoctorRequestsAdapter { request ->
        val intent = Intent(context, ShowRequestActivity::class.java).apply {
            putExtra(Constants.REQUEST, request)
            putExtra(Constants.IS_APPROVE, true)
            putExtra(Constants.IS_ADMIN, true)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            admin = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.doctor_requests, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doctorRequestsRecycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        downloadAdminRequests()
    }

    private fun downloadAdminRequests() {
        disposable.add(
            RestApi.get().getAllRequests()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ requestSuccessDownloaded(it) }, { onError(it) })
        )
    }

    private fun requestSuccessDownloaded(requestsList: List<Request>) {
        adapter.setNewRequests(requestsList)
    }

    private fun onError(error: Throwable) {
        Log.e("MYTAG", error.toString())
    }

    companion object {

        @JvmStatic
        fun newInstance(admin: Admin?): Fragment =
            AdminRequestsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, admin)
                }
            }
    }
}
