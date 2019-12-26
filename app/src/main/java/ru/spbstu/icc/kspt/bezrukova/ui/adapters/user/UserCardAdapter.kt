package ru.spbstu.icc.kspt.bezrukova.ui.adapters.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.request_item.view.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.model.CardNode
import ru.spbstu.icc.kspt.bezrukova.model.Record

class UserCardAdapter(
    private val cardNodeSelected: (cardNode: CardNode) -> Unit
) : RecyclerView.Adapter<UserCardAdapter.ViewHolder>() {

    private var cardNodes: List<CardNode> = listOf()

    fun setNewCardNodes(newCardNodes: List<CardNode>) {
        cardNodes = newCardNodes
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
        cardNodes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date = view.requestDate
        private val specialization = view.requestDoctorSpecialization

        init {
            view.requestItem.setOnClickListener {
                cardNodeSelected(cardNodes[adapterPosition])
            }
        }

        fun bind() {
            val cardNode = cardNodes[adapterPosition]
            date.text = cardNode.date
            specialization.text = cardNode.doctor.specialization.name
        }
    }
}