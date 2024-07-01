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
@Table(name = "matches")
public class Match extends Auditor{

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private long id;

   @Column(name = "time", updatable = false)
   private Integer time;

   @Column(name = "moving_time", updatable = false)
   private Integer movingTime;

   @Column(name = "cumulative_time", updatable = false)
   private Integer cumulativeTime;

   @Column(name = "bet", updatable = false)
   private Integer eloBet;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "player1_id", referencedColumnName = "id", updatable = false)
   @ToString.Exclude
   private Player player1;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "player2_id", referencedColumnName = "id", updatable = false)
   @ToString.Exclude
   private Player player2;

   @Column(name = "result")
   private Integer result;

}
