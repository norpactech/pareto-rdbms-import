package com.norpactech.pf.rdbms.utils;

import com.norpactech.pf.rdbms.enums.EnumApiCodes;

public class ApiErrorMeta extends Meta {
  
  public ApiErrorMeta(EnumApiCodes apiCode, String errorCode, String message, String hint, String detail) {
    super(apiCode, errorCode, message, hint, detail);
  }
}

