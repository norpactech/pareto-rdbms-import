package com.norpactech.pf.mysql.exception;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 * 
 * This software is proprietary and may not be used, copied, modified, or distributed 
 * without permission. It is provided "as is" without warranties of any kind. 
 * 
 * For details, see the LICENSE file in this project root.
 */
public class EmptyRequestFileException extends Exception {

  private static final long serialVersionUID = -9020136419406112894L;

  public EmptyRequestFileException() {
    super("Empty or null Request File");
  }
}
