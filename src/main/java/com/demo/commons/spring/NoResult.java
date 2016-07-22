package com.demo.commons.spring;

/**
 * 不返回结果
 *
 */
public class NoResult {
    private NoResult() {

    }

    private static NoResult noResult = new NoResult();

    public static NoResult getInstance() {
        return noResult;
    }
}
