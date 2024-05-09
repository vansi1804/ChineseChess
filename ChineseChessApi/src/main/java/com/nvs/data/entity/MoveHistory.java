package com.nvs.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "move_histories")
public class MoveHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", referencedColumnName = "id", updatable = false)
    private Match match;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", referencedColumnName = "id", updatable = false)
    private com.nvs.data.entity.Training training;

    @Column(name = "turn")
    private long turn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_id", referencedColumnName = "id")
    private Piece piece;

    @Column(name = "to_col")
    private int toCol;

    @Column(name = "to_row")
    private int toRow;
}
