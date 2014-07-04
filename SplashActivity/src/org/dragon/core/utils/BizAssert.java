/**
 * 
 */
package org.dragon.core.utils;

/**
 * 框架中核心包Assert,用于输入参数等需要断言的工具类.
 * 
 * @author dengjie
 * @version 1.0, 2012-09-10
 * @since 1.0
 * 
 */
public final class BizAssert {

    /**
     * Fails a test with the given message.并且抛出IllegalArgumentException异常.
     * 
     * @param message
     *            异常中附带的信息
     */
    public static void fail(String message) {
        throw new IllegalArgumentException(message);
    }

    /**
     * 格式化异常信息，将期望值和实际值都赋值出来打印到异常信息中
     * 
     * @param message
     *            用户定义的异常附带信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     * @return 最后打印出来的异常信息
     */
    public static String equalsformat(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null)
            formatted = message + " ";
        return formatted + "expected:<" + expected + "> but was:<" + actual + ">";
    }

    /**
     * 如果不相等报错的处理方法
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void failNotEquals(String message, Object expected, Object actual) {
        fail(equalsformat(message, expected, actual));
    }

    /**
     * 断言两个Object是否相等，这个方法是帮助各种Java类型进行判断的核心方法。例如：String,Long,Integer等。
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(String message, Object expected, Object actual) {
        if (expected == null && actual == null)
            return;
        if (expected != null && expected.equals(actual))
            return;
        failNotEquals(message, expected, actual);
    }

    /**
     * 断言两个Object是否相等，这个方法是帮助各种Java类型进行判断的核心方法。例如：String,Long,Integer等。
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(Object expected, Object actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * 断言期望字符串和实际字符串是否相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(String message, String expected, String actual) {
        if (expected == null && actual == null)
            return;
        if (expected != null && expected.equals(actual))
            return;
        failNotEquals(message, expected, actual);
    }

    /**
     * 断言期望字符串和实际字符串是否相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(String expected, String actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * 断言两个long类型的值是否相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(String message, long expected, long actual) {
        assertEquals(message, new Long(expected), new Long(actual));
    }

    /**
     * 断言两个long类型的值是否相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(long expected, long actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * 断言两个boolean类型的值是否相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(String message, boolean expected, boolean actual) {
        assertEquals(message, Boolean.valueOf(expected), Boolean.valueOf(actual));
    }

    /**
     * 断言两个boolean类型的值是否相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(boolean expected, boolean actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * 断言两个byte类型的值是否相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(String message, byte expected, byte actual) {
        assertEquals(message, new Byte(expected), new Byte(actual));
    }

    /**
     * 断言两个byte类型的值是否相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(byte expected, byte actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * 断言两个char类型的值是否相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(String message, char expected, char actual) {
        assertEquals(message, new Character(expected), new Character(actual));
    }

    /**
     * 断言两个char类型的值是否相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(char expected, char actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * 断言两个short类型的值是否相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(String message, short expected, short actual) {
        assertEquals(message, new Short(expected), new Short(actual));
    }

    /**
     * 断言两个short类型的值是否相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(short expected, short actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * 断言两个int类型的值是否相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(String message, int expected, int actual) {
        assertEquals(message, new Integer(expected), new Integer(actual));
    }

    /**
     * 断言两个int类型的值是否相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertEquals(int expected, int actual) {
        assertEquals(null, expected, actual);
    }

    // ////////////////////////////////////////////
    /**
     * 格式化异常信息，将期望值和实际值都赋值出来打印到异常信息中
     * 
     * @param message
     *            用户定义的异常附带信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     * @return 最后打印出来的异常信息
     */
    public static String notEqualsformat(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null)
            formatted = message + " ";
        return formatted + "expected not equal to:<" + expected + "> but was:<" + actual + ">";
    }

    /**
     * 如果相等报错的处理方法
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void failEquals(String message, Object expected, Object actual) {
        fail(notEqualsformat(message, expected, actual));
    }

    /**
     * 断言两个Object是否不相等，这个方法是帮助各种Java类型进行判断的核心方法。例如：String,Long,Integer等。
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(String message, Object expected, Object actual) {
        if (expected == null && actual == null)
            failEquals(message + " .expected&actual is null", expected, actual);
        if (expected != null && !expected.equals(actual))
            return;
        failEquals(message, expected, actual);
    }

    /**
     * 断言两个Object是否不相等，这个方法是帮助各种Java类型进行判断的核心方法。例如：String,Long,Integer等。
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(Object expected, Object actual) {
        assertNotEquals(null, expected, actual);
    }

    /**
     * 断言期望字符串和实际字符串是否不相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(String message, String expected, String actual) {
        if (expected == null && actual == null)
            failEquals(message + " .expected&actual is null", expected, actual);
        if (expected != null && !expected.equals(actual))
            return;
        failEquals(message, expected, actual);
    }

    /**
     * 断言期望字符串和实际字符串是否不相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(String expected, String actual) {
        assertNotEquals(null, expected, actual);
    }

    /**
     * 断言两个long类型的值是否不相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(String message, long expected, long actual) {
        assertNotEquals(message, new Long(expected), new Long(actual));
    }

    /**
     * 断言两个long类型的值是否不相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(long expected, long actual) {
        assertNotEquals(null, expected, actual);
    }

    /**
     * 断言两个boolean类型的值是否不相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(String message, boolean expected, boolean actual) {
        assertNotEquals(message, Boolean.valueOf(expected), Boolean.valueOf(actual));
    }

    /**
     * 断言两个boolean类型的值是否不相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(boolean expected, boolean actual) {
        assertNotEquals(null, expected, actual);
    }

    /**
     * 断言两个byte类型的值是否不相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(String message, byte expected, byte actual) {
        assertNotEquals(message, new Byte(expected), new Byte(actual));
    }

    /**
     * 断言两个byte类型的值是否不相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(byte expected, byte actual) {
        assertNotEquals(null, expected, actual);
    }

    /**
     * 断言两个char类型的值是否不相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(String message, char expected, char actual) {
        assertNotEquals(message, new Character(expected), new Character(actual));
    }

    /**
     * 断言两个char类型的值是否不相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(char expected, char actual) {
        assertNotEquals(null, expected, actual);
    }

    /**
     * 断言两个short类型的值是否不相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(String message, short expected, short actual) {
        assertNotEquals(message, new Short(expected), new Short(actual));
    }

    /**
     * 断言两个short类型的值是否不相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(short expected, short actual) {
        assertNotEquals(null, expected, actual);
    }

    /**
     * 断言两个int类型的值是否不相等
     * 
     * @param message
     *            信息
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(String message, int expected, int actual) {
        assertNotEquals(message, new Integer(expected), new Integer(actual));
    }

    /**
     * 断言两个int类型的值是否不相等
     * 
     * @param expected
     *            期望值
     * @param actual
     *            实际值
     */
    public static void assertNotEquals(int expected, int actual) {
        assertNotEquals(null, expected, actual);
    }
}
