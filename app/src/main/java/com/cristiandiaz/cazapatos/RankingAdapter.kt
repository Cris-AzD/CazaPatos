package com.cristiandiaz.cazapatos

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankingAdapter(private val dataSet: ArrayList<Player>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER: Int = 0
    private val TYPE_ITEM: Int = 1

    class ViewHolderHeader(view: View) : RecyclerView.ViewHolder(view) {
        val textViewPosicion: TextView = view.findViewById(R.id.textViewPosicion)
        val textViewPatosCazados: TextView = view.findViewById(R.id.textViewPatosCazados)
        val textViewUsuario: TextView = view.findViewById(R.id.textViewUsuario)
        val imageViewMedal: ImageView = view.findViewById(R.id.imageViewMedal)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewPosicion: TextView = view.findViewById(R.id.textViewPosicion)
        val textViewPatosCazados: TextView = view.findViewById(R.id.textViewPatosCazados)
        val textViewUsuario: TextView = view.findViewById(R.id.textViewUsuario)
        val imageViewMedal: ImageView = view.findViewById(R.id.imageViewMedal)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ranking_list, parent, false)
        return if (viewType == TYPE_HEADER) {
            ViewHolderHeader(view)
        } else {
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderHeader) {
            holder.textViewPosicion.text = "#"
            holder.textViewPosicion.paintFlags = holder.textViewPosicion.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            holder.textViewPosicion.setTextColor(holder.textViewPosicion.context.getColor(R.color.colorPrimaryDark))
            holder.textViewPatosCazados.paintFlags = holder.textViewPatosCazados.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            holder.textViewPatosCazados.setTextColor(holder.textViewPatosCazados.context.getColor(R.color.colorPrimaryDark))
            holder.textViewUsuario.paintFlags = holder.textViewUsuario.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            holder.textViewUsuario.setTextColor(holder.textViewUsuario.context.getColor(R.color.colorPrimaryDark))
            holder.imageViewMedal.visibility = View.INVISIBLE

        } else if (holder is ViewHolder) {
            val playerPosition = position
            val player = dataSet[position - 1]

            holder.textViewPosicion.text = playerPosition.toString()
            holder.textViewPatosCazados.text = player.huntedDucks.toString()
            holder.textViewUsuario.text = player.username

            //MEDALLAS
            holder.imageViewMedal.visibility = View.VISIBLE
            when (playerPosition) {
                1 -> holder.imageViewMedal.setImageResource(R.drawable.gold)   // 1er Lugar
                2 -> holder.imageViewMedal.setImageResource(R.drawable.silver) // 2do Lugar
                3 -> holder.imageViewMedal.setImageResource(R.drawable.bronze) // 3er Lugar
                else -> {
                    holder.imageViewMedal.setImageResource(R.drawable.empty)
                }
            }
        }
    }

    override fun getItemCount() = dataSet.size + 1
}