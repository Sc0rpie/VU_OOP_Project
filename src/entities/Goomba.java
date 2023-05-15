package entities;

import java.awt.*;

import static utils.Constants.EnemyConstants.*;

public class Goomba extends Enemy{

    public Goomba(float x, float y) {
        super(x, y, GOOMBA_WIDTH, GOOMBA_HEIGHT, GOOMBA);
        initHitbox(x,y,GOOMBA_WIDTH,GOOMBA_HEIGHT);

    }

//    public void update() {
//
//    }
}
