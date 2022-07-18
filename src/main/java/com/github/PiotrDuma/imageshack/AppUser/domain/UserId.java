package com.github.PiotrDuma.imageshack.AppUser.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
class UserId implements Serializable {
  private final Long id;

  public UserId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof UserId)){
      return false;
    }
    boolean equal = ((UserId) obj).id==this.id?true:false;
    return equal;
  }


  @Override
  public String toString() {
    return "UserId{" +
        "id=" + id +
        '}';
  }
}
