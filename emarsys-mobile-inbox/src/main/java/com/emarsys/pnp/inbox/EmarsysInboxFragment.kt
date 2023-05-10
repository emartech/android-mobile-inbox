package com.emarsys.pnp.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.emarsys.plugnplay.inbox.R

class EmarsysInboxFragment : Fragment() {
    private val viewModel: EmarsysInboxViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ems_inbox_fragment_holder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction().replace(R.id.container, EmarsysInboxListFragment())
            .commitNow()
        childFragmentManager.addOnBackStackChangedListener {
            if (childFragmentManager.backStackEntryCount == 0)
                viewModel.selectedItem.value = null
        }

        viewModel.selectedItem.observe(viewLifecycleOwner) {
            if (childFragmentManager.backStackEntryCount == 0 && it != null) {
                childFragmentManager.beginTransaction()
                    .replace(R.id.container, EmarsysInboxDetailFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(EmarsysInboxDetailFragment::class.simpleName)
                    .commit()
            }
        }
    }
}
