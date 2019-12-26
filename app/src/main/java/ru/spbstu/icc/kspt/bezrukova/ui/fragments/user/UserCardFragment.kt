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
import kotlinx.android.synthetic.main.user_card.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.api.RestApi
import ru.spbstu.icc.kspt.bezrukova.model.CardNode
import ru.spbstu.icc.kspt.bezrukova.model.User
import ru.spbstu.icc.kspt.bezrukova.model.UserCard
import ru.spbstu.icc.kspt.bezrukova.ui.activities.ShowCardNodeActivity
import ru.spbstu.icc.kspt.bezrukova.ui.activities.ShowRecordActivity
import ru.spbstu.icc.kspt.bezrukova.ui.adapters.user.UserCardAdapter
import ru.spbstu.icc.kspt.bezrukova.ui.adapters.user.UserRecordsAdapter
import ru.spbstu.icc.kspt.bezrukova.utils.Constants
import ru.spbstu.icc.kspt.bezrukova.utils.RequestsBody

private const val ARG_PARAM1 = "param1"

class UserCardFragment : Fragment() {
    private var user: User? = null
    private var disposable = CompositeDisposable()
    private var disposable2 = CompositeDisposable()

    private var adapter = UserCardAdapter { cardNode ->
        val intent = Intent(context, ShowCardNodeActivity::class.java).apply {
            putExtra(Constants.CARD_NODES, cardNode)
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
        inflater.inflate(R.layout.user_card, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userCardRecycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        disposable.add(
            RestApi.get().getUserCard(RequestsBody.getUserId(user?.userId!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setUserCard(it)
                }, {
                    onError(it)
                })
        )

        disposable2.add(
            RestApi.get().getUserCardNotes(RequestsBody.getUserId(user?.userId!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setUserCardNotes(it)
                }, {
                    onError(it)
                })
        )
    }

    private fun setUserCardNotes(cardNodes: List<CardNode>) {
        adapter.setNewCardNodes(cardNodes)
    }

    private fun setUserCard(userCard: UserCard) {
        user_card_fio.text = getString(R.string.user_card_fio, userCard.user.userName)
        user_card_oms.text = getString(R.string.user_card_oms, userCard.oms)
        user_card_passport.text = getString(R.string.user_card_passport, userCard.passport)
        user_card_chronic.text = getString(R.string.user_card_chronic, userCard.chronicDesease)
        user_card_notes.text = getString(R.string.user_card_notes, userCard.notes)
    }

    private fun onError(error: Throwable) {
        Log.e("MYTAG", error.toString())
    }

    override fun onStop() {
        disposable.dispose()
        disposable2.dispose()
        super.onStop()
    }


    companion object {
        @JvmStatic
        fun newInstance(user: User?): Fragment =
            UserCardFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, user)
                }
            }
    }
}
