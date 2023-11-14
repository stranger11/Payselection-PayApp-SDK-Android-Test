package payselection.demo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import payselection.demo.R
import payselection.demo.databinding.ActivityMainBinding
import payselection.demo.ui.checkout.CheckoutFragment
import payselection.demo.ui.result.ResultFragment
import payselection.demo.ui.result.ResultFragment.Companion.ARG_IS_SUCCESS
import payselection.payments.sdk.ui.ThreeDsDialogFragment


class MainActivity : AppCompatActivity(), ThreeDsDialogFragment.ThreeDSDialogListener {

    private lateinit var viewBinding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, CheckoutFragment())
        fragmentTransaction.commit()
    }

    override fun onAuthorizationCompleted() {
        val bundle = Bundle()
        bundle.putBoolean(ARG_IS_SUCCESS, true)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, ResultFragment.createInstance(bundle))
            .addToBackStack(ResultFragment::class.java.canonicalName)
        fragmentTransaction.commit()
    }

    override fun onAuthorizationFailed() {
        val bundle = Bundle()
        bundle.putBoolean(ARG_IS_SUCCESS, false)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, ResultFragment.createInstance(bundle))
            .addToBackStack(ResultFragment::class.java.canonicalName)
        fragmentTransaction.commit()
    }
}