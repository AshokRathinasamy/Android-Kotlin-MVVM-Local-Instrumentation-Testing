package com.mvvm.localtest.addedittask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mvvm.localtest.util.setupRefreshLayout
import com.mvvm.localtest.util.setupSnackbar
import com.google.android.material.snackbar.Snackbar
import com.mvvm.localtest.tasks.ADD_EDIT_RESULT_OK
import com.mvvm.localtest.util.EventObserver

import com.mvvm.localtest.R
import com.mvvm.localtest.databinding.FragmentAddeditTaskBinding

class AddEditFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentAddeditTaskBinding
    private val args: AddEditFragmentArgs by navArgs()
    private val viewModel by viewModels<AddEditViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_addedit_task, container, false)
        viewDataBinding = FragmentAddeditTaskBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()
        setupNavigation()
        this.setupRefreshLayout(viewDataBinding.refreshLayout)
        viewModel.start(args.taskId)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        viewModel.taskUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action = AddEditFragmentDirections
                .actionAddEditTaskFragmentToTasksFragment(ADD_EDIT_RESULT_OK)
            findNavController().navigate(action)
        })
    }
}
