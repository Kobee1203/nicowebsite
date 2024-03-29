/*
 * Copyright (C) 2005-2010 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.service.cmr.repository.datatype;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Support for generic conversion between types.
 * 
 * Additional conversions may be added. Basic inter-operability supported.
 * 
 * Direct conversion and two stage conversions via Number are supported. We do not support conversion by any route at the moment
 * 
 * @author andyh
 * 
 */

public class DefaultTypeConverter {
    /**
     * Default Type Converter
     */
    public static TypeConverter INSTANCE = new TypeConverter();

    /**
     * Initialise default set of Converters
     */
    static {

        //
        // From string
        //

        INSTANCE.addConverter(String.class, Class.class, new TypeConverter.Converter<String, Class>() {
            public Class convert(String source) {
                try {
                    return Class.forName(source);
                } catch (ClassNotFoundException e) {
                    throw new TypeConversionException("Failed to convert string to class: " + source, e);
                }
            }
        });

        INSTANCE.addConverter(String.class, Boolean.class, new TypeConverter.Converter<String, Boolean>() {
            public Boolean convert(String source) {
                return Boolean.valueOf(source);
            }
        });

        INSTANCE.addConverter(String.class, Character.class, new TypeConverter.Converter<String, Character>() {
            public Character convert(String source) {
                if ((source == null) || (source.length() == 0)) {
                    return null;
                }
                return Character.valueOf(source.charAt(0));
            }
        });

        INSTANCE.addConverter(String.class, Number.class, new TypeConverter.Converter<String, Number>() {
            public Number convert(String source) {
                try {
                    return NumberFormat.getNumberInstance().parse(source);
                } catch (ParseException e) {
                    throw new TypeConversionException("Failed to parse number " + source, e);
                }
            }
        });

        INSTANCE.addConverter(String.class, Byte.class, new TypeConverter.Converter<String, Byte>() {
            public Byte convert(String source) {
                return Byte.valueOf(source);
            }
        });

        INSTANCE.addConverter(String.class, Short.class, new TypeConverter.Converter<String, Short>() {
            public Short convert(String source) {
                return Short.valueOf(source);
            }
        });

        INSTANCE.addConverter(String.class, Integer.class, new TypeConverter.Converter<String, Integer>() {
            public Integer convert(String source) {
                return Integer.valueOf(source);
            }
        });

        INSTANCE.addConverter(String.class, Long.class, new TypeConverter.Converter<String, Long>() {
            public Long convert(String source) {
                return Long.valueOf(source);
            }
        });

        INSTANCE.addConverter(String.class, Float.class, new TypeConverter.Converter<String, Float>() {
            public Float convert(String source) {
                return Float.valueOf(source);
            }
        });

        INSTANCE.addConverter(String.class, Double.class, new TypeConverter.Converter<String, Double>() {
            public Double convert(String source) {
                return Double.valueOf(source);
            }
        });

        INSTANCE.addConverter(String.class, BigInteger.class, new TypeConverter.Converter<String, BigInteger>() {
            public BigInteger convert(String source) {
                return new BigInteger(source);
            }
        });

        INSTANCE.addConverter(String.class, BigDecimal.class, new TypeConverter.Converter<String, BigDecimal>() {
            public BigDecimal convert(String source) {
                return new BigDecimal(source);
            }
        });

        /*INSTANCE.addConverter(String.class, Date.class, new TypeConverter.Converter<String, Date>()
        {
            public Date convert(String source)
            {
                try
                {
                    Date date = ISO8601DateFormat.parse(source);
                    return date;
                }
                catch (PlatformRuntimeException e)
                {
                    throw new TypeConversionException("Failed to convert date " + source + " to string", e);
                }
            }
        });*/

        INSTANCE.addConverter(String.class, InputStream.class, new TypeConverter.Converter<String, InputStream>() {
            public InputStream convert(String source) {
                try {
                    return new ByteArrayInputStream(source.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new TypeConversionException("Encoding not supported", e);
                }
            }
        });

        /*INSTANCE.addConverter(String.class, Locale.class, new TypeConverter.Converter<String, Locale>() {
            public Locale convert(String source) {
                return I18NUtil.parseLocale(source);
            }
        });*/

        //
        // From Locale
        //

        INSTANCE.addConverter(Locale.class, String.class, new TypeConverter.Converter<Locale, String>() {
            public String convert(Locale source) {
                String localeStr = source.toString();
                if (localeStr.length() < 6) {
                    localeStr += "_";
                }
                return localeStr;
            }
        });

        //
        // From enum
        //

        INSTANCE.addConverter(Enum.class, String.class, new TypeConverter.Converter<Enum, String>() {
            public String convert(Enum source) {
                return source.toString();
            }
        });

        // From Class

        INSTANCE.addConverter(Class.class, String.class, new TypeConverter.Converter<Class, String>() {
            public String convert(Class source) {
                return source.getName();
            }
        });

        //
        // Number to Subtypes and Date
        //

        INSTANCE.addConverter(Number.class, Boolean.class, new TypeConverter.Converter<Number, Boolean>() {
            public Boolean convert(Number source) {
                return new Boolean(source.longValue() > 0);
            }
        });

        INSTANCE.addConverter(Number.class, Byte.class, new TypeConverter.Converter<Number, Byte>() {
            public Byte convert(Number source) {
                return Byte.valueOf(source.byteValue());
            }
        });

        INSTANCE.addConverter(Number.class, Short.class, new TypeConverter.Converter<Number, Short>() {
            public Short convert(Number source) {
                return Short.valueOf(source.shortValue());
            }
        });

        INSTANCE.addConverter(Number.class, Integer.class, new TypeConverter.Converter<Number, Integer>() {
            public Integer convert(Number source) {
                return Integer.valueOf(source.intValue());
            }
        });

        INSTANCE.addConverter(Number.class, Long.class, new TypeConverter.Converter<Number, Long>() {
            public Long convert(Number source) {
                return Long.valueOf(source.longValue());
            }
        });

        INSTANCE.addConverter(Number.class, Float.class, new TypeConverter.Converter<Number, Float>() {
            public Float convert(Number source) {
                return Float.valueOf(source.floatValue());
            }
        });

        INSTANCE.addConverter(Number.class, Double.class, new TypeConverter.Converter<Number, Double>() {
            public Double convert(Number source) {
                return Double.valueOf(source.doubleValue());
            }
        });

        INSTANCE.addConverter(Number.class, Date.class, new TypeConverter.Converter<Number, Date>() {
            public Date convert(Number source) {
                return new Date(source.longValue());
            }
        });

        INSTANCE.addConverter(Number.class, String.class, new TypeConverter.Converter<Number, String>() {
            public String convert(Number source) {
                return source.toString();
            }
        });

        INSTANCE.addConverter(Number.class, BigInteger.class, new TypeConverter.Converter<Number, BigInteger>() {
            public BigInteger convert(Number source) {
                if (source instanceof BigDecimal) {
                    return ((BigDecimal) source).toBigInteger();
                } else {
                    return BigInteger.valueOf(source.longValue());
                }
            }
        });

        INSTANCE.addConverter(Number.class, BigDecimal.class, new TypeConverter.Converter<Number, BigDecimal>() {
            public BigDecimal convert(Number source) {
                if (source instanceof BigInteger) {
                    return new BigDecimal((BigInteger) source);
                } else {
                    return BigDecimal.valueOf(source.longValue());
                }
            }
        });

        INSTANCE.addDynamicTwoStageConverter(Number.class, String.class, InputStream.class);

        //
        // Date, Timestamp ->
        //

        INSTANCE.addConverter(Timestamp.class, Date.class, new TypeConverter.Converter<Timestamp, Date>() {
            public Date convert(Timestamp source) {
                return new Date(source.getTime());
            }
        });

        INSTANCE.addConverter(Date.class, Number.class, new TypeConverter.Converter<Date, Number>() {
            public Number convert(Date source) {
                return Long.valueOf(source.getTime());
            }
        });

        /*INSTANCE.addConverter(Date.class, String.class, new TypeConverter.Converter<Date, String>() {
            public String convert(Date source) {
                try {
                    return ISO8601DateFormat.format(source);
                } catch (PlatformRuntimeException e) {
                    throw new TypeConversionException("Failed to convert date " + source + " to string", e);
                }
            }
        });*/

        INSTANCE.addConverter(Date.class, Calendar.class, new TypeConverter.Converter<Date, Calendar>() {
            public Calendar convert(Date source) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(source);
                return calendar;
            }
        });

        INSTANCE.addDynamicTwoStageConverter(Date.class, String.class, InputStream.class);

        //
        // Boolean ->
        //

        final Long LONG_FALSE = new Long(0L);
        final Long LONG_TRUE = new Long(1L);
        INSTANCE.addConverter(Boolean.class, Long.class, new TypeConverter.Converter<Boolean, Long>() {
            public Long convert(Boolean source) {
                return source.booleanValue() ? LONG_TRUE : LONG_FALSE;
            }
        });

        INSTANCE.addConverter(Boolean.class, String.class, new TypeConverter.Converter<Boolean, String>() {
            public String convert(Boolean source) {
                return source.toString();
            }
        });

        INSTANCE.addDynamicTwoStageConverter(Boolean.class, String.class, InputStream.class);

        //
        // Character ->
        //

        INSTANCE.addConverter(Character.class, String.class, new TypeConverter.Converter<Character, String>() {
            public String convert(Character source) {
                return source.toString();
            }
        });

        INSTANCE.addDynamicTwoStageConverter(Character.class, String.class, InputStream.class);

        //
        // Byte
        //

        INSTANCE.addConverter(Byte.class, String.class, new TypeConverter.Converter<Byte, String>() {
            public String convert(Byte source) {
                return source.toString();
            }
        });

        INSTANCE.addDynamicTwoStageConverter(Byte.class, String.class, InputStream.class);

        //
        // Short
        //

        INSTANCE.addConverter(Short.class, String.class, new TypeConverter.Converter<Short, String>() {
            public String convert(Short source) {
                return source.toString();
            }
        });

        INSTANCE.addDynamicTwoStageConverter(Short.class, String.class, InputStream.class);

        //
        // Integer
        //

        INSTANCE.addConverter(Integer.class, String.class, new TypeConverter.Converter<Integer, String>() {
            public String convert(Integer source) {
                return source.toString();
            }
        });

        INSTANCE.addDynamicTwoStageConverter(Integer.class, String.class, InputStream.class);

        //
        // Long
        //

        INSTANCE.addConverter(Long.class, String.class, new TypeConverter.Converter<Long, String>() {
            public String convert(Long source) {
                return source.toString();
            }
        });

        INSTANCE.addDynamicTwoStageConverter(Long.class, String.class, InputStream.class);

        //
        // Float
        //

        INSTANCE.addConverter(Float.class, String.class, new TypeConverter.Converter<Float, String>() {
            public String convert(Float source) {
                return source.toString();
            }
        });

        INSTANCE.addDynamicTwoStageConverter(Float.class, String.class, InputStream.class);

        //
        // Double
        //

        INSTANCE.addConverter(Double.class, String.class, new TypeConverter.Converter<Double, String>() {
            public String convert(Double source) {
                return source.toString();
            }
        });

        INSTANCE.addDynamicTwoStageConverter(Double.class, String.class, InputStream.class);

        //
        // BigInteger
        //

        INSTANCE.addConverter(BigInteger.class, String.class, new TypeConverter.Converter<BigInteger, String>() {
            public String convert(BigInteger source) {
                return source.toString();
            }
        });

        INSTANCE.addDynamicTwoStageConverter(BigInteger.class, String.class, InputStream.class);

        //
        // Calendar
        //

        INSTANCE.addConverter(Calendar.class, Date.class, new TypeConverter.Converter<Calendar, Date>() {
            public Date convert(Calendar source) {
                return source.getTime();
            }
        });

        /*INSTANCE.addConverter(Calendar.class, String.class, new TypeConverter.Converter<Calendar, String>() {
            public String convert(Calendar source) {
                try {
                    return ISO8601DateFormat.format(source.getTime());
                } catch (PlatformRuntimeException e) {
                    throw new TypeConversionException("Failed to convert date " + source + " to string", e);
                }
            }
        });*/

        //
        // BigDecimal
        //

        INSTANCE.addConverter(BigDecimal.class, String.class, new TypeConverter.Converter<BigDecimal, String>() {
            public String convert(BigDecimal source) {
                return source.toString();
            }
        });

        INSTANCE.addDynamicTwoStageConverter(BigDecimal.class, String.class, InputStream.class);

        //
        // Input Stream
        //

        INSTANCE.addConverter(InputStream.class, String.class, new TypeConverter.Converter<InputStream, String>() {
            public String convert(InputStream source) {
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = source.read(buffer)) > 0) {
                        out.write(buffer, 0, read);
                    }
                    byte[] data = out.toByteArray();
                    return new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new TypeConversionException("Cannot convert input stream to String.", e);
                } catch (IOException e) {
                    throw new TypeConversionException("Conversion from stream to string failed", e);
                } finally {
                    if (source != null) {
                        try {
                            source.close();
                        } catch (IOException e) {
                        }
                        ;
                    }
                }
            }
        });

        INSTANCE.addDynamicTwoStageConverter(InputStream.class, String.class, Date.class);

        INSTANCE.addDynamicTwoStageConverter(InputStream.class, String.class, Double.class);

        INSTANCE.addDynamicTwoStageConverter(InputStream.class, String.class, Long.class);

        INSTANCE.addDynamicTwoStageConverter(InputStream.class, String.class, Boolean.class);

    }

}
