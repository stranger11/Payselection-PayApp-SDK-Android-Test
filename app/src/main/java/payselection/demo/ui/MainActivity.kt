package payselection.demo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import payselection.demo.R
import payselection.demo.databinding.ActivityMainBinding
import payselection.demo.ui.checkout.CheckoutFragment
import payselection.demo.ui.error.ErrorFragment
import payselection.demo.ui.success.SuccessFragment
import payselection.payments.sdk.ui.ThreeDsDialogFragment


class MainActivity : AppCompatActivity() , ThreeDsDialogFragment.ThreeDSDialogListener {

    private lateinit var viewBinding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, CheckoutFragment())
        fragmentTransaction.commit()
    }

    override fun onAuthorizationCompleted() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, SuccessFragment())
            .addToBackStack(SuccessFragment::class.java.canonicalName)
        fragmentTransaction.commit()
    }

    override fun onAuthorizationFailed() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, ErrorFragment())
            .addToBackStack(ErrorFragment::class.java.canonicalName)
        fragmentTransaction.commit()
    }
}