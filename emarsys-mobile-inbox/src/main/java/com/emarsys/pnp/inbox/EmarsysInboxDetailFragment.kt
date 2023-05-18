package com.emarsys.pnp.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.emarsys.plugnplay.inbox.databinding.EmsInboxFragmentDetailBinding

open class EmarsysInboxDetailFragment : Fragment() {
    val viewModel: EmarsysInboxViewModel by activityViewModels()
    lateinit var binding: EmsInboxFragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EmsInboxFragmentDetailBinding.inflate(inflater, container, false)

        val view = binding.root

        binding.toolbar.setNavigationOnClickListener { view.findNavController().popBackStack() }

        binding.pager.setBackgroundColor(EmarsysInboxConfig.bodyBackgroundColor)
        binding.pager.adapter = EmarsysInboxDetailAdapter(requireContext(), viewModel)
        viewModel.selectedItem.value?.let { binding.pager.setCurrentItem(it, false) }

        return view
    }
}
