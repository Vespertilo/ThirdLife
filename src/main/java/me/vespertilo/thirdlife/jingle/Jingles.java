package me.vespertilo.thirdlife.jingle;

import me.vespertilo.thirdlife.ThirdLife;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Jingles {

    // A G E G E
    //wizard magic!
    //https://minecraft.fandom.com/wiki/Note_Block#Notes
    public static void playWhistle(Player p) {
        playSoundWithDelay(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.189207f, 5);
        playSoundWithDelay(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.059463f, 10);
        playSoundWithDelay(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.890899f, 15);
        playSoundWithDelay(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.059463f, 20);
        playSoundWithDelay(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.890899f, 22);

        playSoundWithDelay(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.890899f, 37);
        playSoundWithDelay(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.793701f, 42);
        playSoundWithDelay(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.707107f, 47);
        playSoundWithDelay(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.793701f, 52);
        playSoundWithDelay(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.890899f, 54);
    }

    private static void playSoundWithDelay(Player p, Sound sound, float volume, float pitch, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.playSound(p.getLocation(), sound, volume, pitch);
            }
        }.runTaskLater(ThirdLife.getInstance(), delay);
    }
}
