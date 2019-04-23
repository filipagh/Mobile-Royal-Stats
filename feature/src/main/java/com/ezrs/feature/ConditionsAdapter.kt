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
import io.swagger.client.model.ConditionView

class ConditionsAdapter(private val myDataset: List<ConditionView>) :
        RecyclerView.Adapter<ConditionsAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById(R.id.conditionListName) as TextView
        val field = view.findViewById(R.id.conditionListField) as TextView
        val operator = view.findViewById(R.id.conditionListOperator) as TextView
        val value = view.findViewById(R.id.conditionListValue) as TextView
        val delete = view.findViewById(R.id.deleteCondition) as Button
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ConditionsAdapter.MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.condition_list_item, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.field.text = myDataset[position].field
        holder.name.text = myDataset[position].name
        holder.operator.text = myDataset[position].operator
        holder.value.text = myDataset[position].value
        holder.view.setOnClickListener { view ->
            val intent = Intent(view.context, CreateCondition::class.java)
            intent.putExtra(CreateCondition.INTENT_ID, myDataset[position].id)
            view.context.startActivity(intent)
        }
        holder.delete.setOnClickListener { view ->
            val api = ConditionsApi()
            api.delete(myDataset[position].id, view.context.getSharedPreferences(LoginActivity.PREFERENCE, Activity.MODE_PRIVATE).getString(LoginActivity.APIKEY, ""), {
                val text = "Zmazane!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(holder.view.context, text, duration)
                toast.show()
            }, { err ->
                val text = "Something went wrong!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(holder.view.context, text, duration)
                toast.show()
            })
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
