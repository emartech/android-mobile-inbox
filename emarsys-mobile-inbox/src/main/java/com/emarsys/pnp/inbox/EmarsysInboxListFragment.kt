package com.emarsys.pnp.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emarsys.plugnplay.inbox.databinding.EmsInboxListFragmentBinding
import com.google.android.material.snackbar.Snackbar

class EmarsysInboxListFragment : Fragment() {private val viewModel: EmarsysInboxViewModel by activityViewModels()
    private lateinit var binding: EmsInboxListFragmentBinding
    companion object {
        private lateinit var instance_: EmarsysInboxListFragment
        val instance get() = instance_
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        instance_ = this

        binding = EmsInboxListFragmentBinding.inflate(inflater, container, false)

        val view = binding.root

        binding.recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recycler.adapter = EmarsysInboxListAdapter(viewModel) {
            view.findNavController().navigate(EmarsysInboxListFragmentDirections.actionInboxListFragmentToInboxDetailFragment(it))
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.messages.observe(viewLifecycleOwner) {
            (binding.recycler.adapter as EmarsysInboxListAdapter).submitList(it)
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
        }).attachToRecyclerView(binding.recycler)

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}
