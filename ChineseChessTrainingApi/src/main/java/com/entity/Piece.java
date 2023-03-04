package com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "piece")
public class Piece {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "is_red")
    private boolean isRed;
    @Column(name = "image")
	private String image;
    @Column(name = "start_col")
    private int startCol;
    @Column(name = "start_row")
    private int startRow;
}
