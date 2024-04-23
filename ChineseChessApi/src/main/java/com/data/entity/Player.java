package com.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false)
  private long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false)
  private User user;

  @Column(name = "elo")
  private int elo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rank_id", referencedColumnName = "id")
  private Rank rank;

  @Column(name = "win")
  private int win;

  @Column(name = "draw")
  private int draw;

  @Column(name = "lose")
  private int lose;
}
