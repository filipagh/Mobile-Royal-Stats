package com.ezrs.feature

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import io.swagger.client.api.ConditionsApi
import io.swagger.client.model.ClanStats
import io.swagger.client.model.ConditionView

class ClanPlayersAdapter(private val myDataset: ClanStats) :
        RecyclerView.Adapter<ClanPlayersAdapter.ClanPlayersHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ClanPlayersHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById(R.id.playerListName) as TextView
        val role = view.findViewById(R.id.rolePlayerListField) as TextView
        val trophy = view.findViewById(R.id.trophyPlayerListField) as TextView
        val donation = view.findViewById(R.id.donationPlayerListField) as TextView
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ClanPlayersAdapter.ClanPlayersHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.clan_players_list_item, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ClanPlayersHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ClanPlayersHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.text = "${myDataset.members[position].name} / level: ${myDataset.members[position].expLevel}"
        holder.donation.text = "Donations: ${myDataset.members[position].donations}"
        holder.role.text = "Role: ${myDataset.members[position].role}"
        holder.trophy.text = "Trophy: ${myDataset.members[position].trophies}"
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.members.size
}
