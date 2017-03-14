package cn.nekocode.kotgo.sample.ui.page2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import cn.nekocode.kotgo.component.ui.KtPresenter
import cn.nekocode.kotgo.sample.data.DO.Meizi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
class Page2Presenter() : KtPresenter<Contract.View>(), Contract.Presenter {
    companion object {
        const val KEY_ARG_MEIZI = "KEY_ARG_MEIZI"
        const val KEY_RLT_MEIZI = "KEY_RLT_MEIZI"

        fun pushForResult(presenter: KtPresenter<*>, requestCode: Int, meizi: Meizi,
                          tag: String = Page2Fragment::class.java.canonicalName) {

            val args = Bundle()
            args.putParcelable(KEY_ARG_MEIZI, meizi)
            presenter.pushForResult(requestCode, tag, Page2Fragment::class.java, args)
        }
    }

    var view: Contract.View? = null

    override fun onViewCreated(view: Contract.View?, savedInstanceState: Bundle?) {
        this.view = view

        val meizi = arguments.getParcelable<Meizi>(KEY_ARG_MEIZI)
        Observable.just(meizi)
                .map {
                    // DO to VO
                    MeiziVO(it.id, it.url, it)
                }
                .bindLifecycle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view?.showMeizi(it)
                }
    }

    override fun onImageClick(meiziVO: MeiziVO) {
        // VO to DO
        val meizi = (meiziVO.data ?: return) as Meizi

        // Set result and finish
        val rlt = Intent()
        rlt.putExtra(KEY_RLT_MEIZI, meizi)
        setResult(Activity.RESULT_OK, rlt)
        popThis()
    }
}