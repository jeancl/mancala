    const offline_mensagem = {message: "Service Offline"};

    var gameId;
    var playerTurn;
    var gameOver = false;
    var player1FinalScore = 0;
    var player2FinalScore = 0;
    var winner;

    const startGame = async () => {
        const response = await fetch('http://localhost:8080/mancala/start', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        }).catch((err) => {
            showError(offline_mensagem);
        });
        const boardResponse = await response.json();

        if(201 === response.status) {
            setBoardElementsFromJson(boardResponse);
        } else {
            showError(boardResponse);
        }
    }

    function showError(boardResponse) {                
        document.getElementById("errorMessage").innerHTML = boardResponse.message;
        
        $('#errorModal').modal('show');
    }

    const updateGame = async (pitPostion, player) => {
        if(playerTurn === player && !gameOver) {
            let boardUpdateRequest = {
                gameId: gameId,
                playerTurn: playerTurn,
                pit: pitPostion
            };

            const response = await fetch('http://localhost:8080/mancala/update', {
                method: 'PUT',
                body: JSON.stringify(boardUpdateRequest),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).catch((err) => {
                showError(offline_mensagem);
            });
            const boardResponse = await response.json();

            setBoardElementsFromJson(boardResponse);
        }

        checkGameOver();

        if(gameOver) {
            getFinalScore();
        }
    }

    function setBoardElementsFromJson(boardResponse) {
        gameId = boardResponse.gameId;

        playerTurn = boardResponse.playerTurn;
                
        document.getElementById("gameId").innerHTML = gameId;
        
        if("PLAYER_1" == playerTurn) {
            document.getElementById("playerTurn").innerHTML = "Player 1";
        } else {
            document.getElementById("playerTurn").innerHTML = "Player 2";
        }

        document.getElementById("player1score").innerHTML = boardResponse.player1Pits[0];
        document.getElementById("seedP1-1").innerHTML = boardResponse.player1Pits[1];
        document.getElementById("seedP1-2").innerHTML = boardResponse.player1Pits[2];
        document.getElementById("seedP1-3").innerHTML = boardResponse.player1Pits[3];
        document.getElementById("seedP1-4").innerHTML = boardResponse.player1Pits[4];
        document.getElementById("seedP1-5").innerHTML = boardResponse.player1Pits[5];
        document.getElementById("seedP1-6").innerHTML = boardResponse.player1Pits[6];

        document.getElementById("player2score").innerHTML = boardResponse.player2Pits[0];
        document.getElementById("seedP2-1").innerHTML = boardResponse.player2Pits[1];
        document.getElementById("seedP2-2").innerHTML = boardResponse.player2Pits[2];
        document.getElementById("seedP2-3").innerHTML = boardResponse.player2Pits[3];
        document.getElementById("seedP2-4").innerHTML = boardResponse.player2Pits[4];
        document.getElementById("seedP2-5").innerHTML = boardResponse.player2Pits[5];
        document.getElementById("seedP2-6").innerHTML = boardResponse.player2Pits[6];
    }

    const checkGameOver = async () => {
        const response = await fetch('http://localhost:8080/mancala/status/'+gameId, {
            method: 'GET'
        }).catch((err) => {
            showError(offline_mensagem);
        });
        const jsonResponse = await response.json();

        gameOver = jsonResponse.gameOver;
    }

    const getFinalScore = async () => {
        const response = await fetch('http://localhost:8080/mancala/update/'+gameId+"/finalscore", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            }
        }).catch((err) => {
            showError(offline_mensagem);
        });
        const jsonResponse = await response.json();

        player1FinalScore = jsonResponse.player1FinalScore;
        player2FinalScore = jsonResponse.player2FinalScore;

        if(player1FinalScore > player2FinalScore) {
            winner = "Player 1 victory!";
        } else if (player1FinalScore < player2FinalScore) {
            winner = "Player 2 victory!";
        } else {
            winner = "DRAW";
        }

        document.getElementById("player1FinalScore").innerHTML = player1FinalScore;
        document.getElementById("player2FinalScore").innerHTML = player2FinalScore;
        document.getElementById("winner").innerHTML = winner;

        $('#finalScoreModal').modal('show');
    }

    const newGame = async () => {
        const response = await fetch('http://localhost:8080/mancala/delete/'+gameId, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).catch((err) => {
            showError(offline_mensagem);
        });
        
        gameOver = false;
        player1FinalScore = 0;
        player2FinalScore = 0;

        startGame();
    }