import java.awt.event.*;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.*;

import java.awt.Dimension;
import java.util.Arrays;

import javax.sound.midi.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

public class KeyListenerExample {

	public static Synthesizer syn;
	public static MidiChannel[] mc;

	public static void main(String[] args) throws MidiUnavailableException {

		//create arrays of keys
		//create arrays of notes

		JFrame frame = new JFrame("Key Listener");
		JPanel pane = new JPanel();

		final boolean[] keys = new boolean[12];

		final Integer[] kClist = new Integer[]{KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, 81, 87, 69, 82, 65, 83, 68, 70};
		final int[] notes = new int[]{55,56,57,58,59,60,61,62,63,64,65,66};

		//shifting octaves
		final Integer[] kClistNumPad = new Integer[]{97,98,99,100,101,102,103,104,105};
		final int[] shifterVals = new int[]{-4,-3,-2,-1,0,1,2,3,4};

		syn = MidiSystem.getSynthesizer();
		syn.open();     
		mc = syn.getChannels();
		Instrument[] instr = syn.getDefaultSoundbank().getInstruments();
		syn.loadInstrument(instr[90]);

		KeyListener listener = new KeyListener() {
			int shifter = 0;
			int instrument = 2;
			
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();

				//IF NOT NUMPAD KEYS
				if(keyCode<96){
					int keyPos = Arrays.asList(kClist).indexOf(keyCode);

					if(!keys[keyPos]){
						keys[keyPos] = true;
						mc[instrument].noteOn(notes[keyPos]+shifter,499);
					}
				}
				else{
					int keyPos = Arrays.asList(kClistNumPad).indexOf(keyCode);
					shifter = 12*shifterVals[keyPos];
				}


			}

			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();

				int keyPos = java.util.Arrays.asList(kClist).indexOf(keyCode);

				keys[keyPos] = false;
				mc[instrument].noteOff(notes[keyPos]+shifter,499);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		}; //EO KeyListener

		pane.setFocusable(true);
		pane.requestFocusInWindow();
		pane.addKeyListener(listener);
		frame.getContentPane().add(pane);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(200, 200));
        frame.setAlwaysOnTop(true);


		frame.pack();

		frame.setVisible(true);

	}//EOmain

}//EO class
