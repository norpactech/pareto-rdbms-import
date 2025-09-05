package com.norpactech.pf.config;

import java.util.UUID;

public class Globals {

  private static UUID idTenant;

  public static UUID getIdTenant() {
    return idTenant;
  }

  public static void setIdTenant(UUID idTenant) {
    Globals.idTenant = idTenant;
  }
}