package ru.spbstu.icc.kspt.bezrukova.ui.fragments.user


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
import kotlinx.android.synthetic.main.user_requests.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.api.RestApi
import ru.spbstu.icc.kspt.bezrukova.model.Request
import ru.spbstu.icc.kspt.bezrukova.model.User
import ru.spbstu.icc.kspt.bezrukova.ui.activities.AddNewRequestActivity
import ru.spbstu.icc.kspt.bezrukova.ui.activities.ShowRequestActivity
import ru.spbstu.icc.kspt.bezrukova.ui.adapters.user.UserRequestsAdapter
import ru.spbstu.icc.kspt.bezrukova.utils.Constants
import ru.spbstu.icc.kspt.bezrukova.utils.RequestsBody

private const val ARG_PARAM1 = "param1"

class UserRequestsFragment : Fragment() {

    private var user: User? = null

    private var disposable = CompositeDisposable()

    private var adapter = UserRequestsAdapter { request ->
        val intent = Intent(context, ShowRequestActivity::class.java).apply {
            putExtra(Constants.REQUEST, request)
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
        inflater.inflate(R.layout.user_requests, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRequestsRecycler.adapter = adapter

        userRequestPlusButton.setOnClickListener {
            val intent = Intent(context, AddNewRequestActivity::class.java).apply {
                putExtra(Constants.USER, user)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        downloadUserRequests()
    }

    private fun downloadUserRequests() {
        disposable.add(
            RestApi.get().getRequests(RequestsBody.getLogin(user?.login.toString()))
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

    override fun onStop() {
        disposable.dispose()
        super.onStop()
    }

    companion object {

        @JvmStatic
        fun newInstance(user: User?): Fragment =
            UserRequestsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, user)
                }
            }
    }
}
