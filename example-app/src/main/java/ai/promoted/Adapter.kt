package ai.promoted

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private val content: List<AbstractContent>,
    private val clickListener: OnClickListener
) : RecyclerView.Adapter<Adapter.VH>() {
    constructor(
        content: List<AbstractContent>,
        clickListener: (content: AbstractContent) -> Unit
    ) : this(
        content,
        object : OnClickListener {
            override fun onClick(item: AbstractContent) {
                clickListener.invoke(item)
            }
        }
    )

    interface OnClickListener {
        fun onClick(item: AbstractContent)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView = itemView.findViewById(R.id.name)
        val openButton: Button = itemView.findViewById(R.id.open)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fake_content, parent, false)

        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val content = content[position]
        holder.nameTv.text = content.name
        holder.openButton.setOnClickListener {
            clickListener.onClick(content)
        }
    }

    override fun getItemCount(): Int {
        return content.size
    }
}