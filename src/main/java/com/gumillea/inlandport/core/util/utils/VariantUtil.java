package com.gumillea.inlandport.core.util.utils;

public class VariantUtil {
    public static class Prefixes {
        public static final String MOSSY = "mossy_";
        public static final String CRACKED = "cracked_";
        public static final String CHISELED = "chiseled_";
        public static final String POLISHED = "polished_";
    }

    public static class Suffixes {
        public static final String SLAB = "_slab";
        public static final String STAIRS = "_stairs";
        public static final String WALL = "_wall";

        public static final String BRICKS = "_bricks";
        public static final String TILES = "_tiles";

        public static final String PILLAR = "_pillar";
    }

    public static class Types {
        public static final String[] WOODEN = {"wooden"};
        public static final String[] STONE = {"stone"};
        public static final String[] MINERAL = {"mineral"};

        public static final String[] WOODEN_OR_MINERAL = {"mineral", "wooden"};
        public static final String[] STONE_OR_MINERAL = {"mineral", "stone"};
    }

}
