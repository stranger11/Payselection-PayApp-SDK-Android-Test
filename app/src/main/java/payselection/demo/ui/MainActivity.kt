package payselection.demo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import payselection.demo.databinding.ActivityMainBinding
import payselection.payments.sdk.models.requests.pay.*
import payselection.payments.sdk.ui.ThreeDsDialogFragment


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}