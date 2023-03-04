package com.example.chinesechesstrainning.support;

import com.example.chinesechesstrainning.model.Match;
import com.example.chinesechesstrainning.model.MatchDetail;
import com.example.chinesechesstrainning.model.Piece;

import java.util.ArrayList;

public class DataTest {

    public static ArrayList<Piece> getPieces(){
        ArrayList<Piece> pieces = new ArrayList<>();
        pieces.add(new Piece(1, "Tốt", true));
        pieces.add(new Piece(2, "Tốt", true));
        pieces.add(new Piece(3, "Tốt", true));
        pieces.add(new Piece(4, "Tốt", true));
        pieces.add(new Piece(5, "Tốt", true));
        pieces.add(new Piece(6, "Pháo", true));
        pieces.add(new Piece(7, "Pháo", true));
        pieces.add(new Piece(8, "Xe", true));
        pieces.add(new Piece(9, "Xe", true));
        pieces.add(new Piece(10, "Mã", true));
        pieces.add(new Piece(11, "Mã", true));
        pieces.add(new Piece(12, "Tượng", true));
        pieces.add(new Piece(13, "Tượng", true));
        pieces.add(new Piece(14, "Sĩ", true));
        pieces.add(new Piece(15, "Sĩ", true));
        pieces.add(new Piece(16, "Tướng", true));
        pieces.add(new Piece(17, "Tốt", false));
        pieces.add(new Piece(18, "Tốt", false));
        pieces.add(new Piece(19, "Tốt", false));
        pieces.add(new Piece(20, "Tốt", false));
        pieces.add(new Piece(21, "Tốt", false));
        pieces.add(new Piece(22, "Pháo", false));
        pieces.add(new Piece(23, "Pháo", false));
        pieces.add(new Piece(24, "Xe", false));
        pieces.add(new Piece(25, "Xe", false));
        pieces.add(new Piece(26, "Mã", false));
        pieces.add(new Piece(27, "Mã", false));
        pieces.add(new Piece(28, "Tượng", false));
        pieces.add(new Piece(29, "Tượng", false));
        pieces.add(new Piece(30, "Sĩ", false));
        pieces.add(new Piece(31, "Sĩ", false));
        pieces.add(new Piece(32, "Tướng", false));
        return pieces;
    }

    public static ArrayList<Match> matchesData(){
        ArrayList<Match> matches = new ArrayList<>();
        matches.add(new Match(1,"Tuyển tập pháo đầu",1));
        matches.add(new Match(2,"Tuyển tập bình phong mã",2));
        matches.add(new Match(3,"Tuyển tập phi tượng",3));
        matches.add(new Match(4,"Thuận pháo",1));
        matches.add(new Match(5,"Nghịch pháo",1));
        matches.add(new Match(6,"Trận 1",4));
        matches.add(new Match(7,"Trận 1",5));

        return matches;
    }

    public static ArrayList<Match> getMatches(){
        ArrayList<Match> matches = new ArrayList<>();
        for (Match match: matchesData()) {
            if (match.getId() == match.getParentMatchId()){
                matches.add(match);
            }
        }
        return matches;
    }
    public static Match getParentMatchById(long id){
        for (Match parentMatch:matchesData()) {
            for (Match childMatch:matchesData()) {
                if (childMatch.getId() == id
                        && childMatch.getParentMatchId() == parentMatch.getId()
                        && childMatch.getId() != parentMatch.getId()){
                    return parentMatch;
                }
            }
        }
        return null;
    }
    public static ArrayList<Match> getChildMatchesById(long id){
        ArrayList<Match> childMatches = new ArrayList<>();
        for (Match parentMatch:matchesData()) {
            for (Match childMatch:matchesData()) {
                if (parentMatch.getId() == id
                        && parentMatch.getId() == childMatch.getParentMatchId()
                        && childMatch.getId() != childMatch.getParentMatchId()){
                    childMatches.add(childMatch);
                }
            }
        }
        return childMatches;
    }

    public static ArrayList<MatchDetail> matchDetailsData(){
        ArrayList<MatchDetail> matchDetails = new ArrayList<>();
        matchDetails.add(new MatchDetail(6,1,6,8,8,5,8));
        matchDetails.add(new MatchDetail(6,2,23,8,3,5,3));
        matchDetails.add(new MatchDetail(6,3,10,8,10,7,8));
        matchDetails.add(new MatchDetail(6,4,27,8,1,7,3));
        matchDetails.add(new MatchDetail(6,5,8,9,10,9,9));
        matchDetails.add(new MatchDetail(6,6,25,9,1,8,1));
        matchDetails.add(new MatchDetail(6,7,8,9,9,4,9));
        matchDetails.add(new MatchDetail(6,8,25,8,1,8,7));
        matchDetails.add(new MatchDetail(6,9,8,4,9,4,2));
        matchDetails.add(new MatchDetail(6,10,26,2,1,1,3));
        matchDetails.add(new MatchDetail(6,11,9,1,10,1,9));
        matchDetails.add(new MatchDetail(6,12,22,2,3,2,10));
        matchDetails.add(new MatchDetail(6,13,7,2,8,2,3));
        matchDetails.add(new MatchDetail(6,14,25,8,7,8,3));
        matchDetails.add(new MatchDetail(6,15,8,4,2,7,2));
        matchDetails.add(new MatchDetail(6,16,24,1,1,2,1));
        matchDetails.add(new MatchDetail(6,17,7,2,3,7,3));
        matchDetails.add(new MatchDetail(6,18,29,7,1,9,3));
        matchDetails.add(new MatchDetail(6,19,6,5,8,5,4));
        matchDetails.add(new MatchDetail(6,20,31,6,1,5,2));
        matchDetails.add(new MatchDetail(6,21,7,7,3,9,3));
        matchDetails.add(new MatchDetail(6,22,25,8,3,8,1));
        matchDetails.add(new MatchDetail(6,23,9,1,9,8,9));
        matchDetails.add(new MatchDetail(6,24,25,8,1,6,1));
        matchDetails.add(new MatchDetail(6,25,8,7,2,8,2));
        matchDetails.add(new MatchDetail(6,26,24,2,1,2,5));
        matchDetails.add(new MatchDetail(6,27,7,9,3,9,1));
        matchDetails.add(new MatchDetail(6,28,25,6,1,9,1));
        matchDetails.add(new MatchDetail(6,29,8,8,2,8,1));
        matchDetails.add(new MatchDetail(6,30,25,9,1,8,1));
        matchDetails.add(new MatchDetail(6,31,9,8,9,8,1));
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        matchDetails.add(new MatchDetail(7,1,6,8,8,5,8));
        matchDetails.add(new MatchDetail(7,2,22,2,3,5,3));
        matchDetails.add(new MatchDetail(7,3,10,8,10,7,8));
        matchDetails.add(new MatchDetail(7,4,27,8,1,9,3));
        matchDetails.add(new MatchDetail(7,5,8,9,10,8,10));
        matchDetails.add(new MatchDetail(7,6,25,9,1,8,1));
        matchDetails.add(new MatchDetail(7,7,11,2,10,1,8));
        matchDetails.add(new MatchDetail(7,8,26,2,1,3,3));
        matchDetails.add(new MatchDetail(7,9,9,1,10,2,10));
        matchDetails.add(new MatchDetail(7,10,24,1,1,2,1));
        matchDetails.add(new MatchDetail(7,11,5,1,7,1,6));
        matchDetails.add(new MatchDetail(7,12,21,9,4,9,5));
        matchDetails.add(new MatchDetail(7,13,8,8,10,8,6));
        matchDetails.add(new MatchDetail(7,14,24,2,1,2,5));
        matchDetails.add(new MatchDetail(7,15,11,1,8,2,6));
        matchDetails.add(new MatchDetail(7,16,24,2,5,6,5));
        matchDetails.add(new MatchDetail(7,17,8,8,6,4,6));
        matchDetails.add(new MatchDetail(7,18,23,8,3,8,10));
        matchDetails.add(new MatchDetail(7,19,10,7,8,8,10));
        matchDetails.add(new MatchDetail(7,20,25,8,1,8,9));
        matchDetails.add(new MatchDetail(7,21,11,2,6,4,5));
        matchDetails.add(new MatchDetail(7,22,22,5,3,8,3));
        matchDetails.add(new MatchDetail(7,23,11,4,5,3,3));
        matchDetails.add(new MatchDetail(7,24,24,6,5,6,10));
        matchDetails.add(new MatchDetail(7,25,16,5,10,6,10));
        matchDetails.add(new MatchDetail(7,26,22,8,3,8,10));

        return matchDetails;
    }

    public static ArrayList<MatchDetail> getMatchDetailsByMatchId(long id){
        ArrayList<MatchDetail> matchDetails = new ArrayList<>();
        for (MatchDetail matchDetail:matchDetailsData()) {
            if (matchDetail.getMatchId() == id){
                matchDetails.add(matchDetail);
            }
        }
        return matchDetails;
    }

}
