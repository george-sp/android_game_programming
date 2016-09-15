package com.codeburrow.platformer.player;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A weapon for the player that fires bullets.
 */
public class MachineGun {

    // The maximum number of bullets the machine-gun can have.
    private int maxBullets = 10;
    // Needed to keep track of its bullets.
    private int numBullets;
    private int nextBullet;
    // Bullets per second - the upgradeable aspect of the weapon.
    private int rateOfFire = 1;
    // Track the (system) time the last bullet was fired.
    private long lastShotTime;

    private CopyOnWriteArrayList<Bullet> bullets;

    int speed = 25;

    public MachineGun() {
        bullets = new CopyOnWriteArrayList<>();
        lastShotTime = -1;
        nextBullet = -1;
    }

    /**
     * Update all the bullets controlled by the gun.
     *
     * @param fps
     * @param gravity
     */
    public void update(long fps, float gravity) {
        for (Bullet bullet : bullets) {
            bullet.update(fps, gravity);
        }
    }

    public int getRateOfFire() {
        return rateOfFire;
    }

    public void setRateOfFire(int rate) {
        rateOfFire = rate;
    }

    public int getNumBullets() {
        return numBullets;
    }

    public float getBulletX(int bulletIndex) {
        if (bullets != null && bulletIndex < numBullets) {
            return bullets.get(bulletIndex).getX();
        }
        return -1f;
    }

    public float getBulletY(int bulletIndex) {
        if (bullets != null) {
            return bullets.get(bulletIndex).getY();
        }
        return -1f;
    }

    /**
     * Helper method to stop drawing a bullet.
     *
     * @param index
     */
    public void hideBullet(int index) {
        bullets.get(index).hideBullet();
    }

    /**
     * Get the direction of travel.
     *
     * @param index
     * @return
     */
    public int getDirection(int index) {
        return bullets.get(index).getDirection();
    }

    public void upgradeRateOfFire() {
        rateOfFire += 2;
    }

    /**
     * Shoots a bullet.
     * <p/>
     * Compares the time of the last fired shot against the current rate of fire.
     *
     * @param ownerX
     * @param ownerY
     * @param ownerFacing
     * @param ownerHeight
     * @return True, if a bullet was successfully fired.
     */
    public boolean shoot(float ownerX, float ownerY, int ownerFacing, float ownerHeight) {
        boolean shotFired = false;
        if (System.currentTimeMillis() - lastShotTime > 1000 / rateOfFire) {
            // Spawn another bullet;
            nextBullet++;

            if (numBullets >= maxBullets) {
                numBullets = maxBullets;
            }
            if (nextBullet == maxBullets) {
                nextBullet = 0;
            }
            lastShotTime = System.currentTimeMillis();
            bullets.add(nextBullet, new Bullet(ownerX, (ownerY + ownerHeight / 3), speed, ownerFacing));
            shotFired = true;
            numBullets++;
        }
        return shotFired;
    }
}
