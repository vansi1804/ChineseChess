package com.data.dto;

import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayBoardDTO {

    @NotNull(message = ErrorMessage.NULL_DATA)
    private PieceDTO[][] state;

    /* print test */
    public void print(Object title, PieceDTO pieceDTO) {
        System.out.println("\n===========================================");
        System.out.println(title.toString());
        System.out.println("===========================================");

        for (int row = 0; row < state[0].length; row++) {
            for (int col = 0; col < state.length; col++) {
                System.out.print(getSymbolOutput(pieceDTO, col, row));
            }
            System.out.println();
        }

        System.out.println("===========================================");
    }

    private String getSymbolOutput(PieceDTO pieceDTO, int col, int row) {
        if (state[col][row] == null) {
            if ((pieceDTO != null)
                    && ((col == pieceDTO.getCurrentCol() - 1) && (row == pieceDTO.getCurrentRow() - 1))) {
                return "[  ] ";
            } else {
                return " +   ";
            }
        } else {
            String p = state[col][row].getName().charAt(0) + (state[col][row].isRed() ? "1" : "2");
            if ((pieceDTO != null) && (state[col][row].getId() == pieceDTO.getId())) {
                return "[" + p + "] ";
            } else {
                return " " + p + "  ";
            }
        }
    }

}
