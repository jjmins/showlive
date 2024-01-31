package com.showlive.assignment.ui.search

import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showlive.assignment.data.entity.Character
import com.showlive.assignment.data.entity.CharacterItem
import com.showlive.assignment.data.entity.Loading
import com.showlive.assignment.databinding.FragmentSearchBinding
import com.showlive.assignment.ui.adapter.AutoBindHolderFactory
import com.showlive.assignment.ui.adapter.buildAdapter
import com.showlive.assignment.ui.base.BaseFragment
import com.showlive.assignment.ui.character.CharacterViewHolder
import com.showlive.assignment.ui.character.LoadingViewHolder
import com.showlive.assignment.ui.util.livedata.event
import com.showlive.assignment.ui.util.livedata.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModels()

    private val event by lazy {
        SearchEvent(viewModel)
    }

    private val adapter by lazy {
        AutoBindHolderFactory<CharacterItem>()
            .add(
                Character::class,
                CharacterViewHolder.DIFF,
                event,
                CharacterViewHolder.CREATOR
            )
            .add(
                Loading::class,
                LoadingViewHolder.DIFF,
                event,
                LoadingViewHolder.CREATOR
            )
            .buildAdapter()
    }

    companion object {
        fun newInstance(): Fragment {
            return SearchFragment()
        }
    }

    override fun setupUi(): Unit = with(binding) {
        val layoutManager = GridLayoutManager(context, 2)
        etCharacter.doOnTextChanged { text, _, _, _ ->
            text?.let {
                viewModel.updateSearch(it.toString())
            }
        }

        rvCharacter.layoutManager = layoutManager
        rvCharacter.adapter = adapter

        rvCharacter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val totalItemCount = layoutManager.itemCount - 1
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val firstVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (viewModel.isPageable && totalItemCount <= firstVisibleItem) {
                    viewModel.loadMoreCharacter()
                }
            }
        })
    }

    override fun observeData() = with(viewModel) {
        observe(items) {
            adapter.submitList(it.toMutableList())
        }
        event(isLoading) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }
        event(showToast) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun loadData() {
        viewModel.loadCharacters()
    }
}