package com.ssafy.enjoytrip.exception;

public class SQLException extends RuntimeException {
  private static final String message = "잘못된 정보를 입력하였습니다.";

  public SQLException() {
    super(message);
  }

  public SQLException(String message) {
        super(message);
    }
}
