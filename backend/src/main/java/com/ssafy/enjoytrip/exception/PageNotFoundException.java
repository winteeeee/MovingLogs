package com.ssafy.enjoytrip.exception;

public class PageNotFoundException extends RuntimeException {
    public PageNotFoundException(int page, int size) {
        super("Page not found [page: " + page + ", size: " + size + "]");
    }
}
