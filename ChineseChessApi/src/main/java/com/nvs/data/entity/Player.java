package com.nvs.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "players")
public class Player{

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private long id;

   @OneToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false)
   @ToString.Exclude
   private User user;

   @Column(name = "elo")
   private int elo;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "rank_id", referencedColumnName = "id")
   @ToString.Exclude
   private Rank rank;

   @Column(name = "win")
   private int win;

   @Column(name = "draw")
   private int draw;

   @Column(name = "lose")
   private int lose;

}
