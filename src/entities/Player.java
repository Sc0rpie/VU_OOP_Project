package entities;

import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static utils.Constants.GRAVITY;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

public class Player extends Entity{

    ScheduledExecutorService executor;

    private BufferedImage[][] animations;
    private int aniSpeed = 20;
    private boolean moving = false, attacking = false;
    private boolean left, right, up, down, jump, lastLeft, didFinishStart = false;
    private float playerSpeed = 0.7f * Game.SCALE;
    private float poleSpeed = 0.3f * Game.SCALE;
    private int[][] levelData;
    private boolean dead = false;
    private boolean finished = false;
    private boolean poleMoveFinished = false;
    private int polePosX, endPosX;
    private boolean rendering = true;
    private boolean executorStarted = false, executorRunning = false;
    private boolean restartRequest = false;

    // Gravity and jumping
    private float deathSpeed = -2.5f * Game.SCALE;
    private float jumpSpeed = -2.5f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        this.state = IDLE;
        initHitbox(16, 16);
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    public void update() {
        if (finished) {
            setAnimation();
            updateFinishPos();
        } else {
            if (!dead) {
                updatePos();
                updateAnimationTick();
                setAnimation();
            } else {
                setAnimation();
                updateDeathPos();
            }
        }
    }

    private void updateFinishPos() {
        if (!executorStarted) {
            executor = Executors.newSingleThreadScheduledExecutor();
            executorStarted = true;
        }
        System.out.println("RUNNING FINISHED ANIM");
        if (!didFinishStart) {
            polePosX = (int)(hitbox.x + hitbox.width/2);
            endPosX = polePosX + 6*Game.TILES_SIZE;
            executor.scheduleWithFixedDelay(() -> {
                didFinishStart = true;
                if (!poleMoveFinished && CanMoveHere(hitbox.x, hitbox.y+poleSpeed, hitbox.width, hitbox.height, levelData))
                    hitbox.y += poleSpeed;
                else {
                    if (hitbox.x < endPosX) {
                        setPlayerSpeed(0.4f * Game.SCALE);
                        poleMoveFinished = true;
                        right = true;
                        state = RUNNING;
                        System.out.println("Animation tick" + aniTick);
                        setAnimation();
                        updateAnimationTick();
                        updatePos();
                    } else {
                        finished = false;
                        right = false;
                        rendering = false;
                        executorStarted = false;
                        setPlayerSpeed(0.7f * Game.SCALE);
                        executor.shutdown();
                    }
                }
            }, 500, 500, TimeUnit.MILLISECONDS);
        }

    }

    private void updateDeathPos() {
        if (!executorStarted) {
            executor = Executors.newSingleThreadScheduledExecutor();
            executorStarted = true;
        }
        if (!executorRunning) {
            executor.scheduleWithFixedDelay(() -> {
                System.out.println("RUNNING DEATH ANIM");
                executorRunning = true;
                hitbox.y += deathSpeed;
                deathSpeed += GRAVITY;
                if (deathSpeed > 2.5f*Game.SCALE)
                    deathSpeed = 2.5f*Game.SCALE - 1.0f*Game.SCALE;
                if (hitbox.y > Game.GAME_HEIGHT) {
                    executor.shutdown();
                }
            }, 500, 500, TimeUnit.MILLISECONDS);
        }
        if (hitbox.y > Game.GAME_HEIGHT) {
            restartOnDeath();
        }


    }

    private void restartOnDeath() {
        restartRequest = true;
    }

    public void render(Graphics g, int levelOffset) {
        if (rendering) {
            BufferedImage anim = animations[state][aniIndex];
            if (left || lastLeft)
            {
                anim = flipImage(anim, true, false);
            }
            g.drawImage(anim,(int)(hitbox.x) - levelOffset,(int)(hitbox.y), width, height,null);
            drawHitbox(g, levelOffset);
        }
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.MARIO_ATLAS);

        animations = new BufferedImage[5][3];
        for (int i = 0; i < animations.length; i++)
            for (int j = 0; j < animations[i].length; j++)
                    animations[i][j] = img.getSubimage(j*16, i*16, 16, 16);
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
        if (!IsEntityOnFloor(hitbox, levelData))
            inAir = true;
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = state;

        if (finished && !poleMoveFinished)
            state = FINISH;
        else if (poleMoveFinished)
            state = RUNNING;
        else{
            if (!dead) {
                if (moving) {
                    state = RUNNING;
                } else {
                    state = IDLE;
                }

                if (inAir) {
                    if (airSpeed < 0)
                        state = JUMP;
                    else
                        state = JUMP;
                }
            } else
                state = DEAD;
        }


        if (startAni != state) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {

        moving = false;
//        System.out.println("Current tile: " + hitbox.x/Game.TILES_SIZE);
        if (jump)
            jump();

        if (!inAir)
            if ((!left && !right) || (left && right))
                return;


        float xSpeed = 0;

        if (left)
        {
            xSpeed -= playerSpeed;
            lastLeft = true;
        }
        if (right)
        {
            xSpeed += playerSpeed;
            lastLeft = false;
        }


        if (!inAir)
            if (!IsEntityOnFloor(hitbox, levelData))
                inAir = true;

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y+airSpeed, hitbox.width, hitbox.height, levelData)) {
//                System.out.println("CanMoveHere | xSpeed: " + xSpeed + " airSpeed: " + airSpeed);
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
//                System.out.println("Can't move here | xSpeed: " + xSpeed + " airSpeed: " + airSpeed);
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if(airSpeed > 0)
                    resetInAir();
                else {
                    airSpeed = fallSpeedAfterCollision;
                }
            }
        } else
            updateXPos(xSpeed);

        moving = true;
    }

    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
//        System.out.println("Player data: " + hitbox.x + " " + hitbox.y + " " + hitbox.width + " " + hitbox.height);
        if(CanMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
//            System.out.println("In updateXPos");
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void jumpUponKill() {
        airSpeed = jumpSpeed;
    }
    public void setFinishState(boolean finished) {
        this.finished = finished;
        if (!finished)
            return;
        finished = true;
        hitbox.x -= Game.TILES_SIZE/2;
    }
    public boolean getFinishState() {
        return finished;
    }
    public void setPlayerSpeed (float playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        dead = false;
        finished = false;
        poleMoveFinished = false;
        didFinishStart = false;
        right = false;
        executorStarted = false;
        executorRunning = false;
        restartRequest = false;
        state = IDLE;
        deathSpeed = -2.5f * Game.SCALE;

        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnFloor(hitbox, levelData))
            inAir = true;
    }

    public boolean isRendering() {
        return rendering;
    }
    public void setRendering(boolean rendering) {
        this.rendering = rendering;
    }
    public void setRestartRequest(boolean restartRequest) {
        this.restartRequest = restartRequest;
    }
    public boolean getRestartRequest() {
        return restartRequest;
    }
}
