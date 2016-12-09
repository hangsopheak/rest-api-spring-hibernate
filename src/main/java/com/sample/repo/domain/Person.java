package com.sample.repo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* Entity bean with JPA annotations
* Hibernate provides JPA implementation
* @author Sophea
*
*/
@Entity
@Table(name="Person")   
public class Person extends AbstractLongDomainEntity {

   @Id
   @Column(name="id")
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   private Long id;
   
   /**name*/
   private String name;
   /**country*/
   private String country;

   public String getName() {
       return name;
   }

   public void setName(String name) {
       this.name = name;
   }

   public String getCountry() {
       return country;
   }

   public void setCountry(String country) {
       this.country = country;
   }
   
   public String toString(){
       return "id="+id+", name="+name+", country="+country;
   }

/**
 * @return the id
 */
public Long getId() {
    return id;
}

/**
 * @param id the id to set
 */
public void setId(Long id) {
    this.id = id;
}

}
