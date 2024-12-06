import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import pl.przelewy24.p24lib.transfer.TransferActivity
import pl.przelewy24.p24lib.transfer.TransferResult
import pl.przelewy24.p24lib.transfer.request.TrnRequestParams

class Przelewy24Delegate(
    private val activity: ComponentActivity
) : DefaultLifecycleObserver {

    private var activityLauncher: ActivityResultLauncher<TrnRequestParams?>? = null

    var onPrzelewy24Result: (Boolean) -> Unit = {}

    init {
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        val delegateIdent = this::class.java.simpleName

        activityLauncher = activity
            .activityResultRegistry
            .register(delegateIdent, owner, Przelewy24Contract()) {
                it?.run(onPrzelewy24Result::invoke)
            }
    }

    fun startPrzelewy24(
        params: TrnRequestParams,
    ) {
        activityLauncher?.launch(params)
    }

    private inner class Przelewy24Contract : ActivityResultContract<TrnRequestParams?, Boolean?>() {
        override fun createIntent(context: Context, input: TrnRequestParams?): Intent =
            TransferActivity.getIntentForTrnRequest(context, input)

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) {
                val parseResult = TransferActivity.parseResult<TransferResult>(intent)
                parseResult?.isSuccess
            } else {
                null
            }
        }
    }
}