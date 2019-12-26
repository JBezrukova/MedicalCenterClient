package ru.spbstu.icc.kspt.bezrukova.ui.adapters.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.request_item.view.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.model.Record

class UserRecordsAdapter(
    private val recordsSelected: (record: Record) -> Unit
) : RecyclerView.Adapter<UserRecordsAdapter.ViewHolder>() {

    private var records: List<Record> = listOf()

    fun setNewRecords(newRecords: List<Record>) {
        records = newRecords
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
        records.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date = view.requestDate
        private val specialization = view.requestDoctorSpecialization

        init {
            view.requestItem.setOnClickListener {
                recordsSelected(records[adapterPosition])
            }
        }

        fun bind() {
            val record = records[adapterPosition]
            date.text = record.date
            specialization.text = record.doctor.specialization.name
        }
    }
}