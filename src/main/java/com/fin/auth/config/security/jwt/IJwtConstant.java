package com.fin.auth.config.security.jwt;

import java.util.Date;

public interface IJwtConstant {

    String SECRET_KEY="wpembytrwcvnryxksdbqwjebruyGHyudqgwveytrtrCSnwifoesarjbwe";
    String JWT_HEADER="Authorization";
    Date expiresAt = new Date(new Date().getTime()+86400000);


}
