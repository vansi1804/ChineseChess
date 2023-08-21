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
import lombok.NoArgsConstructor;

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
    private Training training;

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
