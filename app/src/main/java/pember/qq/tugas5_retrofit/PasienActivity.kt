package pember.qq.tugas5_retrofit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pember.qq.tugas5_retrofit.adapter.PasienAdapter
import pember.qq.tugas5_retrofit.model.Pasien
import pember.qq.tugas5_retrofit.model.PasienResponse
import pember.qq.tugas5_retrofit.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasienActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var tvTotalPasien: TextView
    private lateinit var tvLakiLaki: TextView
    private lateinit var tvPerempuan: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutLoading: LinearLayout
    private lateinit var layoutError: LinearLayout
    private lateinit var tvError: TextView
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var btnRetry: TextView
    private lateinit var btnLogout: TextView

    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: PasienAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasien)

        sessionManager = SessionManager(this)

        tvUserName = findViewById(R.id.tvUserName)
        tvTotalPasien = findViewById(R.id.tvTotalPasien)
        tvLakiLaki = findViewById(R.id.tvLakiLaki)
        tvPerempuan = findViewById(R.id.tvPerempuan)
        recyclerView = findViewById(R.id.recyclerView)
        layoutLoading = findViewById(R.id.layoutLoading)
        layoutError = findViewById(R.id.layoutError)
        tvError = findViewById(R.id.tvError)
        layoutEmpty = findViewById(R.id.layoutEmpty)
        btnRetry = findViewById(R.id.btnRetry)
        btnLogout = findViewById(R.id.btnLogout)

        tvUserName.text = sessionManager.getUserName() ?: "Pengguna"

        adapter = PasienAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnRetry.setOnClickListener { fetchPasien() }

        btnLogout.setOnClickListener {
            sessionManager.clearSession()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        fetchPasien()
    }

    private fun fetchPasien() {
        showLoading(true)
        hideError()

        val token = "Bearer ${sessionManager.getToken()}"
        RetrofitClient.instance.getPasien(token).enqueue(object : Callback<PasienResponse> {

            override fun onResponse(
                call: Call<PasienResponse>,
                response: Response<PasienResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success) {
                        val list = body.data
                        if (list.isEmpty()) {
                            showEmpty(true)
                        } else {
                            showEmpty(false)
                            adapter.updateData(list)
                            updateStats(list)
                        }
                    } else {
                        showError(body?.message ?: "Gagal memuat data pasien.")
                    }
                } else {
                    showError("Gagal memuat data. Kode: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PasienResponse>, t: Throwable) {
                showLoading(false)
                showError("Koneksi gagal: ${t.localizedMessage}")
            }
        })
    }

    private fun updateStats(list: List<Pasien>) {
        tvTotalPasien.text = list.size.toString()
        tvLakiLaki.text = list.count { it.jenis_kelamin == "L" }.toString()
        tvPerempuan.text = list.count { it.jenis_kelamin == "P" }.toString()
    }

    private fun showLoading(show: Boolean) {
        layoutLoading.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        tvError.text = message
        layoutError.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideError() {
        layoutError.visibility = View.GONE
    }

    private fun showEmpty(show: Boolean) {
        layoutEmpty.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }
}