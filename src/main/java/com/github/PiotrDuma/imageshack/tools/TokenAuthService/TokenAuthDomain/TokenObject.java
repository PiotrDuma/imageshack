package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

public class TokenObject {
  private static final String NULL_VALUE = "Token value cannot be null.";
  private static final String NULL_TYPE = "Token type cannot be null.";
  private final String tokenValue;
  private final TokenAuthType tokenType;

  public TokenObject(String tokenValue, TokenAuthType tokenType) {
    this.tokenValue = tokenValueValidate(tokenValue);
    this.tokenType = tokenTypeValidator(tokenType);
  }

  public String getTokenValue() {
    return tokenValue;
  }

  public TokenAuthType getTokenType(){
    return tokenType;
  }

  private static String tokenValueValidate(String tokenValue){
    if(tokenValue == null){
      throw new IllegalArgumentException(NULL_VALUE);
    }
    return tokenValue;
  }

  private static TokenAuthType tokenTypeValidator(TokenAuthType tokenType){
    if(tokenType == null){
      throw new IllegalArgumentException(NULL_TYPE);
    }
    return tokenType;
  }

  @Override
  public String toString() {
    return "TokenObject{" +
        "tokenValue='" + tokenValue + '\'' +
        ", tokenType=" + tokenType +
        '}';
  }
}
