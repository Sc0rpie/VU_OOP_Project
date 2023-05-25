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


/**
 * Žaidėjo klasė, paveldinti iš "Entity" klasės.
 */
public class Player extends Entity{

    /**
     * Objektas, leidžiantis atidėti žaidimo kodo vykdyma kažkuriam laikui.
     */
    ScheduledExecutorService executor;
    /**
     * Žaidėjo animacijos paveiksliukų masyvas.
     */
    private BufferedImage[][] animations;
    /**
     * Žaidėjo animacijos greitis.
     */
    private int aniSpeed = 20;
    /**
     * moving ir attacking kintamieji naudojami būsenų patikrinimams.
     */
    private boolean moving = false, attacking = false;
    /**
     * Kintamasis nustatantis, ar žaidėjas juda į kairę.
     */
    private boolean left;
    /**
     * Kintamasis nustatantis, ar žaidėjas juda į kairę.
     */
    private boolean right;
    /**
     * Kintamasis nustatantis, ar žaidėjas juda į viršų.
     */
    private boolean up;
    /**
     * Kintamasis nustatantis, ar žaidėjas juda į apačią.
     */
    private boolean down;
    /**
     * Kintamasis nustatantis, ar žaidėjas pašoka.
     */
    private boolean jump;
    /**
     * Kintamasis nustatantis, kur žaidėjas turi žiūrėti.
     */
    private boolean lastLeft;
    /**
     * Kintamasis nustatantis, ar pabaigos animacija prasidėjo.
     */
    private boolean didFinishStart = false;
    /**
     * Žaidėjo greitis.
     */
    private float playerSpeed = 0.7f * Game.SCALE;
    /**
     * Žaidėjo greitis nusileidžiant finišo stulpu.
     */
    private float poleSpeed = 0.3f * Game.SCALE;
    /**
     * Lygio duomenys.
     */
    private int[][] levelData;
    /**
     * Kintamasis nusakantis, ar žaidėjas miręs.
     */
    private boolean dead = false;
    /**
     * Kintamasis nusakantis, ar žaidėjas baigė lygį.
     */
    private boolean finished = false;
    /**
     * Kintamasis nusakantis, ar nusileidimas nuo stulpo buvo pabaigtas.
     */
    private boolean poleMoveFinished = false;
    /**
     * Saugoma stulpo x koordinatė (naudojama animacijai).
     */
    private int polePosX;

    /**
     * Saugoma pilies x koordinatė (naudojama animacijai).
     */
    private int endPosX;
    /**
     * Būsena nusakanti, ar žaidėjas piešiamas.
     */
    private boolean rendering = true;
    /**
     * Būsena nusakanti ar "executor" jau buvo paleistas.
     */
    private boolean executorStarted = false;
    /**
     * Būsena nusakanti ar "executor" jau veikia.
     */
    private boolean executorRunning = false;
    /**
     * Būsena nusakanti ar reikalingas žaidimo perkrovimas.
     */
    private boolean restartRequest = false;
    /**
     * Mirties greitis (skrydis į viršų ir žemyn).
     */
    private float deathSpeed = -2.5f * Game.SCALE;
    /**
     * Šuoliuko greitis.
     */
    private float jumpSpeed = -2.5f * Game.SCALE;
    /**
     * Žaidėjo greitis atsitrenkiant į objektą.
     */
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    /**
     * Žaidėjo konstruktorius.
     *
     * @param x      žaidėjo pradinė x koordinatė
     * @param y      žaidėjo pradinė y koordinatė
     * @param width  žaidėjo plotis
     * @param height žaidėjo aukštis
     */
    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        this.state = IDLE;
        initHitbox(16, 16);
    }

    /**
     * Nustato žaidėjo atsiradimo vietą.
     *
     * @param spawn atsiradimo vieta
     */
    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    /**
     * Atnaujina žaidėjo būseną ir poziciją.
     */
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

    /**
     * Nustato žaidėjo animaciją baigiant lygį.
     */
    private void updateFinishPos() {
        if (!executorStarted) {
            executor = Executors.newSingleThreadScheduledExecutor();
            executorStarted = true;
        }
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

    /**
     * Nustato žaidėjo animaciją mirties atveju.
     */
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

    /**
     * Užklausa perkrauti lygį mirus.
     */
    private void restartOnDeath() {
        restartRequest = true;
    }

    /**
     * Nupaišo žaidėją.
     *
     * @param g            grafikos kontekstas
     * @param levelOffset  lygio postūmis
     */
    public void render(Graphics g, int levelOffset) {
        if (rendering) {
            BufferedImage anim = animations[state][aniIndex];
            if (left || lastLeft)
            {
                anim = flipImage(anim, true, false);
            }
            g.drawImage(anim,(int)(hitbox.x) - levelOffset,(int)(hitbox.y), width, height,null);
        }
    }

    /**
     * Įkelia žaidėjo animacijas.
     */
    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.MARIO_ATLAS);

        animations = new BufferedImage[5][3];
        for (int i = 0; i < animations.length; i++)
            for (int j = 0; j < animations[i].length; j++)
                    animations[i][j] = img.getSubimage(j*16, i*16, 16, 16);
    }

    /**
     * Įkelia lygio duomenis.
     *
     * @param levelData lygio duomenys
     */
    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
        if (!IsEntityOnFloor(hitbox, levelData))
            inAir = true;
    }

    /**
     * Atnaujina animacijos laikmačio reikšmę ir keičia animacijos indeksą.
     */
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

    /**
     * Nustato žaidėjo būseną/animaciją.
     */
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

    /**
     * Nunulinama žaidėjo animacijos laikmačio reikšmę ir animacijos indeksą.
     */
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    /**
     * Atnaujinama žaidėjo pozicija.
     * Jei žaidėjas yra ant žemės, jis gali judėti į šonus.
     * Jei žaidėjas yra ore, jis gali judėti į šonus ir žemyn.
     * Jei žaidėjas yra ant žemės ir nejudės, jo būsena nustatoma į IDLE.
     */
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
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
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

    /**
     * Tikrinama ar žaidėjas gali atlikti šuolį.
     * Jei žaidėjas yra ant žemės, jis gali atlikti šuolį (airSpeed = jumpSpeed).
     */
    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    /**
     * Nustatomos pradinės inAir ir airSpeed reikšmės.
     */
    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    /**
     * Atnaujinama žaidėjo x pozicija.
     *
     * @param xSpeed žaidėjo x greitis
     */
    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    /**
     * Nustatomos pradinės žaidėjo vaikščiojimo krypties reikšmės.
     */
    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    /**
     * Nustato ar žaidėjas atakuoja.
     *
     * @param attacking ar žaidėjas atakuoja
     */
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    /**
     * Nustato ar žaidėjas juda į kairę.
     *
     * @param left ar žaidėjas juda į kairę
     */
    public void setLeft(boolean left) {
        this.left = left;
    }

    /**
     * Nustato ar žaidėjas juda į dešinę.
     *
     * @param right ar žaidėjas juda į dešinę
     */
    public void setRight(boolean right) {
        this.right = right;
    }

    /**
     * Nustato ar žaidėjas šoka.
     *
     * @param jump ar žaidėjas šoka
     */
    public void setJump(boolean jump) {
        this.jump = jump;
    }

    /**
     * Grąžina ar žaidėjas miręs.
     *
     * @return ar žaidėjas miręs
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Nustato ar žaidėjas miręs.
     *
     * @param dead ar žaidėjas miręs
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * airSpeed yra nustatomas į jumpSpeed (šuoliukas).
     */
    public void jumpUponKill() {
        airSpeed = jumpSpeed;
    }

    /**
     * Nustato ar žaidėjas pabaigė lygį. Hitbox'as yra perstumiamas į kairę animacijos tikslams.
     * @param finished ar žaidėjas pabaigė lygį.
     */
    public void setFinishState(boolean finished) {
        this.finished = finished;
        if (!finished)
            return;
        finished = true;
        hitbox.x -= Game.TILES_SIZE/2;
    }

    /**
     * Grąžina ar žaidėjas pabaigė lygį.
     *
     * @return ar žaidėjas pabaigė lygį
     */
    public boolean getFinishState() {
        return finished;
    }

    /**
     * Nustato žaidėjo greitį.
     *
     * @param playerSpeed žaidėjo greitis
     */
    public void setPlayerSpeed (float playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    /**
     * Didžioji dalis parametrų yra nustatomi į pradines reikšmes.
     * Tai yra daroma tam, kad žaidėjas galėtų pradėti žaidimą iš naujo.
     */
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

    /**
     * Grąžina ar žaidėjas yra piešiamas.
     * Žaidėjas nėra piešiamas tuo atveju, kai yra pabaigtas lygis.
     * @return ar žaidėjas yra piešiamas.
     */
    public boolean isRendering() {
        return rendering;
    }

    /**
     * Nustato ar žaidėjas yra piešiamas.
     * @param rendering ar žaidėjas yra piešiamas.
     */
    public void setRendering(boolean rendering) {
        this.rendering = rendering;
    }

    /**
     * Patikrina, ar buvo prašyta perleisti žaidimą.
     *
     * @return ar buvo prašyta perleisti žaidimą
     */
    public boolean getRestartRequest() {
        return restartRequest;
    }
}
