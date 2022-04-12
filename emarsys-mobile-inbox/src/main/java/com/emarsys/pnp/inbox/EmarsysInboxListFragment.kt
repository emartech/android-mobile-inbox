package com.emarsys.pnp.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emarsys.plugnplay.inbox.databinding.EmsInboxListFragmentBinding
import com.google.android.material.snackbar.Snackbar

class EmarsysInboxListFragment : Fragment() {
    private val viewModel: EmarsysInboxViewModel by activityViewModels()
    private var _binding: EmsInboxListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EmsInboxListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notificationRecycleView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.notificationRecycleView.adapter = EmarsysInboxRecyclerViewAdapter(viewModel)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.removed(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.notificationRecycleView)

        viewModel.messages.observe(viewLifecycleOwner) {
            (binding.notificationRecycleView.adapter as EmarsysInboxRecyclerViewAdapter).submitList(it)
        }
        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it
        }
        viewModel.error.observe(viewLifecycleOwner) {
            viewModel.error.value?.let {
                Snackbar.make(binding.swipeRefreshLayout, it, Snackbar.LENGTH_LONG).show()
                viewModel.error.value = null
            }
        }
    }
}
