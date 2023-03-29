package com.data.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_match")
public class Match implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "total_time")
    private int time;
    @Column(name = "moving_time")
    private int movingTime;
    @Column(name = "cumulative_time")
    private int cumulativeTime;
    @Column(name = "bet")
    private int bet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player1_id", referencedColumnName = "id")
    private Player player1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player2_id", referencedColumnName = "id")
    private Player player2;
    @Column(name = "start_at")
    private LocalDateTime startAt;
    @Column(name = "result")
    private long result;
}
