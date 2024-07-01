package com.nvs.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "pieces")
public class Piece implements Serializable{

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private int id;

   @Column(name = "name", updatable = false)
   private String name;

   @Column(name = "is_red", updatable = false)
   private boolean isRed;

   @Column(name = "image")
   private String image;

   @Column(name = "start_col", updatable = false)
   private int currentCol;

   @Column(name = "start_row", updatable = false)
   private int currentRow;

}
