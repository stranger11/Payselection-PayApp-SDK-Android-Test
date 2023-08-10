package payselection.payments.sdk.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import payselection.payments.sdk.databinding.DialogSdkThreeDsBinding

class ThreeDsDialogFragment : androidx.fragment.app.DialogFragment() {
    interface ThreeDSDialogListener {
        fun onAuthorizationCompleted()
        fun onAuthorizationFailed()
    }

    companion object {

        private const val URL = "url"
        private const val SUCCESS = "TransactionStatus:success"
        private const val FAIL = "TransactionStatus:fail"
        fun newInstance(url: String) = ThreeDsDialogFragment().apply {
            arguments = Bundle().also {
                it.putString(URL, url)
            }
        }
    }

    private var _binding: DialogSdkThreeDsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogSdkThreeDsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listener = null
    }

    private var listener: ThreeDSDialogListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        binding.webView.webChromeClient = ThreeDsWebViewClient()
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true

        requireArguments().getString(URL)?.let {
            binding.webView.loadUrl(it)
        }

        binding.icClose.setOnClickListener {
            listener?.onAuthorizationFailed()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private inner class ThreeDsWebViewClient : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            consoleMessage?.let {
                if (it.message().contains(SUCCESS)) {
                    listener?.onAuthorizationCompleted()
                    dismiss()
                }
                if (it.message().contains(FAIL)) {
                    listener?.onAuthorizationFailed()
                    dismiss()
                }
            }
            return super.onConsoleMessage(consoleMessage)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = parentFragmentManager.fragments.firstOrNull() as? ThreeDSDialogListener
        if (listener == null) {
            listener = context as? ThreeDSDialogListener
        }
    }
}
