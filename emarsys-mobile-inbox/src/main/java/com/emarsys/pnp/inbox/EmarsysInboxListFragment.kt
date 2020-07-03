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
import com.emarsys.plugnplay.inbox.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.ems_inbox_list_fragment.*

class EmarsysInboxListFragment : Fragment() {
    private val viewModel: EmarsysInboxViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ems_inbox_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationRecycleView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        notificationRecycleView.adapter = EmarsysInboxRecyclerViewAdapter(viewModel)

        swipeRefreshLayout.setOnRefreshListener {
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
        }).attachToRecyclerView(notificationRecycleView)

        viewModel.messages.observe(viewLifecycleOwner) {
            (notificationRecycleView.adapter as EmarsysInboxRecyclerViewAdapter).submitList(it)
        }
        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            swipeRefreshLayout.isRefreshing = it
        }
        viewModel.error.observe(viewLifecycleOwner) {
            viewModel.error.value?.let {
                Snackbar.make(swipeRefreshLayout, it, Snackbar.LENGTH_LONG).show()
                viewModel.error.value = null
            }
        }
    }
}
