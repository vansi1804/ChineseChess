package com.nvs.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

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
    @JoinColumn(name = "player1_id", referencedColumnName = "id", updatable = false)
    private Player player1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player2_id", referencedColumnName = "id", updatable = false)
    private Player player2;

    @Column(name = "result")
    private Integer result;
}
