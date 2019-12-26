package ru.spbstu.icc.kspt.bezrukova.ui.fragments.user


import android.content.Context
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
import kotlinx.android.synthetic.main.user_records.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.api.RestApi
import ru.spbstu.icc.kspt.bezrukova.model.Record
import ru.spbstu.icc.kspt.bezrukova.model.User
import ru.spbstu.icc.kspt.bezrukova.ui.activities.ShowRecordActivity
import ru.spbstu.icc.kspt.bezrukova.ui.adapters.user.UserRecordsAdapter
import ru.spbstu.icc.kspt.bezrukova.utils.Constants
import ru.spbstu.icc.kspt.bezrukova.utils.RequestsBody

private const val ARG_PARAM1 = "param1"

class UserRecordsFragment : Fragment() {
    private var user: User? = null

    private var disposable = CompositeDisposable()

    private var adapter = UserRecordsAdapter { record ->
        val intent = Intent(context, ShowRecordActivity::class.java).apply {
            putExtra(Constants.RECORD, record)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.user_records, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRecordsRecycler.adapter = adapter

    }

    override fun onResume() {
        super.onResume()

        downloadUserRecords()
    }

    private fun downloadUserRecords() {
        disposable.add(
            RestApi.get().getRecords(RequestsBody.getLogin(user?.login.toString()))
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
        fun newInstance(user: User?): Fragment =
            UserRecordsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, user)
                }
            }
    }
}
