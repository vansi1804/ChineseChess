package com.nvs.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends Auditor{

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private long id;

   @Column(name = "phone_number")
   private String phoneNumber;

   @Column(name = "password")
   private String password;

   @Column(name = "name")
   private String name;

   @Column(name = "avatar")
   private String avatar;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "vip_id", referencedColumnName = "id")
   @ToString.Exclude
   private Vip vip;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "role_id", referencedColumnName = "id")
   @Fetch(FetchMode.JOIN)
   @ToString.Exclude
   private Role role;

   @Column(name = "status")
   private String status;

}
