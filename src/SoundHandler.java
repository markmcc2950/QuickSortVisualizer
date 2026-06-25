import javax.sound.midi.*;
import java.util.concurrent.*;

public class SoundHandler {

    private static Synthesizer synth;
    private static MidiChannel channel;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    /**
     * Initialize the MIDI synthesizer once at startup.
     * Call this before playing any sounds.
     */
    public static void init() throws Exception {
        synth = MidiSystem.getSynthesizer();
        synth.open();
        channel = synth.getChannels()[0];

        // Disable reverb and chorus to prevent echo/decay
        channel.controlChange(91, 0);  // Reverb depth = 0
        channel.controlChange(93, 0);  // Chorus depth = 0

        // Use a short-decay instrument (piano)
        channel.programChange(0);
    }

    /**
     * Play a tone at the specified frequency for the specified duration.
     * Returns immediately - sound plays asynchronously on a scheduled thread.
     *
     * @param frequencyHz Frequency in Hz (e.g., 440 for A4)
     * @param durationMs  Duration in milliseconds
     */
    public static void playSound(int frequencyHz, int durationMs) {
        if (synth == null || !synth.isOpen() || channel == null) {
            return;
        }

        // Convert frequency to MIDI note number
        int note = (int)(12 * (Math.log(frequencyHz / 440.0) / Math.log(2)) + 69);
        note = Math.max(0, Math.min(127, note));

        // Turn note on immediately
        channel.noteOn(note, 127);

        final int finalNote = note;

        // Schedule note off after duration
        scheduler.schedule(() -> {
            channel.noteOff(finalNote);
            channel.allSoundOff();
        }, durationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Play a tone and block for the specified duration.
     * Use this only if you need synchronous playback.
     */
    public static void playSoundSync(int frequencyHz, int durationMs) throws Exception {
        if (synth == null || !synth.isOpen() || channel == null) {
            return;
        }

        int note = (int)(12 * (Math.log(frequencyHz / 440.0) / Math.log(2)) + 69);
        note = Math.max(0, Math.min(127, note));

        channel.noteOn(note, 127);
        Thread.sleep(durationMs);
        channel.noteOff(note);
        channel.allSoundOff();
    }

    /**
     * Play two tones simultaneously (a chord) for the specified duration.
     * Returns immediately - sounds play asynchronously.
     *
     * @param freq1 First frequency in Hz
     * @param freq2 Second frequency in Hz
     * @param durationMs Duration in milliseconds
     */
    public static void playChord(int freq1, int freq2, int durationMs) {
        if (synth == null || !synth.isOpen() || channel == null) {
            return;
        }

        // Convert frequencies to MIDI notes
        int note1 = (int)(12 * (Math.log(freq1 / 440.0) / Math.log(2)) + 69);
        int note2 = (int)(12 * (Math.log(freq2 / 440.0) / Math.log(2)) + 69);
        note1 = Math.max(0, Math.min(127, note1));
        note2 = Math.max(0, Math.min(127, note2));

        // Turn both notes on
        channel.noteOn(note1, 100);
        channel.noteOn(note2, 100);

        final int n1 = note1;
        final int n2 = note2;

        // Schedule both notes off after duration
        scheduler.schedule(() -> {
            channel.noteOff(n1);
            channel.noteOff(n2);
            channel.allSoundOff();
        }, durationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Clean up resources. Call at application shutdown.
     */
    public static void shutdown() {
        scheduler.shutdown();
        if (synth != null && synth.isOpen()) {
            if (channel != null) {
                channel.allSoundOff();
            }
            synth.close();
            synth = null;
            channel = null;
        }
    }
}