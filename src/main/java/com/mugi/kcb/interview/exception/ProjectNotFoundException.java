/**
 * Author: Mugi
 * https://github.com/samuelmugi
 * User:mugimacharia
 * Date:23/08/2024
 * Time:15:45
 */

package com.mugi.kcb.interview.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(String message) {
        super(message);
    }
}