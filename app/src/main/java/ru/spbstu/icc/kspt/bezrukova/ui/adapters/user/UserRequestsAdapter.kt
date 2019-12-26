package ru.spbstu.icc.kspt.bezrukova.ui.adapters.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.request_item.view.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.model.Request

class UserRequestsAdapter(
    private val requestSelected: (request: Request) -> Unit
) : RecyclerView.Adapter<UserRequestsAdapter.ViewHolder>() {

    private var requests: List<Request> = listOf()

    fun setNewRequests(newRequests: List<Request>) {
        requests = newRequests
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.request_item,
                parent,
                false
            )
        )

    override fun getItemCount(): Int =
        requests.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date = view.requestDate
        private val specialization = view.requestDoctorSpecialization

        init {
            view.requestItem.setOnClickListener {
                requestSelected(requests[adapterPosition])
            }
        }

        fun bind() {
            val request = requests[adapterPosition]
            date.text = request.date
            specialization.text = request.doctor.specialization.name
        }
    }
}