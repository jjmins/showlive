package com.showlive.assignment.ui.search

import com.showlive.assignment.data.entity.Character
import com.showlive.assignment.ui.character.CharacterViewHolder

class SearchEvent(private val viewModel: SearchViewModel) : CharacterViewHolder.Event {
    override fun onClickItem(item: Character, position: Int) {
        if (item.isFavorite) viewModel.deleteFavoriteItem(item, position) else viewModel.saveFavoriteItem(item, position)
    }
}