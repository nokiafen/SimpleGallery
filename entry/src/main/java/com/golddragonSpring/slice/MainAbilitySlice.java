package com.golddragonSpring.slice;

import com.golddragonSpring.ResourceTable;
import com.golddragonSpring.gallery.PagerGallery;
import com.golddragonSpring.indicator.TIndicator;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        initPageSlider();
    }

    private void initPageSlider() {

        PagerGallery pagerGallery = (PagerGallery) findComponentById(ResourceTable.Id_page_slider);
        pagerGallery.setImages(new int[]{ResourceTable.Media_bg_family, ResourceTable.Media_ova, ResourceTable.Media_bg_friend, ResourceTable.Media_bg_splash});
        pagerGallery.setSelectIndex(3);

        TIndicator TIndicator = (TIndicator) findComponentById(ResourceTable.Id_tIndicator);
        TIndicator.attachToViewPager(pagerGallery);
    }
}
