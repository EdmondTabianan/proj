package entity;

import main.GamePanel;

public class Projectile extends Entity {

    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }
    
    public void set(int worldX, int worldY, String Direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.Direction = Direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
        
        // IMPORTANT: Set the speed for the projectile
        this.speed = 5; // Set a default speed (adjust as needed)
        
        // Set attack damage if not already set
        if (this.attack == 0) {
            this.attack = 2; // Default attack damage
        }
        
        // Reset sprite animation when firing
        this.spriteCounter = 0;
        this.spriteNum = 1;
        
    }
    
    public void update() {
        // CRITICAL: Don't process dead projectiles
        if (alive == false) {
            return;
        }
        
        // ===== MOVE PROJECTILE FIRST =====
        // Move before checking collision to avoid hitting the shooter
        switch (Direction) {
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }

        // ===== CHECK WALL COLLISION =====
        collisionOn = false;
        gp.cChecker.checkTile(this);
        if (collisionOn == true) {
            this.alive = false;
            
            return;
        }

        // ===== CHECK COLLISION AFTER MOVING =====
        if (user != null && user == gp.player) {
            // Player-fired projectile - damage monsters
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            
            if (monsterIndex != 999 && monsterIndex >= 0 && monsterIndex < gp.monster[gp.currentMap].length) {
                
                Entity monster = gp.monster[gp.currentMap][monsterIndex];
                
                if (monster != null && monster.invincible == false) {
                    
                    
                    
                    // APPLY SLOW EFFECT for ice projectiles
                    if (this.name != null && this.name.equals("Ice")) {
                        monster.slowed = true;
                        monster.slowCounter = 0;
                        monster.slowDuration = 180; // 3 seconds
                        
                        // Store original speed if not already stored
                        if (monster.defaultSpeed == 0) {
                            monster.defaultSpeed = monster.speed;
                        }
                        
                        // Apply slow effect (reduce speed by half)
                        monster.speed = monster.defaultSpeed / 2;
                        if (monster.speed < 1) monster.speed = 1;
                        
                        // Set blue tint
                        monster.tintColor = new java.awt.Color(100, 150, 255, 150);
                        
                        gp.ui.showMessage("Monster slowed!");
                        
                    }
                    
                    gp.player.damageMonster(monsterIndex, this.attack, this.knockBackPower);
                    this.alive = false;
                    return;
                }
            }
            
            // Check for interactive tiles
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            
            if (iTileIndex != 999 && iTileIndex >= 0 && iTileIndex < gp.iTile[gp.currentMap].length) {
                
                if (gp.iTile[gp.currentMap][iTileIndex] != null && 
                    gp.iTile[gp.currentMap][iTileIndex].destructible == true) {
                    
                    gp.iTile[gp.currentMap][iTileIndex].life--;
                   
                    
                    if (gp.iTile[gp.currentMap][iTileIndex].life <= 0) {
                        gp.iTile[gp.currentMap][iTileIndex] = gp.iTile[gp.currentMap][iTileIndex].getDestroyedForm();
                    }
                }
                this.alive = false;
                return;
            }
        }
        else if (user != null && user != gp.player) {
            // Monster-fired projectile - damage player WITH GUARD CHECK
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            
            if (contactPlayer == true && gp.player.invincible == false) {
                
                damagePlayerWithGuard(this.attack);
                this.alive = false;
                return;
            }
        }

        // ===== LIFETIME MANAGEMENT =====
        life--;
        if (life <= 0) {
            alive = false;
            
            return;
        }
        
        // ===== SPRITE ANIMATION =====
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            }
            else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    
    public void damagePlayerWithGuard(int attack) {
        if (gp.player == null) return;
        
        if (gp.player.invincible == false) {
            
            int damage = attack - gp.player.defense;
            if (damage < 1) {
                damage = 1;
            }
            
            // Check if player is guarding
            if (gp.player.guarding == true) {
                // Get the direction the projectile is coming from
                String attackDirection = this.Direction;
                
                // Get the direction the player needs to face to block
                String oppositeDirection = getOppositeDirection(attackDirection);
                
                // Check if player is facing the projectile
                if (gp.player.Direction.equals(oppositeDirection)) {
                    // SUCCESSFULLY GUARDED - TAKE ZERO DAMAGE!
                    if (gp.se != null) gp.playSE(15); // Guard sound
                    gp.ui.showMessage("Perfect Guard! No damage!");
                    
                    // Set invincibility but take NO damage
                    gp.player.invincible = true;
                    gp.player.invincibleCounter = 0;
                    gp.player.transparent = true;
                    
                    return;
                    
                } else {
                    // Guard failed - wrong direction
                    if (gp.se != null) gp.playSE(6); // Hurt sound
                    gp.ui.showMessage(damage + " damage from projectile!");
                    
                    // Apply damage
                    gp.player.life -= damage;
                }
            } else {
                // Not guarding
                if (gp.se != null) gp.playSE(6); // Hurt sound
                gp.ui.showMessage(damage + " damage from projectile!");
                
                // Apply damage
                gp.player.life -= damage;
            }
            
            // Set invincibility
            gp.player.invincible = true;
            gp.player.invincibleCounter = 0;
            gp.player.transparent = true;
            
            if (gp.player.life <= 0) {
                gp.player.dying = true;
            }
        }
    }
    
    public String getOppositeDirection(String Direction) {
        String oppositeDirection = "";
        
        switch (Direction) {
            case "up": oppositeDirection = "down"; break;
            case "down": oppositeDirection = "up"; break;
            case "left": oppositeDirection = "right"; break;
            case "right": oppositeDirection = "left"; break;
        }
        return oppositeDirection;
    }
    
    public boolean haveResource(Entity user) {
        boolean haveResource = false;
        
        if (user != null && user.type == type_player) {
            if (this instanceof object.OBJ_Arrows) {
                if (user.arrow >= this.useCost) {
                    haveResource = true;
                }
            }
            else if (this instanceof object.OBJ_ice) {
                if (user.mana >= this.useCost) {
                    haveResource = true;
                }
            }
        }
        return haveResource;
    }
    
    public void SubtractResource(Entity user) {
        if (user != null && user.type == type_player) {
            if (this instanceof object.OBJ_Arrows) {
                user.arrow -= this.useCost;
                if (user.arrow < 0) user.arrow = 0;
                
            }
            else if (this instanceof object.OBJ_ice) {
                user.mana -= this.useCost;
                if (user.mana < 0) user.mana = 0;
                
            }
        }
    }
    
    public void damagePlayer(int attack) {
        damagePlayerWithGuard(attack);
    }
}