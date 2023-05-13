package entities;

import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

public class Player extends Entity{

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 20;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, right, up, down, jump, lastLeft;
    private float playerSpeed = 0.7f * Game.SCALE;
    private int[][] levelData;
//    private float xDrawOffset = 21 * Game.SCALE;
//    private float yDrawOffset = 4 * Game.SCALE;

    // Gravity and jumping
    private float airSpeed = 0f;
    private float gravity = 0.05f * Game.SCALE;
    private float jumpSpeed = -2.5f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x,y, (int)(16*Game.SCALE), (int)(16*Game.SCALE));
    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g, int levelOffset) {
        BufferedImage anim = animations[playerAction][aniIndex];
        if (left || lastLeft)
        {
            anim = flipImage(anim, true, false);
        }
        g.drawImage(anim,(int)(hitbox.x) - levelOffset,(int)(hitbox.y), width, height,null);
        drawHitbox(g);
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.MARIO_ATLAS);

        animations = new BufferedImage[3][3];
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
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = JUMP;
        }

        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {

        moving = false;

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
                System.out.println("CanMoveHere | xSpeed: " + xSpeed);
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
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
        System.out.println("Player data: " + hitbox.x + " " + hitbox.y + " " + hitbox.width + " " + hitbox.height);
        if(CanMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            System.out.println("In updateXPos");
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

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
