package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PieceDTO {
    private int id;
    private String name;
    private boolean isRed;
	private String image;
    private int currentCol;
    private int currentRow;
}
