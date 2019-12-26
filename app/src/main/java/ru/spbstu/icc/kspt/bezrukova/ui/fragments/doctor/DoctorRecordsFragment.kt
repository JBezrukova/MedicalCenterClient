package ru.spbstu.icc.kspt.bezrukova.ui.fragments.doctor


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
import kotlinx.android.synthetic.main.doctor_records.*
import kotlinx.android.synthetic.main.user_records.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.api.RestApi
import ru.spbstu.icc.kspt.bezrukova.model.Doctor
import ru.spbstu.icc.kspt.bezrukova.model.Record
import ru.spbstu.icc.kspt.bezrukova.ui.activities.ShowRecordActivity
import ru.spbstu.icc.kspt.bezrukova.ui.adapters.doctor.DoctorRecordsAdapter
import ru.spbstu.icc.kspt.bezrukova.ui.adapters.user.UserRecordsAdapter
import ru.spbstu.icc.kspt.bezrukova.utils.Constants
import ru.spbstu.icc.kspt.bezrukova.utils.RequestsBody

private const val ARG_PARAM1 = "param1"

class DoctorRecordsFragment : Fragment() {
    private var doctor: Doctor? = null

    private var disposable = CompositeDisposable()

    private var adapter = DoctorRecordsAdapter { record ->
        val intent = Intent(context, ShowRecordActivity::class.java).apply {
            putExtra(Constants.RECORD, record)
            putExtra(Constants.NOT_DELETE, true)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doctor = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.doctor_records, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doctorRecordsRecycler.adapter = adapter

    }

    override fun onResume() {
        super.onResume()

        downloadDoctorRecords()
    }

    private fun downloadDoctorRecords() {
        disposable.add(
            RestApi.get().getRecordsForDoctor(RequestsBody.getId(doctor?.id ?: -1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { recordsSuccessDownloaded(it)}, {onError(it)})
        )
    }

    override fun onStop() {
        disposable.dispose()
        super.onStop()
    }

    private fun recordsSuccessDownloaded(recordsList: List<Record>) {
        adapter.setNewRecords(recordsList)
    }

    private fun onError(error: Throwable) {
        Log.e("MYTAG", error.toString())
    }

    companion object {

        @JvmStatic
        fun newInstance(doctor: Doctor?): Fragment =
            DoctorRecordsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, doctor)
                }
            }
    }
}
