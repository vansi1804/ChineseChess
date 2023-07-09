package com.data.dto;

import java.util.stream.IntStream;

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

        IntStream.rangeClosed(1, state.length)
                .boxed()
                .flatMap(col -> IntStream.rangeClosed(1, state[0].length)
                        .mapToObj(row -> getSymbolOutput(pieceDTO, col, row)))
                .forEach(System.out::print);

        System.out.println("===========================================");
    }

    private String getSymbolOutput(PieceDTO pieceDTO, int col, int row) {
        if (state[col - 1][row - 1] == null) {
            if ((pieceDTO != null) && ((col == pieceDTO.getCurrentCol()) && (row == pieceDTO.getCurrentRow()))) {
                return "[  ] ";
            } else {
                return " +   ";
            }
        } else {
            String p = state[col - 1][row - 1].getName().charAt(0) + (state[col - 1][row - 1].getColor() ? "1" : "2");
            if ((pieceDTO != null) && (state[col - 1][row - 1].getId() == pieceDTO.getId())) {
                return "[" + p + "] ";
            } else {
                return " " + p + "  ";
            }
        }
    }

}
