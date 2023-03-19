package com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_move_history")
public class MoveHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "turn")
    private long turn;
    @Column(name = "player_id")
    private long playerId;
    @Column(name = "piece_id")
    private int pieceId;
    @Column(name = "from_col")
    private int fromCol;
    @Column(name = "fromRow")
    private int fromRow;
    @Column(name = "toCol")
    private int toCol;
    @Column(name = "toRow")
    private int toRow;
}
