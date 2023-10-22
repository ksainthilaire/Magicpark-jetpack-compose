package com.magicpark.features.shop

import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem


sealed interface ShopState  {
    
    object Loading : ShopState
    
    class ShopList(
        val items: List<ShopItem>,
        val categories: List<ShopCategory>
    ) : ShopState

}
