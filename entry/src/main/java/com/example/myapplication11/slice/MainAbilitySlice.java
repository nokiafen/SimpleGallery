package com.example.myapplication11.slice;

import com.example.myapplication11.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

import java.util.ArrayList;
import java.util.List;

public class MainAbilitySlice extends AbilitySlice {
    private int scale = 1;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initPageSlider();


    }

    private void initPageSlider() {
//        pageSlider.setProvider(new PageProvider(getdata()));
    }

//    private List<PageProvider.DataItem> getdata() {
//        List sourceList = new ArrayList();
////        sourceList.add(new PageProvider.DataItem(ResourceTable.Media_bg_family));
////        sourceList.add(new PageProvider.DataItem(ResourceTable.Media_ova));
////        sourceList.add(new PageProvider.DataItem(ResourceTable.Media_bg_friend));
////        sourceList.add(new PageProvider.DataItem(ResourceTable.Media_bg_splash));
//        return  sourceList;
//    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
