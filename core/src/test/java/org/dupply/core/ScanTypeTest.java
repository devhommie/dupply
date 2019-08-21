package org.dupply.core;

import org.junit.Assert;
import org.junit.Test;

public class ScanTypeTest {

    @Test
    public void testGetEnum() {
        String value1 = "eesfsdf";
        String value2 = "NAME_AND_SIZE";
        String value3 = "NAME_AND_SIZE_AND_HASH";
        String value4 = "SIZE_AND_HASH";
        String value5 = "";
        String value6 = null;

        Assert.assertNull(ScanType.fromString(value1));
        Assert.assertEquals(ScanType.NAME_AND_SIZE, ScanType.fromString(value2));
        Assert.assertEquals(ScanType.NAME_AND_SIZE_AND_HASH, ScanType.fromString(value3));
        Assert.assertEquals(ScanType.SIZE_AND_HASH, ScanType.fromString(value4));
        Assert.assertNull(ScanType.fromString(value5));
        Assert.assertNull(ScanType.fromString(value6));

    }

}
