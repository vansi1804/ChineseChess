package com.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "matches")
public class Match extends Auditor {

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
  @JoinColumn(
    name = "player1_id",
    referencedColumnName = "id",
    updatable = false
  )
  private Player player1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "player2_id",
    referencedColumnName = "id",
    updatable = false
  )
  private Player player2;

  @Column(name = "result")
  private boolean result;
}
