package com.example.oleksandr.checkpalindrome;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void checkPalindrome()throws Exception{
        MainActivity main = new MainActivity();

        assertTrue(main.isPalindrome("111"));

        assertFalse(main.isPalindrome("123"));
    }
}