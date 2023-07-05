package com.data.entity;

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
@Table(name = "pieces")
public class Piece implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private int id;

    @Column(name = "name", columnDefinition = "nvarchar(255)")
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
