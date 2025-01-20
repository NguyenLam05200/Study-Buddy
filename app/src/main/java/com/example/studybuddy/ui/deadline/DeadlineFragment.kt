package com.example.studybuddy.ui.deadline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.DeadlineViewModelFactory
import com.example.studybuddy.R
import com.example.studybuddy.data.repository.DeadlineRepository
import com.example.studybuddy.data.local.DatabaseProvider

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DeadlineFragment : Fragment() {

    // Initialize Realm and ViewModel
    private lateinit var viewModel: DeadlineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Khởi tạo repository và factory
        val repository = DeadlineRepository(DatabaseProvider.getDatabase())
        val factory = DeadlineViewModelFactory(repository)

        // Sử dụng factory để tạo ViewModel
        viewModel = ViewModelProvider(this, factory)[DeadlineViewModel::class.java]


        val rootView = inflater.inflate(R.layout.fragment_deadline, container, false)

        // Set up RecyclerView
        setupRecyclerView(rootView)

        // Add button for adding new deadlines
        setupAddButton(rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize default deadlines if needed
        viewModel.initializeDefaultDeadlines()
    }

    private fun setupRecyclerView(rootView: View) {
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewDeadlines)
        val emptyTextView: TextView = rootView.findViewById(R.id.textViewEmptyDeadlines)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = DeadlineAdapter(
            onAction = { deadline, action ->
                when (action) {
                    DeadlineAdapter.Action.EDIT -> navigateToEditDeadline(deadline.id)
                    DeadlineAdapter.Action.DELETE -> viewModel.deleteDeadline(deadline)
                }
            },
            onItemClick = { deadline ->
                navigateToDeadlineDetails(deadline.id)
            }
        )

        recyclerView.adapter = adapter

        // Collect data from Flow
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deadlines.collectLatest { deadlines ->
                adapter.submitList(deadlines)
                emptyTextView.visibility = if (deadlines.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }


    private fun setupAddButton(rootView: View) {
        val addButton: Button = rootView.findViewById(R.id.buttonNewDeadline)
        addButton.setOnClickListener {
            navigateToAddDeadline()
        }
    }

    private fun navigateToAddDeadline() {
        val action = DeadlineFragmentDirections.actionDeadlineFragToManageDeadlineFrag(
            deadlineId = -1, // Giá trị mặc định cho chế độ ADD
            mode = "ADD"
        )
        findNavController().navigate(action)
    }

    private fun navigateToEditDeadline(deadlineId: Int) {
        // Navigate to EditDeadlineFragment
        val action = DeadlineFragmentDirections.actionDeadlineFragToManageDeadlineFrag(
            deadlineId = deadlineId,
            mode = "EDIT"
        )
        findNavController().navigate(action)
    }

    private fun navigateToDeadlineDetails(deadlineId: Int) {
        val action = DeadlineFragmentDirections.actionDeadlineFragToManageDeadlineFrag(
            deadlineId = deadlineId,
            mode = "DETAILS"
        )
        findNavController().navigate(action)
    }
}
