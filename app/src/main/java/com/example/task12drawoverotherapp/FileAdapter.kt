package com.example.task12drawoverotherapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileAdapter : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private var files = listOf<File>()

    fun submitList(newList: List<File>) {
        files = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.file_name_layout, parent, false) // Updated layout name
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position])
    }

    override fun getItemCount(): Int = files.size

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fileNameText: TextView = itemView.findViewById(R.id.titleText)

        fun bind(file: File) {
            fileNameText.text = file.name
            /*fun bind(file: File) {
            (itemView as TextView).text = file.name
        }*/
        }

    }
}
