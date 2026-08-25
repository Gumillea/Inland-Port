package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.common.block.family.Variant;
import com.gumillea.inlandport.core.util.helpers.IPClientHelper;

public class IPRenderTypes {

    public static void setRenderTypes() {
        setCutout();
    }

    private static void setCutout() {
        IPClientHelper.setCutout(
                Variant.get(IPBlocks.GUMILLEA.get(), Variant.DOOR),
                Variant.get(IPBlocks.GUMILLEA.get(), Variant.TRAPDOOR)
        );
        IPClientHelper.setTranslucent(
                IPBlocks.SLUG_HUSK.get()
        );
    }

}
