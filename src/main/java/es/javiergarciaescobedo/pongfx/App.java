package es.javiergarciaescobedo.pongfx;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Javier García Escobedo (javiergarciaescobedo.es)
 */
public class App extends Application {

    final short WORLD_WIDTH = 500;
    final short WORLD_HEIGHT = 300;
    final Color WORLD_BACKGROUND = Color.BLACK;

    // Ball data
    final Color BALL_BACKGROUND = Color.WHITE;
    float ballSpeedX;
    float ballSpeedY;
    final byte BALL_DEFAULT_SPEED = 3;
    final byte BALL_RADIUS = 5;

    // Paddles data
    final Color PADDLE_BACKGROUND = Color.WHITE;
    float paddle1SpeedY;
    float paddle2SpeedY;
    final byte PADDLE_WIDTH = 10;
    final byte PADDLE_HEIGHT = 50;
    final byte PADDLES_MARGIN = 30; // Margin to scene borders
    final short PADDLE1_POSX = PADDLES_MARGIN - PADDLE_WIDTH;
    final short PADDLE2_POSX = WORLD_WIDTH - PADDLES_MARGIN;
    final byte PADDLES_DEFAULT_SPEED = 5;
    
    // Scores for players
    int score1 = 0;
    int score2 = 0;
    
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();        
        root.setBackground(Background.EMPTY);  // Transparent Pane
        Scene scene = new Scene(root, WORLD_WIDTH, WORLD_HEIGHT, WORLD_BACKGROUND);

        // Creating the ball
        Circle ball = new Circle(BALL_RADIUS);
        ball.setTranslateX(WORLD_WIDTH * 0.5f);
        ball.setTranslateY(WORLD_HEIGHT * 0.5f);
        ball.setFill(BALL_BACKGROUND);
        root.getChildren().add(ball);     
        
        // Set ball direction randomly
        Random random = new Random();
        boolean coin = random.nextBoolean();
        if(coin) {
            ballSpeedX = BALL_DEFAULT_SPEED;
        } else {
            ballSpeedX = -BALL_DEFAULT_SPEED;
        }
        ballSpeedY = BALL_DEFAULT_SPEED;
        
        
        // Create player1 paddle
        Rectangle paddle1 = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_BACKGROUND);
        paddle1.setTranslateX(PADDLE1_POSX);
        paddle1.setTranslateY(WORLD_HEIGHT * 0.5f - PADDLE_HEIGHT * 0.5f);
        root.getChildren().add(paddle1);                
        paddle1SpeedY = 0;
        
        // Create player2 paddle
        Rectangle paddle2 = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_BACKGROUND);
        paddle2.setTranslateX(PADDLE2_POSX);
        paddle2.setTranslateY(WORLD_HEIGHT * 0.5f - PADDLE_HEIGHT * 0.5f);
        root.getChildren().add(paddle2);                
        paddle2SpeedY = 0;
        
        // Create Label for player 1 score
        Label labelScore1 = new Label();
        labelScore1.setText(String.valueOf(score1));
        labelScore1.setFont(new Font(24));
        labelScore1.setTextFill(Color.WHITE);
        labelScore1.setTranslateX(WORLD_WIDTH * 0.5f - 30);
        labelScore1.setTranslateY(10);
        root.getChildren().add(labelScore1);          
        
        // Create Label for player 2 score
        Label labelScore2 = new Label();
        labelScore2.setText(String.valueOf(score2));
        labelScore2.setFont(new Font(24));
        labelScore2.setTextFill(Color.WHITE);
        labelScore2.setTranslateX(WORLD_WIDTH * 0.5f + 30);
        labelScore2.setTranslateY(10);
        root.getChildren().add(labelScore2);

        primaryStage.setTitle("PongFX");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Keyboard control
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch(event.getCode()) {
                    case A: // Player 1 Up
                        paddle1SpeedY = -PADDLES_DEFAULT_SPEED;
                        break;
                    case Z: // Player 1 Down
                        paddle1SpeedY = PADDLES_DEFAULT_SPEED;
                        break;
                    case UP: // Player 2 Up
                        paddle2SpeedY = -PADDLES_DEFAULT_SPEED;
                        break;
                    case DOWN: // Player 2 Down
                        paddle2SpeedY = PADDLES_DEFAULT_SPEED;
                        break;
                }
            }
        });
        
        // Game loop
        Timeline timeline = new Timeline(
            // 0.017 seconds ~= 60 FPS
            new KeyFrame(Duration.seconds(0.017), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent ae) {

                    // Paddle1 new position
                    paddle1.setTranslateY(paddle1.getTranslateY() + paddle1SpeedY);
                    // Check if paddle 1 is out of bounds
                    if(paddle1.getTranslateY() <= 0) {
                        // Paddle 1 is out of upper bound. Stopping paddle 1
                        paddle1.setTranslateY(0);
                        paddle1SpeedY = 0;
                    } else {
                        if(paddle1.getTranslateY() + PADDLE_HEIGHT >= WORLD_HEIGHT) {
                            // Paddle 1 is out of lower bound. Stopping paddle 1
                            paddle1.setTranslateY(WORLD_HEIGHT - PADDLE_HEIGHT - 1);
                            paddle1SpeedY = 0;
                        }
                    }

                    // Paddle2 new position                    
                    paddle2.setTranslateY(paddle2.getTranslateY() + paddle2SpeedY);
                    // Check if paddle 1 is out of bounds
                    if(paddle2.getTranslateY() <= 0) {
                        // Paddle 2 is out of upper bound. Stopping paddle 2
                        paddle2.setTranslateY(0);
                        paddle2SpeedY = 0;
                    } else {
                        if(paddle2.getTranslateY() + PADDLE_HEIGHT >= WORLD_HEIGHT) {
                            // Paddle 2 is out of lower bound. Stopping paddle 2
                            paddle2.setTranslateY(WORLD_HEIGHT - PADDLE_HEIGHT - 1);
                            paddle2SpeedY = 0;
                        }
                    }

                    // Animate ball
                    ball.setTranslateX(ball.getTranslateX() + ballSpeedX);
                    ball.setTranslateY(ball.getTranslateY() + ballSpeedY);

                    byte contactZone = -1;
                    // Check ball collision with paddle1
                    if(ball.getTranslateX() - BALL_RADIUS >= PADDLE1_POSX && 
                            ball.getTranslateX() - BALL_RADIUS < PADDLE1_POSX + PADDLE_WIDTH && 
                            ball.getTranslateY() >= paddle1.getTranslateY() && 
                            ball.getTranslateY() <= paddle1.getTranslateY() + PADDLE_HEIGHT) {
                        // Change ball direction to right
                        ballSpeedX = BALL_DEFAULT_SPEED;
                        // Detect contact zone in paddle1
                        contactZone = (byte)((ball.getTranslateY() - paddle1.getTranslateY()) / (PADDLE_HEIGHT / 5));
                    } 

                    // Check ball collision with paddle2
                    if(ball.getTranslateX() + BALL_RADIUS >= PADDLE2_POSX && 
                            ball.getTranslateX() + BALL_RADIUS < PADDLE2_POSX + PADDLE_WIDTH && 
                            ball.getTranslateY() >= paddle2.getTranslateY() && 
                            ball.getTranslateY() <= paddle2.getTranslateY() + PADDLE_HEIGHT) {
                        // Change ball direction to left
                        ballSpeedX = -BALL_DEFAULT_SPEED;
                        // Detect contact zone in paddle2
                        contactZone = (byte)((ball.getTranslateY() - paddle2.getTranslateY()) / (PADDLE_HEIGHT / 5));
                    } 
                    
                    // Change ball speed Y if touched in some paddle
                    if(contactZone != -1) {
                        switch(contactZone) {
                            case 0: // Upper area
                                ballSpeedY = BALL_DEFAULT_SPEED * -1.5f;
                                break;
                            case 1: // Upper-middle area
                                ballSpeedY = BALL_DEFAULT_SPEED * -1.25f;
                                break;
                            case 2: // Middle area
                                ballSpeedY = BALL_DEFAULT_SPEED;
                                break;
                            case 3: // Lower-middle area
                                ballSpeedY = BALL_DEFAULT_SPEED * 1.25f;
                                break;
                            case 4: // Lower area
                                ballSpeedY = BALL_DEFAULT_SPEED * 1.5f;
                                break;                                
                        }
                        contactZone = -1;
                    }

                        // Test if ball is out RIGHT border
                    if(ball.getTranslateX() - BALL_RADIUS <= 0) {
                        // Move the ball to center
                        ball.setTranslateX(WORLD_WIDTH * 0.5f);
                        ball.setTranslateY(WORLD_HEIGHT * 0.5f);
                        ballSpeedX = BALL_DEFAULT_SPEED;
                        ballSpeedY = BALL_DEFAULT_SPEED;
                        // Increase player 2 score
                        score2++;
                        labelScore2.setText(String.valueOf(score2));
                    }

                    // Test if ball is out LEFT border
                    if(ball.getTranslateX() + BALL_RADIUS >= WORLD_WIDTH - 1) {
                        // Move the ball to center
                        ball.setTranslateX(WORLD_WIDTH * 0.5f);
                        ball.setTranslateY(WORLD_HEIGHT * 0.5f);
                        ballSpeedX = -BALL_DEFAULT_SPEED;
                        ballSpeedY = BALL_DEFAULT_SPEED;
                        // Increase player 1 score
                        score1++;
                        labelScore1.setText(String.valueOf(score1));
                    }                    

                    // Bounding ball upper or bottom bounds
                    if(ball.getTranslateY() <= 0 || 
                            ball.getTranslateY() >= WORLD_HEIGHT - 1) {
                        ballSpeedY *= -1;
                    }

                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
