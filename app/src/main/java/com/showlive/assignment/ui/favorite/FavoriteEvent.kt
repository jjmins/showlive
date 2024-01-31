package com.showlive.assignment.ui.favorite

import com.showlive.assignment.data.entity.Character
import com.showlive.assignment.ui.character.CharacterViewHolder

class FavoriteEvent(private val viewModel: FavoriteViewModel) : CharacterViewHolder.Event {
    override fun onClickItem(item: Character, position: Int) {
        viewModel.deleteFavoriteItem(item)
    }
}