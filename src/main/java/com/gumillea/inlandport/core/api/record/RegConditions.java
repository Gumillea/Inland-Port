package com.gumillea.inlandport.core.api.record;

import java.util.*;

public record RegConditions(Object... objects) {

    public static RegConditions merge(RegConditions... conds) {
        Set<Object> set = new HashSet<>();

        for (RegConditions cond : conds) {
            set.addAll(Arrays.asList(cond.objects));
        }

        return new RegConditions(set.toArray());
    }

}
