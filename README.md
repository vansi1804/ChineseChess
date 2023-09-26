# ChineseChess API

- Object managerment:
  + User: admin(player), player
  + Training: is played matches created by admin
  + Match: is created by 2 player
- Rule: every moving piece is valid with piece's move rule
- Use minimax algorithm to find all piece alive in current board (for red, back or both) to find all best available moves(all valid moves have best score evaluted)
