package ru.spbstu.icc.kspt.bezrukova.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.show_user_node_activity.*
import ru.spbstu.icc.kspt.bezrukova.R
import ru.spbstu.icc.kspt.bezrukova.model.CardNode
import ru.spbstu.icc.kspt.bezrukova.utils.Constants

class ShowCardNodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_user_node_activity)

        val cardNode = intent.getParcelableExtra<CardNode>(Constants.CARD_NODES) ?: return

        updateCardNodes(cardNode)
    }

    private fun updateCardNodes(cardNode: CardNode) {
        user_card_note_date.text = getString(R.string.user_card_note_date, cardNode.date)
        user_card_note_doctor_spec.text =
            getString(R.string.user_card_note_doctor_spec, cardNode.doctor.specialization.name)
        user_card_note_doctor_name.text =
            getString(R.string.user_card_note_doctor_name, cardNode.doctor.name)
        user_card_note_complaints.text =
            getString(R.string.user_card_note_complaints, cardNode.complaints)
        user_card_note_treatment.text =
            getString(R.string.user_card_note_treatment, cardNode.treatment)
        user_card_note_test.text = getString(R.string.user_card_note_test, cardNode.test)
    }
}
