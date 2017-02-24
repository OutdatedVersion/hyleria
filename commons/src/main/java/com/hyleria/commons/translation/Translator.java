package com.hyleria.commons.translation;

/**
 * Convert the two items to and
 * from each other.
 *
 * @author Ben (OutdatedVersion)
 * @since Feb/23/2017 (12:00 AM)
 */
public abstract class Translator<A, B>
{

    /** instance of A */
    A a;

    /** instance of B */
    B b;

    /**
     * Turn {@code B} into {@code A}
     *
     * @param b an instance of B
     * @return an instance of A from B
     */
    public abstract A read(B b);

    /**
     * Turn {@code A} into {@code B}
     *
     * @param a an instance of A
     * @return an instance of B from A
     */
    public abstract B write(A a);

}
