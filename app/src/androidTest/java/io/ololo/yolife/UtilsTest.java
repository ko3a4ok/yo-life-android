package io.ololo.yolife;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import io.ololo.yolife.utils.Utils;

/**
 * Created by ko3a4ok on 01.10.15.
 */
public class UtilsTest extends AndroidTestCase {

    @SmallTest
    public void testPositive() {
        assertEquals(Utils.positive(7), 7);
        assertEquals(Utils.positive(0), 0);
        assertEquals(Utils.positive(-100), 0);
    }
}
