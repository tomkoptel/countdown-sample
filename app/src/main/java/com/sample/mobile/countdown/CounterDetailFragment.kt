package com.sample.mobile.countdown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.sample.mobile.countdown.dummy.DummyContent
import kotlinx.android.synthetic.main.counter_detail.*

/**
 * A fragment representing a single Counter detail screen.
 * This fragment is either contained in a [CounterListActivity]
 * in two-pane mode (on tablets) or a [CounterDetailActivity]
 * on handsets.
 */
class CounterDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: DummyContent.DummyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            if (args.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                args.getString(ARG_ITEM_ID)?.let { id ->
                    item = DummyContent.ITEM_MAP[id]
                }
            }
        }
        debug("onCreate for counter=${item?.id} reusing existing")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = view
        return if (rootView == null) {
            debug("onCreateView for counter=${item?.id} creating new layout")
            inflater.inflate(R.layout.counter_detail, container, false)
        } else {
            debug("onCreateView for counter=${item?.id} reusing existing")
            rootView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item?.let {
            counterDetail.text = it.content
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        item?.let {
            debug("onActivityCreated for counter=${it.id}")

            val viewModel: CounterViewModel = getViewModel(it)
            val viewLifecycleOwner: LifecycleOwner = this
            viewModel.state.observe(viewLifecycleOwner, Observer<CounterViewModel.State> { state ->
                startButton.setText(state.startButtonText)
                counterText.text = state.counter
            })

            startButton.setOnClickListener {
                viewModel.triggerCounter()
            }
            resetButton.setOnClickListener {
                viewModel.reset()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        debug("onCreate for counter=${item?.id} reusing existing")
    }

    override fun onResume() {
        super.onResume()
        debug("onCreate for counter=${item?.id} reusing existing")
    }

    override fun onPause() {
        super.onPause()
        debug("onPause for counter=${item?.id} reusing existing")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        debug("onSaveInstanceState for counter=${item?.id} reusing existing")
    }

    override fun onStop() {
        super.onStop()
        debug("onStop for counter=${item?.id} reusing existing")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        debug("onDestroyView for counter=${item?.id} reusing existing")
    }

    override fun onDestroy() {
        super.onDestroy()
        debug("onDestroy for counter=${item?.id} reusing existing")
    }

    private fun getViewModel(it: DummyContent.DummyItem): CounterViewModel {
        val viewModelFactory = ViewModelProvider.NewInstanceFactory()

        val viewModel: CounterViewModel = ViewModelProviders
            .of(requireActivity(), viewModelFactory)
            .get(it.id, CounterViewModel::class.java)

        return viewModel
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
