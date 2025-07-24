package com.norpactech.pf.rdbms.vo;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 * 
 * This software is proprietary and may not be used, copied, modified, or distributed 
 * without permission. It is provided "as is" without warranties of any kind. 
 * 
 * For details, see the LICENSE file in this project root.
 */
public class DownloadResponseVO {

  private int responseCode;
  private byte[] zipFile;
  
  public DownloadResponseVO(int responseCode, byte[] zipFile) {
    this.responseCode = responseCode;
    this.zipFile = zipFile;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  public byte[] getZipFile() {
    return zipFile;
  }

  public void setZipFile(byte[] zipFile) {
    this.zipFile = zipFile;
  }
}
