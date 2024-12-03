/**
 * Author: Mugi
 * https://github.com/samuelmugi
 * User:mugimacharia
 * Date:23/08/2024
 * Time:15:45
 */

package com.mugi.kcb.interview.exception;

public class ConstraintViolationException extends RuntimeException {
    public ConstraintViolationException(String message) {
        super(message);
    }
}