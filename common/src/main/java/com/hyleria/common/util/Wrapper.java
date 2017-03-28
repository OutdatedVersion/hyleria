package com.hyleria.common.util;

/**
 * Created for hyleria. All rights reserved/retained
 *
 * Utility for getting around final and lambas
 * @author Jp78 (jp78.me)
 * @since Tuesday, March 2017
 */
public class Wrapper<T>
{
    private T t; //The thing to wrap

    public T get()
    {
        return t;
    }

    public void set(T t)
    {
        this.t = t;
    }
}
