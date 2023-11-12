package com.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "pieces")
public class Piece extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private int id;

    @Column(name = "name", updatable = false)
    private String name;

    @Column(name = "is_red", updatable = false)
    private boolean isRed;

    @Column(name = "image")
    private String image;

    @Column(name = "start_col", updatable = false)
    private int currentCol;

    @Column(name = "start_row", updatable = false)
    private int currentRow;

}
