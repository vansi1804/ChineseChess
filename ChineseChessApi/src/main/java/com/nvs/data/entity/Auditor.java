package com.nvs.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditor implements Serializable{

   @CreatedDate
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "created_date", updatable = false)
   private Date createdDate;

   @CreatedBy
   @Column(name = "created_by_user_id", updatable = false)
   private Long createdByUserId;

   @LastModifiedDate
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "last_modified_date")
   private Date lastModifiedDate;

   @LastModifiedBy
   @Column(name = "last_modified_by_user_id")
   private Long lastModifiedByUserId;
}
