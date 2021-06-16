package mrc.heli.dot.utils;

import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.element.Element;
import ohos.agp.utils.Color;

public class AttrUtil {
    private AttrSet attrSet;

    public AttrUtil(AttrSet attrSet) {
        this.attrSet = attrSet;
        AttrHelper attrHelper = new AttrHelper();
    }

//    private <V> V getValue(String key, V defValue) {
//        if (attrSet.getAttr(key).isPresent()) {
//            Attr attr = attrSet.getAttr(key).get();
////            Type type = new TypeToken<V>() {}.getType();
//            /*if (type instanceof String){
//
//            }*/
////            if (type.getTypeName().equals(String.class.getSimpleName()))
//            /*switch (type.getTypeName()){
//                case String.class.getSimpleName():break;
//            }*/
////            Class<?> vClass = new TypeToken<V>() {}.getRawType();
//            /*if (defValue instanceof String) {
//                return (V) attr.getStringValue();
//            } else if (defValue instanceof Boolean) {
//                return (V) ((Boolean) attr.getBoolValue());
//            } else if (defValue instanceof Boolean) {
//                return (V) ((Boolean) attr.getColorValue());
//            }*/
//        }
//
//        return defValue;
//    }

    /*private Attr getAttr(String key) {
        //不知道不存在的时候是不是会自动返回null
        return attrSet.getAttr(key).isPresent() ? attrSet.getAttr(key).get() : null;
    }*/

    public String getStringValue(String key, String defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getStringValue();
        } else {
            return defValue;
        }
    }

    /**
     * getDimension
     *
     * @param key
     * @param defValue
     * @return
     */
    public float getFloatValue(String key, float defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getFloatValue();
        } else {
            return defValue;
        }
    }

    public float getDimension(String key, float defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getDimensionValue();
        } else {
            return defValue;
        }
    }

    /**
     * getInt、getDimensionPixelSize
     *
     * @param key
     * @param defValue
     * @return
     */
    public int getIntValue(String key, int defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getIntegerValue();
        } else {
            return defValue;
        }
    }

    /*
    重命名一下，符合Android的习惯
     */
    public int getInt(String key, int defValue) {
        return getIntValue(key, defValue);
    }

    public int getDimensionPixelSize(String key, int defValue) {
//        return getIntValue(key, defValue);
        return (int) getDimension(key, defValue);
    }

    /**
     * getColor
     *
     * @param key
     * @param defValue
     * @return
     */
    public int getColorValue(String key, int defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getColorValue().getValue();
        } else {
            return defValue;
        }
    }

    public int getColor(String key, int defValue) {
        return getColorValue(key, defValue);
    }

    /**
     * getBoolean
     *
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBoolValue(String key, boolean defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getBoolValue();
        } else {
            return defValue;
        }
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getBoolValue(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return getFloatValue(key, defValue);
    }

    public Color getColorValue(String key, Color defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getColorValue();
        } else {
            return defValue;
        }
    }

    public boolean getBooleanValue(String key, boolean defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getBoolValue();
        } else {
            return defValue;
        }
    }

    public Element getElementValue(String key, Element defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getElement();
        } else {
            return defValue;
        }
    }

    public boolean hasValue(String key) {
        return attrSet.getAttr(key).isPresent();
    }
}
