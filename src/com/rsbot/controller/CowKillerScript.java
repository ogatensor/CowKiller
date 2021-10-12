package com.rsbot.controller;

import com.rsbot.controller.util.Sleep;
import org.osbot.Con;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

//Used by osbot sdn to gather metadata about script.
@ScriptManifest(info="cow killing", logo="", version=0, author="mossboy", name="Cow Killer Script")

public class CowKillerScript extends Script {
    private final int FOOD_ID = 2142; // 2142 is cooked meat
    private final int [] ENEMY_IDS = {17766, 17744, 17735, 17674, 15645}; // cow ids

    @Override
    public void onStart() throws InterruptedException {
        super.onStart();
    }
    @Override
    public void onExit() throws InterruptedException {
        super.onExit();
    }

    @Override
    public int onLoop() throws InterruptedException {
        log("[CowKillerScript.onLoop]: Player Position: " + myPosition().toString());

        isPlayerReadyToAttack();
        attack();
        isInventoryFull();

        return 700;
    }

    private boolean hasFood() {
        return getInventory().contains(FOOD_ID);
    }

    private boolean isPlayerReadyToAttack () {
        if (!hasFood()) {
            return false; // no food
        }

        if (getCombat().isFighting()) {
            return false; // already fighting
        }

        if(myPlayer().isAnimating() || myPlayer().isMoving() || myPlayer().isUnderAttack()) {
            return false;
        }
        return true; //ready to fight
    }

    private void attack() throws InterruptedException {
        if(!isPlayerReadyToAttack()) {
            return;
        }

        if(checkHealth() < 50) {
            heal();
        }

        //find more efficient way to grab refs
        NPC enemy = getNpcs().closest(ENEMY_IDS);
        if(enemy != null && !enemy.isUnderAttack()) {
            Sleep.sleepUntil(() -> enemy.interact("Attack"), 2000);
        }
    }

    private int checkHealth() {
        float staticHP = getSkills().getStatic(Skill.HITPOINTS);
        float dynamicHP = getSkills().getDynamic(Skill.HITPOINTS);

        return (int) (100 * (dynamicHP / staticHP));
    }

    private void heal() throws InterruptedException {
        Sleep.sleepUntil(() -> getInventory().getItem(FOOD_ID).interact("Eat"), 3142);
    }

    private boolean isInventoryFull() {
        boolean inv = getInventory().isFull();
        log("[CowKillerScript.isInventoryFull]: The inventory is full");
        return true;
    }
}
