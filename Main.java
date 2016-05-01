import java.awt.event.*;

import javax.sound.midi.*;

import javax.swing.*;

import java.awt.Dimension;
import java.util.Arrays;


public class main {

	public static Synthesizer syn;
	public static MidiChannel[] mc;

	public static void main(String[] args) throws MidiUnavailableException {

		//create arrays of keys
		//create arrays of notes

		JFrame frame = new JFrame("Key Listener");
		JPanel pane = new JPanel();

		//true or false of which keys are pressed
		final boolean[] keys = new boolean[15];

		//list of actual keyboard characters used
		final Integer[] kClist = new Integer[]{KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, 81, 87, 69, 82, 84, 65, 83, 68, 70, 71};
		//each corresponding note to the index of key
		final int[] notes = new int[]{55,56,57,58,59,59,60,61,62,63,63,64,65,66,67};

		//shifting octaves
		final Integer[] kClistNumPad = new Integer[]{97,98,99,100,101,102,103,104,105};
		final int[] shifterVals = new int[]{-4,-3,-2,-1,0,1,2,3,4};

		syn = MidiSystem.getSynthesizer();
		syn.open();     

		//syn.loadAllInstruments(soundbank)
		Instrument[] instr = syn.getDefaultSoundbank().getInstruments();
		syn.loadInstrument(instr[66]);
		
		mc = syn.getChannels();


		KeyListener listener = new KeyListener() {
			//octave control
			int shifter = 0;
			int instrument = 0;
			//which ever keys are pressed during sus will remain "pressed" (MAYBE NOT PLAYED, BUT PRESSED) during sus
			boolean sus = false;

			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();

				System.out.println(keyCode);

				//IF NOT NUMPAD KEYS
				if(keyCode<96&&keyCode>48){
					int keyPos = Arrays.asList(kClist).indexOf(keyCode);

					if(!keys[keyPos]){
						keys[keyPos] = true;
						mc[instrument].noteOn(notes[keyPos]+shifter,500);
					}
				}
				if(keyCode>96 && keyCode<106){
					int keyPos = Arrays.asList(kClistNumPad).indexOf(keyCode);
					shifter = 12*shifterVals[keyPos];
				}
				//if(keyCode>52 && keyCode<58)

				//"+" button for sus
				if(keyCode == 107){
					sus = !sus;
					System.out.println(sus);

					if(sus == false){

						//set all notes as not pressed
						Arrays.fill(keys, false);

						//turn off all notes
						for(int i = 0; i<keys.length; i++){
							mc[instrument].noteOff(notes[i]+shifter,500);
						}


					}
				}



				//"Space" or "-" button to strum all held down keys
				if(keyCode == 32||keyCode == 109){
					for(int i = 0; i<keys.length; i++){
						if(keys[i]){
							mc[instrument].noteOn(notes[i], 500);
						}
					}
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();

				if(keyCode<96&&sus == false&&keyCode>48){
					int keyPos = java.util.Arrays.asList(kClist).indexOf(keyCode);
					keys[keyPos] = false;
					mc[instrument].noteOff(notes[keyPos]+shifter,500);
				}



			}//EO keyReleased

			@Override
			public void keyTyped(KeyEvent e) {
				int keyCode = e.getKeyCode();

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
