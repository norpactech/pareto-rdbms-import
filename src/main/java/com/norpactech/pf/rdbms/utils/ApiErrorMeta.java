package com.norpactech.pf.rdbms.utils;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import com.norpactech.pf.rdbms.enums.EnumApiCodes;

public class ApiErrorMeta extends Meta {
  
  public ApiErrorMeta(EnumApiCodes apiCode, String errorCode, String message, String hint, String detail) {
    super(apiCode, errorCode, message, hint, detail);
  }
}

