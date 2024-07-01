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

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "move_histories")
public class MoveHistory{

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "match_id", referencedColumnName = "id", updatable = false)
   @ToString.Exclude
   private Match match;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "training_id", referencedColumnName = "id", updatable = false)
   @ToString.Exclude
   private Training training;

   @Column(name = "turn")
   private long turn;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "piece_id", referencedColumnName = "id")
   @ToString.Exclude
   private Piece piece;

   @Column(name = "to_col")
   private int toCol;

   @Column(name = "to_row")
   private int toRow;

}
