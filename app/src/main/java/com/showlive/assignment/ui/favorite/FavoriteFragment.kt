package com.showlive.assignment.ui.favorite

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.showlive.assignment.data.entity.Character
import com.showlive.assignment.data.entity.CharacterItem
import com.showlive.assignment.data.entity.Loading
import com.showlive.assignment.databinding.FragmentFavoriteBinding
import com.showlive.assignment.ui.adapter.AutoBindHolderFactory
import com.showlive.assignment.ui.adapter.buildAdapter
import com.showlive.assignment.ui.base.BaseFragment
import com.showlive.assignment.ui.search.SearchEvent
import com.showlive.assignment.ui.character.CharacterViewHolder
import com.showlive.assignment.ui.character.LoadingViewHolder
import com.showlive.assignment.ui.util.livedata.event
import com.showlive.assignment.ui.util.livedata.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>(FragmentFavoriteBinding::inflate) {
    override val viewModel: FavoriteViewModel by viewModels()

    private val event by lazy {
        FavoriteEvent(viewModel)
    }

    private val adapter by lazy {
        AutoBindHolderFactory<Character>()
            .add(
                Character::class,
                CharacterViewHolder.DIFF,
                event,
                CharacterViewHolder.CREATOR
            )
            .buildAdapter()
    }

    companion object {
        fun newInstance() : Fragment {
            return FavoriteFragment()
        }
    }

    override fun setupUi() = with(binding) {
        rvCharacter.layoutManager = GridLayoutManager(context, 2)
        rvCharacter.adapter = adapter
    }

    override fun observeData() = with(viewModel) {
        observe(items) {
            binding.tvEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(it.toMutableList())
        }
        event(isLoading) {
            Log.e("call", "observeEvent")
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }
        event(showToast) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun loadData() {
        viewModel.getAllFavoriteItems()
    }
}