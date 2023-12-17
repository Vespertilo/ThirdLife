package me.vespertilo.thirdlife.jingle;

import me.vespertilo.thirdlife.ThirdLife;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Jingles {

    public static void playWhistle(Player p) {
        playNoteDelayed(p, Note.A2, 1.0f, 5);
        playNoteDelayed(p, Note.G2, 1.0f, 10);
        playNoteDelayed(p, Note.E, 1.0f, 15);
        playNoteDelayed(p, Note.G2, 1.0f, 20);
        playNoteDelayed(p, Note.E, 1.0f, 22);

        playNoteDelayed(p, Note.E, 1.0f, 37);
        playNoteDelayed(p, Note.D, 1.0f, 42);
        playNoteDelayed(p, Note.C, 1.0f, 47);
        playNoteDelayed(p, Note.D, 1.0f, 52);
        playNoteDelayed(p, Note.E, 1.0f, 54);
    }


    private static void playNoteDelayed(Player p, Note note, float volume, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, volume, note.getPitch());
            }
        }.runTaskLater(ThirdLife.getInstance(), delay);
    }

    //wizard magic!
    //https://minecraft.fandom.com/wiki/Note_Block#Notes
    public enum Note {
        FSHARP(0),
        G(1),
        GSHARP(2),
        A(3),
        ASHARP(4),
        B(5),
        C(6),
        CSHARP(7),
        D(8),
        DSHARP(9),
        E(10),
        F(11),
        FSHARP2(12),
        G2(13),
        GSHARP2(14),
        A2(15),
        ASHARP2(16),
        B2(17),
        C2(18),
        CSHARP2(19),
        D2(20),
        DSHARP2(21),
        E2(22),
        F2(23),
        FSHARP3(24);

        private int useCount;

        Note(int useCount) {
            this.useCount = useCount;
        }

        public float getPitch() {
            return (float) Math.pow(2, (useCount - 12) / 12f);
        }
    }
}
