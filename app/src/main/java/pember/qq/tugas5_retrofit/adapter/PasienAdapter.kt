package pember.qq.tugas5_retrofit.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pember.qq.tugas5_retrofit.R
import pember.qq.tugas5_retrofit.model.Pasien

class PasienAdapter(private val list: MutableList<Pasien>) :
    RecyclerView.Adapter<PasienAdapter.PasienViewHolder>() {

    inner class PasienViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAvatar: TextView = itemView.findViewById(R.id.tvAvatar)
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvJenisKelamin: TextView = itemView.findViewById(R.id.tvJenisKelamin)
        val tvTanggalLahir: TextView = itemView.findViewById(R.id.tvTanggalLahir)
        val tvNoTelepon: TextView = itemView.findViewById(R.id.tvNoTelepon)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasienViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pasien, parent, false)
        return PasienViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasienViewHolder, position: Int) {
        val pasien = list[position]

        // Inisial 2 huruf
        val namaParts = pasien.nama.trim().split(" ")
        val inisial = if (namaParts.size >= 2) {
            "${namaParts[0].first()}${namaParts[1].first()}"
        } else {
            pasien.nama.take(2)
        }.uppercase()
        holder.tvAvatar.text = inisial

        // Avatar circle — warna langsung, tanpa drawable file
        val avatarColor = if (pasien.jenis_kelamin == "L") Color.parseColor("#1E88E5")
        else Color.parseColor("#E91E8C")

        val circle = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(avatarColor)
        }
        holder.tvAvatar.background = circle

        // Teks jenis kelamin
        holder.tvNama.text = pasien.nama
        holder.tvJenisKelamin.text = if (pasien.jenis_kelamin == "L") "♂ Laki-laki" else "♀ Perempuan"
        holder.tvTanggalLahir.text = formatTanggal(pasien.tanggal_lahir)
        holder.tvNoTelepon.text = "☎ ${pasien.no_telepon}"
        holder.tvAlamat.text = "📍 ${pasien.alamat}"
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<Pasien>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    private fun formatTanggal(tanggal: String): String {
        return try {
            val parts = tanggal.split("-")
            val bulan = listOf(
                "", "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
                "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
            )
            "${parts[2]} ${bulan[parts[1].toInt()]} ${parts[0]}"
        } catch (e: Exception) {
            tanggal
        }
    }
}