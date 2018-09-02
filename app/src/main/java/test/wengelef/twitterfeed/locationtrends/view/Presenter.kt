package test.wengelef.twitterfeed.locationtrends.view

interface Presenter<T : Presenter.View> {
    interface View

    fun start(view: T)
    fun destroy()
}