package com.sample.mobile.countdown

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: CounterViewModel = getViewModel()
        val viewLifecycleOwner: LifecycleOwner = this
        viewModel.state.observe(viewLifecycleOwner, Observer<CounterViewModel.State> { state ->
            startButton.setText(state.startButtonText)
            textView.text = state.counter
        })

        startButton.setOnClickListener {
            viewModel.triggerCounter()
        }
        resetButton.setOnClickListener {
            viewModel.reset()
        }
    }

    private fun getViewModel(): CounterViewModel {
        val viewModelFactory = ViewModelProvider.NewInstanceFactory()

        val viewModel: CounterViewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(CounterViewModel::class.java)

        return viewModel
    }
}
