package org.nds.dbdroid.unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.nds.dbdroid.log.Logger;
import org.nds.dbdroid.query.QueryValueResolver;

public class Tests {

    private static final Logger log = Logger.getLogger(Tests.class);

    private static QueryValueResolver queryValueResolver;

    @BeforeClass
    public static void setUp() {
        queryValueResolver = new QueryValueResolver() {

            @Override
            protected String toShortString(Short value) {
                return "Short value";
            }

            @Override
            protected String toNumberString(Number value) {
                return "Number value";
            }

            @Override
            protected String toNullString(Object value) {
                return "Null value";
            }

            @Override
            protected String toLongString(Long value) {
                return "Long value";
            }

            @Override
            protected String toIntegerString(Integer value) {
                return "Integer value";
            }

            @Override
            protected String toFloatString(Float value) {
                return "Float value";
            }

            @Override
            protected String toDoubleString(Double value) {
                return "Double value";
            }

            @Override
            protected String toCollectionString(Collection<?> values) {
                return "Collection value";
            }

            @Override
            protected String toCharacterString(Character value) {
                return "Character value";
            }

            @Override
            protected String toByteString(Byte value) {
                return "Byte value";
            }

            @Override
            protected String toBooleanString(Boolean value) {
                return "Boolean value";
            }

            @Override
            protected String toMapString(Map<?, ?> value) {
                return "Map value";
            }

            @Override
            protected String toObjectString(Object value) {
                return "Object value";
            }
        };
    }

    @Test
    public void test() {
        Object[] values = new Object[] { true, (short) 1, new BigDecimal(1), null, (long) 1, (int) 1, 1f, (double) 1, new ArrayList<String>(), 'a',
                (byte) 1, new byte[] { 1 }, new Boolean(true), new Short((short) 1), new AtomicInteger(1), null, new Long(1), new Integer(1),
                new Float(1), new Double(1), new HashMap<Object, Object>(), new Character('a'), new Byte((byte) 1),
                new Byte[] { new Byte((byte) 1) }, new Tests() };

        for (Object obj : values) {
            log.debug(queryValueResolver.toString(obj));
        }
    }
}
