
```cpp
#include<iostream>
#include <ctime>

void drawBoard(char *spaces);
void playerMove(char *spaces, char player);
void computerMove(char *spaces, char computer);
bool checkWinner(char *spaces, char player, char computer);
bool checkTie(char *spaces);

int main()
{
    char spaces[9] = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    char player = 'X';
    char computer = 'O';
    bool running = true;

    drawBoard(spaces);

    while(running){
        playerMove(spaces, player);
        drawBoard(spaces);
        if(checkWinner(spaces, player, computer)){
            running = false;
            break;
        }
        else if(checkTie(spaces)){
            running = false;
            break;
        }

        computerMove(spaces, computer);
        drawBoard(spaces);
        if(checkWinner(spaces, player, computer)){
            running = false;
            break;
        }
        else if(checkTie(spaces)){
            running = false;
            break;
        }
    }
    std::cout << "Thanks for playing!\n";

    return 0;
}
void drawBoard(char *spaces){
    std::cout << '\n';
    std::cout << "     |     |     " << '\n';
    std::cout << "  " << spaces[0] << "  |  " << spaces[1] << "  |  " << spaces[2] << "  " << '\n';
    std::cout << "_____|_____|_____" << '\n';
    std::cout << "     |     |     " << '\n';
    std::cout << "  " << spaces[3] << "  |  " << spaces[4] << "  |  " << spaces[5] << "  " << '\n';
    std::cout << "_____|_____|_____" << '\n';
    std::cout << "     |     |     " << '\n';
    std::cout << "  " << spaces[6] << "  |  " << spaces[7] << "  |  " << spaces[8] << "  " << '\n';
    std::cout << "     |     |     " << '\n';
    std::cout << '\n';
}
void playerMove(char *spaces, char player){
    int number;
    do{
        std::cout << "Enter a spot to place a marker (1-9): ";
        std::cin >> number;
        number--;
        if(spaces[number] == ' '){
           spaces[number] = player;
           break; 
        }
    }while(!number > 0 || !number < 8);
}
void computerMove(char *spaces, char computer){
    int number;
    srand(time(0));

    while(true){
        number = rand() % 9;
        if(spaces[number] == ' '){
            spaces[number] = computer;
            break;
        }
    }
}
bool checkWinner(char *spaces, char player, char computer){

    if((spaces[0] != ' ') && (spaces[0] == spaces[1]) && (spaces[1] == spaces[2])){
        spaces[0] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
    }
    else if((spaces[3] != ' ') && (spaces[3] == spaces[4]) && (spaces[4] == spaces[5])){
        spaces[3] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
    }
    else if((spaces[6] != ' ') && (spaces[6] == spaces[7]) && (spaces[7] == spaces[8])){
        spaces[6] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
    }
    else if((spaces[0] != ' ') && (spaces[0] == spaces[3]) && (spaces[3] == spaces[6])){
        spaces[0] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
    }
    else if((spaces[1] != ' ') && (spaces[1] == spaces[4]) && (spaces[4] == spaces[7])){
        spaces[1] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
    }
    else if((spaces[2] != ' ') && (spaces[2] == spaces[5]) && (spaces[5] == spaces[8])){
        spaces[2] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
    }
    else if((spaces[0] != ' ') && (spaces[0] == spaces[4]) && (spaces[4] == spaces[8])){
        spaces[0] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
    }
    else if((spaces[2] != ' ') && (spaces[2] == spaces[4]) && (spaces[4] == spaces[6])){
        spaces[2] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
    }
    else{
        return false;
    }

    return true;
}
bool checkTie(char *spaces){

    for(int i = 0; i < 9; i++){
        if(spaces[i] == ' '){
            return false;
        }
    }
    std::cout << "IT'S A TIE!\n";
    return true;
}
```


```cpp
#include <iostream>
#include <cmath>
#include <random>
#include <iomanip>
#include <algorithm>

void drawBoard(char (*spaces)[3]);
void playerMove(char (*spaces)[3], char player);
void computerMove(char (*spaces)[3], char computer);
bool checkWinner(char (*spaces)[3], char player, char computer);
bool checkTie(char (*spaces)[3]);
int main(){
    char spaces[3][3] = {
        {' ', ' ', ' '},
        {' ', ' ', ' '},
        {' ', ' ', ' '}
    };
    char player = 'X';
    char computer = 'O';
    bool running = true;
    drawBoard(spaces);
    while(running){
        playerMove(spaces, player);
        drawBoard(spaces);
        if(checkWinner(spaces, player, computer)){
            running = false;
            break;
        }
        else if(checkTie(spaces)){
            running = false;
            break;
        }

        computerMove(spaces, computer);
        drawBoard(spaces);
        if(checkWinner(spaces, player, computer)){
            running = false;
            break;
        }
        else if(checkTie(spaces)){
            running = false;
            break;
        }
    }
    std::cout << "Thanks for playing!\n";

    return 0;
}
void drawBoard(char (*spaces)[3]){
    std::cout << '\n';
    std::cout << "     |     |     " << '\n';
    std::cout << "  " << spaces[0][0] << "  |  " << spaces[0][1] << "  |  " << spaces[0][2] << "  " << '\n';
    std::cout << "_____|_____|_____" << '\n';
    std::cout << "     |     |     " << '\n';
    std::cout << "  " << spaces[1][0] << "  |  " << spaces[1][1] << "  |  " << spaces[1][2] << "  " << '\n';
    std::cout << "_____|_____|_____" << '\n';
    std::cout << "     |     |     " << '\n';
    std::cout << "  " << spaces[2][0] << "  |  " << spaces[2][1] << "  |  " << spaces[2][2] << "  " << '\n';
    std::cout << "     |     |     " << '\n';
    std::cout << '\n';
}
void playerMove(char (*spaces)[3], char player){
    int number;
    do{
        std::cout << "Enter a spot to place a marker (1-9): ";
        std::cin >> number;
        number--;
        if(spaces[number/3][number%3] == ' '){
            spaces[number/3][number%3] = player;
            break;
        }
    }while(!number > 0 || !number < 8);
}
void computerMove(char (*spaces)[3], char computer){
    int number;
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dis(0,8);
    int countX = 0;
    int count_ = 0;
    int count = 0;
    for(int i = 0;i < sizeof(spaces)/sizeof(spaces[0]);i++){
        for(int j = 0;j < sizeof(spaces[0])/sizeof(spaces[0][0]);j++){
            if(spaces[i][j] == 'X'){
                countX++;
            }
            if(spaces[i][j] == ' '){
                count_++;
            }
        }
        if(countX == 2 && count_ == 1){
            for(int j = 0;j < sizeof(spaces[0])/sizeof(spaces[0][0]);j++){
                if(spaces[i][j] == ' '){
                    spaces[i][j] = computer;
                    count++;
                }
            }
            break;
        }
        countX = 0;
        count_ = 0;
    }
    if(count == 0){
        for(int i = 0;i < sizeof(spaces)/sizeof(spaces[0]);i++){
            for(int j = 0;j < sizeof(spaces[0])/sizeof(spaces[0][0]);j++){
                if(spaces[j][i] == 'X'){
                    countX++;
                }
                if(spaces[j][i] == ' '){
                    count_++;
                }
            }
            if(countX == 2 && count_ == 1){
                for(int j = 0;j < sizeof(spaces[0])/sizeof(spaces[0][0]);j++){
                    if(spaces[j][i] == ' '){
                        spaces[j][i] = computer;
                        count++;
                    }
                }
                break;
            }
            countX = 0;
            count_ = 0;
        }
    }
    
    if(count == 0){
        for(int i = 0;i < sizeof(spaces)/sizeof(spaces[0]);i++){
            if(spaces[i][i] == 'X'){
                countX++;
            }
            if(spaces[i][i] == ' '){
                count_++; 
            }
        }
        if(countX == 2 && count_ == 1){
            for(int i = 0;i < sizeof(spaces)/sizeof(spaces[0]);i++){
                if(spaces[i][i] == ' '){
                    spaces[i][i] = computer;
                    count++;
                }
            }
        }
    }
    if(count == 0){
        for(int i = 0;i < sizeof(spaces)/sizeof(spaces[0]);i++){
            if(spaces[i][2-i] == 'X'){
                countX++;
            }
            if(spaces[i][2-i] == ' '){
                count_++;
            }
        }
        if(countX == 2 && count_ == 1){
            for(int i = 0;i < sizeof(spaces)/sizeof(spaces[0]);i++){
                if(spaces[i][2-i] == ' '){
                    spaces[i][2-i] = computer;
                    count++;
                }
            }
        }
    }
    while(count == 0){
        if(spaces[1][1] == ' '){
            spaces[1][1] = computer;
            break;
        }
        number = dis(gen);
        if(spaces[number/3][number%3] == ' '){
            spaces[number/3][number%3] = computer;
            break;
        }
    }
}
bool checkWinner(char (*spaces)[3], char player, char computer){
    bool count = false;
    for(int i = 0;i < sizeof(spaces)/sizeof(spaces[0]);i++){
        if (spaces[i][0] != ' ' && spaces[i][0] == spaces[i][1] && spaces[i][0] == spaces[i][2])
        {
            spaces[i][0] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
            count = true;
        }
    }
    for(int i = 0;i < sizeof(spaces)/sizeof(spaces[0]);i++){
        if (spaces[0][i] != ' ' && spaces[0][i] == spaces[1][i] && spaces[0][i] == spaces[2][i])
        {
            spaces[0][i] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
            count = true;
        }
    }
    if (spaces[0][0] != ' ' && spaces[0][0] == spaces[1][1] && spaces[0][0] == spaces[2][2])
    {
        spaces[0][0] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
        count = true;
    }
    if (spaces[0][2] != ' ' && spaces[0][2] == spaces[1][1] && spaces[0][2] == spaces[2][0])
    {
        spaces[0][2] == player ? std::cout << "YOU WIN!\n" : std::cout << "YOU LOSE!\n";
        count = true;
    }
    
    return count;
}
bool checkTie(char (*spaces)[3]){
    for(int i = 0;i < sizeof(spaces)/sizeof(spaces[0]);i++){
        for(int j = 0;j < sizeof(spaces[0])/sizeof(spaces[0][0]);j++){
            if(spaces[i][j] == ' '){
                return false;
            }
        }
    }
    std::cout << "IT'S A TIE!\n";
    return true;
}
```