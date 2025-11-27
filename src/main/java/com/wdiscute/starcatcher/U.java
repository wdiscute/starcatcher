package com.wdiscute.starcatcher;

import java.util.List;

public class U
{
    @SafeVarargs
    public static <T> boolean containsAny(List<T> list, T... contains) {
        for (T s : contains)
            if(list.contains(s)) return true;

        return false;
    }

    @SafeVarargs
    public static <T> boolean containsAll(List<T> list, T... contains) {
        for (T s : contains)
            if(!list.contains(s)) return false;

        return true;
    }

    @SafeVarargs
    public static <T> boolean containsNone(List<T> list, T... contains)
    {
        return !containsAny(list, contains);
    }
}
