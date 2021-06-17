simple-gallery
=========================
simple-gallery a library  displaying images as gallery.

<img src="screenshot/Screenshot_1.jpg"  width="626" height="1280"><img src="screenshot/Screenshot_2.jpg"   width="626" height="1280">
<img src="screenshot/Screenshot_3.jpg"  width="626" height="1280"><img src="screenshot/Screenshot_4.jpg"   width="626" height="1280">
![Sample Image](screenshot/SVID_20210615_172455_1.gif)
## Including in your project

##### smart-gallery to your project

    1.Copy the gallery/library folder to the project directory

    2.Modify settings.gradle under the project and add the dependency on this module as follows:

    include ':demo', ':mylibrary'

    3. Introduce the dependency of the module under the project.  you need to modify the build.gradle file under the main module to add the dependency:
    implementation project(path: ':mylibrary')

    Solution 2: to be done ?

More on the  configuration can be found in the Project.

Usage
-----
Define in xml as follow 
Sample code:
```xml
           <mrc.heli.dot.gallery.PagerGallery
            ohos:id="$+id:page_slider"
            ohos:height="match_parent"
            ohos:width="match_parent"
            ohos:background_element="#FFFFFF"
            ohos:layout_alignment="horizontal_center"
            />
```
set images with codes below , also you can attach it with an indicator.

```
             pagerGallery.setImages(new int[]{ResourceTable.Media_1temp, ResourceTable.Media_2temp, ResourceTable.Media_3temp, ResourceTable.Media_4temp});
             pagerGallery.setSelectIndex(3);

            TIndicator TIndicator = (TIndicator) findComponentById(ResourceTable.Id_tIndicator);
            TIndicator.attachToViewPager(pagerGallery);
```

Credits
-------
Author:
* heli Mrc


License
-------
    The MIT License (MIT)

    Copyright (c) 2014 heli Mrc

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

