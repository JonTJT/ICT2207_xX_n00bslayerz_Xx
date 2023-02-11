package com.example.assignment1

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import org.w3c.dom.Text


class OrganisationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getIntExtra("id",0)
        setContentView(R.layout.activity_organisation)
        renderInfo(id)

        findViewById<TextView>(R.id.wesley_button).setOnClickListener(::profile)
        findViewById<TextView>(R.id.mengrong_button).setOnClickListener(::profile)
        findViewById<TextView>(R.id.lynette_button).setOnClickListener(::profile)
        findViewById<TextView>(R.id.minyao_button).setOnClickListener(::profile)
        findViewById<TextView>(R.id.keefe_button).setOnClickListener(::profile)
        findViewById<TextView>(R.id.jon_button).setOnClickListener(::profile)
    }

    private fun renderInfo(id: Int) {
        val industry_name = findViewById<TextView>(R.id.industry_name)
        val wesley_button = findViewById<Button>(R.id.wesley_button)
        val mengrong_button = findViewById<Button>(R.id.mengrong_button)
        val lynette_button = findViewById<Button>(R.id.lynette_button)
        val minyao_button = findViewById<Button>(R.id.minyao_button)
        val keefe_button = findViewById<Button>(R.id.keefe_button)
        val jon_button = findViewById<Button>(R.id.jon_button)

        when(id) {
            R.id.itBtn -> {
                industry_name.setText(R.string.it)
                wesley_button.setText(R.string.wesley_it)
                mengrong_button.setText(R.string.mengrong_it)
                lynette_button.setText(R.string.lynette_it)
                minyao_button.setText(R.string.minyao_it)
                keefe_button.setText(R.string.keefe_it)
                jon_button.setText(R.string.jon_it)
            }
            R.id.constrBtn -> {
                industry_name.setText(R.string.construction)
                wesley_button.setText(R.string.wesley_construction)
                mengrong_button.setText(R.string.mengrong_construction)
                lynette_button.setText(R.string.lynette_construction)
                minyao_button.setText(R.string.minyao_construction)
                keefe_button.setText(R.string.keefe_construction)
                jon_button.setText(R.string.jon_construction)
            }
            R.id.eduBtn -> {
                industry_name.setText(R.string.education)
                wesley_button.setText(R.string.wesley_education)
                mengrong_button.setText(R.string.mengrong_education)
                lynette_button.setText(R.string.lynette_education)
                minyao_button.setText(R.string.minyao_education)
                keefe_button.setText(R.string.keefe_education)
                jon_button.setText(R.string.jon_education)
            }
            R.id.enterBtn -> {
                industry_name.setText(R.string.entertainment)
                wesley_button.setText(R.string.wesley_entertainment)
                mengrong_button.setText(R.string.mengrong_entertainment)
                lynette_button.setText(R.string.lynette_entertainment)
                minyao_button.setText(R.string.minyao_entertainment)
                keefe_button.setText(R.string.keefe_entertainment)
                jon_button.setText(R.string.jon_entertainment)
            }
            R.id.financeBtn -> {
                industry_name.setText(R.string.finance)
                wesley_button.setText(R.string.wesley_finance)
                mengrong_button.setText(R.string.mengrong_finance)
                lynette_button.setText(R.string.lynette_finance)
                minyao_button.setText(R.string.minyao_finance)
                keefe_button.setText(R.string.keefe_finance)
                jon_button.setText(R.string.jon_finance)
            }
            R.id.fnbBtn -> {
                industry_name.setText(R.string.fnb)
                wesley_button.setText(R.string.wesley_fnb)
                mengrong_button.setText(R.string.mengrong_fnb)
                lynette_button.setText(R.string.lynette_fnb)
                minyao_button.setText(R.string.minyao_fnb)
                keefe_button.setText(R.string.keefe_fnb)
                jon_button.setText(R.string.jon_fnb)
            }
        }
    }
    private fun profile(view: View) {
        val intent = Intent(this, ProfileTemplate::class.java)
        intent.putExtra("id", view.id)
        intent.putExtra("industry_name", findViewById<TextView>(R.id.industry_name).text)
        startActivity(intent)
    }
}
