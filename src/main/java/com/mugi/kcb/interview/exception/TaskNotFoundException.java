/**
 * Author: Mugi
 * https://github.com/samuelmugi
 * User:mugimacharia
 * Date:23/08/2024
 * Time:15:45
 */

package com.mugi.kcb.interview.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}