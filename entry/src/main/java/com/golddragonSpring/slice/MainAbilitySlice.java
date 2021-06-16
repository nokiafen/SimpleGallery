package com.golddragonSpring.slice;

import com.golddragonSpring.ResourceTable;

import mrc.heli.dot.gallery.PagerGallery;
import mrc.heli.dot.indicator.TIndicator;
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
        pagerGallery.setImages(new int[]{ResourceTable.Media_1temp, ResourceTable.Media_2temp, ResourceTable.Media_3temp, ResourceTable.Media_4temp});
        pagerGallery.setSelectIndex(3);

        TIndicator TIndicator = (TIndicator) findComponentById(ResourceTable.Id_tIndicator);
        TIndicator.attachToViewPager(pagerGallery);
    }
}
