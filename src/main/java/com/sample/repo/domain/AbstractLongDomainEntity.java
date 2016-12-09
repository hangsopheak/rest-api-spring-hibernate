package com.sample.repo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author sophea
 */
@MappedSuperclass
public abstract class AbstractLongDomainEntity  implements Serializable {

    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_VERSION = "version";
    public static final String COLUMN_NAME_STATE = "state";
    private static final long serialVersionUID = 1982069997503175834L;
    /**unique id*/
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /**version number changes */
    @Basic
    @JsonIgnore
    private Long version;
    /**state */
//    @Basic
//    private Long state;
//    /**created by*/
//    @Basic
//    @JsonIgnore
//    private String createdBy;
    /**created date*/
    @Basic
    @JsonIgnore
    private Date createdDate;
    /**updated by*/
//    @Basic
//    @JsonIgnore
//    private String updatedBy;
    /**updated date*/
    @Basic
    @JsonIgnore
    private Date updatedDate;

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

    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

   

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

   
}
