public enum GameState {
    READY,           // game created, secret chosen
    AWAITING_GUESS,  // at least one valid guess made, not yet won
    WON              // player found the secret number
}
